package com.url.shorturl.controller;

import com.url.shorturl.model.UrlMapping;
import com.url.shorturl.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/urlservice")
public class UrlShortController {

    private UrlService urlService;

    @Value("${shortener.base.url:http://localhost:8080}")
    private String baseUrl;

    public UrlShortController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<Map<String,String>> shortenUrl(@RequestBody Map<String, String> request)
    {
        String longUrl = request.get("url");
        if(longUrl == null || longUrl.isEmpty())
        {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Url is empty"));
        }

        try{
            new URI(longUrl).toURL();
        }
        catch (MalformedURLException  |URISyntaxException e)
        {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "URL is invalid"));
        }

        String shortCode = urlService.shortenUrl(longUrl);
        String absoluteUrl = baseUrl + shortCode;

        return ResponseEntity.ok().body(Collections.singletonMap("shortUrl", absoluteUrl));

    }

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException
    {
        urlService.retriveLongUrl(shortCode).ifPresentOrElse(
                longUrl ->{
                    try{
                        response.sendRedirect(longUrl);
                    } catch (IOException e) {
                        System.err.println("Error redirecting to URL"+ longUrl);
                        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    }
                },
                () -> response.setStatus(HttpStatus.NOT_FOUND.value())
        );
    }

    @GetMapping("/details/{shortCode}")
    public ResponseEntity<UrlMapping> getDetails(@PathVariable String shortCode)
    {
        return urlService.getUrlDetails(shortCode).map(ResponseEntity :: ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


}
