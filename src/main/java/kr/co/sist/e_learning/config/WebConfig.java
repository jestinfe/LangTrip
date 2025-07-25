package kr.co.sist.e_learning.config;

import kr.co.sist.e_learning.config.interceptor.AdminLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminLoggingInterceptor adminLoggingInterceptor;

    @Value("${file.upload-dir.root}")       // C:/dev/workspace/e_learning_uploads
    private String uploadRootDir;

    @Value("${upload.path.profile}")        // /userprofile
    private String profilePath;

    @Value("${upload.path.community}")      // /community
    private String communityPath;
    
    @Value("${upload.path.courseImg}")      // /community
    private String courseImg;
    
    @Value("${upload.path.courseVideo}")      // /community
    private String courseVideo;
    
    

    @Value("${upload.path.quiz}")           // /upload/images/quiz
    private String quizImagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 프로필 이미지 URL: /userprofile/xxx.png → 로컬 경로: {uploadRootDir}/userprofile
        registry.addResourceHandler(profilePath + "/**")
                .addResourceLocations("file:///" + uploadRootDir + "/userprofile/");

        // 커뮤니티 이미지 URL: /community/xxx.jpg → 로컬 경로: {uploadRootDir}/community
        registry.addResourceHandler(communityPath + "/**")
                .addResourceLocations("file:///" + uploadRootDir + "/community/");
        // 커뮤니티 이미지 URL: /community/xxx.jpg → 로컬 경로: {uploadRootDir}/community
        
        registry.addResourceHandler(courseImg + "/**")
        .addResourceLocations("file:///" + uploadRootDir + "/courseImg/");

        registry.addResourceHandler(courseVideo + "/**")
        .addResourceLocations("file:///" + uploadRootDir + "/courseVideo/");


        // 퀴즈 이미지 URL: /upload/images/quiz/xxx.png → 로컬 경로: {uploadRootDir}/quiz
        registry.addResourceHandler(quizImagePath + "/**")
                .addResourceLocations("file:///" + uploadRootDir + "/quiz/");

        // 기본 static 이미지 경로 (클래스패스)
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
