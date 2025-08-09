package ddd.darayo.festival.presentation.artist.exchanges;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import ddd.darayo.festival.domain.dto.ArtistContentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SaveArtistReq {
    private String password;
    @JsonUnwrapped
    private ArtistContentDTO content;
    private List<String> aliasList;
}
