package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;


/**
 * @author Django
 * @date 2022-02-20 11:23
 * @DESC:登录控制页面
 */


@Controller
public class AdminHandler {

    private Logger logger = LoggerFactory.getLogger(AdminHandler.class);

    @Autowired
    private AdminService adminService;



    /**
     * 更新用户信息
     * @param admin
     * @param pageNum
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/admin/update.html")
    public String updateAdmin(
            Admin admin,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("keyword") String keyword

    ){
        adminService.updateAdmin(admin);

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;


    }

    /**
     * 选中要修改的数据
     * @param adminId
     * @param pageNum
     * @param keyword
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId,
                             @RequestParam("pageNum") Integer pageNum,
                             @RequestParam("keyword") String keyword,
                             ModelMap modelMap

                             ){


        Admin admin = adminService.getAdminById(adminId);
        modelMap.addAttribute("admin",admin);

        return "admin-edit";
    }


    /**
     * 登录
     * @param loginAcct
     * @param userPswd
     * @param session
     * @return
     */
    @RequestMapping(value = "/admin/do/login.html")
    public String doLogin(@RequestParam(value = "loginAcct", required = false) String loginAcct,
                          @RequestParam(value = "userPswd", required = false) String userPswd,
                          HttpSession session) {

        //调用service执行登录验证
        //如果能够返回，则表示登录成功，如果不正确则会抛出异常
        Admin admin = adminService.getAdminByLoginAccount(loginAcct, userPswd);

        //将登录成功返回的对象存入session
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        //return "admin-main";//转发到页面，但是如果刷新一下的话就得再查一次数据库，使用重定向最好

        //重定向-浏览器向服务器发送请求-服务器给浏览器说你去访问谁-但是浏览器无法访问web-info,所以需要在mvc中配置view-controller

        return "redirect:/admin/to/main/page.html";
    }

    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/admin/do/logout.html")
    public String doLogout(HttpSession session) {
        //强制Session失效
        session.invalidate();
        //返回到登录页面
        return "redirect:/admin/to/login/page.html";
    }

    @RequestMapping(value = "/admin/get/page.html")
    public String getPageInfo(
            //使用defaultValue，去指定默认值，在请求中没有携带对应参数时使用默认值
            //keyword默认使用空字符串，和SQL语句配合使用，实现两种情况
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, ModelMap modelMap) {

        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";


    }

    @RequestMapping(value = "/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String removeAdmin(@PathVariable("adminId") Integer adminId,
                              @PathVariable("pageNum") Integer pageNum,
                              @PathVariable("keyword") String keyword
                              ) {
        //执行删除
        int result = adminService.remove(adminId);
        HashMap<String, Object> resultMap = new HashMap<>();
        String flag = "Y";
        //页面跳转,删除后回到查询页面
        if (result >= 0) {
            resultMap.put("flag", flag);
        } else {
            flag = "N";
            resultMap.put("flag",flag);
        }
        //方案一：直接回到查询页面
        //return "admin-page";//会无法显示分页数据，这种方案不可行

        //方案二:转发到/admin/get/page.html,这个能携带数据，但是会把删除操作再做一次也不可行，也浪费性能
        //return "forward:/admin/get/page.html";

        //方案三：重定向到 /admin/get/page.html，同时为了保持原本所在的页面和查询关键词再附加pageNum和keyword请求参数

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    /**
     * 新增管理员
     * @param admin
     * @return
     */
    @RequestMapping(value = "/admin/save.html")
    public String saveAdmin(Admin admin){
        logger.info("AdminHandler.saveAdmin admin",admin);
        try {

            adminService.saveAdmin(admin);
        }catch (Exception e){
            logger.error("AdminHandler.saveAdmin e:",e);
        }
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }


}
