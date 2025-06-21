package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.PerformanceHall;
import ddd.darayo.festival.domain.entity.PerformancePlace;
import ddd.darayo.festival.domain.repository.PerformanceHallRepository;
import ddd.darayo.festival.domain.repository.PerformancePlaceRepository;
import ddd.darayo.festival.domain.service.mapper.PlaceHallMapper;
import ddd.darayo.festival.domain.service.mapper.PlaceMapper;
import ddd.darayo.festival.presentation.place.exchanges.AddPlaceReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceManagement {
    private final PlaceMapper placeMapper;
    private final PlaceHallMapper placeHallMapper;
    private final PerformancePlaceRepository performancePlaceRepository;

    public PerformancePlace createNewPlace(AddPlaceReq req) {
        PerformancePlace placeEntity = placeMapper.toPlaceEntity(req);
        for (String hall : req.placeHalls()) {
            PerformanceHall hallEntity = placeHallMapper.toHallEntity(hall);
            placeEntity.addHall(hallEntity);
        }
        return performancePlaceRepository.save(placeEntity);
    }



}
