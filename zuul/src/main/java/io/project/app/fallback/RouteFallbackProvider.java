package io.project.app.fallback;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RouteFallbackProvider implements FallbackProvider {

    public ClientHttpResponse fallbackResponse(Throwable cause) {
        return new ClientHttpResponse() {

            @Override
            public HttpStatus getStatusCode() {
                log.error("STATUS CODE: " + cause.getLocalizedMessage());

                return HttpStatus.SERVICE_UNAVAILABLE;
            }

            @Override
            public int getRawStatusCode() {
                log.error("RawStatusCode  ");
                log.error("ERROR: " + cause.getMessage());
                return HttpStatus.SERVICE_UNAVAILABLE.value();
            }

            @Override
            public String getStatusText() {
                log.error("Status TEXT " + HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
                log.error("ERROR: " + cause.getLocalizedMessage());
                return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() {
                if (cause != null && cause.getMessage() != null) {
                    log.error("Route:{} Message：{}", getRoute(), cause.getMessage());
                    return new ByteArrayInputStream(cause.getMessage().getBytes());
                } else {
                    log.error("Route:{} Message：{}", getRoute(), "Some error");
                    return new ByteArrayInputStream("Some bytes".getBytes());
                }
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }

    @Override
    public String getRoute() {
        log.info("Zuuul failback route");
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String s, Throwable throwable) {
        //log.error(throwable.fillInStackTrace() + "FallbackResponse ");
        return fallbackResponse(throwable.fillInStackTrace());
    }
}
