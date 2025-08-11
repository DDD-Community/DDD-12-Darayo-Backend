package ddd.darayo.festival.infra.applemusic;

import com.fasterxml.jackson.databind.ObjectMapper;
import ddd.darayo.festival.presentation.applemusic.dto.AppleMusicSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class AppleMusicClient {

    private final AppleMusicTokenProvider appleMusicTokenProvider;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AppleMusicSearchResponse search(String term, String types) throws Exception {
        String token = appleMusicTokenProvider.getDeveloperToken();

        URI uri = UriComponentsBuilder
                .fromUriString("https://api.music.apple.com/v1/catalog/kr/search")
                .queryParam("term", term)
                .queryParam("types", types)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String responseBody = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class).getBody();
        return objectMapper.readValue(responseBody, AppleMusicSearchResponse.class);
    }
}
