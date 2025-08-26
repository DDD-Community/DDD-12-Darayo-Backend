package ddd.darayo.festival.presentation.artist.exchanges;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ddd.darayo.festival.domain.dto.ArtistAliasContentDTO;

public record EditArtistAliasReq(
        @JsonUnwrapped
        ArtistAliasContentDTO content
) { }
