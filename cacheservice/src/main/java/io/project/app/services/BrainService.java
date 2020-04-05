package io.project.app.services;

import io.project.app.domain.BrainData;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import io.project.app.repositories.BrainRepository;

/**
 *
 * @author armena
 */
@Service
@Component
@Slf4j
public class BrainService {

    @Autowired
    private BrainRepository searchRepository;

    @Autowired
    private RedisTemplate<String, BrainData> redisTemplate;

    public BrainData save(BrainData searchData) {
        return searchRepository.save(searchData);
    }

    public Optional<BrainData> find(String id) {
        return searchRepository.findById(id);

    }

    public Optional<BrainData> findByValue(String value) {
        return searchRepository.findByValue(value);

    }

    public void delete(BrainData value) {
        searchRepository.delete(value);

    }

    public BrainData search(String id) {
        log.info("Search by id " + id);
        BrainData get = redisTemplate.opsForValue().get(id);
        log.info(get.toString());
        return get;

    }

}
