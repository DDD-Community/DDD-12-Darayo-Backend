#!/usr/bin/env python3
import argparse
import json
import os
import sys
from typing import Any, Dict, List, Optional, Set, Tuple


def load_spec(path: str) -> Dict[str, Any]:
    ext = os.path.splitext(path)[1].lower()
    with open(path, "r", encoding="utf-8") as f:
        data = f.read()
    if ext in [".json", ""]:
        return json.loads(data)
    elif ext in [".yaml", ".yml"]:
        try:
            import yaml  # type: ignore
        except Exception as e:
            raise SystemExit(
                f"YAML file detected but PyYAML is not available. Install pyyaml or convert to JSON. File: {path}"
            )
        return yaml.safe_load(data)
    else:
        # Try JSON as a fallback
        try:
            return json.loads(data)
        except Exception:
            raise SystemExit(f"Unsupported spec format for file: {path}")


OperationKeys = [
    "get",
    "put",
    "post",
    "delete",
    "options",
    "head",
    "patch",
    "trace",
]


def schema_signature(schema: Optional[Dict[str, Any]]) -> str:
    if not schema:
        return "<none>"
    if "$ref" in schema:
        return f"ref:{schema['$ref']}"
    t = schema.get("type")
    fmt = schema.get("format")
    if t == "array":
        return f"array<{schema_signature(schema.get('items'))}>"
    if t == "object":
        props = schema.get("properties") or {}
        keys = sorted(list(props.keys()))
        return f"object{{{','.join(keys)}}}"
    if t:
        return f"{t}{f'({fmt})' if fmt else ''}"
    # fallback minimal signature
    return json.dumps({k: v for k, v in schema.items() if k in ("type", "format", "$ref")}, sort_keys=True)


# ---- Deep $ref resolution and diff utilities ----

class RefResolver:
    """Resolves local JSON Pointer $ref entries within an OpenAPI document.

    - Only supports internal refs (starting with '#/')
    - Merges sibling keys beside $ref into the resolved target
    - Detects cycles and stops expansion to avoid infinite recursion
    """

    def __init__(self, document: Dict[str, Any]) -> None:
        self.doc = document

    def resolve_pointer(self, pointer: str) -> Any:
        if not pointer.startswith("#/"):
            raise ValueError(f"Only local refs are supported: {pointer}")
        parts = [p.replace("~1", "/").replace("~0", "~") for p in pointer[2:].split("/")]
        node: Any = self.doc
        for p in parts:
            if isinstance(node, dict) and p in node:
                node = node[p]
            else:
                raise KeyError(f"$ref not found: {pointer}")
        return node

    def merge_ref(self, ref_obj: Dict[str, Any], seen: Optional[Set[str]] = None) -> Any:
        ref = ref_obj.get("$ref")
        if not ref:
            return ref_obj
        if seen is None:
            seen = set()
        if ref in seen:
            # cycle detected; return the ref itself
            return {"$ref": ref}
        seen.add(ref)
        target = self.resolve_pointer(ref)
        resolved = dict(target) if isinstance(target, dict) else target
        if isinstance(resolved, dict):
            for k, v in ref_obj.items():
                if k == "$ref":
                    continue
                resolved[k] = v
        return resolved


MISSING = object()


def _is_ref(obj: Any) -> bool:
    return isinstance(obj, dict) and "$ref" in obj


