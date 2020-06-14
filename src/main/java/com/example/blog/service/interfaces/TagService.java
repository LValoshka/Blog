package com.example.blog.service.interfaces;

import com.example.blog.dto.TagCountDTO;

import java.util.List;

public interface TagService {
    List<TagCountDTO> countArticlesWithTag();
}
