/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.project.app.common.httpclients;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.project.app.common.requests.SaveCityRequest;
import io.vavr.control.Try;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author armena
 */
@Service
@Component
@Slf4j
public class CacheHttpClient {

    /**
     * Discovery Client
     */
    @Autowired
    private EurekaClient discoveryClient;

    /**
     * Rest template
     */
    @Autowired
    private RestTemplate restTemplate;

    public Optional<String> getCityByKey(String key) {

        String homePage = this.serviceUrl();
        String url = homePage + "api/v2/caches/location?key=" + key;

        log.debug("URL  " + url);
        Try<String> result = Try.of(() -> this.restTemplate.getForObject(url, String.class));

        if (!result.isSuccess()) {
            log.error("Could not get a data: ", result.getCause());
            return Optional.empty();
        }
        return Optional.ofNullable(result.get());
    }

    public String putDataIntoCache(SaveCityRequest saveCityRequest) {

        String homePage = this.serviceUrl();

        final HttpHeaders headers = new HttpHeaders();

        String url = homePage + "api/v2/caches/location";

        final Try<ResponseEntity<String>> result = Try.of(() -> this.restTemplate.exchange(url,
                HttpMethod.PUT, new HttpEntity<>(saveCityRequest, headers), String.class));

        if (!result.isSuccess()) {

            int statusCodeValue = result.get().getStatusCodeValue();

            if (statusCodeValue == HttpStatus.EXPECTATION_FAILED.value()) {

                log.error("Could not save to database");

                return result.get().getBody();
            }

            if (statusCodeValue == HttpStatus.BAD_REQUEST.value()) {

                log.error("Bad Request");

                return result.get().getBody();
            }

            return "Request To Cache service failed";
        }

        return result.get().getBody();
    }

    private String serviceUrl() {
        
        InstanceInfo instance = discoveryClient.getNextServerFromEureka("cacheservice", true);

        return instance.getHomePageUrl();
    }
}
