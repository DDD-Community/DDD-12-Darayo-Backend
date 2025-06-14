package ddd.darayo.festival.presentation.timetable;

import ddd.darayo.festival.domain.service.TimetableManagement;
import ddd.darayo.festival.presentation.timetable.exchanges.EditTimetableReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/timetable")
@RequiredArgsConstructor
public class TimetableAdminController {
    private final TimetableManagement timetableManagement;

    @PutMapping("/{timetableId}")
    public ResponseEntity<Void> editTimetable(
            @PathVariable("timetableId") long timetableId,
            @RequestBody EditTimetableReq req
    ) {
        timetableManagement.editTimetable(timetableId, req);
        return ResponseEntity.ok().build();
    }
}
