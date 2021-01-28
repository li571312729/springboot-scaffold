package com.baili.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baili.admin.entity.SysUser;
import com.baili.admin.vo.AdminInfoVO;
import com.baili.common.vo.CountNature;
import com.baili.common.entity.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * (SysUser)表数据库访问层
 *
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUser> {
    //根据部门查询该部门下的员工
    List<SysUser> selectdeptEmployeeList(String userId, Integer projectId, Integer webDeptId);
    //显示当前项目下员工详情
    EmployeeVO showEmployeeDetail(Integer projectId, String userId);
    //分页显示该项目部门下员工信息
    IPage<EmployeeVO> selectEmployeePage(Page page, Integer projectId, Integer webDeptId);
    //统计该项目该部门人数
    Integer selectCountByDeptAndProject(Integer webDeptId, Integer projectId);
    //显示用户信息
    AdminInfoVO selectAdminAllInfo(Long userId);
    //分页查询该项目下当前部门里的人员信息
    List<EmployeeVO> selectUserData(Integer projectId, Integer webDeptId, Integer page, Integer size);

    //统计人数
    List<CountNature> countEmployee(Map<String, Object> map);
    //条件查询
    List<SysUser> selectUserDatas(Map<String, Object> map);
}