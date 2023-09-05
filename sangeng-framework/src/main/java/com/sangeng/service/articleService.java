package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.vo.ArticleAddVo;
import com.sangeng.domain.vo.PageVo;

import java.util.List;

public interface articleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNm, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetails(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(ArticleAddVo articleAddVo);

    ResponseResult<PageVo> adminArticleList(Integer pageNum, Integer pageSize, String title, String summary);


    ResponseResult articleJump(Long id);

    ResponseResult deleteArticle(String id);
}
