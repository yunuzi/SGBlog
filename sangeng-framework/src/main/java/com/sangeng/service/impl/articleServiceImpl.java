package com.sangeng.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.ArticleTag;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.*;
import com.sangeng.mapper.articleMapper;
import com.sangeng.service.ArticleTagService;
import com.sangeng.service.CategoryService;
import com.sangeng.service.articleService;
import com.sangeng.constants.utils.BeanCopyUtils;
import com.sangeng.constants.utils.RedisCache;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class articleServiceImpl extends ServiceImpl<articleMapper, Article> implements articleService {


    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;



    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多查询10条数据
        Page<Article> page = new Page(1,10);
        page(page, queryWrapper);

        List<Article> records = page.getRecords();
        //前端不需要这么多数据，使用VO进行封装
        //使用Bean拷贝将records拷贝进list
        List<HotArticleVo> list = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);


        return ResponseResult.okResult(list);
    }

    @Override
    public ResponseResult articleList(Integer pageNm, Integer pageSize, Long categoryId) {
        /**
         *在首页和分类页面都需要查询文章列表。
         *
         *首页：查询所有的文章 不会传入categoryId
         *
         *分类页面：查询对应分类下的文章  会传入对应的categoryId
         *
         * 要求：①只能查询正式发布的文章 ②置顶的文章要显示在最前面  对isTop进行升序排列
         */
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        if(categoryId != null && categoryId > 0){
            //需要添加categoryId查询条件
            queryWrapper.eq(Article::getCategoryId,categoryId);
        }
        //正式发布的文章
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);

        //置顶的文章要显示在最前面  对isTop进行升序排列
        queryWrapper.orderByAsc(Article::getIsTop);

        if(pageNm == null){
            pageNm = 1;
        }
        //分页查询
        Page<Article> page = new Page<>(pageNm,pageSize);

        page(page, queryWrapper);
        List<Article> records = page.getRecords();
        //查询categoryName
        records.stream()
                .map(new Function<Article, Article>() {
                    @Override
                    public Article apply(Article article) {
                        Category category = categoryService.getById(article.getCategoryId());
                        article.setCategoryName(category.getName());
                        return article;
                    }
                }).collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(records, ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    /**
     * 浏览文章
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticleDetails(Long id) {
        //查询数据库
        Article article = getById(id);
        //查询redis中的浏览量
         Integer viewCount = redisCache.getCacheMapValue("articleViewCount", id.toString());
         article.setViewCount(Long.valueOf(viewCount));

        //封装VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);

        //查询categoryName
        if(articleDetailVo.getId() != null){
            articleDetailVo.setCategoryName(categoryService.getById(articleDetailVo.getId()).getName());
        }
        //返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("articleViewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addArticle(ArticleAddVo articleAddVo) {
    //新增sg_article表
        Article article = BeanCopyUtils.copyBean(articleAddVo, Article.class);
        save(article);

        //更新sg_article_tag表
        List<Long> articleTagIds = articleAddVo.getTags();
        List<ArticleTag> articleTags = articleTagIds.stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);


        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> adminArticleList(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.hasText(title),Article::getTitle,title);
        queryWrapper.like(Strings.hasText(summary),Article::getTitle,summary);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult articleJump(Long id) {
        //根据id查询到对应的文章
        Article article = getById(id);
        ArticleJumpVo articleJumpVo = BeanCopyUtils.copyBean(article, ArticleJumpVo.class);

        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
         //根据id查询对应的tags
        List<ArticleTag> tags = articleTagService.list(queryWrapper);
        List<Long> tagIds = tags.stream()
                .map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());

        articleJumpVo.setTags(tagIds);
        return ResponseResult.okResult(articleJumpVo);
    }

    @Override
    public ResponseResult deleteArticle(String id) {
        removeById(Long.parseLong(id));
        return ResponseResult.okResult();
    }
}
