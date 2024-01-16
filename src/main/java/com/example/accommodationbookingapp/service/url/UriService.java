package com.example.accommodationbookingapp.service.url;

import java.net.URI;
import java.net.URL;

public interface UriService {
    URI buildUriWithSessionId(String sessionId, URL baseUrl);
}
