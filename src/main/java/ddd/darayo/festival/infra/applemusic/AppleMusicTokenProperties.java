package ddd.darayo.festival.infra.applemusic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "applemusic")
public class AppleMusicTokenProperties {
    private String teamId;
    private String keyId;
    private String privateKeyPath;
}
