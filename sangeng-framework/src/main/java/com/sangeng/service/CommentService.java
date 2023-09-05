package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-04-03 19:49:02
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String articleType, Long articleId, Long pageNum, Long pageSize);

    ResponseResult addComment(Comment comment);
}
