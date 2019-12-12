package com.shsxt.crm.controller;

import com.github.pagehelper.PageInfo;
import com.shsxt.base.BaseController;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.query.CusDevPlanQuery;
import com.shsxt.crm.service.CusDevPlanService;
import com.shsxt.crm.service.SaleChanceService;
import com.shsxt.crm.vo.CusDevPlan;
import com.shsxt.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/12/6.
 */
@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {
    @Resource
    private CusDevPlanService cusDevPlanService;
    @Resource
    private SaleChanceService saleChanceService;
    @RequestMapping("index")
    public String index(){
        return "cus_dev_plan";
    }


    @RequestMapping("toCusDevPlanManager")
    public String toCusDevPlanManager(Integer sid, Model model){
        SaleChance saleChance = saleChanceService.queryById(sid);
        model.addAttribute(saleChance);
        return "cus_dev_plan_manager";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlanParams(CusDevPlanQuery cusDevPlanQuery,
                                                    @RequestParam(defaultValue = "1") Integer total,
                                                    @RequestParam(defaultValue = "10") Integer rows){
        PageInfo<CusDevPlan> cusDevPlanPageInfo = cusDevPlanService.queryForPage(cusDevPlanQuery);
        Map<String, Object> result = new HashMap<>();
        result.put("total",cusDevPlanPageInfo.getTotal());
        result.put("rows",cusDevPlanPageInfo.getList());
        return result;
    }
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(CusDevPlan cusDevPlan){

        cusDevPlanService.saveCusDevPlan(cusDevPlan);
        return success("计划项添加成功!");
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("计划项更新成功!");
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer[]ids){
        cusDevPlanService.deleteCusDevPlan(ids);
        return success("计划项数据删除失败!");
    }

}
