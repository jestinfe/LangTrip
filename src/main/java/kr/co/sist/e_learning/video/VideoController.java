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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.sist.e_learning.course.CourseNoticeDTO;
import kr.co.sist.e_learning.course.CourseService;
import kr.co.sist.e_learning.course.CourseStatDTO;
import kr.co.sist.e_learning.course.NoticeService;


@Controller
public class VideoController {

	@Value("${file.upload-dir.courseVideo}")  
    private String courseVideoPath;

	@Autowired
	private VideoService vs;
	
	@Autowired
	private CourseService cs;
	
	@Autowired
	private NoticeService ns;
	
	@GetMapping("/upload/upload_video")
	public String showUploadForm(@RequestParam("seq") String courseSeq, Model model) {
		model.addAttribute("courseSeq", courseSeq);
		return "upload/upload_video";
	}
	
	
	
	@GetMapping("/ui/upload_frm")
	public String uploadFrm() {
		
		return "ui/upload_frm";
	}
	
	@GetMapping("/upload/modify_video_form")
	public String showModifyForm(@RequestParam("videoSeq") int videoSeq, Model model) {
	    VideoDTO video = vs.getVideoBySeq(videoSeq);
	    model.addAttribute("video", video);
	    return "upload/modify_video";
	}
	
	@PostMapping("/upload/modify_video")
	public String modifyVideo(@RequestParam(value = "upfile", required = false) MultipartFile upfile,
	                          @ModelAttribute VideoDTO vDTO,
	                          RedirectAttributes redirectAttributes) throws Exception {

	    // 파일이 새로 업로드되었으면 저장
	    if (upfile != null && !upfile.isEmpty()) {
	        String originalName = upfile.getOriginalFilename();
	        String fileExt = originalName.substring(originalName.lastIndexOf(".") + 1);
	        String uuid = UUID.randomUUID().toString();
	        String securityFileName = uuid + "." + fileExt;

	        Path path = Paths.get(courseVideoPath, securityFileName);
	        Files.createDirectories(path.getParent());
	        upfile.transferTo(path.toFile());

	        // 새로운 파일 정보로 대체
	        vDTO.setFileName(securityFileName);
	        vDTO.setFilePath("/courseVideo/" + securityFileName);
	    }

	    vDTO.setUploadDate(new Date());  // 날짜 갱신
	    vs.modifyVideo(vDTO);            // DB update 처리

	    redirectAttributes.addFlashAttribute("msg", "비디오 수정 완료");
	    return "redirect:/upload/upload_course?seq=" + vDTO.getCourseSeq();
	}
	
	
	@PostMapping("/upload/delete_video")
	public String deleteVideo(@RequestParam("videoSeq") int videoSeq,
	                          @RequestParam("courseSeq") String courseSeq) {
	    vs.deleteVideo(videoSeq); // 삭제 로직 수행
	    return "redirect:/upload/upload_course?seq=" + courseSeq;
	}
	
	
	@PostMapping("/upload/upload_video")
	@ResponseBody
	public Map<String, Object> videoData(
			@RequestParam("upfile") List<MultipartFile> mfList,
			@ModelAttribute VideoDTO vDTO, Model model
			)
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
			
			
			int videoOrder = vs.getMaxVideoOrder(vDTO.getCourseSeq());
			VideoDTO newDTO = new VideoDTO();
			newDTO.setDescription(vDTO.getDescription());
			newDTO.setTitle(vDTO.getTitle());
			newDTO.setFileName(securityFileName);
			newDTO.setCourseSeq(vDTO.getCourseSeq());
			newDTO.setFilePath("/courseVideo/"+securityFileName);
			newDTO.setIsCompleted("N");
			newDTO.setUploadDate(new Date());
			
			newDTO.setVideoOrder(videoOrder);
			newDTO.setType("video");
			
			if(newDTO.getCourseSeq() != null && newDTO.getFileName() != null) {
				vs.addVideo(newDTO);
			}else {
				result.put("msg", "시발련아");
			}
						
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
	
	//업로드폼 버튼 -> 비디오 폼으로
	@GetMapping("/video/watch")
	public String showVideo(@RequestParam("videoSeq") String videoSeq, 
			@RequestParam("courseSeq") String courseSeq, Model model, Authentication authentication,
			@RequestParam(required = false) String from) {
		
		Object principal = authentication.getPrincipal();
		Long userSeq = null;
		if(principal instanceof Long) {
			userSeq = (Long) principal;
		}
		
		CourseNoticeDTO cnDTO = ns.getLatestLearning(courseSeq);
		
		
		VideoDTO vDTO = vs.showVideo(videoSeq);
		model.addAttribute("cnDTO", cnDTO);
		model.addAttribute("userSeq", userSeq);
		model.addAttribute("courseSeq", courseSeq);
		model.addAttribute("videoData", vDTO);
		model.addAttribute("from",from != null ? from : "user");
		
		return "video/video_frm";
	}
	
	@PostMapping("/course/stat/update")
	@ResponseBody
	public ResponseEntity<?> updateStat(@RequestBody CourseStatDTO csDTO) {
		 System.out.println("컨트롤러 도착 updateStat 요청: " + csDTO);
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("userSeq", csDTO.getUserSeq());
	    map.put("videoSeq", csDTO.getVideoSeq());
	    map.put("courseSeq", csDTO.getCourseSeq());
	    System.out.println("시발:"+csDTO.getCourseSeq());
	    System.out.println(csDTO.getUserSeq());
	    System.out.println(csDTO.getVideoSeq());
	    System.out.println(csDTO.getLastTime());
	    
	    
		if(cs.existCourseStat(map)>0) 
		{ 
			System.out.println("업뎃");
			cs.updateCourseStat(csDTO);
		}else {
			System.out.println("추가");
			cs.insertCourseStat(csDTO);
		}
		
		return ResponseEntity.ok("updated");
	}
	
	@GetMapping("/course/stat/get")
	@ResponseBody
	public ResponseEntity<CourseStatDTO> getCourseStat(
	        @RequestParam("userSeq") Long userSeq,
	        @RequestParam("videoSeq") int videoSeq,
	        @RequestParam("courseSeq") String courseSeq
	) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("userSeq", userSeq);
	    map.put("videoSeq", videoSeq);
	    map.put("courseSeq", courseSeq);
	    
	    CourseStatDTO stat = cs.selectCourseStat(map);
	    
	    return ResponseEntity.ok(stat);
	}
	
	
	

}
