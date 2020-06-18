package com.example.blog.controller;

import com.example.blog.dto.TagCountDTO;
import com.example.blog.service.interfaces.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags_cloud")
    public ResponseEntity<Set<TagCountDTO>> cloudOfTags() {
        return new ResponseEntity<>(tagService.countArticlesWithTag(), HttpStatus.OK);
    }
}
