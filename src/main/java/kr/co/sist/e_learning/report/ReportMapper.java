package kr.co.sist.e_learning.report;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.e_learning.pagination.PageRequestDTO;

@Mapper
public interface ReportMapper {
	List<ReportDTO> selectReports(PageRequestDTO pDTO);
	int countReports(PageRequestDTO pDTO);
}