def deep_diff(
    a: Any,
    b: Any,
    resolver_a: RefResolver,
    resolver_b: RefResolver,
    path: List[str] | None = None,
) -> List[Dict[str, Any]]:
    """Deeply diff two JSON-compatible values with on-demand $ref resolution.

    Returns list of dicts: {path, old, new}, where MISSING denotes absence.
    """
    if path is None:
        path = []
    diffs: List[Dict[str, Any]] = []

    # Resolve references before comparing
    if _is_ref(a):
        a = resolver_a.merge_ref(a)
    if _is_ref(b):
        b = resolver_b.merge_ref(b)

    if type(a) != type(b):
        diffs.append({"path": "/" + "/".join(path), "old": a, "new": b})
        return diffs

    if isinstance(a, dict):
        a_keys = set(a.keys())
        b_keys = set(b.keys())
        for k in sorted(a_keys - b_keys):
            diffs.append({"path": "/" + "/".join(path + [k]), "old": a[k], "new": MISSING})
        for k in sorted(b_keys - a_keys):
            diffs.append({"path": "/" + "/".join(path + [k]), "old": MISSING, "new": b[k]})
        for k in sorted(a_keys & b_keys):
            diffs.extend(deep_diff(a[k], b[k], resolver_a, resolver_b, path + [k]))
        return diffs

    if isinstance(a, list):
        # Compare 'required' arrays as sets for clearer diffs
        if path and path[-1] == "required":
            sa = set(a)
            sb = set(b)
            for item in sorted(sa - sb):
                diffs.append({"path": "/" + "/".join(path + [str(item)]), "old": item, "new": MISSING})
            for item in sorted(sb - sa):
                diffs.append({"path": "/" + "/".join(path + [str(item)]), "old": MISSING, "new": item})
            return diffs
        # Default index-wise compare
        min_len = min(len(a), len(b))
        for i in range(min_len):
            diffs.extend(deep_diff(a[i], b[i], resolver_a, resolver_b, path + [str(i)]))
        for i in range(min_len, len(a)):
            diffs.append({"path": "/" + "/".join(path + [str(i)]), "old": a[i], "new": MISSING})
        for i in range(min_len, len(b)):
            diffs.append({"path": "/" + "/".join(path + [str(i)]), "old": MISSING, "new": b[i]})
        return diffs

    # Primitive
    if a != b:
        diffs.append({"path": "/" + "/".join(path), "old": a, "new": b})
    return diffs


def analyze_schema_breaking(diffs: List[Dict[str, Any]], context: str) -> List[str]:
    msgs: List[str] = []
    for d in diffs:
        p = d.get("path", "")
        old = d.get("old", MISSING)
        new = d.get("new", MISSING)
        if "/properties/" in p and new is MISSING:
            prop = p.split("/properties/")[-1]
            msgs.append(f"{context} property removed: {prop}")
        if p.endswith("/required") or "/required" in p:
            oset = set(old or []) if old is not MISSING and isinstance(old, list) else set()
            nset = set(new or []) if new is not MISSING and isinstance(new, list) else set()
            became_req = sorted(list(nset - oset))
            if became_req:
                msgs.append(f"{context} new required properties: {', '.join(became_req)}")
        if p.endswith("/type") and old is not MISSING and new is not MISSING and old != new:
            msgs.append(f"{context} type changed: {old} -> {new}")
    return msgs


def compare_parameters(old: List[Dict[str, Any]], new: List[Dict[str, Any]], resolver_old: RefResolver, resolver_new: RefResolver):
    def key(p: Dict[str, Any]) -> Tuple[str, str]:
        return (p.get("name", ""), p.get("in", ""))

    old_map = {key(p): p for p in (old or [])}
    new_map = {key(p): p for p in (new or [])}
    added = []
    removed = []
    changed = []
    breaking = []

    for k in new_map.keys() - old_map.keys():
        added.append({"name": k[0], "in": k[1], "new": new_map[k]})

    for k in old_map.keys() - new_map.keys():
        removed.append({"name": k[0], "in": k[1], "old": old_map[k]})
        breaking.append(f"Removed parameter {k[0]} in={k[1]}")

    for k in old_map.keys() & new_map.keys():
        o = old_map[k]
        n = new_map[k]
        deltas = {}
        if bool(o.get("required")) != bool(n.get("required")):
            deltas["required"] = {"old": bool(o.get("required")), "new": bool(n.get("required"))}
            if not bool(o.get("required")) and bool(n.get("required")):
                breaking.append(f"Parameter {k[0]} in={k[1]} became required")
        # deep compare schema with $ref resolution
        o_schema = (o.get("schema") or {})
        n_schema = (n.get("schema") or {})
        sdiff = deep_diff(o_schema, n_schema, resolver_old, resolver_new)
        if sdiff:
            deltas["schema_diff"] = sdiff
            breaking.extend(analyze_schema_breaking(sdiff, f"Parameter {k[0]} in={k[1]}"))
        if deltas:
            changed.append({"name": k[0], "in": k[1], "changes": deltas})

    return {"added": added, "removed": removed, "changed": changed, "breaking": breaking}


