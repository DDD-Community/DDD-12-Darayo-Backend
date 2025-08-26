package ddd.darayo.festival.common;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("test")
public abstract class ContainerBaseTest {
    private static final String ROOT = "root";
    private static final String ROOT_PASSWORD = "test";

    @Autowired
    private DataInitializer dataInitializer;

    protected static MySQLContainer<?> mysql;

    static {
        mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                .withDatabaseName("test")
                .withUsername(ROOT)
                .withEnv("MYSQL_ROOT_PASSWORD", ROOT_PASSWORD)
                .withInitScript("testdb/schema.sql");

        mysql.start();
    }

    @DynamicPropertySource
    static void configureProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", () -> ROOT);
        registry.add("spring.datasource.password", () -> ROOT_PASSWORD);
    }

    @BeforeEach
    void setData() {
        dataInitializer.setUpInitData();
    }

    @AfterEach
    void cleanTables() {
        dataInitializer.deleteAll();
    }
}
