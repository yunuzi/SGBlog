package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.TagDto;
import com.sangeng.domain.entity.LoginUser;
import com.sangeng.domain.entity.Tag;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.TagVo;
import com.sangeng.mapper.TagMapper;
import com.sangeng.service.TagService;
import com.sangeng.constants.utils.BeanCopyUtils;
import com.sangeng.constants.utils.SecurityUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-04-12 20:21:03
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> tagPageList(Integer pageNum, Integer pageSize, String name, String remark) {

        //查询条件
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Strings.hasText(name),Tag::getName,name);
        queryWrapper.eq(Strings.hasText(remark),Tag::getRemark,remark);

        //分页查询
        Page page = new Page(pageNum,pageSize);
        page(page,queryWrapper);
        //转换成PageVo
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(TagDto tagDto) {
        //获取当前token
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        //更新操作
        save(tag);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(String id) {
        long tagId = Long.parseLong(id);
//        //查询到需要更改的tag
//        Tag tag = getById(tagId);
//        tag.setDelFlag(1);
//        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Tag::getId,tagId);
//        update(tag,queryWrapper);
          removeById(tagId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(String id) {
        long tagId = Long.parseLong(id);
//        通过id查询到需要更改的tag
        Tag tag = getById(tagId);
        //用TagVo
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        //封装
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult revise(TagDto tagDto) {
        Tag tag = getById(tagDto.getId());
        tag.setName(tagDto.getName());
        tag.setRemark(tagDto.getRemark());
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getDelFlag, SystemConstants.TAG_NORMAL_FLAG);
        List<Tag> tagList = list(queryWrapper);

        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tagList, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }
}

