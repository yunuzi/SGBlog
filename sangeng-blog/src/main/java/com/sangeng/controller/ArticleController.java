package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.service.articleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private articleService articleService;

//    @GetMapping("/")
//    public List<Article> test(){
//        return articleService.list();
//    }

    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){

        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNm,Integer pageSize,Long categoryId){
       return articleService.articleList(pageNm,pageSize,categoryId);

    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetails(@PathVariable Long id){
       return articleService.getArticleDetails(id);
    }


    /**
     * ②更新浏览量时去更新redsi中的数据
     * @param id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id ){
        return articleService.updateViewCount(id);
    }

}
