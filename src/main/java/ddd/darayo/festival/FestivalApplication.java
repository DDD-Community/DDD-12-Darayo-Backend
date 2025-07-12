package ddd.darayo.festival;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class FestivalApplication {

    @Value("${firebase.service-account-key}")
    private static String firebaseServiceAccountKeyPath;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FestivalApplication.class, args);
        
        // Firebase 초기화를 애플리케이션 시작 후에 실행
        String keyPath = context.getEnvironment().getProperty("firebase.service-account-key");
        initializeFirebase(keyPath);
    }

    private static void initializeFirebase(String keyPath) {
        try {
            Path serviceAccountKeyPath = Paths.get(keyPath);
            
            if (!Files.exists(serviceAccountKeyPath)) {
                log.warn("Firebase 서비스 계정 키 파일이 존재하지 않습니다: {}", keyPath);
                log.warn("Firebase 기능이 비활성화됩니다.");
                return;
            }
            
            log.info("Firebase 서비스 계정 키 파일 로드 중: {}", keyPath);
            
            try (FileInputStream serviceAccount = new FileInputStream(serviceAccountKeyPath.toFile())) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                log.info("Firebase 초기화가 성공적으로 완료되었습니다.");
            }
        } catch (IOException e) {
            log.error("Firebase 초기화 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("Firebase 초기화 실패", e);
        }
    }
}
