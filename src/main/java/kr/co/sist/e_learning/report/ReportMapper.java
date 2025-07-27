package kr.co.sist.e_learning.report;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportMapper {
    boolean existsTodayReport(Map<String, Object> paramMap);

    void insertReport(ReportDTO dto);

    void insertReportReasonUser(Map<String, Object> paramMap);
}
