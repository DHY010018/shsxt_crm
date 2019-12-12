package com.shsxt.crm.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shsxt.base.BaseController;
import com.shsxt.crm.annotations.RequirePermission;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.service.SaleChanceService;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2019/12/5.
 */
@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
        @Resource
    private UserService userService;
    @Resource
    private SaleChanceService saleChanceService;

    @RequestMapping("index")
    public String index(){
        return "sale_chance";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object>queryByParmsSaleChance(SaleChanceQuery saleChanceQuery,
                                                    @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer rows,
                                                    @RequestParam(defaultValue = "0") Integer falg,HttpServletRequest request){
        saleChanceQuery.setPageNum(page);
        saleChanceQuery.setPageSize(rows);
       if(falg==1){
           //当falg==1的时候相当于进入了客户开发计划管理主页面(所以设置分配人)
        saleChanceQuery.setAssignMan(LoginUserUtil.releaseUserIdFromCookie(request));
       }
       return saleChanceService.querySaleChanceByPrams(saleChanceQuery);
    }

        @RequestMapping("save")
        @ResponseBody
        @RequirePermission(aclValue = "101002")
    public ResultInfo saveSaleChance(HttpServletRequest request,SaleChance saleChance){
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChance.setCreateMan(userService.queryById(userId).getTrueName());
           saleChanceService.saveSaleChance(saleChance);
        return success("营销数据添加成功!");
    }
    @RequestMapping("update")
    @ResponseBody
    @RequirePermission(aclValue = "101004")
    public ResultInfo upDateSaleChance(SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return success("营销数据更新成功!");
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer[]ids){
        saleChanceService.deleteSaleChance(ids);
        return  success("删除记录成功!");
    }
    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer devResult,Integer sid){
        saleChanceService.updateSaleChanceDevResult(devResult,sid);
        return success("营销机会开发状态更新成功!");
    }
}
