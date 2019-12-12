package com.shsxt.crm.service;

import com.shsxt.base.BaseService;
import com.shsxt.crm.db.dao.ModuleMapper;
import com.shsxt.crm.db.dao.PermissionMapper;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.TreeModule;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/12/9.
 */
@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;

    public List<Map<String,Object>> queryAllModules(){
        return  moduleMapper.queryAllModules();
    }

    public List<TreeModule> queryAllModules02(Integer rid){
        List<TreeModule> treeModules = moduleMapper.queryAllModules02();
        //根据角色Id查询权限表中 对应的资源Id
        List<Integer> moduleId=permissionMapper.queryAllModuleIdsByRoleId(rid);
        //判断可操作资源是否为空
        if (moduleId!=null && moduleId.size()>0){
            //遍历所有资源
            treeModules.forEach(treeModule -> {
                //判断权限表中的资源Id 都包含了哪些资源
                if(moduleId.contains(treeModule.getId())){
                    //为当前资源属性赋值
                    treeModule.setChecked(true);
                }
            });
        }
        return treeModules;
    }


    public void saveModule(Module module){
        /*
        * 1.参数校验
        *       模块名非空校验  同一层级下模块名不能相同
        *       gread层级 非空 0|1|2
        *       顶级菜单 parentId  为空  二三级菜单 parentId 不能为空
        *       权限码非空不可重复
        *2.设置字段值
        *       is_valid  有效字段值
         *      createDate 创建时间
         *      updateDate 更新时间
         * 3.执行添加 判断添加结果
        * */
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名不能为空!");
        Integer grade =module.getGrade();
        AssertUtil.isTrue(null==grade,"请指定层级!");
        AssertUtil.isTrue(!(grade==0|| grade==1||grade==2),"层级值非法!");
        Module temp= moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(),module.getModuleName());
        AssertUtil.isTrue(temp!=null,"同一层级下模块名不可重复!");
        if(grade==1||grade==2){
        AssertUtil.isTrue(module.getParentId()==null,"请指定上级菜单!");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入菜单权限码!");
        temp= moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(temp != null,"权限码不可重复!");
        module.setIsValid((byte)1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(save(module)<1,"模块添加失败!");
    }


    public void updateModule(Module module){
        /**
         * 1.参数校验
         *     id 非空  记录必须存在
         *     模块名非空   同一层级下  模块名不可重复
         *     grade 层级 非空 0|1|2
         *     顶级菜单 parentId  为空  二三级菜单 parentId 不能为空
         *     权限码  非空  不可重复
         *
         * 2.设置字段值
         *      updateDate
         * 3.执行更新 判断更新结果
         */
       Integer mid=module.getId();
       AssertUtil.isTrue(mid==null||queryById(mid)==null,"待更新记录不存在!");
       AssertUtil.isTrue(module.getModuleName()==null,"模块名不能为空!");
        Module mdl = moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName());
        AssertUtil.isTrue(mdl!=null && !(mdl.getId().equals(module.getId())),"同一层级下模块名不可重复!");
       Integer gread= module.getGrade();
       AssertUtil.isTrue(gread==null,"请指定层级!");
        AssertUtil.isTrue((gread==1 || gread==2 || gread==0),"非法层级值!");
        if (mid==1||mid==2){
            AssertUtil.isTrue(module.getParentId()==null,"请指定上级菜单!");
        }
        AssertUtil.isTrue(module.getIsValid()==null,"权限码值不能为空!");
        Module module1 = moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(module != null,"权限码不可重复!");
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(update(module)<1,"模块更新失败!");
    }
    public void deleteModuleByModuleId(Integer mid){
        Module module = queryById(mid);
        AssertUtil.isTrue(mid==null||module==null,"待删除记录不存在!");
        /**
         * 如果当前待删除的菜单 存在子菜单  不允许删除当前菜单
         */
        Integer count = moduleMapper.countSubModulesByMid(mid);
        AssertUtil.isTrue(count>0,"当前菜单存在子菜单,暂不支持删除!");
        module.setIsValid((byte)0);
        AssertUtil.isTrue(update(module)<1,"模块删除失败!");
    }
}
