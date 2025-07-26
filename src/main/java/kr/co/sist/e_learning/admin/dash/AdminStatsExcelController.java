package kr.co.sist.e_learning.admin.dash;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.e_learning.admin.auth.AdminUserDetails;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class AdminStatsExcelController {

    @Autowired
    private AdminDashService dashboardService;

    // 관리자 인증 확인
    private String getOrInitAdminId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("인증되지 않은 관리자입니다.");
        }
        AdminUserDetails details = (AdminUserDetails) auth.getPrincipal();
        return details.getUsername();
    }

    // ✅ 1. 가입경로 + 일별 가입자 수 + 탈퇴사유 통합 엑셀
    @GetMapping("/admin/statistics/excel/user_summary")
    public void exportUserSummaryExcel(HttpServletResponse response, Authentication auth) throws IOException {
        getOrInitAdminId(auth);

        List<AdminDashDTO> signupPathStats = dashboardService.getSignupPathStats();
        List<AdminDashDTO> signupStats = dashboardService.getDailySignupStats();
        List<AdminDashDTO> unsignStats = dashboardService.getUnsignReasonStats();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("회원 통계");

        String[] headers = {"날짜", "가입자 수", "유입 경로", "경로별 가입자 수", "탈퇴 사유", "탈퇴자 수"};

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        int maxSize = Math.max(signupStats.size(), Math.max(signupPathStats.size(), unsignStats.size()));

        for (int i = 0; i < maxSize; i++) {
            Row row = sheet.createRow(rowNum++);
            if (i < signupStats.size()) {
                AdminDashDTO dto = signupStats.get(i);
                row.createCell(0).setCellValue(dto.getRegDate());
                row.createCell(1).setCellValue(dto.getDailyCount());
            }
            if (i < signupPathStats.size()) {
                AdminDashDTO dto = signupPathStats.get(i);
                row.createCell(2).setCellValue(dto.getSignupPath());
                row.createCell(3).setCellValue(dto.getDailyCount());
            }
            if (i < unsignStats.size()) {
                AdminDashDTO dto = unsignStats.get(i);
                row.createCell(4).setCellValue(dto.getUnsignPath());
                row.createCell(5).setCellValue(dto.getUnsignCount());
            }
        }

        autoSizeColumns(sheet, headers.length);
        downloadWorkbook(response, workbook, "user_summary_stats");
    }

    // ✅ 2. 광고 클릭 수 엑셀
    @GetMapping("/admin/statistics/excel/ad_click")
    public void exportAdClickExcel(HttpServletResponse response, Authentication auth) throws IOException {
        getOrInitAdminId(auth);

        List<AdminDashDTO> stats = dashboardService.getAdClickStats();
        String[] headers = {"배너 ID", "제목", "클릭 수"};

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("광고 클릭 수");

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (AdminDashDTO dto : stats) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dto.getBannerId());
            row.createCell(1).setCellValue(dto.getTitle());
            row.createCell(2).setCellValue(dto.getClickCount());
        }

        autoSizeColumns(sheet, headers.length);
        downloadWorkbook(response, workbook, "ad_click_stats");
    }

    // 📦 공통 다운로드
    private void downloadWorkbook(HttpServletResponse response, Workbook workbook, String prefix) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = prefix + "_" + timestamp + ".xlsx";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // 📏 자동 열 너비 조정
    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
