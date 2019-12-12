package com.shsxt.crm.query;

import com.shsxt.base.BaseQuery;

/**
 * Created by Administrator on 2019/12/8.
 */
public class UserQuery extends BaseQuery{
    private String userName;
    private String phone;
    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
