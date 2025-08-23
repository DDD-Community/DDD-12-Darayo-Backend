package ddd.darayo.festival.e2e.admin.readonly;

import ddd.darayo.festival.common.ReadonlyContainerBaseTest;
import ddd.darayo.festival.common.ServiceTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ServiceTest
@AutoConfigureMockMvc
@Tag("readonly")
public class AdminPerformanceReadonlyGetE2ETest extends ReadonlyContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void GET_admin_performance_returns_seeded_list() throws Exception {
        mockMvc.perform(get("/api/admin/performance").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].performance").exists());
    }
}

