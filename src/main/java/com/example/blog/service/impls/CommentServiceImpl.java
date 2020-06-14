package com.example.blog.service.impls;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.Comment;
import com.example.blog.model.User;
import com.example.blog.repository.ArticleRepository;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.interfaces.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ArticleRepository articleRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveComment(Comment comment, String email, Article articleId) {
        User user = userRepository.findByUsername(email).orElse(null);
        comment.setPost(articleId);
        comment.setAuthor(user);
        commentRepository.save(comment);
    }

    @Override
    public List<Comment> findAllComments(Article articleId) {
        return commentRepository.findAllByPost(articleId);
    }

    @Override
    public Comment findComment(Article articleId, Integer commentId) {
        return commentRepository.findByPostAndId(articleId, commentId).orElse(null);
    }

    @Override
    public void deleteComment(String email, Article articleId, Comment commentId) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(articleId.getAuthor().equals(user) && commentId.getAuthor().equals(user)){
            commentRepository.delete(commentId);
        }
    }
}
