package kr.co.sist.e_learning.admin.course;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCourseController {

    private final AdminCourseService courseService;

    // 1. 강의 목록 조회 (검색, 정렬, 페이징 포함)
    @GetMapping("/courses")
    public String showCourseList(
            @ModelAttribute PageRequestDTO pageRequestDTO,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "includeInactive", defaultValue = "false") boolean includeInactive,
            Model model) {

        PageResponseDTO<AdminCourseDTO> responseDTO = courseService.getCourses(pageRequestDTO, title, category, includeInactive);
        model.addAttribute("courseList", responseDTO.getList());
        model.addAttribute("totalCount", responseDTO.getTotalCnt());
        model.addAttribute("pageResponseDTO", responseDTO);
        model.addAttribute("title", title);
        model.addAttribute("category", category);
        model.addAttribute("includeInactive", includeInactive);

        return "admin/course/course_list";
    }

    // 2. 강의 상세 조회
    @GetMapping("/courses/{courseSeq}")
    public String showCourseDetail(@PathVariable String courseSeq, Model model) {
        AdminCourseDTO course = courseService.getCourseDetail(courseSeq);
        model.addAttribute("course", course);
        // Dummy data for roadmap, videos, quizzes for now
        // TODO: Replace with actual data from DB
        model.addAttribute("adminName", "관리자"); // Placeholder
        model.addAttribute("adminInitial", "A"); // Placeholder
        model.addAttribute("roadmap", java.util.Arrays.asList(
            new Object() { public String number = "1"; public String title = "1강: 인사말과 자기소개"; public String description = "기본 인사말과 자기소개 방법을 배웁니다."; },
            new Object() { public String number = "2"; public String title = "2강: 일상 대화"; public String description = "일상생활에서 자주 사용하는 표현을 익힙니다."; }
        ));
        model.addAttribute("videos", java.util.Arrays.asList(
            new Object() { public String title = "영상 1: 인사말 복습"; public String description = "1강 복습 영상"; },
            new Object() { public String title = "영상 2: 자기소개 연습"; public String description = "2강 연습 영상"; }
        ));
        model.addAttribute("quizzes", java.util.Arrays.asList(
            new Object() { public String title = "퀴즈 1: 인사말"; public String description = "인사말 테스트"; },
            new Object() { public String title = "퀴즈 2: 일상 대화"; public String description = "일상 대화 테스트"; }
        ));
        return "admin/course/course_detail";
    }

    // 3. 공개 여부 변경
    @PostMapping("/course/toggle")
    public String toggleCourseVisibility(@RequestParam String courseSeq, @RequestParam String isPublic) {
        courseService.updateCourseVisibility(courseSeq, isPublic);
        return "redirect:/admin/courses";
    }

    // 4. 강의 소프트 삭제
    @PostMapping("/courses/delete/{courseSeq}")
    public String softDeleteCourse(@PathVariable String courseSeq) {
        courseService.softDeleteCourse(courseSeq);
        return "redirect:/admin/courses";
    }
}
