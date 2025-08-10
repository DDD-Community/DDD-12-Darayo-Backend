package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.Artist;
import ddd.darayo.festival.domain.repository.projection.ArtistDetailProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    @Query("""
            SELECT DISTINCT a.id as id, a.displayName as name, a.description as description, a.imageUrl as imageUrl,
            al.id as aliasId, al.name as alias
            FROM ArtistAlias al
            JOIN al.artist a
            """)
    List<ArtistDetailProjection> findAllArtistDetail();

    @Modifying
    @Query("""
        UPDATE Artist a 
        SET a.displayName = :displayName, 
            a.description = :description,
            a.imageUrl = :imageUrl
        WHERE a.id = :artistId
        """)
    int updateArtistById(
            @Param("artistId") Long artistId,
            @Param("displayName") String displayName,
            @Param("description") String description,
            @Param("imageUrl") String imageUrl
    );
}
