package com.url.shorturl.repository;


import com.url.shorturl.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlMapping, String> {

    Optional<UrlMapping> findByLongUrl(String longUrl);

}

