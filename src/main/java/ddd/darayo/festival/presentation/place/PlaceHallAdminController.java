package ddd.darayo.festival.presentation.place;

import ddd.darayo.festival.presentation.place.exchanges.EditHallReq;
import ddd.darayo.festival.presentation.place.exchanges.AddPlaceHallReq;

import ddd.darayo.festival.domain.entity.PerformanceHall;
import ddd.darayo.festival.domain.service.PlaceManagement;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/admin/place/{placeId}/hall")
@RequiredArgsConstructor
public class PlaceHallAdminController {
    private final PlaceManagement placeManagement;

    @PutMapping("/{hallId}")
    public ResponseEntity<Void> editHall(
            @PathVariable Long hallId,
            @RequestBody EditHallReq editHallReq
    ) {
        placeManagement.editHall(hallId, editHallReq);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Long> addHall(
            @PathVariable Long placeId,
            @RequestBody AddPlaceHallReq addPlaceHallReq
    ) {
        PerformanceHall hall = placeManagement.addHall(placeId, addPlaceHallReq);
        return ResponseEntity.ok(hall.getId());
    }
}
