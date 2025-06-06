package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.Artist;
import ddd.darayo.festival.domain.entity.ArtistAlias;
import ddd.darayo.festival.domain.repository.ArtistRepository;
import ddd.darayo.festival.presentation.artist.exchanges.SaveArtistReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistManagement {
    private ArtistRepository artistRepository;

    public Artist createArtist(SaveArtistReq dto) {
        Artist artist = new Artist(dto.getName(), dto.getDescription());
        artist.addAlias(new ArtistAlias(null, dto.getName(), null));
        return artistRepository.save(artist);
    }
}
