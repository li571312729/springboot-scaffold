package com.baili.admin.vo;

import lombok.Data;

import java.util.Date;


/**
 * @author Administrator
 */
@SuppressWarnings("serial")
@Data
public class SysUserVO {
    //用户id
    private Long userId;
    //用户名
    private String username;
    //真实姓名
    private String name;
    //密码
    private String password;
    //部门编号
    private String webDeptId;
    //工号
    private String employeeWorkNum;
    //邮箱
    private String email;
    //手机号
    private String mobile;
    //状态 0:禁用，1:正常
    private Integer status;
    //所在公司
    private String company;
    //职务
    private String position;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //权限标识
    private String roleSign;
    }