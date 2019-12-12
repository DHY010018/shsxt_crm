package com.shsxt.crm.service;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.shsxt.base.BaseService;
import com.shsxt.crm.query.CusDevPlanQuery;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.vo.CusDevPlan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/12/7.
 */
@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    public void saveCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setCreateDate(new Date());
        checkParams(cusDevPlan.getPlanDate(),cusDevPlan.getPlanItem(),cusDevPlan.getExeAffect());
            AssertUtil.isTrue(save(cusDevPlan)<1,"计划项数据添加失败!");
    }
    private void checkParams(Date planDate, String planItem, String exeAffect) {
        AssertUtil.isTrue(planDate == null,"请指定计划向时间");
        AssertUtil.isTrue(StringUtils.isBlank(planItem),"请输入计划项内容!");
        AssertUtil.isTrue(StringUtils.isBlank(exeAffect),"请输入执行效果!");
    }
    //计划项数据更新
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        //参数校验
        checkParams(cusDevPlan.getPlanDate(),cusDevPlan.getPlanItem(),cusDevPlan.getExeAffect());
        cusDevPlan.setUpdateDate(new Date());
            //判断数据是否存在
            AssertUtil.isTrue(cusDevPlan.getId()==null||queryById(cusDevPlan.getId())==null,"待更新计划项不存在!");
            AssertUtil.isTrue(update(cusDevPlan)<1,"计划项数据更新失败!");
    }
    //删除计划项数据
    public void deleteCusDevPlan(Integer[]ids){
        //判断前台传送过来id是否为空,或者数组长度是否为空
    AssertUtil.isTrue(ids==null||ids.length==0,"请选择待删除计划项数据!");
    //删除数据,判断删除结果的影响行数是否等于数组长度
    AssertUtil.isTrue(deleteBatch(ids)!=ids.length,"计划项数据删除失败!");
    }

}
