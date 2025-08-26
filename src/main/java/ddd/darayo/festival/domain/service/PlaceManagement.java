package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.PerformanceHall;
import ddd.darayo.festival.domain.entity.PerformancePlace;
import ddd.darayo.festival.domain.exception.constant.PlaceError;
import ddd.darayo.festival.domain.repository.PerformanceHallRepository;
import ddd.darayo.festival.domain.repository.PerformancePlaceRepository;
import ddd.darayo.festival.domain.service.mapper.PlaceMapper;
import ddd.darayo.festival.infra.aop.TouchPerformanceUpdatedAt;
import ddd.darayo.festival.presentation.place.exchanges.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceManagement {
    private final PlaceMapper placeMapper;
    private final PerformancePlaceRepository performancePlaceRepository;
    private final PerformanceHallRepository performanceHallRepository;
    private final ddd.darayo.festival.domain.repository.PerformanceRepository performanceRepository;

    public PerformancePlace createNewPlace(AddPlaceReq req) {
        PerformancePlace placeEntity = placeMapper.toPlaceEntity(req.content());
        for (var hallDto : req.placeHalls()) {
            placeEntity.addHall(new PerformanceHall(null, hallDto, null));
        }
        return performancePlaceRepository.save(placeEntity);
    }

    public List<GetAllPlaceRes> getAllPlaces() {
        return performancePlaceRepository.findAllPlacesFetched()
                .stream()
                .map(placeMapper::toGetAllPlaces)
                .toList();
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.PLACE_ID, key = "#placeId")
    public void editPlace(Long placeId, EditPlaceReq req) {
        PerformancePlace place = performancePlaceRepository.findById(placeId)
                .orElseThrow(PlaceError.PLACE_NOT_EXIST::toException);

        place.update(req.content().name(), req.content().address());
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.PLACE_ID, key = "#placeId")
    public PerformanceHall addHall(Long placeId, AddPlaceHallReq req) {
        PerformanceHall newHall = new PerformanceHall(null, req.content().name(), new PerformancePlace(placeId));
        return performanceHallRepository.save(newHall);
    }

    @TouchPerformanceUpdatedAt(by = TouchPerformanceUpdatedAt.By.HALL_ID, key = "#hallId")
    public void editHall(Long hallId, EditHallReq req) {
        PerformanceHall hall = performanceHallRepository.findById(hallId)
                .orElseThrow(PlaceError.PLACE_HALL_NOT_EXIST::toException);
        hall.updateName(req.content().name());
    }
}
