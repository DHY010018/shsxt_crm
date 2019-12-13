package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.db.dao.CustomerMapper;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.vo.Customer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2019/12/12.
 */
@Service
public class CustomerService extends BaseService<Customer,Integer>{
@Resource
private CustomerMapper customerMapper;

public void saveCustomer(Customer customer){
    /**
     * 1.参数校验
     *      客户姓名非空 不可重复
     *      手机号非空
     *      法人 非空
     * 2.字段值设置
     *      khno 客户编号  值唯一  后台生成
     *      state   0-未流失(默认)   1-已流失
     *      isValid  1-有效
     *      createDate  updateDate
     * 3.执行添加 判断结果
     */
   checkParms(customer);
    Customer temp= customerMapper.queryCustomerByName(customer.getName());
    AssertUtil.isTrue(temp!=null,"客户已存在!");
//设置字段值
  String khno= "khno"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
  customer.setKhno(khno);
  customer.setState(0);
  customer.setIsValid(1);
  customer.setCreateDate(new Date());
  customer.setUpdateDate(new Date());
  AssertUtil.isTrue(save(customer)<1,"客户添加失败!");

}
    private void checkParms(Customer customer) {
    AssertUtil.isTrue(customer.getName()==null,"请输入用户名!");
    AssertUtil.isTrue(customer.getPhone()==null,"请输入手机号!");
    AssertUtil.isTrue(customer.getPhone().length()!=11,"手机号码格式不合法!");
    AssertUtil.isTrue(customer.getFr()==null,"请指定法人代表!");
    }
        //客户更新
    public void updateCustomer(Customer customer){
        /**
         * 1.参数校验
         *      客户姓名非空 不可重复
         *      手机号非空
         *      法人 非空
         *      记录Id 必须存在
         * 2.字段值设置
         *       updateDate
         * 3.执行添加 判断结果
         */
        checkParms(customer);
        Customer temp= customerMapper.queryCustomerByName(customer.getName());
        AssertUtil.isTrue(null==customer.getId()||null==queryById(customer.getId()),"待更新记录不存在!");
        AssertUtil.isTrue(temp!=null && !(temp.getId().equals(customer.getId())),"客户已存在,请更换名称!" );
        customer.setUpdateDate(new Date());
        AssertUtil.isTrue(update(customer)<1,"更新失败!");

    }
    //删除客户
    public void delectCustomer(Integer id){
        /**
         * 1.参数校验
         *      客户姓名非空 不可重复
         *      手机号非空
         *      法人 非空
         *      记录Id 必须存在
         * 2.字段值设置
         *       isValid  0-无效
         * 3.执行添加 判断结果
         */
       AssertUtil.isTrue(id==null,"请选择要删除的客户记录");
        Customer customer = queryById(id);
        AssertUtil.isTrue(customer==null,"待删除记录不存在");
        customer.setIsValid(0);
        AssertUtil.isTrue(update(customer)<1,"客户记录删除失败!");
    }

}