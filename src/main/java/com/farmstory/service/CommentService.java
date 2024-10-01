package com.farmstory.service;

import com.farmstory.dto.ArticleDTO;
import com.farmstory.dto.CommentDTO;
import com.farmstory.entity.Article;
import com.farmstory.entity.Comment;
import com.farmstory.entity.User;
import com.farmstory.repository.ArticleRepository;
import com.farmstory.repository.CommentRepository;
import com.farmstory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    public CommentDTO selectCommentByuid(int no, String uid){


        
        return null;
    }


    public CommentDTO insertComment(CommentDTO commentdto) {
        Comment comment  = modelMapper.map(commentdto, Comment.class);
        User user = User.builder()
                .uid(comment.getUser().getUid())
                .nick(comment.getUser().getNick())
                .build();
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);
        if(savedComment != null){
            articleService.updateCommentCount(comment.getParent(),true);
        }

        CommentDTO commentDTO = modelMapper.map(savedComment, CommentDTO.class);
        log.info("insert commentDTO : "+commentDTO.toString());
        return  commentDTO;
    }
    public CommentDTO selectComment(CommentDTO commentdto) {return null;}

   //일반 article
    public List<CommentDTO> selectComments(int no,String type) {
        if(type.equals("article")){
            Optional<List<Comment>> optList = commentRepository.findCommentsByParent(no);
            if (optList.isPresent()) {
                List<Comment> comments = optList.get();
                log.info(comments);
                return comments.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
            }
        }

        return null;


    }

    //cs article
    public List<CommentDTO> selectComments(String csNo,String type) {
        if(type.equals("cs")){
            Optional<List<Comment>> optList = commentRepository.findCommentsByCsParent(csNo);
            if (optList.isPresent()) {
                List<Comment> comments = optList.get();
                log.info(comments);
                return comments.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
            }
        }
        return null;

    }

    public CommentDTO updateComment(CommentDTO commentdto) {
        return null;
    }
    public int deleteComment(int no) {

       Optional<Comment> Comment = commentRepository.findById(no);

       if(Comment.isPresent()) {
           Comment comment = Comment.get();
           commentRepository.deleteById(comment.getNo());
           articleService.updateCommentCount(comment.getParent(),false);
           return comment.getParent();

       }
       return 0;


    }

}
