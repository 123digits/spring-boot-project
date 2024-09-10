package com.example.spring_boot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.spring_boot.service.RedisService;

@Controller
@RequestMapping("/cache")
@EnableScheduling
public class CacheController {
    @Autowired
    private final RedisService redisService;

    @PutMapping("/camkeys")
    public ResponseEntity<Void> putRefreshRedisCamKeys() {
        refreshRedisCamKeys();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/casport")
    public ResponseEntity<Void> putRefreshRedisCasport() {
        refreshRedisCasport();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/all")
    @Scheduled(cron = "0 0 0/12 * * *")
    public ResponseEntity<Void> putRefreshRedisAll() {
        refreshRedisCamKeys(); // 1st: refresh the camkeys list
        refreshRedisCasport(); // 2nd: refresh all user credentials
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void refreshRedisCamKeys() {
        // call camkeys endpoint
        // parse in streaming fashion with fasterxml
        // while not label and equal to camkeyList; loop
        // next() to "["
        // while next object not "]"
        // readObject(CamKey - jblocks)
        // redis put("camkey:" + camkey.getName(), camKey.getControlSet()) with 24 hour expiration
        // NOTE: redis will automatically expire old
    }

    private void refreshRedisCasport() {
        // loop over all keys with "casport:*"
        redisService.getCasportUserKeys().forEach(key -> {
            // split by ":" and 0th element is "casport", 1st element is subjectDn and 2nd element is issuerDn
            String[] parts = key.split(":");
            String subjectDn = parts[0];
            String issuerDn = parts[1];

            List<String> controlSet; 
            redisService.storeCasportControlSet(subjectDn, issuerDn, null);
        });

        // loop over all keys with "casport:*"
        // for each key
        //   split by ":" and 0th element is "casport", 1st element is subjectDn and 2nd element is issuerDn
        //   call insertUser(subjectDn, issuerDn);
    }

    public void insertUser(String subjectDn, String issuerDn) {

    }

    p
}
