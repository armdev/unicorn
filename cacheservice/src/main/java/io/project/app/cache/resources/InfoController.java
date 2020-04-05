/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.project.app.cache.resources;

import io.project.app.common.services.RandomUUID;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 *
 * @author armena
 */
@RestController
@RequestMapping("/api/v2/data")
@Slf4j
public class InfoController {

    @Autowired
    private RandomUUID randomUUID;

    @GetMapping("/random/cache")
    public Mono<String> fetch() {
        return Mono.just(randomUUID.randomIdProvider());
    }

    @GetMapping
    public String get() {
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            log.info("HOSTNAME IS " + InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException ex) {
        }
        final String date = new Date(System.currentTimeMillis()).toString();
        return hostName;
    }

}
