package kr.co.sist.e_learning.course;

import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String listCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false, defaultValue = "uploadDate,desc") String sort,
            Model model) {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("pageSize", pageSize);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        params.put("sort", sort);
        params.put("offset", (page - 1) * pageSize);
        params.put("limit", pageSize);

        PageResponseDTO<UserCourseListDTO> responseDTO = courseService.getAllCourses(params);

        model.addAttribute("courseList", responseDTO.getList());
        model.addAttribute("currentPage", responseDTO.getPage());
        model.addAttribute("totalPages", responseDTO.getTotalPages());
        model.addAttribute("startPage", Math.max(1, responseDTO.getStartPage()));
        model.addAttribute("endPage", Math.max(1, responseDTO.getEndPage()));
        model.addAttribute("totalCount", responseDTO.getTotalCnt());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("sort", sort);

        return "ksh/course_list";
    }
}