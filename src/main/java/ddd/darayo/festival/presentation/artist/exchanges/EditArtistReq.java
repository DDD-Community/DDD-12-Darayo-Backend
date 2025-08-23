package ddd.darayo.festival.presentation.artist.exchanges;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ddd.darayo.festival.domain.dto.ArtistContentDTO;

public record EditArtistReq(
        @JsonUnwrapped
        ArtistContentDTO content
) { }
