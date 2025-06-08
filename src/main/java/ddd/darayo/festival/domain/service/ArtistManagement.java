package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.Artist;
import ddd.darayo.festival.domain.entity.ArtistAlias;
import ddd.darayo.festival.domain.exception.constant.ArtistError;
import ddd.darayo.festival.domain.repository.ArtistRepository;
import ddd.darayo.festival.domain.repository.projection.ArtistDetailProjection;
import ddd.darayo.festival.presentation.artist.exchanges.ArtistDetailRes;
import ddd.darayo.festival.presentation.artist.exchanges.SaveArtistAliasesReq;
import ddd.darayo.festival.presentation.artist.exchanges.SaveArtistReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
@RequiredArgsConstructor
public class ArtistManagement {
    private final ArtistRepository artistRepository;

    public Artist createArtist(SaveArtistReq dto) {
        Artist artist = new Artist(dto.getName(), dto.getDescription());
        artist.addAlias(new ArtistAlias(null, dto.getName(), null));
        return artistRepository.save(artist);
    }

    public void createArtistAlias(SaveArtistAliasesReq dto) {
        Artist artist = artistRepository.findById(dto.getArtistId())
                .orElseThrow(ArtistError.ARTIST_NOT_EXISTS::toException);
        for (String alias : dto.getAliases()) {
            artist.addAlias(new ArtistAlias(null, alias, null));
        }
    }

    public List<ArtistDetailRes> findAllArtists() {
        List<ArtistDetailProjection> artists = artistRepository.findAllArtistDetail();
        Map<Long, ArtistDetailRes> artistsMap = new HashMap<>();

        for (ArtistDetailProjection artist : artists) {
            artistsMap.computeIfAbsent(
                artist.getId(),
                id -> new ArtistDetailRes(
                        artist.getId(),
                        artist.getName(),
                        artist.getDescription(),
                        new ArrayList<>()
                )
            )
            .getAliases()
            .add(new ArtistDetailRes.AliasDetail(artist.getAliasId(), artist.getAlias()));
        }
        return artistsMap.values().stream().toList();
    }
}
