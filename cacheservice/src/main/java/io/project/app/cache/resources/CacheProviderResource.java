package io.project.app.cache.resources;

import io.project.app.common.requests.SaveCityRequest;
import io.project.app.domain.BrainData;
import io.project.app.services.BrainService;
import java.util.Optional;
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
    private BrainService brainService;

    @GetMapping(path = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> get(
            @Valid @RequestParam(name = "key", required = true) final String key
    ) {

        Optional<BrainData> data = brainService.find(key);

        if (data.isPresent()) {

            return ResponseEntity.status(HttpStatus.OK).body(data.get().getValue());
        }

        if (!data.isPresent()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Did not found data");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hey, bad request");

    }

    @PutMapping(path = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> put(@RequestBody SaveCityRequest saveCityRequest
    ) {

        BrainData data = new BrainData();
        data.setKey(saveCityRequest.getKey());
        data.setValue(saveCityRequest.getCity());
        BrainData savedData = brainService.save(data);
        
        if (savedData.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(savedData.getId());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not save:Bad Request");

    }

}
