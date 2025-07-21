package kr.co.sist.e_learning.admin.log;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminLogExcelController {

    @Autowired
    private AdminLogService adminLogService;

    @GetMapping("/admin/log/excel")
    public void downloadExcel(HttpServletResponse response,
                              @RequestParam(required = false) String searchType,
                              @RequestParam(required = false) String searchKeyword,
                              @RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String endDate) throws IOException {

        AdminLogDTO searchDTO = new AdminLogDTO();
        searchDTO.setSearchType(searchType);
        searchDTO.setSearchKeyword(searchKeyword);
        searchDTO.setStartDate(startDate);
        searchDTO.setEndDate(endDate);

        List<AdminLogDTO> logs = adminLogService.getAllAdminLogs(searchDTO);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Admin Logs");

            // Header
            String[] headers = {"Log ID", "Admin ID", "Action Type", "Target ID", "Log Time", "Details"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Data rows
            int rowNum = 1;
            for (AdminLogDTO log : logs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(log.getLogId());
                row.createCell(1).setCellValue(log.getAdminId());
                row.createCell(2).setCellValue(log.getActionType());
                row.createCell(3).setCellValue(log.getTargetId());
                row.createCell(4).setCellValue(
                	log.getLogTime().toLocalDateTime().format(formatter)
                );                
                row.createCell(5).setCellValue(log.getDetails());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String fileName = "admin_logs_" + LocalDate.now() + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            workbook.write(response.getOutputStream());
        }
    }

}
