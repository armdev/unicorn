/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.project.app.location;

import io.project.app.common.httpclients.CacheHttpClient;
import io.project.app.common.requests.SaveCityRequest;
import io.project.app.common.services.RandomUUID;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 *
 * @author armena
 */
@RestController
@RequestMapping("/api/v2/data")
@Slf4j
@EnableFeignClients
public class LocationController {

    @Autowired
    private RandomUUID randomUUID;

    @Autowired
    private CacheHttpClient cacheHttpClient;

    @GetMapping("/random/location")
    public Mono<String> put(@RequestParam String city) {
        String randomIdProvider = randomUUID.randomIdProvider();

        log.info("Send city with id " + randomIdProvider);
        SaveCityRequest saveCityRequest = new SaveCityRequest();
        saveCityRequest.setCity(city);
        saveCityRequest.setKey(randomIdProvider);
        saveCityRequest.setExpireSeconds(30);
        String putDataIntoCache = cacheHttpClient.putDataIntoCache(saveCityRequest);
        log.info("Result from cache service: " + putDataIntoCache);

        return Mono.just(putDataIntoCache + " key is " + randomIdProvider);
    }

    @GetMapping("/random/location/key")
    public Mono<String> get(@RequestParam String key) {
       

        log.info("Get city with id " + key);
       
        Optional<String> cityByKey = cacheHttpClient.getCityByKey(key);
        log.info("Result from cache service: " + cityByKey.get());

        return Mono.just(key + " key and city returned " + cityByKey.get());
    }
}
