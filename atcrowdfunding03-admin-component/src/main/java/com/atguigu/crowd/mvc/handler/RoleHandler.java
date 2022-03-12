package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Django
 * @date 2022-02-28 19:29
 * @DESC: 角色管理接口
 */
@Controller
public class RoleHandler {

    @Autowired
    private RoleService roleService;

    private Logger logger = LoggerFactory.getLogger(RoleHandler.class);

    /**
     * 根据关键词执行分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param keyWord
     * @param modelMap
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                    @RequestParam(value = "keyWord", defaultValue = "") String keyWord,
                                                    ModelMap modelMap
    ) {
        logger.info("RoleHandler.getPageInfo: pageNum", pageNum + " pageSize:" + pageSize + "keyWord:" + keyWord);

        try {
            PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyWord);

            return ResultEntity.successWithData(pageInfo);
        } catch (Exception e) {
            logger.error("RoleHandler.getPageInfo: e", e);
        }
        return ResultEntity.failed("网络异常，请稍后重试！");
    }


    /**
     * 新增role
     *
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/role/save.json")
    public ResultEntity<String> saveRole(Role role) {

        logger.info("RoleHandler.saveRole role", role);
        int i = 0;
        try {

            i = roleService.addRole(role);

        } catch (Exception e) {

            logger.error("RoleHandler.saveRole ", e);
        }
        if (i > 0) {
            return ResultEntity.successWithoutData();

        } else {
            return ResultEntity.failed("新增失败！");
        }

    }

    /**
     * 更新角色
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/role/update.json")
    public ResultEntity<String> updateRole(Role role) {

        logger.info("RoleHandler.updateRole role", role);

        int result = 0;
        try {

            result = roleService.updateRole(role);


        } catch (Exception e) {
            logger.error("RoleHandler.updateRole ", e);
        }

        if (result > 0) {
            return ResultEntity.successWithData("修改成功！");
        } else {

            return ResultEntity.failed("修改失败失败！");
        }
    }


    /**
     * 批量删除与单个删除
     * @param roleIdList
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "role/delete/by/roleId/array.json")
    public ResultEntity<String> removeRole(@RequestBody List<Integer> roleIdList){
        logger.error("RoleHandler.removeRole roleIdList",roleIdList);
        try{
            roleService.removeRole(roleIdList);
        }catch (Exception e){
            logger.error("RoleHandler.removeRole e",e);
        }

        return ResultEntity.successWithoutData();
    }

}
