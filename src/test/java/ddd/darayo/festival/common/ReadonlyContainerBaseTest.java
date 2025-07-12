package ddd.darayo.festival.common;

import org.junit.jupiter.api.AfterAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;


public abstract class ReadonlyContainerBaseTest {
    private static final String ROOT = "root";
    private static final String ROOT_PASSWORD = "test";

    protected static MySQLContainer<?> mysql;

    static {
        mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                .withDatabaseName("test")
                .withUsername(ROOT)
                .withEnv("MYSQL_ROOT_PASSWORD", ROOT_PASSWORD)
                .withInitScripts("testdb/schema.sql", "testdb/data.sql");

        mysql.start();
    }

    @AfterAll
    static void stopContainer() {
        mysql.stop();
    }

    @DynamicPropertySource
    static void configureProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", () -> ROOT);
        registry.add("spring.datasource.password", () -> ROOT_PASSWORD);
    }
}