def compare_request_body(old: Dict[str, Any], new: Dict[str, Any], resolver_old: RefResolver, resolver_new: RefResolver):
    old_ct = set(((old or {}).get("content") or {}).keys())
    new_ct = set(((new or {}).get("content") or {}).keys())
    added = sorted(list(new_ct - old_ct))
    removed = sorted(list(old_ct - new_ct))
    changed = []
    breaking = []
    for ct in old_ct & new_ct:
        o_schema = (((old.get("content") or {}).get(ct) or {}).get("schema") or {})
        n_schema = (((new.get("content") or {}).get(ct) or {}).get("schema") or {})
        sdiff = deep_diff(o_schema, n_schema, resolver_old, resolver_new)
        if sdiff:
            changed.append({"contentType": ct, "schema_diff": sdiff})
            breaking.extend(analyze_schema_breaking(sdiff, f"RequestBody {ct}"))
    for ct in removed:
        breaking.append(f"RequestBody content type removed: {ct}")
    return {"added": added, "removed": removed, "changed": changed, "breaking": breaking}


def compare_responses(old: Dict[str, Any], new: Dict[str, Any], resolver_old: RefResolver, resolver_new: RefResolver):
    old_map = old or {}
    new_map = new or {}
    old_codes = set(old_map.keys())
    new_codes = set(new_map.keys())
    added = sorted(list(new_codes - old_codes))
    removed = sorted(list(old_codes - new_codes))
    changed = []
    breaking = []
    for code in old_codes & new_codes:
        o = old_map.get(code) or {}
        n = new_map.get(code) or {}
        delta = {}
        if (o.get("description") or "").strip() != (n.get("description") or "").strip():
            delta["description"] = {"old": o.get("description"), "new": n.get("description")}
        o_ct = set(((o.get("content") or {}).keys()))
        n_ct = set(((n.get("content") or {}).keys()))
        ct_added = sorted(list(n_ct - o_ct))
        ct_removed = sorted(list(o_ct - n_ct))
        ct_changed = []
        for ct in o_ct & n_ct:
            o_schema = (((o.get("content") or {}).get(ct) or {}).get("schema") or {})
            n_schema = (((n.get("content") or {}).get(ct) or {}).get("schema") or {})
            sdiff = deep_diff(o_schema, n_schema, resolver_old, resolver_new)
            if sdiff:
                ct_changed.append({"contentType": ct, "schema_diff": sdiff})
        if ct_added:
            delta["content_added"] = ct_added
        if ct_removed:
            delta["content_removed"] = ct_removed
        if ct_changed:
            delta["content_changed"] = ct_changed
        if delta:
            changed.append({"code": code, "changes": delta})
            # analyze schema-level breaking
            for item in delta.get("content_changed", []) or []:
                breaking.extend(analyze_schema_breaking(item.get("schema_diff", []), f"Response {code} {item.get('contentType')}"))
        if code.startswith("2") and (ct_removed or ct_changed):
            breaking.append(f"Response {code} changed: potential breaking change")
    for code in removed:
        if code.startswith("2"):
            breaking.append(f"Response {code} removed (breaking)")
    return {"added": added, "removed": removed, "changed": changed, "breaking": breaking}


