package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.query.CustomerQuery;
import com.shsxt.crm.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Administrator on 2019/12/12.
 */
@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController{

    @Resource
    private CustomerService customerService;

    @RequestMapping("index")
    public String index(){
        return "customer";
    }
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery,@RequestParam(defaultValue = "1") Integer total,
                                                    @RequestParam(defaultValue = "10") Integer rows){

        customerQuery.setPageNum(total);
        customerQuery.setPageSize(rows);
       return customerService.baseServiceQueryByParams(customerQuery);
    }

}
