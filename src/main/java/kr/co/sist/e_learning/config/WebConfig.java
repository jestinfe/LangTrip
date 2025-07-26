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

    @Value("${file.upload-dir.root}")        // C:/dev/workspace/e_learning_uploads
    private String uploadRootDir;

    @Value("${upload.path.profile}")         // /userprofile
    private String profilePath;

    @Value("${upload.path.community}")       // /community
    private String communityPath;
    
    @Value("${upload.path.courseImg}")       // /courseImg
    private String courseImgPath; // 변수명 변경: courseImg -> courseImgPath (명확성을 위해)
    
    @Value("${upload.path.courseVideo}")     // /courseVideo
    private String courseVideoPath; // 변수명 변경: courseVideo -> courseVideoPath (명확성을 위해)
    
    @Value("${upload.path.quiz}")            // /quiz (application.properties에 /upload/img/quiz로 되어있다면 해당 경로로 수정 필요)
    private String quizImagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 프로필 이미지 URL: /userprofile/xxx.png → 로컬 경로: {uploadRootDir}/userprofile
        registry.addResourceHandler(profilePath + "/**")
                .addResourceLocations("file:///" + uploadRootDir + "/userprofile/");

        // 커뮤니티 이미지 URL: /community/xxx.jpg → 로컬 경로: {uploadRootDir}/community
        registry.addResourceHandler(communityPath + "/**")
                .addResourceLocations("file:///" + uploadRootDir + "/community/");
        
        // 강의 이미지 URL: /courseImg/xxx.jpg → 로컬 경로: {uploadRootDir}/courseImg
        registry.addResourceHandler(courseImgPath + "/**") // ⭐ courseImgPath 변수 사용
                .addResourceLocations("file:///" + uploadRootDir + "/courseImg/");
        
        // 강의 비디오 URL: /courseVideo/xxx.mp4 → 로컬 경로: {uploadRootDir}/courseVideo
        registry.addResourceHandler(courseVideoPath + "/**") // ⭐ courseVideoPath 변수 사용
                .addResourceLocations("file:///" + uploadRootDir + "/courseVideo/");

        // 퀴즈 이미지 URL: /quiz/xxx.png → 로컬 경로: {uploadRootDir}/quiz
        // (만약 application.properties에서 upload.path.quiz가 /upload/img/quiz라면,
        //  registry.addResourceHandler("/upload/img/quiz/**") 로 변경해야 합니다.)
        registry.addResourceHandler(quizImagePath + "/**")
                .addResourceLocations("file:///" + uploadRootDir + "/quiz/");

        // 기본 static 리소스 (CSS, JS, 기타 이미지 등)
        // /images/**는 /static/images/만 매핑하므로, 모든 static 리소스를 위해 /**를 추가합니다.
        registry.addResourceHandler("/**") // ⭐ 모든 정적 자원 요청을 처리
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminLoggingInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**");
    }
}
