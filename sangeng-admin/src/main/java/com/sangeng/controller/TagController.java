package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.TagDto;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, String name, String remark){

        return tagService.tagPageList(pageNum,pageSize,name,remark);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        return tagService.addTag(tagDto);
    }

    @DeleteMapping("/{id}")
    public  ResponseResult deleteTag(@PathVariable(value="id") String id){
        //数据库中该条数据还是存在的，只是修改了逻辑删除字段的值
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable String id){
        return tagService.getTag(id);
    }

    @PutMapping
    public ResponseResult revise(@RequestBody TagDto tagDto){
        return tagService.revise(tagDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }

}