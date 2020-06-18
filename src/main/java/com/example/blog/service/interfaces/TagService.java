package com.example.blog.service.interfaces;

import com.example.blog.dto.TagCountDTO;

import java.util.Set;

public interface TagService {
    Set<TagCountDTO> countArticlesWithTag();
}
