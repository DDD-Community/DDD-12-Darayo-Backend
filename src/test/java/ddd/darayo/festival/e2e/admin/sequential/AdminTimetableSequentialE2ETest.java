package ddd.darayo.festival.e2e.admin.sequential;

import com.fasterxml.jackson.databind.ObjectMapper;
import ddd.darayo.festival.common.ContainerBaseTest;
import ddd.darayo.festival.domain.constant.ParticipationType;
import ddd.darayo.festival.domain.entity.Performance;
import ddd.darayo.festival.domain.repository.PerformanceRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Tag("sequential")
@Execution(ExecutionMode.SAME_THREAD)
public class AdminTimetableSequentialE2ETest extends ContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Test
    void timetable_full_flow_updates_updatedAt() throws Exception {
        Long performanceId = 1L; // seeded
        Long hallId = 1L; // seeded hall for place 1
        // 1) create timetable
        LocalDateTime createStart = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        var createBody = Map.of(
                "performanceDate", LocalDate.now().plusDays(7).toString(),
                "startTime", LocalTime.of(18, 0).toString(),
                "endTime", LocalTime.of(21, 0).toString(),
                "hallId", hallId
        );
        String createdTimetableId = mockMvc.perform(post("/api/admin/performance/{performanceId}/timetable", performanceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBody)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        LocalDateTime createEnd = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime afterCreate = performanceRepository.findById(performanceId).map(Performance::getUpdatedAt).orElseThrow();
        assertWithin100msWindow(afterCreate, createStart, createEnd);

        // 2) edit timetable
        LocalDateTime editStart = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        var editBody = Map.of(
                "performanceDate", LocalDate.now().plusDays(8).toString(),
                "startTime", LocalTime.of(17, 0).toString(),
                "endTime", LocalTime.of(22, 0).toString(),
                "hallId", hallId
        );
        mockMvc.perform(put("/api/admin/performance/{performanceId}/timetable/{timetableId}", performanceId, Long.valueOf(createdTimetableId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editBody)))
                .andExpect(status().isOk());
        LocalDateTime editEnd = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime afterEdit = performanceRepository.findById(performanceId).map(Performance::getUpdatedAt).orElseThrow();
        assertWithin100msWindow(afterEdit, editStart, editEnd);

        // 3) add artist to timetable
        LocalDateTime addArtistStart = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        mockMvc.perform(put("/api/admin/performance/{performanceId}/timetable/{timetableId}/artist", performanceId, Long.valueOf(createdTimetableId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("artistId", 1L, "participationType", ParticipationType.MAIN.name()))))
                .andExpect(status().isOk());
        LocalDateTime addArtistEnd = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime afterArtistAdd = performanceRepository.findById(performanceId).map(Performance::getUpdatedAt).orElseThrow();
        assertWithin100msWindow(afterArtistAdd, addArtistStart, addArtistEnd);

        // 4) delete artist from timetable (artist id 1)
        LocalDateTime delArtistStart = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        mockMvc.perform(delete("/api/admin/timetable/{timetableId}/artist/{artistId}", Long.valueOf(createdTimetableId), 1L))
                .andExpect(status().isOk());
        LocalDateTime delArtistEnd = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime afterArtistDelete = performanceRepository.findById(performanceId).map(Performance::getUpdatedAt).orElseThrow();
        assertWithin100msWindow(afterArtistDelete, delArtistStart, delArtistEnd);

        // 5) delete timetable
        LocalDateTime delTbStart = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        mockMvc.perform(delete("/api/admin/timetable/{timetableId}", Long.valueOf(createdTimetableId)))
                .andExpect(status().isNoContent());
        LocalDateTime delTbEnd = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime afterDelete = performanceRepository.findById(performanceId).map(Performance::getUpdatedAt).orElseThrow();
        assertWithin100msWindow(afterDelete, delTbStart, delTbEnd);
    }

    private void assertWithin100msWindow(LocalDateTime actual, LocalDateTime start, LocalDateTime end) {
        LocalDateTime lower = start.minusNanos(100_000_000);
        LocalDateTime upper = end.plusNanos(100_000_000);
        assertThat(actual).isAfterOrEqualTo(lower);
        assertThat(actual).isBeforeOrEqualTo(upper);
    }
}
