package ddd.darayo.festival.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("test")
public class DataInitializer {
    private static final int OFF = 0;
    private static final int ON = 1;
    private static final int FIRST_COLUMN = 1;
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager em;

    private final List<String> deleteDMLs = new ArrayList<>();

    @Transactional
    public void deleteAll() {
        if (deleteDMLs.isEmpty()) {
            init();
        }
        setForeignKeyEnabled(OFF);
        truncateAllTables();
        setForeignKeyEnabled(ON);
    }

    @Transactional
    public void setUpInitData() {
        try {
            Resource resource = new ClassPathResource("testdb/data.sql");
            ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
        } catch (SQLException e) {
            log.error("DATA INITALIZATION FAILED", e);
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    private void setForeignKeyEnabled(final int enabled) {
        em.createNativeQuery("SET foreign_key_checks = " + enabled).executeUpdate();
    }

    private void truncateAllTables() {
        deleteDMLs.stream()
                .map(em::createNativeQuery)
                .forEach(Query::executeUpdate);
    }

    private void init() {
        try (final Statement statement = dataSource.getConnection().createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SHOW TABLES ");

            while (resultSet.next()) {
                final String tableName = resultSet.getString(FIRST_COLUMN);
                deleteDMLs.add("TRUNCATE " + tableName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
