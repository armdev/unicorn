package io.project.app.cache.resources;

import io.project.app.cache.services.CacheProviderService;
import io.project.app.common.requests.SaveCityRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@Slf4j
@RequestMapping("/api/v2/caches")
public class CacheProviderResource {

    @Autowired
    private CacheProviderService cacheProviderService;

    @GetMapping(path = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> get(
            @Valid @RequestParam(name = "key", required = true) final String key
    ) {

        long startTime = System.currentTimeMillis();
        log.info("------  start: request/response time in milliseconds: " + startTime + " ----");
        boolean exists = cacheProviderService.exists(key);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Did not found data");
        }
        String valueOfKey = cacheProviderService.get(key, String.class);
        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info("------  end: request/response time in milliseconds: " + elapsedTime + " ----");

        log.info("valueOfKey " + valueOfKey + " ----");

        return ResponseEntity.status(HttpStatus.OK).body(valueOfKey);

    }

    @PutMapping(path = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> put(@RequestBody SaveCityRequest saveCityRequest
    ) {

        boolean exists = cacheProviderService.exists(saveCityRequest.getKey());
        if (exists) {

            cacheProviderService.delete(saveCityRequest.getKey());

            boolean set = cacheProviderService.set(saveCityRequest.getKey(), saveCityRequest.getCity(), saveCityRequest.getExpireSeconds());

            if (set) {
                return ResponseEntity.status(HttpStatus.OK).body("Data cached, old is deleted");
            }

            if (!set) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not set cache, try again");
            }
        }

        if (!exists) {

            boolean set = cacheProviderService.set(saveCityRequest.getKey(), saveCityRequest.getCity(), saveCityRequest.getExpireSeconds());

            if (set) {
                return ResponseEntity.status(HttpStatus.OK).body("Data is cached");
            }

            if (!set) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not set cache");
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");

    }

}
