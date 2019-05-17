package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.BaseAttrInfo;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo> {
    // 根据三级分类Id进行查询 List<BaseAttrInfo>
    public List<BaseAttrInfo> getBaseAttrInfoListByCatalog3Id(long catalog3Id);
    // 根据平台属性值的Id 查询平台属性集合
    List<BaseAttrInfo> selectAttrInfoListByIds(@Param("valueIds") String valueIds);
}