def compare_paths(old: Dict[str, Any], new: Dict[str, Any]):
    old_paths = old.get("paths") or {}
    new_paths = new.get("paths") or {}
    resolver_old = RefResolver(old)
    resolver_new = RefResolver(new)
    added_paths = sorted(list(set(new_paths.keys()) - set(old_paths.keys())))
    removed_paths = sorted(list(set(old_paths.keys()) - set(new_paths.keys())))
    changed_ops = []
    breaking = []

    for p in removed_paths:
        breaking.append(f"Path removed: {p}")

    for path in sorted(list(set(old_paths.keys()) & set(new_paths.keys()))):
        o_item = old_paths[path] or {}
        n_item = new_paths[path] or {}
        o_ops = {k: v for k, v in o_item.items() if k in OperationKeys}
        n_ops = {k: v for k, v in n_item.items() if k in OperationKeys}
        added_methods = sorted(list(set(n_ops.keys()) - set(o_ops.keys())))
        removed_methods = sorted(list(set(o_ops.keys()) - set(n_ops.keys())))
        for m in removed_methods:
            breaking.append(f"Operation removed: {m.upper()} {path}")
        for method in sorted(list(set(o_ops.keys()) & set(n_ops.keys()))):
            o = o_ops[method] or {}
            n = n_ops[method] or {}
            op_changes = {}
            if (o.get("summary") or "") != (n.get("summary") or ""):
                op_changes["summary"] = {"old": o.get("summary"), "new": n.get("summary")}
            if (o.get("operationId") or "") != (n.get("operationId") or ""):
                op_changes["operationId"] = {"old": o.get("operationId"), "new": n.get("operationId")}
            if bool(o.get("deprecated")) != bool(n.get("deprecated")):
                op_changes["deprecated"] = {"old": bool(o.get("deprecated")), "new": bool(n.get("deprecated"))}

            param_diff = compare_parameters(
                o.get("parameters") or [], n.get("parameters") or [], resolver_old, resolver_new
            )
            if any(param_diff[k] for k in ("added", "removed", "changed")):
                op_changes["parameters"] = param_diff

            rb_diff = compare_request_body(
                o.get("requestBody") or {}, n.get("requestBody") or {}, resolver_old, resolver_new
            )
            if any(rb_diff[k] for k in ("added", "removed", "changed")):
                op_changes["requestBody"] = rb_diff

            resp_diff = compare_responses(
                o.get("responses") or {}, n.get("responses") or {}, resolver_old, resolver_new
            )
            if any(resp_diff[k] for k in ("added", "removed", "changed")):
                op_changes["responses"] = resp_diff

            if op_changes:
                changed_ops.append({"path": path, "method": method, "changes": op_changes})
                breaking.extend(
                    [
                        f"{method.upper()} {path}: {msg}"
                        for msg in (
                            param_diff.get("breaking", [])
                            + rb_diff.get("breaking", [])
                            + resp_diff.get("breaking", [])
                        )
                    ]
                )

        if added_methods or removed_methods:
            changed_ops.append(
                {
                    "path": path,
                    "method": "_overview_",
                    "changes": {"methods_added": added_methods, "methods_removed": removed_methods},
                }
            )

    return {
        "added_paths": added_paths,
        "removed_paths": removed_paths,
        "changed_ops": changed_ops,
        "breaking": breaking,
    }


def compare_components(old: Dict[str, Any], new: Dict[str, Any]):
    old_comp = old.get("components") or {}
    new_comp = new.get("components") or {}
    result = {}
    breaking: List[str] = []

    def cmp_named(section: str):
        o = (old_comp.get(section) or {})
        n = (new_comp.get(section) or {})
        o_keys = set(o.keys())
        n_keys = set(n.keys())
        added = sorted(list(n_keys - o_keys))
        removed = sorted(list(o_keys - n_keys))
        changed = []
        sect_breaking: List[str] = []

        resolver_old = RefResolver(old)
        resolver_new = RefResolver(new)
        for name in o_keys & n_keys:
            o_item = o.get(name) or {}
            n_item = n.get(name) or {}
            delta = {}
            if section == "schemas":
                sdiff = deep_diff(o_item, n_item, resolver_old, resolver_new, path=["components", "schemas", name])
                if sdiff:
                    delta["schema_diff"] = sdiff
                    sect_breaking.extend(analyze_schema_breaking(sdiff, f"Schema {name}"))
            else:
                o_sig = schema_signature(o_item)
                n_sig = schema_signature(n_item)
                if o_sig != n_sig:
                    delta["signature"] = {"old": o_sig, "new": n_sig}
            if delta:
                changed.append({"name": name, "changes": delta})

        for nm in removed:
            sect_breaking.append(f"Component removed {section}: {nm}")

        return {"added": added, "removed": removed, "changed": changed, "breaking": sect_breaking}

    for sec in ["schemas", "parameters", "responses"]:
        result[sec] = cmp_named(sec)
        breaking.extend(result[sec].get("breaking", []))

    return {"sections": result, "breaking": breaking}


