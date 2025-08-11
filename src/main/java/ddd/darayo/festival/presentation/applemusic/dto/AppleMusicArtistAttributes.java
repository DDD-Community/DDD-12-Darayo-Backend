package ddd.darayo.festival.presentation.applemusic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppleMusicArtistAttributes {
    private AppleMusicArtwork artwork;
    private List<String> genreNames;
    private String name;
}
