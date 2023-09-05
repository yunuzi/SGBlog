package com.sangeng.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.RoleDto;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.service.RoleService;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize,String roleName,String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<Role>();
        queryWrapper.like(Strings.hasText(roleName), Role::getRoleName, roleName);
        queryWrapper.like(Strings.hasText(roleName), Role::getRoleName, roleName);
        queryWrapper.orderByAsc(Role::getRoleSort);
        //分页查询
        Page<Role> page = new Page<>(pageNum, pageSize);
        roleService.page(page, queryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleDto roleDto){
        Role role = roleService.getById(roleDto.getId());

        role.setStatus(roleDto.getStatus());

        roleService.save(role);
        return ResponseResult.okResult();

    }




}
