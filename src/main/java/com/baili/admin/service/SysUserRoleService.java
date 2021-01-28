package com.baili.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baili.admin.entity.SysUserRole;

/**
 * (SysUserRole)表服务接口
 *
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    // 移交项目
    boolean transferProject(SysUserRole source, SysUserRole target) throws Exception;
}