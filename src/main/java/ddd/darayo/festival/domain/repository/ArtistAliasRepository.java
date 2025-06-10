package ddd.darayo.festival.domain.repository;

import ddd.darayo.festival.domain.entity.ArtistAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistAliasRepository extends JpaRepository<ArtistAlias, Long> {
}
