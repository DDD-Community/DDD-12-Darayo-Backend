package ddd.darayo.festival.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String displayName;

    @Column(length = 512)
    private String description;

    @Column(length = 1024)
    private String imageUrl;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ArtistAlias> aliases = new ArrayList<>();

    public Artist(String displayName, String description, String imageUrl) {
        this.displayName = displayName;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Artist(long id) {
        this.id = id;
    }

    public void addAlias(ArtistAlias alias) {
        this.aliases.add(alias);
        alias.setArtist(this);
    }
}