def build_report(old: Dict[str, Any], new: Dict[str, Any]):
    report: Dict[str, Any] = {"summary": {}, "details": {}, "breaking_changes": []}
    paths_diff = compare_paths(old, new)
    comps_diff = compare_components(old, new)
    report["details"]["paths"] = paths_diff
    report["details"]["components"] = comps_diff
    breaking = []
    breaking.extend(paths_diff.get("breaking", []))
    breaking.extend(comps_diff.get("breaking", []))
    report["breaking_changes"] = sorted(list(set(breaking)))
    report["summary"] = {
        "paths_added": len(paths_diff.get("added_paths", [])),
        "paths_removed": len(paths_diff.get("removed_paths", [])),
        "ops_changed": len(paths_diff.get("changed_ops", [])),
        "components_changed_sections": [
            sec for sec, data in comps_diff.get("sections", {}).items() if any(data.get(k) for k in ("added", "removed", "changed"))
        ],
        "breaking_count": len(report["breaking_changes"]),
    }
    return report


def _short_val(v: Any) -> str:
    if v is MISSING:
        return "<MISSING>"
    if isinstance(v, dict):
        if "$ref" in v:
            return f"ref({v['$ref']})"
        if "enum" in v and isinstance(v["enum"], list):
            return "enum{" + ",".join(map(str, v["enum"])) + "}"
        t = v.get("type")
        if t == "array":
            return "array<" + _short_val(v.get("items", {})) + ">"
        if t == "object":
            keys = sorted(list((v.get("properties") or {}).keys()))
            return "object{" + ",".join(keys) + "}"
        if t:
            fmt = v.get("format")
            return f"{t}{f'({fmt})' if fmt else ''}"
        # fallback concise
        return schema_signature(v)
    if isinstance(v, list):
        return f"list(len={len(v)})"
    return json.dumps(v, ensure_ascii=False)


def _summarize_schema_diffs(diffs: List[Dict[str, Any]], max_lines: int) -> List[str]:
    lines: List[str] = []
    for d in diffs:
        p = d.get("path", "/")
        old = d.get("old", MISSING)
        new = d.get("new", MISSING)
        if new is MISSING and old is not MISSING:
            lines.append(f"- {p}: removed")
        elif old is MISSING and new is not MISSING:
            lines.append(f"- {p}: added ({_short_val(new)})")
        else:
            lines.append(f"- {p}: {_short_val(old)} -> {_short_val(new)}")
        if len(lines) >= max_lines:
            lines.append(f"  ... and {len(diffs) - max_lines} more changes")
            break
    return lines


