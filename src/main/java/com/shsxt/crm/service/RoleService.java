package com.shsxt.crm.service;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.shsxt.base.BaseService;
import com.shsxt.crm.db.dao.ModuleMapper;
import com.shsxt.crm.db.dao.PermissionMapper;
import com.shsxt.crm.db.dao.RoleMapper;
import com.shsxt.crm.db.dao.UserRoleMapper;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.vo.Permission;
import com.shsxt.crm.vo.Role;
import com.shsxt.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.sql.PseudoColumnUsage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;

    public List<Map<String,Object>> queryAllRoles(){
        return roleMapper.queryAllRoles();
    }

    //角色添加
    public void saveRole(Role role){
        //角色名非空判断
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空!");
        //角色名唯一校验
        AssertUtil.isTrue(roleMapper.queryByRoleName(role.getRoleName())!=null,"角色名已存在!");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.save(role)<1,"角色添加失败!");
    }
    //更新用户角色
    public void updateRole(Role role){
        //角色名非空判断
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空!");
        //用户角色判空->查询
        AssertUtil.isTrue(role.getId()==null||null==roleMapper.queryById(role.getId()),"待更新角色用户不存在!");
        Role userRole = roleMapper.queryByRoleName(role.getRoleName());
       AssertUtil.isTrue(userRole!=null && !(userRole.getId().equals(role.getId())),"角色已存在!");
       role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.update(role)<1,"角色更新失败!");
    }
    //用户角色删除
    public void deleteRole(Integer roleId){
        AssertUtil.isTrue(roleId==null || roleMapper.queryById(roleId)==null,"待删除记录不存在");
        int count = userRoleMapper.countByRoleId(roleId);
        if (count>0){
            //从表记录删除
            AssertUtil.isTrue(userRoleMapper.deleteByRoleId(roleId)<count,"角色记录删除失败!");
        }
        Role role = roleMapper.queryById(roleId);
        role.setIsValid(0);
        AssertUtil.isTrue(update(role)<1,"角色记录删除失败!");
    }
    /*
    * 角色授权
    * */
    public void addGrant(Integer[] mid, Integer rid) {
        /**
         *  角色授权支持多次授权
         *    第一次授权
         *        1-->1,,2,3,4
         *    第二次授权
         *       1-->1,3,5,6,7
         *     第三次授权
         *        1-->1
         *     第四次授权
         *        1--> mid==null
         * 数据添加实现思路
         *     如果角色存在原始权限
         *        先执行权限记录删除操作
         *     然后添加角色新的权限记录(mid 数据数据存在时)
         */
       int count =permissionMapper.countPermissionByRoleId(rid);
       if (count>0){
          AssertUtil.isTrue( permissionMapper.deleteByRoleId(rid)!=count,"角色授权失败!");
       }
        if(mid!=null&&mid.length>0){
           List<Permission> ps=new ArrayList<Permission>();
            for (Integer id: mid){
                Permission permission=new Permission();
                permission.setModuleId(id);
                permission.setRoleId(rid);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setAclValue(moduleMapper.queryById(id).getOptValue());
                ps.add(permission);
            }
           AssertUtil.isTrue(permissionMapper.saveBatch(ps)!=mid.length,"角色授权失败!");
        }

    }
}
