package com.shsxt.crm.service;

import com.github.pagehelper.PageInfo;
import com.shsxt.base.BaseService;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/12/5.
 */
@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    //营销机会管理//前台接收总数"total",详细信息"rows",选用map集合更恰当一点
    public Map<String,Object>querySaleChanceByPrams(SaleChanceQuery saleChanceQuery){
        //,调用分页查询
        PageInfo<SaleChance> pageInfo = queryForPage(saleChanceQuery);
        //创建map集合
        Map<String, Object> result =new HashMap<String,Object>();
        //将数据存入map集合并返回
        result.put("total",pageInfo.getTotal());
        result.put("rows",pageInfo.getList());
        return result;
    }



    public void saveSaleChance(SaleChance saleChance){
        /**
         * 1.参数合法校验
         *       客户名  联系人  联系方式 非空校验
         * 2.分配时间  分配状态  分配人
         *     分配人默认  null  分配时间 默认 null  分配状态 默认值 0-未分配
         *     如果指定分配人 设置分配时间 分配状态 1-已分配
         * 3.开发结果  0-未开发 1-开发中 2-开发成功 3-开发失败
         *      添加时 默认 未开发
         *      如果指定了分配人  此时为开发中
         * 4. is_valid=1 createDate  updateDate
         */
        //参数校验
        checkParms(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //默认分配状态
        saleChance.setState(0);
        //默认开发结果
        saleChance.setDevResult(0);
        //分配人不为空
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            //设置分配状态,分配时间以及开发结果
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(1);
        }
        //状态设置为有效
        saleChance.setIsValid(1);
        //设置创建时间以及更新时间
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        //调用添加方法,判断的hi否添加成功
        AssertUtil.isTrue(save(saleChance)<1,"营销数据添加失败!");

    }

    private void checkParms(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入用户名!");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人!");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入联系方式!");
    }

    //删除记录
    public void updateSaleChance(SaleChance saleChance){
        /**
         * 1. 参数合法校验
         *     客户名  联系人  联系方式 非空校验
         *     更新记录存在性校验
         * 2.分配人
         *    如果添加时 设置分配人   更新时 没有改动分配人  不做处理
         *    如果添加时没有设置分配人 更新时设置分配人
         *          设置分配时间分配状态
         *     如果添加时 设置分配人   更新时清空分配人
         *            分配时间  分配状态
         *3. 开发状态
         *     默认未开发
         *     如果设置分配人  开发中
         * 4.updateDate
         */
        //用户校验
        checkParms(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //判断前台传入对象是否存在ID值
        Integer sid = saleChance.getId();
        //存在id,去数据库中查找记录
        SaleChance temParms = queryById(sid);
        //判断数据库中知否存在记录
        AssertUtil.isTrue(sid==null || temParms==null,"待更新记录不存在!");
        //添加记录时没有指定分配人,前台传过来的待更新对象指定了分配人
        if(StringUtils.isBlank(temParms.getAssignMan()) && StringUtils.isNotBlank(saleChance.getAssignMan())){
            //设置分配状态以及分配时间
        saleChance.setState(1);
        saleChance.setAssignTime(new Date());
        }
        //添加记录时指定分配人,前台传过来的待更新对象没有指定分配人
        if(StringUtils.isNotBlank(temParms.getAssignMan())&&StringUtils.isBlank(saleChance.getAssignMan())){
            //清除分配状态以及分配时间
            saleChance.setState(0);
            saleChance.setAssignTime(null);
        }
        //默认为无效开发状态
        saleChance.setIsValid(0);
        //当指定分配人的情况下
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            //设置为有效开发状态
            saleChance.setIsValid(1);
        }
        //设置更新时间
        saleChance.setUpdateDate(new Date());
        //调用更新方法,判断是否更新成功
        AssertUtil.isTrue(update(saleChance)<1,"营销机会数据更新失败!");
    }
    public void deleteSaleChance(Integer[] ids) {

        AssertUtil.isTrue(null == ids || ids.length == 0, "请选择待删除记录!");
        AssertUtil.isTrue(deleteBatch(ids) != ids.length, "记录删除失败!");
    }

    //营销机会状态更新
    public void updateSaleChanceDevResult(Integer devResult, Integer sid) {
        AssertUtil.isTrue(devResult == null,"营销机会记录异常!");
        SaleChance saleChance = queryById(sid);
        AssertUtil.isTrue(saleChance==null,"待更新数据不存在!");
        saleChance.setDevResult(devResult);
        AssertUtil.isTrue(update(saleChance)<1,"营销数据更新失败!");

    }
}
