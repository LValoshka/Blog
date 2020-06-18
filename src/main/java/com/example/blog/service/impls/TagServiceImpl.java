package com.example.blog.service.impls;

import com.example.blog.dto.TagCountDTO;
import com.example.blog.repository.ArticleRepository;
import com.example.blog.repository.TagRepository;
import com.example.blog.service.interfaces.TagService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;

    public TagServiceImpl(TagRepository tagRepository, ArticleRepository articleRepository) {
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Set<TagCountDTO> countArticlesWithTag() {
        return tagRepository.findAll().stream().map(tag -> new TagCountDTO(tag.getName(),
                articleRepository.findAllByTagSetIn(Collections.singleton(tag)).orElse(null).size()))
                .collect(Collectors.toSet());
    }
}
