package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.CategoryAdminVo;
import com.sangeng.domain.vo.CategoryVo;
import com.sangeng.mapper.CategoryMapper;
import com.sangeng.service.CategoryService;
import com.sangeng.service.articleService;
import com.sangeng.constants.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-03-29 12:50:23
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private articleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        //1.只要求展示有发布正式文章的分类,从article中查询
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(lambdaQueryWrapper);
        //将articleList中的id取出来
        Set<Long> categoryId = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());


        //2.必须是正常状态的分类
        List<Category> categoryList = listByIds(categoryId);
        categoryList = categoryList.stream().filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装成VO
        List<CategoryVo> getCategoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);



        return ResponseResult.okResult(getCategoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,SystemConstants.STATUS_NORMAL);
        List<Category> categoryList = list(queryWrapper);


        List<CategoryAdminVo> categoryAdminVos = BeanCopyUtils.copyBeanList(categoryList, CategoryAdminVo.class);

        return ResponseResult.okResult(categoryAdminVos);
    }
}

