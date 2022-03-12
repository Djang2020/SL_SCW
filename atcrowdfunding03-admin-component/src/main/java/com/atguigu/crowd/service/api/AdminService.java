package com.atguigu.crowd.service.api;

import java.util.List;

import com.atguigu.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;

public interface AdminService {
	
	void saveAdmin(Admin admin);

	List<Admin> getAll();

	/**
	 * 登录验证
	 * @param loginAccount
	 * @param userPswd
	 * @return
	 */
    Admin getAdminByLoginAccount(String loginAccount, String userPswd);


	/**
	 * 分页查询
	 * @param keyword  查询关键字
	 * @param pageNum  页码
	 * @param pageSize  每页显示条数
	 * @return
	 */
	PageInfo<Admin> getPageInfo(String keyword,Integer pageNum,Integer pageSize);

	/**
	 * 根据id删除管理员
	 * @param adminId
	 * @return
	 */
    int remove(Integer adminId);

	/**
	 * 通过id查询到用户
	 * @param adminId
	 * @return
	 */
	Admin getAdminById(Integer adminId);

	/**
	 * 更新用户信息
	 * @param admin
	 */
    void updateAdmin(Admin admin);
}
