package com.sangeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.vo.MenuVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.service.MenuService;
import com.sangeng.constants.utils.BeanCopyUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult list(String status,String menuName){
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.hasText(status),Menu::getStatus,status);
        queryWrapper.like(Strings.hasText(menuName),Menu::getMenuName,menuName);
        List<Menu> menuList = menuService.list(queryWrapper);
        return ResponseResult.okResult(menuList);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult jumpMenu(@PathVariable Long id){
        Menu menu = menuService.getById(id);
        MenuVo menuVo = BeanCopyUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @PutMapping()
    public ResponseResult update(@RequestBody Menu menu){
        System.out.println();
        if(menu.getId() != menu.getParentId()){
            menuService.updateById(menu);
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);

    }

    @DeleteMapping("/{menuId}")
    public ResponseResult delete(@PathVariable Long id){
        menuService.removeById(id);
        return ResponseResult.okResult();
    }

}
