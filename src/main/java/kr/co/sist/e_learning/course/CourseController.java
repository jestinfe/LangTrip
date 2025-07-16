package kr.co.sist.e_learning.course;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public String getCourseList(
            @ModelAttribute PageRequestDTO pageRequestDTO,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "difficulty", required = false) String difficulty,
            @RequestParam(value = "priceRange", required = false) String priceRange,
            Model model) {

        PageResponseDTO<UserCourseListDTO> responseDTO = courseService.getUserCourses(pageRequestDTO, searchTerm, category, difficulty, priceRange);
        model.addAttribute("courses", responseDTO.getList());
        model.addAttribute("pageResponseDTO", responseDTO);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("category", category);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("priceRange", priceRange);

        return "ksh/course_list";
    }
}
