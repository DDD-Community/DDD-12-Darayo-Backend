package ddd.darayo.festival.presentation.applemusic.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ArtistResponse {
    private final String artworkUrl;
    private final List<String> genreNames;
    private final String name;

    public ArtistResponse(String artworkUrl, List<String> genreNames, String name) {
        this.artworkUrl = artworkUrl;
        this.genreNames = genreNames;
        this.name = name;
    }
}
