package com.example.travelshop.service;

import com.example.travelshop.domain.TravelProduct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.List;

@Component
public class ImagePathResolver {

    private static final List<String> EXTS = List.of(".jpeg", ".jpg", ".png", ".webp");

    public String resolveFor(TravelProduct p) {
        String baseKey = pickKey(p);              
        if (baseKey == null || baseKey.isBlank()) return "/images/default.jpeg";

        String slug = slugify(baseKey);           
        for (String ext : EXTS) {
            String cp = "static/images/" + slug + ext;            
            if (new ClassPathResource(cp).exists()) {
                return "/images/" + slug + ext;                   
            }
        }
        return "/images/default.jpeg";
    }

    private String pickKey(TravelProduct p) {
        if (p.getTitle() != null && !p.getTitle().isBlank()) return p.getTitle();
        if (p.getCategory() != null && !p.getCategory().isBlank()) return p.getCategory();
        return null;
    }

    private String slugify(String s) {
        String n = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");            
        n = n.toLowerCase().trim().replaceAll("\\s+", "-");
        n = n.replaceAll("[^a-z0-9\\-]", "");
        return n;
    }
}
