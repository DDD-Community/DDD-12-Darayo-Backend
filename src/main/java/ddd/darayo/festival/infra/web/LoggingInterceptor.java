package ddd.darayo.festival.infra.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

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

        String requestId = UUID.randomUUID().toString();
        StringBuilder headerBuilder = new StringBuilder();
        if (request.getHeaderNames().hasMoreElements()) {
            headerBuilder.append("{\n");
            request.getHeaderNames().asIterator().forEachRemaining(h -> {
                headerBuilder.append("\t").append(h);
                headerBuilder.append(": ");
                headerBuilder.append(request.getHeader(h));
                headerBuilder.append("\n");
            });
            headerBuilder.append("}");
        }

        log.info("\n[{}] START {} {} {}\n" +
                "Headers: {}\n" +
                "Request Body: {}\n",
                requestId, request.getMethod(), request.getRequestURI(), request.getQueryString(),
                headerBuilder,
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

        log.info("[{}] END Duration: {} ms Status: {}", requestId, elapsedMs, response.getStatus());
    }
}
