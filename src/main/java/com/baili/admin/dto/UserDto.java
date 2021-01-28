package com.baili.admin.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String name;
    private Integer sex;
    private String company;
    private String mobile;
    private String password;
    private String confirmPassword;
}
