package com.sangeng.controller;

import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Comment;
import com.sangeng.domain.entity.User;
import com.sangeng.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {


    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Long pageNum,Long pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_TYPE,articleId,pageNum,pageSize);
    }

    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    public ResponseResult commentList(Long pageNum,Long pageSize){
        return commentService.commentList(SystemConstants.LINK_TYPE,null,pageNum,pageSize);

    }

}
