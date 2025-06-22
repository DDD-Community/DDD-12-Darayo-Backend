package ddd.darayo.festival.domain.service;

import ddd.darayo.festival.domain.entity.PerformanceHall;
import ddd.darayo.festival.domain.entity.PerformancePlace;
import ddd.darayo.festival.domain.exception.constant.PlaceError;
import ddd.darayo.festival.domain.repository.PerformancePlaceRepository;
import ddd.darayo.festival.domain.service.mapper.MapperUtil;
import ddd.darayo.festival.domain.service.mapper.PlaceMapper;
import ddd.darayo.festival.presentation.place.exchanges.AddPlaceReq;
import ddd.darayo.festival.presentation.place.exchanges.EditPlaceReq;
import ddd.darayo.festival.presentation.place.exchanges.GetAllPlaceRes;
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

    public PerformancePlace createNewPlace(AddPlaceReq req) {
        PerformancePlace placeEntity = placeMapper.toPlaceEntity(req);
        List<PerformanceHall> halls = MapperUtil.toPlaceHallEntity(req.placeHalls());
        for (PerformanceHall hall : halls) {
            placeEntity.addHall(hall);
        }
        return performancePlaceRepository.save(placeEntity);
    }

    public List<GetAllPlaceRes> getAllPlaces() {
        return performancePlaceRepository.findAllPlacesFetched()
                .stream()
                .map(placeMapper::toGetAllPlaces)
                .toList();
    }

    public void editPlace(Long placeId, EditPlaceReq req) {
        PerformancePlace place = performancePlaceRepository.findById(placeId)
                .orElseThrow(PlaceError.PLACE_NOT_EXIST::toException);

        place.update(req.placeName(), req.address());
    }

}
