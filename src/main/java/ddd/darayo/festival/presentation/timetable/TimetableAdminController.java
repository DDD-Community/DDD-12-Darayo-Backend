package ddd.darayo.festival.presentation.timetable;

import ddd.darayo.festival.domain.service.TimetableManagement;
import ddd.darayo.festival.presentation.timetable.exchanges.AddTimetableArtistReq;
import ddd.darayo.festival.presentation.timetable.exchanges.EditTimetableReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/timetable")
@RequiredArgsConstructor
public class TimetableAdminController {
    private final TimetableManagement timetableManagement;

    @PutMapping("/{timetableId}")
    public ResponseEntity<Void> editTimetable(
            @PathVariable("timetableId") Long timetableId,
            @RequestBody EditTimetableReq req
    ) {
        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Seoul"));
        timetableManagement.editTimetable(timetableId, req, now);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{timetableId}/artist")
    public ResponseEntity<Void> putTimetableArtist(
            @PathVariable("timetableId") Long timetableId,
            @RequestBody AddTimetableArtistReq req
    ) {
        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Seoul"));
        timetableManagement.putTimetableArtist(timetableId, req.artistId(), req.content().participationType(), now);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{timetableId}/artist/{artistId}")
    public ResponseEntity<Void> deleteTimetableArtist(
            @PathVariable("timetableId") Long timetableId,
            @PathVariable("artistId") Long artistId
    ) {
        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Seoul"));
        timetableManagement.deleteTimetableArtist(timetableId, artistId, now);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{timetableId}")
    public ResponseEntity<Void> deleteTimetable(
            @PathVariable Long timetableId
    ) {
        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.of("Asia/Seoul"));
        timetableManagement.deleteTimetable(timetableId, now);
        return ResponseEntity.noContent().build();
    }
}
