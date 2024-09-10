package com.example.spring_boot.service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import com.example.spring_boot.model.CamKey;

@Service
public class RedisService {
    private static final String CAMKEY_PREFIX = "camkey:";
    private static final String CASPORT_PREFIX = "casport:";

    @Autowired
    private RedisTemplate<String, List<String>> redisCamkeys;

    @Autowired
    private RedisTemplate<String, List<String>> redisCasportUsers;

    public Stream<String> getCamKeyKeys() {
        return redisCamkeys.scan(ScanOptions.scanOptions().match(CAMKEY_PREFIX + "*").build()).stream().map(s -> s.replace(CAMKEY_PREFIX, ""));
    }

    public List<String> getCamkeyControlSet(String camKey) {
        return redisCamkeys.opsForValue().get(String.format("%s%s", CAMKEY_PREFIX, camKey));
    }

    public void storeCamKey(CamKey camkey) {
        redisCamkeys.opsForValue().set(String.format("%s%s", CAMKEY_PREFIX, camkey.getName()), camkey.getControlSet(), Duration.ofDays(1));
    }

    public Stream<String> getCasportUserKeys() {
        return redisCasportUsers.scan(ScanOptions.scanOptions().match(CASPORT_PREFIX + "*").build()).stream().map(s -> s.replace(CASPORT_PREFIX, ""));
    }

    public List<String> getCasportControlSet(String subjectDn, String issuerDn) {
        return redisCasportUsers.opsForValue().get(String.format("%s%s:%s", CASPORT_PREFIX, subjectDn, issuerDn));
    }

    public void storeCasportControlSet(String subjectDn, String issuerDn, List<String> controlSet) {
        redisCasportUsers.opsForValue().set(String.format("%s%s:%s", CASPORT_PREFIX, subjectDn, issuerDn), controlSet, Duration.ofDays(1));
    }
}
