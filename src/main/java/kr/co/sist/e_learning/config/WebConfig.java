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

    @Value("${upload.path}")
    private String uploadPath;

    // 정적 리소스 핸들러
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 커뮤니티 이미지
        registry.addResourceHandler("/images/community/**")
                .addResourceLocations("file:///" + uploadPath + "/");

        // 프로필 이미지
        registry.addResourceHandler("/images/userprofile/**")
                .addResourceLocations("file:///C:/e_learning_uploads/userprofile/");

        // 내부 static 이미지
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
