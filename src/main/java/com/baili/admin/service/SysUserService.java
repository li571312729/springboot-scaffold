package com.baili.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baili.admin.entity.SysUser;
import com.baili.admin.entity.WebDept;
import com.baili.admin.vo.AdminInfoVO;
import com.baili.common.entity.Result;
import com.baili.common.entity.EmployeeVO;
import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Map;

/**
 * (SysUser)表服务接口
 *
 * @since 2020-02-04 13:40:11
 */
public interface SysUserService extends IService<SysUser> {
    SysUser checkUserName(String username);
    Result login(String username, String password);
    //显示当前用户信息
    AdminInfoVO showCurrentAdmin(Long userId);
    //校验验证码
    SysUser checkCode(String mailDTOEmail, String emailCode, Claims claims) throws Exception;
    //修改密码
    int changePassWord(SysUser admin, String newpassword, String comfirmpassword);
    //修改邮箱
    int changeEmail(SysUser admin, String acCode, String code, String email);

    //根据部门查询该部门下的员工
    List<SysUser> selectdeptEmployeeList(String userId, Integer projectId, Integer webDeptId);
    //显示通讯录人员详情
    EmployeeVO showEmployeeDetail(Integer projectId, String userId);
    //显示部门列表
    List<WebDept> selectDeptList();
    //分页显示该项目部门下员工信息
    Map<String,Object> selectEmployeePage(Integer projectId, Integer webDeptId, Integer page, Integer size);

    //条件查询项目下用户信息
    Map<String,Object> selectEmployeePageList(Map<String, Object> map);
}