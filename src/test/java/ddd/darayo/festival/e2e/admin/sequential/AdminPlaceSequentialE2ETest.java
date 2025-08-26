package ddd.darayo.festival.e2e.admin.sequential;

import com.fasterxml.jackson.databind.ObjectMapper;
import ddd.darayo.festival.common.ContainerBaseTest;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Tag("sequential")
@Execution(ExecutionMode.SAME_THREAD)
public class AdminPlaceSequentialE2ETest extends ContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Test
    void edit_place_and_hall_updates_related_performances_updatedAt() throws Exception {
        Long placeId = 1L; // seeded
        Long hallId = 1L; // seeded

        List<Performance> placePerformances = performanceRepository.findByPlace_Id(placeId);

        // edit place
        LocalDateTime editPlaceStart = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        var editPlaceBody = Map.of(
                "placeName", "장소수정",
                "address", "주소수정"
        );
        mockMvc.perform(put("/api/admin/place/{placeId}", placeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editPlaceBody)))
                .andExpect(status().isOk());
        LocalDateTime editPlaceEnd = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        List<Performance> placePerformancesAfter = performanceRepository.findByPlace_Id(placeId);
        for (Performance p : placePerformancesAfter) {
            assertWithin100msWindow(p.getUpdatedAt(), editPlaceStart, editPlaceEnd);
        }

        // edit hall
        Performance p1 = performanceRepository.findById(1L).orElseThrow();
        LocalDateTime beforeHallEdit = p1.getUpdatedAt();

        var editHallBody = Map.of("name", "홀수정");
        LocalDateTime editHallStart = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        mockMvc.perform(put("/api/admin/place/hall/{hallId}", hallId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editHallBody)))
                .andExpect(status().isOk());
        LocalDateTime editHallEnd = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime afterHallEdit = performanceRepository.findById(1L).orElseThrow().getUpdatedAt();
        assertWithin100msWindow(afterHallEdit, editHallStart, editHallEnd);
    }

    private void assertWithin100msWindow(LocalDateTime actual, LocalDateTime start, LocalDateTime end) {
        LocalDateTime lower = start.minusNanos(100_000_000);
        LocalDateTime upper = end.plusNanos(100_000_000);
        assertThat(actual).isAfterOrEqualTo(lower);
        assertThat(actual).isBeforeOrEqualTo(upper);
    }
}
