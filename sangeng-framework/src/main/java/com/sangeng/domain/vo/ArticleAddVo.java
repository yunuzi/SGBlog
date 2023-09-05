package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ArticleAddVo {

    //标题
    private String title;
    //缩略图
    private String thumbnail;
    //是否顶置
    private String isTop;
    //是否允许评论
    private String isComment;
    //文章内容
    private String content;

    //关联的标签id
    private List<Long> tags;
    //关联的分类id
    private Long categoryId;
    //文章摘要
    private String summary;
    //状态
    private String status;
}
