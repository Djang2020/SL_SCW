package com.atguigu.crowd.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mvc.handler.AdminHandler;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import org.springframework.util.StringUtils;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;


    private Logger logger = LoggerFactory.getLogger(AdminHandler.class);

    public void saveAdmin(Admin admin) {

        logger.info("AdminServiceImpl.saveAdmin admin:", admin);

        try {
            //获取密码
            String adminUserPswd = admin.getUserPswd();
            String md5 = CrowdUtil.md5(adminUserPswd);
            admin.setUserPswd(md5);

            //生成创建事件
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createTime = format.format(date);
            admin.setCreateTime(createTime);

            //执行保存
            adminMapper.insert(admin);

            // throw new RuntimeException();
        } catch (Exception e) {
            logger.error("AdminServiceImpl.saveAdmin e:", e);
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            } else {

            }
        }

    }

    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    /**
     * 登录验证
     *
     * @param loginAccount
     * @param userPswd
     * @return
     */
    @Override
    public Admin getAdminByLoginAccount(String loginAccount, String userPswd) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        //criteria对象中封装查询条件
        criteria.andLoginAcctEqualTo(loginAccount);

        //1.根据账号查询对象
        List<Admin> adminList = adminMapper.selectByExample(adminExample);

        //2.判断对象是否为空
        if (StringUtils.isEmpty(adminList) || adminList.size() == 0) {
            //3.为空则抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (adminList.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        //如果admin是null也要抛出异常
        Admin admin = adminList.get(0);
        if (null == admin) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //4.如果不为空，则把数据库密码从Admin对象中取出来
        String adminUserPswdDB = admin.getUserPswd();
        //5.将表单明文密码加密
        String userPsdForm = CrowdUtil.md5(userPswd);
        //6.对比两个密码
        if (Objects.equals(userPsdForm, adminUserPswdDB)) {
            //7.如果一致则返回Admin对象
            return admin;
        } else {
            //8.如果对比结果不一致则抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

    }

    /**
     * 分页查询
     *
     * @param keyword  查询关键字
     * @param pageNum  页码
     * @param pageSize 每页显示条数
     * @return
     */
    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        //1.调用PageHelper的静态方法开启分页功能
        //这里就体现了PageHelper的“非入侵式”设计，原本的业务功能没有修改
        PageHelper.startPage(pageNum, pageSize);

        //2.执行查询
        List<Admin> adminList = adminMapper.selectAdminByKeyWord(keyword);

        //3.封装到PageInfo中
        PageInfo<Admin> adminPageInfo = new PageInfo<>(adminList);

        return adminPageInfo;
    }

    /**
     * 根据id删除管理员
     *
     * @param adminId
     * @return
     */
    @Override
    public int remove(Integer adminId) {

        int i = adminMapper.deleteByPrimaryKey(adminId);
        return i;
    }

    /**
     * 通过id查询到用户
     *
     * @param adminId
     * @return
     */
    @Override
    public Admin getAdminById(Integer adminId) {
        Admin admin = new Admin();
        try {
            logger.info("AdminServiceImpl.getAdminById adminId", adminId);
            admin = adminMapper.selectByPrimaryKey(adminId);

        } catch (Exception e) {

            logger.error("AdminServiceImpl.getAdminById e", e);
        }

        return admin;
    }

    /**
     * 更新用户信息
     *
     * @param admin
     */
    @Override
    public void updateAdmin(Admin admin) {
        try {
            //有选择的更新，对于null的不更新
            adminMapper.updateByPrimaryKeySelective(admin);

        }catch (Exception e){
            logger.error("AdminServiceImpl.updateAdmin:",e);
            if(e instanceof LoginAcctAlreadyInUseForUpdateException){
               throw  new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }


}
