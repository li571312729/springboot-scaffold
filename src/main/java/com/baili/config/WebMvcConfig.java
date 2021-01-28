package com.baili.config;


import com.baili.config.api.RequestLimitIntercept;
import com.baili.resources.FileUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Administrator
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private FileUpload fileUpload;
    @Autowired
    LoginStatusFilter loginStatusFilter;
    @Autowired
    private RequestLimitIntercept requestLimitIntercept;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//       registry.addResourceHandler("img/**").addResourceLocations("file:"+imgPath);
        //配置swagger2
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        //配置静态资源网络映射地址
        registry.addResourceHandler("/**")
                .addResourceLocations("file:" + fileUpload.getImageLocaltion() + "/");
    }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(requestLimitIntercept);
        }
}