package ddd.darayo.festival.presentation.artist.exchanges;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SaveArtistReq {
    private String password;
    private String name;
    private String description;
    private List<String> aliasList;
}
