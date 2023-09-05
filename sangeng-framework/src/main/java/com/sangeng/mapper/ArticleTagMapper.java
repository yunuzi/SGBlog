package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-15 19:19:31
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {


}
