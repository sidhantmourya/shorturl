package com.url.shorturl.service;

import com.url.shorturl.model.UrlMapping;
import com.url.shorturl.repository.UrlRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlService {

    private UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    @Transactional
    public String shortenUrl(String longUrl)
    {
        Optional<UrlMapping> urlMapping = urlRepository.findByLongUrl(longUrl);
        if(urlMapping.isPresent())
        {
            return urlMapping.get().getShortCode();
        }

        String shortCode;
        SecureRandom secureRandom = new SecureRandom();
        String alphabet="abcdefghijklmnopqrstuvwxyz"+ "abcdefghijklmnopqrstuvwxyz".toUpperCase() + "0123456789";
        int shortLength = 7;

        do{

            StringBuilder sb = new StringBuilder();
            for(int i =0; i<shortLength;i++)
            {
                sb.append(alphabet.charAt(secureRandom.nextInt(alphabet.length())));
            }
            shortCode = sb.toString();
        }while (urlRepository.existsById(shortCode));

        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode(shortCode);
        mapping.setLongUrl(longUrl);
        mapping.setCreatedAt(LocalDateTime.now());

        urlRepository.save(mapping);
        return  shortCode;
    }

    @Transactional
    public Optional<String> retriveLongUrl(String shortCode)
    {
        Optional<UrlMapping> urlMapping = urlRepository.findById(shortCode);

        urlMapping.ifPresent(urlMap -> {
            urlMap.setClickCount(urlMap.getClickCount()+1);
            urlRepository.save(urlMap);

        });
        return  urlMapping.map(UrlMapping :: getLongUrl);
    }

    public Optional<UrlMapping> getUrlDetails(String shortCode) {

        return  urlRepository.findById(shortCode);
    }
}
