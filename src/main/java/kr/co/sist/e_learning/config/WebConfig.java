package kr.co.sist.e_learning.config;
//String saveDir = "C:/e_learning_uploads/userprofile";에 저장하기 위한 config
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 외부 userprofile 디렉토리만 따로 지정
        registry.addResourceHandler("/images/userprofile/**")
                .addResourceLocations("file:///C:/e_learning_uploads/userprofile/");

        // 내부 static 디렉토리도 명시적으로 유지 (로고 포함)
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}
