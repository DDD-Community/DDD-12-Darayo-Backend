package ddd.darayo.festival.infra.applemusic;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
@Slf4j
@Component
@RequiredArgsConstructor
public class AppleMusicTokenProvider {

    private final AppleMusicTokenProperties properties;
    private final ResourceLoader resourceLoader;

    private String cachedToken;
    private Instant tokenExpiresAt;

    @PostConstruct
    public void init() throws Exception {
        try{
            generateAndCacheToken(); // 앱 시작 시 미리 생성
            log.info("[AppleMusic] - 토큰 생성 완료");
        } catch (Exception e) {
            log.warn("[AppleMusic] - Apple Music Developer Token 초기화 실패.");
            log.warn("[AppleMusic] - 실패 원인 : ", e);
        }
    }

    public synchronized String getDeveloperToken() throws Exception {
        if (cachedToken == null || Instant.now().isAfter(tokenExpiresAt.minusSeconds(60))) {
            generateAndCacheToken(); // 만료 임박하면 재생성
        }
        return cachedToken;
    }

    private void generateAndCacheToken() throws Exception {
        String privateKeyContent = loadPrivateKey(properties.getPrivateKeyPath());

        byte[] pkcs8Bytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
        KeyFactory kf = KeyFactory.getInstance("EC");
        ECPrivateKey privateKey = (ECPrivateKey) kf.generatePrivate(keySpec);

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                .keyID(properties.getKeyId())
                .type(JOSEObjectType.JWT)
                .build();

        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(15777000); // 6개월

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(properties.getTeamId())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiry))
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claims);
        signedJWT.sign(new ECDSASigner(privateKey));

        this.cachedToken = signedJWT.serialize();
        this.tokenExpiresAt = expiry;
    }

    private String loadPrivateKey(String path) throws Exception {
        Resource resource = resourceLoader.getResource(path);
        String content = new String(Files.readAllBytes(resource.getFile().toPath()));
        return content.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
    }
}
