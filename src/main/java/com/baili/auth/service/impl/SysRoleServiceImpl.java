package com.baili.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baili.auth.dao.SysRoleDao;
import com.baili.auth.entity.SysRole;
import com.baili.auth.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色(SysRole)表服务实现类
 *
 *
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {
    @Autowired
    private SysRoleDao sysRoleDao;
    //根据用户id查询用户角色
    @Override
    public List<SysRole> selectSysRoleByUserId(Long userId) {
        return sysRoleDao.selectSysRoleByUserId(userId);
    }
}