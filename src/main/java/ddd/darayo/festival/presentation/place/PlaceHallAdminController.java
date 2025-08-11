package ddd.darayo.festival.presentation.place;

import ddd.darayo.festival.presentation.place.exchanges.EditHallReq;
import ddd.darayo.festival.presentation.place.exchanges.AddPlaceHallReq;

import ddd.darayo.festival.domain.entity.PerformanceHall;
import ddd.darayo.festival.domain.service.PlaceManagement;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

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
        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Seoul"));
        placeManagement.editHall(hallId, editHallReq, now);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Long> addHall(
            @PathVariable Long placeId,
            @RequestBody AddPlaceHallReq addPlaceHallReq
    ) {
        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Seoul"));
        PerformanceHall hall = placeManagement.addHall(placeId, addPlaceHallReq, now);
        return ResponseEntity.ok(hall.getId());
    }
}
