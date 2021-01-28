package com.baili.sms.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baili.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 阿里云短信服务
 *
 * @author mengkai
 */
@Slf4j
@Component
public class AliyunSmsUtils {

    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    @Value("${sms.regionId}")
    String smsRegionId;
    @Value("${sms.accessKey}")
    String smsAccessKey;
    @Value("${sms.accessKeySecret}")
    String smsAccessKeySecret;
    @Value("${sms.signName}")
    String smsSignName;
    @Value("${sms.templateCode}")
    String smsTemplateCode;

    public boolean sendInsertUserMsg(String telephone, String code) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(smsRegionId, smsAccessKey, smsAccessKeySecret);
        try {
            DefaultProfile.addEndpoint(smsRegionId, smsRegionId, PRODUCT, DOMAIN);
        } catch (ClientException e) {
            throw new BaseException(1223, e.getErrMsg());
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(telephone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(smsSignName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(smsTemplateCode);
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        String msg = "OK";
        if (sendSmsResponse.getCode() != null && msg.equals(sendSmsResponse.getCode())) {
            log.info("短信发送成功");
            return true;
        } else {
            log.error("短信发送失败");
        }
        return false;
    }
}