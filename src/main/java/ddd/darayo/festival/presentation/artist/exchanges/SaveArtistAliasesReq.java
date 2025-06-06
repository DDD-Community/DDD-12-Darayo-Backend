package ddd.darayo.festival.presentation.artist.exchanges;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SaveArtistAliasesReq {
    private Long artistId;
    private List<String> aliases;
}
