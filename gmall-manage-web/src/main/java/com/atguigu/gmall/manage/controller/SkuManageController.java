package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.BaseAttrInfo;
import com.atguigu.gmall.SkuInfo;
import com.atguigu.gmall.SpuImage;
import com.atguigu.gmall.SpuSaleAttr;
import com.atguigu.gmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class SkuManageController {

    @Reference
    private ManageService manageService;
    @RequestMapping("spuImageList")
    @ResponseBody
    public List<SpuImage> spuImageList(String spuId){
        // 调用服务层查询数据
        return   manageService.getSpuImageList(spuId);
    }

    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<SpuSaleAttr> spuSaleAttrList(String spuId){
        return manageService.getSpuSaleAttrList(spuId);
    }

    @RequestMapping(value = "saveSku",method = RequestMethod.POST)
    @ResponseBody
    public void saveSku(SkuInfo skuInfo){
        manageService.saveSku(skuInfo);
    }
    //这个控制方法在ManageController里边已经有了
    /*@Reference
    ManageService manageService;
    //根据三级分类查询属性和属性值列表
    //attrInfoList
    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<BaseAttrInfo> getAttrList(@RequestParam Map<String,String> map){
        String catalog3Id = map.get("catalog3Id") ;
        List<BaseAttrInfo> attrList = manageService.getAttrList(catalog3Id);
        return attrList;
    }*/
}
