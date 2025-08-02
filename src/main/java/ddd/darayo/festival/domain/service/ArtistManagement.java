package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.Artist;
import ddd.darayo.festival.domain.entity.ArtistAlias;
import ddd.darayo.festival.domain.repository.ArtistAliasRepository;
import ddd.darayo.festival.domain.repository.ArtistRepository;
import ddd.darayo.festival.domain.repository.projection.ArtistDetailProjection;
import ddd.darayo.festival.presentation.artist.exchanges.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.*;

import static ddd.darayo.festival.domain.exception.constant.ArtistError.ARTIST_ALIAS_NOT_EXISTS;
import static ddd.darayo.festival.domain.exception.constant.ArtistError.ARTIST_NOT_EXISTS;


@Service
@Transactional
@RequiredArgsConstructor
public class ArtistManagement {
    private final ArtistRepository artistRepository;
    private final ArtistAliasRepository artistAliasRepository;

    public Artist createArtist(SaveArtistReq dto) {
        Artist artist = new Artist(dto.getName(), dto.getDescription());

        val aliasList = dto.getAliasList();

        aliasList.add(dto.getName()); // 자기 자신은 반드시 별칭으로 추가

        aliasList.stream()
                .filter(Objects::nonNull) // null 방지
                .distinct() // 중복 방지
                .forEach(alias -> artist.addAlias(new ArtistAlias(null, alias, null)));

        return artistRepository.save(artist);
    }

    public void createArtistAlias(SaveArtistAliasesReq dto) {
        Artist artist = artistRepository.findById(dto.getArtistId())
                .orElseThrow(ARTIST_NOT_EXISTS::toException);
        for (String alias : dto.getAliases()) {
            artist.addAlias(new ArtistAlias(null, alias, null));
        }
    }

    public void addArtistAlias(Long artistId, String alias) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(ARTIST_NOT_EXISTS::toException);
        artist.addAlias(new ArtistAlias(null, alias, null));
    }

    public void editArtist(EditArtistReq req, long artistId) {
        int result = artistRepository.updateArtistById(artistId, req.name(), req.description());
        if (result < 1) {
            throw ARTIST_NOT_EXISTS.toException();
        }
    }

    public void editArtistAlias(EditArtistAliasReq req, long aliasId) {
        int result = artistAliasRepository.updateArtistAlias(req.alias(), aliasId);
        if (result < 1) {
            throw ARTIST_ALIAS_NOT_EXISTS.toException();
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

    public void deleteArtistAlias(long aliasId) {
        if (!artistAliasRepository.existsById(aliasId)) {
            throw ARTIST_ALIAS_NOT_EXISTS.toException();
        }
        artistAliasRepository.deleteById(aliasId);
    }
}
