package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.TagDto;
import com.sangeng.domain.entity.Tag;
import com.sangeng.domain.vo.PageVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-04-12 20:21:02
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> tagPageList(Integer pageNum, Integer pageSize, String name, String remark);

    ResponseResult addTag(TagDto tagDto);

    ResponseResult deleteTag(String id);

    ResponseResult getTag(String id);

    ResponseResult revise(TagDto tagDto);


    ResponseResult listAllTag();
}
