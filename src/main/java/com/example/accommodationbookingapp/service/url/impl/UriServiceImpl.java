package com.example.accommodationbookingapp.service.url.impl;

import com.example.accommodationbookingapp.service.url.UriService;
import java.net.URI;
import java.net.URL;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UriServiceImpl implements UriService {
    @Override
    public URI buildUriWithSessionId(String sessionId, URL baseUrl) {
        return UriComponentsBuilder.fromUriString(String.valueOf(baseUrl))
                .queryParam("sessionId", sessionId)
                .build()
                .toUri();
    }
}
