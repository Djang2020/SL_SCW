package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;

/**
 * @author Django
 * @date 2022-02-28 19:27
 * @DESC: 角色管理接口
 */

public interface RoleService {


    /**
     * 根据关键词查询
     * @param keyWord
     * @return
     */
    PageInfo<Role> getPageInfo(Integer pageNum,Integer pageSize,String keyWord);

    /**
     * 新增角色
     * @param role
     * @return
     */
    int addRole(Role role);

    /**
     * 修改role
     * @param role
     * @return
     */
    int updateRole(Role role);

    void removeRole(List<Integer> roleIdList);
}
