package ddd.darayo.festival.e2e.admin.sequential;

import com.fasterxml.jackson.databind.ObjectMapper;
import ddd.darayo.festival.common.ContainerBaseTest;
import ddd.darayo.festival.domain.constant.URLType;
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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Tag("sequential")
@Execution(ExecutionMode.SAME_THREAD)
public class AdminPerformanceUrlSequentialE2ETest extends ContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Test
    void add_update_delete_performance_url_updates_updatedAt() throws Exception {
        Long performanceId = 1L; // seeded

        // add
        LocalDateTime addStart = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        var addBody = Map.of(
                "url", "https://example.com/new-info",
                "type", URLType.HOMEPAGE.name()
        );
        mockMvc.perform(post("/api/admin/performance/{performanceId}/performanceURL", performanceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addBody)))
                .andExpect(status().isOk());
        LocalDateTime addEnd = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime afterAdd = performanceRepository.findById(performanceId).map(Performance::getUpdatedAt).orElseThrow();
        assertWithin100msWindow(afterAdd, addStart, addEnd);

        // find the created URL id by refetching performance detail list is not straightforward via controller; skip locating ID via API
        // Instead, update the first URL id (seed has one) to exercise update path
        LocalDateTime updateStart = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        var updateBody = Map.of(
                "url", "https://example.com/updated-info",
                "type", URLType.HOMEPAGE.name()
        );
        mockMvc.perform(put("/api/admin/performance/{performanceId}/performanceURL/{performanceURLId}", performanceId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBody)))
                .andExpect(status().isOk());
        LocalDateTime updateEnd = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime afterUpdate = performanceRepository.findById(performanceId).map(Performance::getUpdatedAt).orElseThrow();
        assertWithin100msWindow(afterUpdate, updateStart, updateEnd);

        // delete seeded URL id 1
        LocalDateTime deleteStart = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        mockMvc.perform(delete("/api/admin/performance/{performanceId}/performanceURL/{performanceURLId}", performanceId, 1L))
                .andExpect(status().isOk());
        LocalDateTime deleteEnd = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime afterDelete = performanceRepository.findById(performanceId).map(Performance::getUpdatedAt).orElseThrow();
        assertWithin100msWindow(afterDelete, deleteStart, deleteEnd);
    }

    private void assertWithin100msWindow(LocalDateTime actual, LocalDateTime start, LocalDateTime end) {
        LocalDateTime lower = start.minusNanos(100_000_000);
        LocalDateTime upper = end.plusNanos(100_000_000);
        assertThat(actual).isAfterOrEqualTo(lower);
        assertThat(actual).isBeforeOrEqualTo(upper);
    }
}
