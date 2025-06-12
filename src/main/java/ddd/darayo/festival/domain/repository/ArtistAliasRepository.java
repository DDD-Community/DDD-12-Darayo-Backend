package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.ArtistAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistAliasRepository extends JpaRepository<ArtistAlias, Long> {
    @Query("UPDATE ArtistAlias al SET al.name = :alias WHERE al.id = :aliasId")
    @Modifying
    int updateArtistAlias(
            @Param("alias") String alias,
            @Param("aliasId") Long aliasId
    );
}
