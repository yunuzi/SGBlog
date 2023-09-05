package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.ArticleTag;
import com.sangeng.domain.vo.ArticleAddVo;
import com.sangeng.domain.vo.ArticleJumpVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.service.ArticleTagService;
import com.sangeng.service.articleService;
import com.sangeng.constants.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private articleService articleService;

    @Autowired
    private ArticleTagService articleTagService;

    @PostMapping
    public ResponseResult addArticle(@RequestBody ArticleAddVo articleAddVo){
        return articleService.addArticle(articleAddVo);
    }

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, String title, String summary){
        return articleService.adminArticleList(pageNum,pageSize,title,summary);
    }

    @GetMapping("/{id}")
    public ResponseResult articleJump(@PathVariable Long id){
        return articleService.articleJump(id);
    }

    @PutMapping
    public ResponseResult update(@RequestBody ArticleJumpVo articleJumpVo){
        //更新sg_article表
        Article article = BeanCopyUtils.copyBean(articleJumpVo, Article.class);
        articleService.updateById(article);
        //得到tags,然后更新sg_article_tag表
        List<Long> tags = articleJumpVo.getTags();
        List<ArticleTag> articleTagList = tags.stream()
                .map(aLong -> new ArticleTag(articleJumpVo.getId(), aLong)).collect(Collectors.toList());

        articleTagService.updateBatchById(articleTagList);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable String id){
        return articleService.deleteArticle(id);
    }

}
