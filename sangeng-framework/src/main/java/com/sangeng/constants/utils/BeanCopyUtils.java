package com.sangeng.constants.utils;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象
        V result = null;
        try {
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    public static <T,V> List<V> copyBeanList(List<T> list, Class<V> clazz){
        return list.stream()
                .map(T -> copyBean(T, clazz))//元素转换
                .collect(Collectors.toList());
    }
}
