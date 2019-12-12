package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.service.PermissionService;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2019/12/3.
 */
@Controller
public class IndexController extends BaseController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserService userService;
    @RequestMapping("index")
    public String index(){
        return"index";
    }
    @RequestMapping("main")
    public String min(HttpServletRequest request){
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
       request.setAttribute("user", userService.queryById(userId));
        List<String> permission= permissionService.queryAllModuleAclValueByUserId(userId);
        request.getSession().setAttribute("permissions",permission);
        return"main";
    }
}
