package io.project.app.common.services;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RandomUUID {

    public String randomIdProvider() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
