package com.shsxt.crm.query;

import com.shsxt.base.BaseQuery;

/**
 * Created by Administrator on 2019/12/5.
 */
public class SaleChanceQuery extends BaseQuery {
private String customerName;
private String createMan;
private Integer state;

private Integer assignMan;

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
