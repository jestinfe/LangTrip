package kr.co.sist.e_learning.config;
import org.springframework.beans.factory.annotation.Value;
//String saveDir = "C:/e_learning_uploads/userprofile";에 저장하기 위한 config
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 커뮤니티 이미지
        registry.addResourceHandler("/images/community/**")
                .addResourceLocations("file:///" + uploadPath + "/");

        // 프로필 이미지 (C:/e_learning_uploads/userprofile/ 같은 경로를 직접 하드코딩했을 수도 있음)
        registry.addResourceHandler("/images/userprofile/**")
                .addResourceLocations("file:///C:/e_learning_uploads/userprofile/");

        // 내부 static 이미지 (로고 같은 것)
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}