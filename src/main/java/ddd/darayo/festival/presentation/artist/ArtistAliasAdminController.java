package ddd.darayo.festival.presentation.artist;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import lombok.RequiredArgsConstructor;

import ddd.darayo.festival.domain.dto.ArtistAliasContentDTO;
import ddd.darayo.festival.domain.service.ArtistManagement;
import ddd.darayo.festival.presentation.artist.exchanges.EditArtistAliasReq;

@RestController
@RequestMapping("/api/admin/artist/{artistId}/alias")
@RequiredArgsConstructor
public class ArtistAliasAdminController {
    private final ArtistManagement artistManagement;

    @PostMapping
    public ResponseEntity<Void> addArtistAlias(
        @PathVariable Long artistId,
        @RequestBody ArtistAliasContentDTO req
    ) {
        artistManagement.addArtistAlias(artistId, req.alias());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{aliasId}")
    public ResponseEntity<Void> editArtistAlias(
        @PathVariable Long aliasId,
        @RequestBody EditArtistAliasReq req
    ) {
        artistManagement.editArtistAlias(req, aliasId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{aliasId}")
    public ResponseEntity<Void> deleteArtistAlias(
        @PathVariable Long aliasId
    ) {
        artistManagement.deleteArtistAlias(aliasId);
        return ResponseEntity.ok().build();
    }
}
