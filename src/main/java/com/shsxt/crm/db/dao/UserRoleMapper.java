package com.shsxt.crm.db.dao;

import com.shsxt.base.BaseMapper;
import com.shsxt.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    public int countByUserRoleId(Integer userId);
    public int deleteByUserRoleId(Integer userId);
    public int countByRoleId(Integer rolerId);
    public int deleteByRoleId(Integer rolerId);
}