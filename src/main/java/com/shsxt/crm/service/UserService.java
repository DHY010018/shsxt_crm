package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.db.dao.UserMapper;
import com.shsxt.crm.db.dao.UserRoleMapper;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.UserIDBase64;
import com.shsxt.crm.vo.User;
import com.shsxt.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.zip.CheckedInputStream;


@Service
public class UserService extends BaseService<User,Integer> {
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserMapper userMapper;
    public UserModel login(String userName, String userPwd){
       /*
       * 1.参数校验
       * 用户名密码 非空判断
       * 2.根据用户名查找记录
       * 3.判断记录是否存在 方法结束 返回结果
       * 4.存在  比对密码
       * 比对失败  方法结束 返回结果
       * 5.比对正确 返回登录用户信息
       * */
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空!");
        //根据用户名查找数据库
        User user= userMapper.queryUserByUserName(userName);
        //判断数据库中是否存在该用户
        AssertUtil.isTrue(user==null,"该用户不存在或已注销");
        //判断密码是否相等
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(userPwd))),"密码错误");
        //创建方法,返回model对象
        return buildUserModelInfo(user);
    }

    private UserModel buildUserModelInfo(User user) {
        //创建model对象将所需字段存入model对象中
        UserModel userModel=new UserModel();
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        return userModel;
    }


    //密码更新
    public void updateUserPawd(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        /*参数校验
        1.userId不能为空,记录必须存在
        2.旧密码不能为空,且必须与数据库密码一致
        3.newPassword不能为空, newPassword新密码必须与confirmPassword确认码一致,与旧密码不相等
        4.明文加密md5
        5.执行更新返回结果
        * */
        User user = userMapper.queryById(userId);
        AssertUtil.isTrue(userId==null|| user==null,"用户不存在或未登录");
        AssertUtil.isTrue( StringUtils.isBlank(oldPassword)||!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码输入错误!");
        AssertUtil.isTrue(newPassword==null,"新密码不能为空!");
        AssertUtil.isTrue(confirmPassword==null,"确认码不能为空!");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"新密码输入不一致");
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码不能与旧密码一致!");
        user.setUserPwd(Md5Util.encode(newPassword));
        AssertUtil.isTrue(userMapper.update(user)<1,"密码更新失败");

    }

    public void saveUser(User user){
        /**
         * 1.参数校验
         *     用户名 非空
         *     真实名 非空
         * 2.用户唯一校验
         *     用户名不能重复
         * 3.字段值设置
         *     userPwd=123456
         *     is_valid
         *     createDate
         *     updateDate
         */
        CheckParams(user.getTrueName(),user.getUserName());
        AssertUtil.isTrue(userMapper.queryUserByUserName(user.getUserName())!=null,"用户名不能重复!");
        user.setUserPwd(Md5Util.encode("123456"));
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(save(user)<1,"添加用户记录失败!");

        /*
        * 用户角色关联
        *
        * */
        Integer userId = userMapper.queryUserByUserName(user.getUserName()).getId();
        relationUserRole(userId,user.getRoleIds());

    }
    private void relationUserRole(Integer userId, List<Integer> roleIds) {

        //更新角色关联时 查询角色表中当前对象是否存在
        int count = userRoleMapper.countByUserRoleId(userId);
        //当前用户角色如果存在,对于当前的用户角色进行删除
        if (count>0){
            AssertUtil.isTrue(userRoleMapper.deleteByUserRoleId(userId)<count,"用户角色更新失败!");
        }
        //添加用户角色关联时,因为是批量添加,所以需要准备一个存储角色对象的list容器
        List<UserRole> userRoles=new ArrayList<UserRole>();
        //判断用户的角色id是否存在
        if (roleIds!=null && roleIds.size()>0){
            //遍历当前对象的角色Id
            roleIds.forEach(rol->{
                //创建角色对象
                UserRole userRole=new UserRole();
                //为角色对象赋值
                userRole.setUserId(userId);
                userRole.setRoleId(rol);
                userRole.setUpdateDate(new Date());
                userRole.setCreateDate(new Date());
                userRoles.add(userRole);
            });
            AssertUtil.isTrue(userRoleMapper.saveBatch(userRoles)!=roleIds.size(),"用户角色关联失败!");
        }

    }
    private void CheckParams(String trueName, String userName) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名称不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(trueName),"请输入真实用户名!");
    }

    public void updateUser(User user){
        /**
         * 1.参数校验
         *     用户名 非空
         *     真实名 非空
         *     用户ID 非空
         * 2.用户唯一校验
         *     用户名不能重复
         *
         */
        CheckParams(user.getTrueName(),user.getUserName());
        User temUser = queryById(user.getId());
        AssertUtil.isTrue(temUser==null,"待更新用户不存在!");
        temUser = userMapper.queryUserByUserName(user.getUserName());
        AssertUtil.isTrue( null!=temUser && !(temUser.getId().equals(user.getId())),"用户名不能重复!");
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(update(user)<1,"用户更新失败!");
        /*
        * 更新用户角色关联
        * */
        relationUserRole(user.getId(),user.getRoleIds());

    }

    public void deleteUser(Integer userId){
        User user = queryById(userId);
        AssertUtil.isTrue(null==userId||null == user,"待删除的用户不存在!");

        /**
         * 级联删除从表记录
         */
        int count = userRoleMapper.countByUserRoleId(userId);
        if(count>0){
            //用户角色记录存在 -->删除原有用户角色记录
            AssertUtil.isTrue(userRoleMapper.deleteByUserRoleId(userId)!=count,"用户删除失败!");
        }
        user.setIsValid(0);
        AssertUtil.isTrue(update(user)<1,"用户删除失败!");

    }

}