def print_text_report(report: Dict[str, Any], detailed: bool = False, max_lines: int = 10):
    s = report.get("summary", {})
    print("== OpenAPI Diff Summary ==")
    print(f"Paths added: {s.get('paths_added', 0)} | Paths removed: {s.get('paths_removed', 0)} | Ops changed: {s.get('ops_changed', 0)}")
    changed_secs = ", ".join(s.get("components_changed_sections", [])) or "-"
    print(f"Components changed: {changed_secs}")
    print(f"Breaking changes: {s.get('breaking_count', 0)}")
    print()

    # Paths details
    pd = report["details"].get("paths", {})
    if pd.get("added_paths"):
        print("-- Paths Added --")
        for p in pd["added_paths"]:
            print(f"+ {p}")
        print()
    if pd.get("removed_paths"):
        print("-- Paths Removed --")
        for p in pd["removed_paths"]:
            print(f"- {p}")
        print()
    if pd.get("changed_ops"):
        print("-- Operations Changed --")
        for op in pd["changed_ops"]:
            path = op["path"]
            method = op["method"]
            changes = op["changes"]
            if method == "_overview_":
                ma = ", ".join(changes.get("methods_added", []) or []) or "-"
                mr = ", ".join(changes.get("methods_removed", []) or []) or "-"
                print(f"~ {path}: methods added [{ma}], removed [{mr}]")
                continue
            print(f"~ {method.upper()} {path}")
            for k, v in changes.items():
                if k == "parameters":
                    print(f"    parameters: +{len(v.get('added', []))} -{len(v.get('removed', []))} ~{len(v.get('changed', []))}")
                    if detailed:
                        # Show changed parameter schema diffs
                        for ch in v.get("changed", []):
                            pname = ch.get("name")
                            pin = ch.get("in")
                            sch = ch.get("changes", {}).get("schema_diff", [])
                            if sch:
                                print(f"      • {pname} in={pin}:")
                                for line in _summarize_schema_diffs(sch, max_lines):
                                    print(f"        {line}")
                elif k == "requestBody":
                    print(f"    requestBody: +{len(v.get('added', []))} -{len(v.get('removed', []))} ~{len(v.get('changed', []))}")
                    if detailed:
                        for item in v.get("changed", []):
                            ct = item.get("contentType")
                            print(f"      • {ct}:")
                            for line in _summarize_schema_diffs(item.get("schema_diff", []), max_lines):
                                print(f"        {line}")
                elif k == "responses":
                    print(f"    responses: +{len(v.get('added', []))} -{len(v.get('removed', []))} ~{len(v.get('changed', []))}")
                    if detailed:
                        for r in v.get("changed", []):
                            code = r.get("code")
                            ch = r.get("changes", {})
                            for item in ch.get("content_changed", []) or []:
                                ct = item.get("contentType")
                                print(f"      • {code} {ct}:")
                                for line in _summarize_schema_diffs(item.get("schema_diff", []), max_lines):
                                    print(f"        {line}")
                else:
                    print(f"    {k}: {v}")
        print()

    # Components details (brief)
    cd = report["details"].get("components", {}).get("sections", {})
    for sec, data in cd.items():
        if any(data.get(k) for k in ("added", "removed", "changed")):
            print(f"-- Components/{sec} --")
            if data.get("added"):
                print("  + ", ", ".join(data["added"]))
            if data.get("removed"):
                print("  - ", ", ".join(data["removed"]))
            if data.get("changed"):
                print("  ~ changed: ", ", ".join([c['name'] for c in data["changed"]]))
            print()

    # Breaking changes
    if report.get("breaking_changes"):
        print("== Potential Breaking Changes ==")
        for b in report["breaking_changes"]:
            print(f"! {b}")


def _sanitize(obj: Any) -> Any:
    """Convert report to JSON-serializable form, replacing MISSING with a marker."""
    if obj is MISSING:
        return "<MISSING>"
    if isinstance(obj, dict):
        return {k: _sanitize(v) for k, v in obj.items()}
    if isinstance(obj, list):
        return [_sanitize(v) for v in obj]
    return obj


def main():
    parser = argparse.ArgumentParser(description="Compare two OpenAPI specs (JSON/YAML).")
    parser.add_argument("old", help="Path to old spec (baseline)")
    parser.add_argument("new", help="Path to new spec")
    parser.add_argument("--json", dest="as_json", action="store_true", help="Output JSON report")
    parser.add_argument("--detailed", action="store_true", help="Show detailed per-path schema diffs")
    parser.add_argument("--diff-lines", type=int, default=10, help="Max schema diff lines per section")
    parser.add_argument(
        "--fail-on-breaking",
        action="store_true",
        help="Exit with non-zero code if potential breaking changes are detected",
    )
    args = parser.parse_args()

    try:
        old = load_spec(args.old)
        new = load_spec(args.new)
    except Exception as e:
        print(f"Error loading specs: {e}", file=sys.stderr)
        sys.exit(1)

    report = build_report(old, new)
    if args.as_json:
        print(json.dumps(_sanitize(report), ensure_ascii=False, indent=2, sort_keys=True))
    else:
        print_text_report(report, detailed=args.detailed, max_lines=args.diff_lines)

    if args.fail_on_breaking and report.get("breaking_changes"):
        sys.exit(2)


if __name__ == "__main__":
    main()
