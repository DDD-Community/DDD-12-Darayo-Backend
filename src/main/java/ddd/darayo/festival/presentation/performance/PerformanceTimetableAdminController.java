package ddd.darayo.festival.presentation.performance;

import ddd.darayo.festival.domain.entity.Timetable;
import ddd.darayo.festival.domain.service.TimetableManagement;
import ddd.darayo.festival.presentation.performance.exchanges.AddTimetableReq;

import ddd.darayo.festival.presentation.timetable.exchanges.AddTimetableArtistReq;
import ddd.darayo.festival.presentation.timetable.exchanges.EditTimetableReq;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/admin/performance/{performanceId}/timetable")
@RequiredArgsConstructor
public class PerformanceTimetableAdminController {
    private final TimetableManagement timetableManagement;

    @PostMapping
    public ResponseEntity<Long> createTimetable(
            @PathVariable Long performanceId,
            @RequestBody AddTimetableReq req) {
        Timetable timetable = timetableManagement.addTimetable(performanceId, req);
        return ResponseEntity.ok(timetable.getId());
    }

    @PutMapping("/{timetableId}")
    public ResponseEntity<Void> editTimetable(
            @PathVariable("timetableId") Long timetableId,
            @RequestBody EditTimetableReq req
    ) {
        timetableManagement.editTimetable(timetableId, req);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{timetableId}/artist")
    public ResponseEntity<Void> putTimetableArtist(
            @PathVariable("timetableId") Long timetableId,
            @RequestBody AddTimetableArtistReq req
    ) {
        timetableManagement.putTimetableArtist(timetableId, req.artistId(), req.content().participationType());
        return ResponseEntity.ok().build();
    }
}
