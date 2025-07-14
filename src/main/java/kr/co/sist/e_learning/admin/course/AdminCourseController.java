package kr.co.sist.e_learning.admin.course;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCourseController {

    private final AdminCourseService courseService;

    // 1. 강의 목록 조회
    @GetMapping("/courses")
    public String showCourseList(Model model) {
        List<AdminCourseDTO> courseList = courseService.getAllCourses();
        model.addAttribute("courseList", courseList);
        return "admin/course/course_list";  // 템플릿 경로
    }

    // 2. 공개 여부 변경
    @PostMapping("/course/toggle")
    public String toggleCourseVisibility(@RequestParam String courseSeq, @RequestParam String isPublic) {
        courseService.updateCourseVisibility(courseSeq, isPublic);
        return "redirect:/admin/courses";
    }
}
