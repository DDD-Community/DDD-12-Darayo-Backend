package ddd.darayo.festival.presentation.place;

import ddd.darayo.festival.domain.entity.PerformanceHall;
import ddd.darayo.festival.domain.entity.PerformancePlace;
import ddd.darayo.festival.domain.service.PlaceManagement;
import ddd.darayo.festival.presentation.place.exchanges.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/place")
public class PlaceController {
    private final PlaceManagement placeManagement;

    @PostMapping
    public ResponseEntity<Long> createPlace(
            @RequestBody AddPlaceReq addPlaceReq
    ) {
        PerformancePlace newPlace = placeManagement.createNewPlace(addPlaceReq);
        return ResponseEntity.ok(newPlace.getId());
    }

    @GetMapping
    public ResponseEntity<List<GetAllPlaceRes>> getPlaces() {
        return ResponseEntity.ok(placeManagement.getAllPlaces());
    }

    @PutMapping("/{placeId}")
    public ResponseEntity<Void> deletePlace(
            @PathVariable long placeId,
            @RequestBody EditPlaceReq editPlaceReq
    ) {
        placeManagement.editPlace(placeId, editPlaceReq);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/hall/{hallId}")
    public ResponseEntity<Void> editHall(
            @PathVariable Long hallId,
            @RequestBody EditHallReq editHallReq
    ) {
        placeManagement.editHall(hallId, editHallReq);
        return ResponseEntity.ok().build();
    }
}
