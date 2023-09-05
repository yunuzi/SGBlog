package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Comment;
import com.sangeng.domain.vo.CommentVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.mapper.CommentMapper;
import com.sangeng.service.CommentService;
import com.sangeng.service.UserService;
import com.sangeng.constants.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-04-03 19:49:02
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    UserService userService;

    @Override
    public ResponseResult commentList(String articleType, Long articleId, Long pageNum, Long pageSize) {
        //查找出 articleId 为-1 的评论（主评论）
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Comment::getRootId,-1);


        queryWrapper.eq(SystemConstants.ARTICLE_TYPE.equals(articleType),Comment::getArticleId,articleId);

        queryWrapper.eq(Comment::getType,articleType);

        //分页查询
        Page<Comment> page = new Page(pageNum,pageSize);
        page(page, queryWrapper);

        //给CommentVo中的 userName,toCommentUserName赋值
        List<CommentVo> commentList = getCommentList(page.getRecords());
        //子评论查询,给commentVo中的children赋值
        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }

        //放入pageVo中
        PageVo pageVo = new PageVo(commentList,page.getTotal());
        //封装并返回
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);

        return ResponseResult.okResult();
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);

        List<CommentVo> commentVos = getCommentList(comments);
        return commentVos;
    }

    /**
     * 给CommentVo中的 userName,toCommentUserName赋值
     * @param list
     * @return
     */
    private List<CommentVo> getCommentList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        for(CommentVo commentVo : commentVos){
            //根据creatBy 来查询用户的昵称
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUserName(nickName);

            //根据toCommentUserId来查询toCommentUserId（昵称）
            //要注意toCommentUserId不能为-1
            if(commentVo.getToCommentUserId() !=  -1){
                String nickName1 = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(nickName1);
            }

        }
        return commentVos;
    }


}

