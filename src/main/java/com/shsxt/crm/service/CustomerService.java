package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.db.dao.CustomerMapper;
import com.shsxt.crm.db.dao.CustomerOrderMapper;
import com.shsxt.crm.db.dao.CustomerlossMapper;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.vo.Customer;
import com.shsxt.crm.vo.CustomerOrder;
import com.shsxt.crm.vo.Customerloss;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/12/12.
 */
@Service
public class CustomerService extends BaseService<Customer,Integer>{
    @Resource
    private CustomerOrderMapper customerOrderMapper;
@Resource
private CustomerMapper customerMapper;

@Resource
private CustomerlossMapper customerlossMapper;

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

    /*
    * 流失客户转移
    * 1.根据条件查询出符合条件的流失客户
    * 2.将符合条件的流失客户转移到客户流失表
    *
    * */
    public void updateCustomerState(){
        List<Customer> customers = customerMapper.queryLossCustomers();
        Integer[]ids=null;
        List<Customerloss> customerlosses=null;
        if (!(CollectionUtils.isEmpty(customers))){
            ids=new Integer[customers.size()];
            customerlosses=new ArrayList<Customerloss>();
            for (int i = 0; i <customers.size() ; i++) {
                ids[i]=customers.get(i).getId();
                Customerloss customerloss=new Customerloss();
                customerloss.setUpdateDate(new Date());
                //atate:客户最终流失状态 0--->暂缓流失  1--->确认流失
                customerloss.setState(0);
                //设置客户最后一次下单时间
                CustomerOrder customerOrder = customerOrderMapper.queryLastCustomerOrderByCusId(customerloss.getId());
                if(null!= customerOrder){
                    customerloss.setConfirmLossTime(customerOrder.getOrderDate());
                }
                customerloss.setIsValid(1);
                customerloss.setCusNo(customers.get(i).getKhno());
                customerloss.setCusName(customers.get(i).getName());
                customerloss.setCusManager(customers.get(i).getCusManager());
                customerloss.setCreateDate(new Date());
                customerlosses.add(customerloss);
            }
           AssertUtil.isTrue( customerlossMapper.saveBatch(customerlosses)!=customerlosses.size(),"客户流失数据添加失败!");
           AssertUtil.isTrue( customerMapper.updateStateBatch(ids)!=ids.length,"客户数据流转失败!");
        }
    }

}
