package ddd.darayo.festival.presentation.artist.exchanges;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDetailRes {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AliasDetail {
        private Long id;
        private String name;
    }

    private Long id;
    private String name;
    private String description;
    private List<AliasDetail> aliases;
}
