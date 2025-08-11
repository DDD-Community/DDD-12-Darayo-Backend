package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.PerformanceHall;
import ddd.darayo.festival.domain.entity.PerformancePlace;
import ddd.darayo.festival.domain.exception.constant.PlaceError;
import ddd.darayo.festival.domain.repository.PerformanceHallRepository;
import ddd.darayo.festival.domain.repository.PerformancePlaceRepository;
import ddd.darayo.festival.domain.service.mapper.MapperUtil;
import ddd.darayo.festival.domain.service.mapper.PlaceMapper;
import ddd.darayo.festival.presentation.place.exchanges.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            placeEntity.addHall(new PerformanceHall(null, hallDto.name(), null));
        }
        return performancePlaceRepository.save(placeEntity);
    }

    public List<GetAllPlaceRes> getAllPlaces() {
        return performancePlaceRepository.findAllPlacesFetched()
                .stream()
                .map(placeMapper::toGetAllPlaces)
                .toList();
    }

    public void editPlace(Long placeId, EditPlaceReq req, LocalDateTime now) {
        PerformancePlace place = performancePlaceRepository.findById(placeId)
                .orElseThrow(PlaceError.PLACE_NOT_EXIST::toException);

        place.update(req.content().name(), req.content().address());
        performanceRepository.findByPlace_Id(placeId).forEach(p -> p.touch(now));
    }

    public PerformanceHall addHall(Long placeId, AddPlaceHallReq req, LocalDateTime now) {
        PerformanceHall newHall = new PerformanceHall(null, req.content().name(), new PerformancePlace(placeId));
        PerformanceHall saved = performanceHallRepository.save(newHall);
        performanceRepository.findByPlace_Id(placeId).forEach(p -> p.touch(now));
        return saved;
    }

    public void editHall(Long hallId, EditHallReq req, LocalDateTime now) {
        PerformanceHall hall = performanceHallRepository.findById(hallId)
                .orElseThrow(PlaceError.PLACE_HALL_NOT_EXIST::toException);
        hall.updateName(req.content().name());
        performanceRepository.findByHallId(hallId).forEach(p -> p.touch(now));
    }
}
