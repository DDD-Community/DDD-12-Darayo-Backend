package ddd.darayo.festival.infra.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse res, Object handler) {
        // 래핑하여 body 재사용 가능하게
        String body = "";
        if (request instanceof ContentCachingRequestWrapper w) {
            body = w.getContentAsString();
        }
        log.info(body);

        String requestId = UUID.randomUUID().toString();
        HttpServletRequest finalRequest = request;
        StringBuilder headerBuilder = new StringBuilder();
        if (request.getHeaderNames().hasMoreElements()) {
            headerBuilder.append("{\n");
            request.getHeaderNames().asIterator().forEachRemaining(h -> {
                headerBuilder.append("\t").append(h);
                headerBuilder.append(": ");
                headerBuilder.append(finalRequest.getHeader(h));
                headerBuilder.append("\n");
            });
            headerBuilder.append("}");
        }

        log.info("\n[{}] {} {} {}\n" +
                "Headers: {}\n" +
                "Request Body: {}\n",
                requestId, request.getMethod(), request.getRequestURI(), request.getQueryString(),
                headerBuilder.toString(),
                body
        );

        request.setAttribute("requestId", requestId);
        request.setAttribute("startNanos", System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String requestId = (String) request.getAttribute("requestId");
        long elapsedMs = (System.nanoTime() - (long) request.getAttribute("startNanos")) / 1_000_000;

        log.info("[{}] Duration: {} ms Status: {}", requestId, elapsedMs, response.getStatus());
    }
}
