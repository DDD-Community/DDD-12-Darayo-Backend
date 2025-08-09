package ddd.darayo.festival.presentation.performance;

import ddd.darayo.festival.domain.service.TimetableManagement;
import ddd.darayo.festival.domain.dto.TimetableArtistContentDTO;
import ddd.darayo.festival.presentation.timetable.exchanges.AddTimetableArtistReq;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/admin/performance/{performanceId}/timetable/{timetableId}/artist")
@RequiredArgsConstructor
public class PerformanceTimetableArtistAdminController {
    private final TimetableManagement timetableManagement;

    @PostMapping
    public ResponseEntity<Void> addTimetableArtist(
            @PathVariable("timetableId") Long timetableId,
            @RequestBody AddTimetableArtistReq req
    ) {
        timetableManagement.putTimetableArtist(timetableId, req.artistId(), req.content().participationType());
        return ResponseEntity.ok().build();
    }
    

    @PutMapping("/{artistId}")
    public ResponseEntity<Void> putTimetableArtist(
            @PathVariable("timetableId") Long timetableId,
            @PathVariable("artistId") Long artistId,
            @RequestBody TimetableArtistContentDTO req
    ) {
        timetableManagement.putTimetableArtist(timetableId, artistId, req.participationType());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> deleteTimetableArtist(
            @PathVariable("timetableId") Long timetableId,
            @PathVariable("artistId") Long artistId
    ) {
        timetableManagement.deleteTimetableArtist(timetableId, artistId);
        return ResponseEntity.ok().build();
    }
}  
