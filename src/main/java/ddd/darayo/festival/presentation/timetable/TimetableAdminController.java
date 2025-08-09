package ddd.darayo.festival.presentation.timetable;

import ddd.darayo.festival.domain.service.TimetableManagement;
import ddd.darayo.festival.presentation.timetable.exchanges.AddTimetableArtistReq;
import ddd.darayo.festival.presentation.timetable.exchanges.EditTimetableReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{timetableId}/artist/{artistId}")
    public ResponseEntity<Void> deleteTimetableArtist(
            @PathVariable("timetableId") Long timetableId,
            @PathVariable("artistId") Long artistId
    ) {
        timetableManagement.deleteTimetableArtist(timetableId, artistId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{timetableId}")
    public ResponseEntity<Void> deleteTimetable(
            @PathVariable Long timetableId
    ) {
        timetableManagement.deleteTimetable(timetableId);
        return ResponseEntity.noContent().build();
    }
}
