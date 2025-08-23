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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Tag("sequential")
@Execution(ExecutionMode.SAME_THREAD)
public class AdminPerformanceSequentialE2ETest extends ContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Test
    void update_performance_updates_updatedAt() throws Exception {
        Long performanceId = 1L; // seeded
        // capture time window
        LocalDateTime tStart = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        var editBody = Map.of(
                "name", "수정된공연명",
                "startDate", LocalDate.now().toString(),
                "endDate", LocalDate.now().plusDays(1).toString(),
                "posterUrl", "https://example.com/new-poster.jpg",
                "banGoods", "금지물품",
                "transportationInfo", "교통정보",
                "remark", "비고"
        );

        mockMvc.perform(put("/api/admin/performance/{performanceId}", performanceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editBody)))
                .andExpect(status().isOk());

        LocalDateTime tEnd = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime updatedAt = performanceRepository.findById(performanceId).map(Performance::getUpdatedAt).orElseThrow();
        assertWithin100msWindow(updatedAt, tStart, tEnd);
    }

    private void assertWithin100msWindow(LocalDateTime actual, LocalDateTime start, LocalDateTime end) {
        // acceptable window: [start - 100ms, end + 100ms]
        LocalDateTime lower = start.minusNanos(100_000_000);
        LocalDateTime upper = end.plusNanos(100_000_000);
        assertThat(actual).isAfterOrEqualTo(lower);
        assertThat(actual).isBeforeOrEqualTo(upper);
    }
}
