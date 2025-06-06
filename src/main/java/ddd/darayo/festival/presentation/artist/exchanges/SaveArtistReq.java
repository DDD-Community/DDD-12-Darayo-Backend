package ddd.darayo.festival.presentation.artist.exchanges;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveArtistReq {
    private String name;
    private String description;
}
