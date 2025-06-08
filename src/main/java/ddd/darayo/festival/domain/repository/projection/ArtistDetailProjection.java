package ddd.darayo.festival.domain.repository.projection;

public interface ArtistDetailProjection {
    Long getId();
    String getName();
    String getDescription();
    Long getAliasId();
    String getAlias();
}
