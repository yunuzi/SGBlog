package com.sangeng.runner;

import com.sangeng.domain.entity.Article;
import com.sangeng.constants.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {


    @Autowired
    private RedisCache redisCache;
    @Autowired
    private com.sangeng.mapper.articleMapper articleMapper;

    /**
     * ①在应用启动时把博客的浏览量存储到redis中
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        //查询mysql中的数据  id 和 viewCount
        List<Article> articleList = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articleList.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));

        //保存到redis中
        redisCache.setCacheMap("articleViewCount",viewCountMap);
    }
}
