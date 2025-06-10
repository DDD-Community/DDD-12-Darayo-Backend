package ddd.darayo.festival.presentation.artist;

import ddd.darayo.festival.domain.service.ArtistManagement;
import ddd.darayo.festival.presentation.artist.exchanges.ArtistDetailRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/artist")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistManagement artistManagement;


}
