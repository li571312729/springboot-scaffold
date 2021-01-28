package com.baili.admin.controller;

import com.baili.admin.util.SystemInfoUtil;
import com.baili.common.entity.Result;
import com.baili.mail.util.JsonUtil;
import com.baili.websocket.entity.WebSocketMessage;
import com.baili.websocket.server.WebSocket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 系统服务信息模块
 */
@Api(tags = "系统服务信息模块")
@RestController
@RequestMapping("systemInfo")
@Slf4j
public class SystemInfoController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private WebSocket webSocket;

    @ApiOperation(value = "获取系统服务信息", notes = "获取系统服务信息")
    @GetMapping("/")
    public Result view() {
        return Result.success(new SystemInfoUtil());
    }

    @ApiOperation(value = "国际化测试", notes = "国际化测试")
    @GetMapping("/locale")
    public Result locale() {
        webSocket.sendInfo(JsonUtil.objToStr(new WebSocketMessage(1, "杨幂别找我啦!")));
        String message = messageSource.getMessage("common.name",
                null, LocaleContextHolder.getLocale());
        return Result.success(message);
    }

}