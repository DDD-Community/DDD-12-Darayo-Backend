package ddd.darayo.festival.presentation.applemusic;

import ddd.darayo.festival.infra.applemusic.AppleMusicClient;
import ddd.darayo.festival.presentation.applemusic.dto.AppleMusicSearchResponse;
import ddd.darayo.festival.presentation.applemusic.dto.ArtistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/applemusic")
@RequiredArgsConstructor
public class AppleMusicController {

    private final AppleMusicClient appleMusicClient;

    @GetMapping("/search")
    public ResponseEntity<List<ArtistResponse>> search(
            @RequestParam String term,
            @RequestParam String types
    ) throws Exception {
        AppleMusicSearchResponse response = appleMusicClient.search(term, types);

        if (response == null || response.getResults() == null || response.getResults().getArtists() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<ArtistResponse> artistsWithImageUrl = response.getResults().getArtists().getData().stream()
                .map(artistData -> {
                    String artworkUrl = artistData.getAttributes().getArtwork() != null ? artistData.getAttributes().getArtwork().getUrl() : null;
                    return new ArtistResponse(
                            artworkUrl,
                            artistData.getAttributes().getGenreNames(),
                            artistData.getAttributes().getName()
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(artistsWithImageUrl);
    }
}
