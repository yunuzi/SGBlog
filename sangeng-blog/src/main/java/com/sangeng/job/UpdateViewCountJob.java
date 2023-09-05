package com.sangeng.job;

import com.sangeng.domain.entity.Article;
import com.sangeng.service.articleService;
import com.sangeng.constants.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private articleService articleService;

    /**
     * ③定时任务每隔59秒把Redis中的浏览量更新到数据库中
     */
    @Scheduled(cron = "0/59 * * * * ?")
    public void updateViewCount(){
        System.out.println("更新");
        //从redis中查询浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("articleViewCount");

        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库中
        articleService.updateBatchById(articles);

    }

}
