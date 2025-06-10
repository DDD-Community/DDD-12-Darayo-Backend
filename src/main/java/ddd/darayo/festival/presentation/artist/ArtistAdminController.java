package ddd.darayo.festival.presentation.artist;

import ddd.darayo.festival.domain.entity.Artist;
import ddd.darayo.festival.domain.exception.DomainException;
import ddd.darayo.festival.domain.service.ArtistManagement;
import ddd.darayo.festival.domain.service.AuthService;
import ddd.darayo.festival.presentation.artist.exchanges.SaveArtistAliasesReq;
import ddd.darayo.festival.presentation.artist.exchanges.SaveArtistReq;
import ddd.darayo.festival.presentation.exception.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/artist")
@RequiredArgsConstructor
public class ArtistAdminController {
    private final AuthService authService;
    private final ArtistManagement artistManagement;

    @PostMapping
    public ResponseEntity<Long> createArtist(@RequestBody SaveArtistReq req) {
        if (!authService.authenticate(req.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Artist artist = artistManagement.createArtist(req);
        return ResponseEntity.ok(artist.getId());
    }

    @PostMapping("/aliases")
    public ResponseEntity<Void> addArtistAlias(@RequestBody SaveArtistAliasesReq req) {
        if (!authService.authenticate(req.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            artistManagement.createArtistAlias(req);
        } catch (DomainException e) {
            throw APIException.from(e, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/aliases/{aliasId}")
    public ResponseEntity<Void> deleteArtistAlias(
            @PathVariable Long aliasId
    ) {
        try {
            artistManagement.deleteArtistAlias(aliasId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DomainException e) {
            throw APIException.from(e, HttpStatus.NOT_FOUND);
        }
    }
}
