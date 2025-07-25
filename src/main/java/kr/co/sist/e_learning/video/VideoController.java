package kr.co.sist.e_learning.video;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.sist.e_learning.course.CourseService;


@Controller
public class VideoController {

	@Value("${file.upload-dir.courseVideo}")  
    private String courseVideoPath;

	@Autowired
	private VideoService vs;
	
	@Autowired
	private CourseService cs;
	
	@GetMapping("/upload/upload_video")
	public String showUploadForm(@RequestParam("seq") String courseSeq, Model model) {
		model.addAttribute("courseSeq", courseSeq);
		return "upload/upload_video";
	}
	
	
	
	@GetMapping("/ui/upload_frm")
	public String uploadFrm() {
		
		return "ui/upload_frm";
	}
	
	
	
	@PostMapping("/upload/upload_video")
	@ResponseBody
	public Map<String, Object> videoData(
			@RequestParam("upfile") List<MultipartFile> mfList,
			@ModelAttribute VideoDTO vDTO, Model model)
			throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		
		
		
		List<String> uploadedFiles = new ArrayList<>();
		List<VideoDTO> dtoList = new ArrayList<VideoDTO>();
		String errMsg = "";
		int videoCnt = 1;
		
		for (MultipartFile mf : mfList) {

//			int maxSize = 1024 * 1024 * 200;

			if (mfList == null || mfList.isEmpty()) {
				errMsg = "올린 파일이 없습니다.";
				result.put("status", "fail");
				result.put("msg", errMsg);
				return result;
			}

//			if (mf.getSize() > maxSize) {
//				  result.put("status", "fail");
//		          result.put("msg", "파일 크기는 200MB까지 가능합니다.");
//		          return result;
//			}

			if (!mf.getContentType().startsWith("video")) {
				result.put("status", "fail");
	            result.put("msg", "영상 파일만 업로드 가능합니다.");
	            return result;
			} // end if

			// 파일명 생성
			String originalName = mf.getOriginalFilename();
			String fileName = "";
			String fileExt = "";

			int fileSeperator = originalName.lastIndexOf(".");
			if (fileSeperator == -1) {
				fileSeperator = originalName.length();
			}

			fileName = originalName.substring(0, fileSeperator);
			if (originalName.contains(".")) {
				fileExt = originalName.substring(fileSeperator + 1);
			}
						
			String baseName= originalName.substring(0, fileSeperator);
			fileName = baseName;
			String fullName = fileName+"."+fileExt;
			String securityName = UUID.randomUUID().toString();
			
			String securityFileName = securityName +"."+fileExt;
			
			Path path = Paths.get(courseVideoPath, securityFileName);
			
			//중복 1씩 증가 
			int cnt = 1;
			StringBuilder fileSB = new StringBuilder();
			while(Files.exists(path)) {
				 securityName = securityName + "(" + cnt + ")";
				 securityFileName = securityName + "." + fileExt;
				    path = Paths.get(courseVideoPath, securityFileName);
				    cnt++;				
			   
			}

			// 디렉토리 생성
			Files.createDirectories(path.getParent());
			mf.transferTo(path.toFile());
			
			int wait = 0;
			while (!path.toFile().exists() && wait < 10) {
			    Thread.sleep(100); // 0.1초씩 대기
			    wait++;
			}
			
			
			
			VideoDTO newDTO = new VideoDTO();
			newDTO.setDescription(vDTO.getDescription());
			newDTO.setTitle(vDTO.getTitle());
			newDTO.setFileName(securityFileName);
			newDTO.setCourseSeq(vDTO.getCourseSeq());
			newDTO.setFilePath("/courseVideo/"+securityFileName);
			newDTO.setIsCompleted("N");
			newDTO.setUploadDate(new Date());
			newDTO.setVideoOrder(videoCnt);
			newDTO.setType("video");
			if(newDTO.getCourseSeq() != null && newDTO.getFileName() != null) {
				vs.addVideo(newDTO);
			}else {
				result.put("msg", "시발련아");
			}
			
			videoCnt++;
			
			if(cs.updateVideoCount(vDTO.getCourseSeq()) == 1) {
			};
			dtoList.add(newDTO);
			result.put("cDTO", newDTO);
			
		} // end for
		
		result.put("status", "success");
		result.put("msg", "업로드 완료");
		result.put("videoList", dtoList);
		
		return result;
	}//upload
//	@PostMapping("/upload/upload_video")
//	@ResponseBody
//	public Map<String, Object> videoData(
//	        @RequestParam("upfile") MultipartFile mf,
//	        @ModelAttribute VideoDTO vDTO, Model model) throws Exception {
//	    Map<String, Object> result = new HashMap<>();
//
//	    if (mf == null || mf.isEmpty()) {
//	        result.put("status", "fail");
//	        result.put("msg", "업로드한 파일이 없습니다.");
//	        return result;
//	    }
//
//	    if (!mf.getContentType().startsWith("video")) {
//	        result.put("status", "fail");
//	        result.put("msg", "영상 파일만 업로드 가능합니다.");
//	        return result;
//	    }
//
//	    // 원본 파일명, 확장자
//	    String originalName = mf.getOriginalFilename();
//	    String fileExt = "";
//	    int dotIndex = originalName.lastIndexOf(".");
//	    if (dotIndex != -1) {
//	        fileExt = originalName.substring(dotIndex + 1);
//	    }
//
//	    String uuid = UUID.randomUUID().toString();
//	    String savedFileName = uuid + "." + fileExt;
//
//	    Path savePath = Paths.get(courseVideoPath, savedFileName);
//	    Files.createDirectories(savePath.getParent());
//	    mf.transferTo(savePath.toFile());
//
//	    VideoDTO newDTO = new VideoDTO();
//	    newDTO.setDescription(vDTO.getDescription());
//	    newDTO.setTitle(vDTO.getTitle());
//	    newDTO.setFileName(savedFileName);
//	    newDTO.setFilePath(savePath.toString());
//	    newDTO.setCourseSeq(vDTO.getCourseSeq());
//	    newDTO.setIsCompleted("N");
//	    newDTO.setUploadDate(new Date());
//	    newDTO.setVideoOrder(1); 
//	    newDTO.setType("video");
//
//	    if (newDTO.getCourseSeq() != null && newDTO.getFileName() != null) {
//	        vs.addVideo(newDTO);
//	    } else {
//	        result.put("status", "fail");
//	        result.put("msg", "강의 번호 또는 파일 이름 누락");
//	        return result;
//	    }
//
//	    result.put("status", "success");
//	    result.put("msg", "업로드 완료");
//	    result.put("cDTO", newDTO);
//	    return result;
//	}
		
	@GetMapping("/video/watch_video")
	public String watchVideoList(Model model) {
		
		model.addAttribute("videoList", vs.searchVideo());
		
		return "video/watch_video";
	}
	
	@GetMapping("/video/watch")
	public String showVideo(@RequestParam String videoSeq, Model model) {
		VideoDTO vDTO = vs.showVideo(videoSeq);
		
		model.addAttribute("videoData", vDTO);
		
		return "video/video_frm";
	}
	
	

}
