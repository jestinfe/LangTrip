package kr.co.sist.e_learning.config;

import kr.co.sist.e_learning.config.interceptor.AdminLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminLoggingInterceptor adminLoggingInterceptor;

    @Value("${upload.path.community}")
    private String communityUploadPath;

    @Value("${upload.path.profile}")
    private String userProfileUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 커뮤니티 이미지 경로
        registry.addResourceHandler("/images/community/**")
                .addResourceLocations("file:///" + communityUploadPath + "/");

        // 2. 유저 프로필 이미지 경로
        registry.addResourceHandler("/userprofile/**") // 브라우저 요청 URL: /userprofile/xxx.png
                .addResourceLocations("file:///" + userProfileUploadDir + "/");

        // 3. 기본 static 이미지
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminLoggingInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**");
    }
}
