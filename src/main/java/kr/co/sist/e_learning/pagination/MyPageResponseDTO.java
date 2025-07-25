package kr.co.sist.e_learning.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MyPageResponseDTO<T> {
    private static final int DEFAULT_BLOCK_SIZE = 5;

    private List<T> dtoList; // 결과 리스트
    private int totalCount; // 전체 결과 수
    private int totalPages; // 전체 페이지 수
    private int page; // 현재 페이지
    private int startPage; // 시작 페이지 번호
    private int endPage; // 끝 페이지 번호
    private boolean hasPrev; // 이전 블록 존재 여부
    private boolean hasNext; // 다음 블록 존재 여부
    private int pageSize; // 페이지 크기
    private MyPageRequestDTO pageRequestDTO; // 요청 DTO

    public MyPageResponseDTO(int totalCount, MyPageRequestDTO pageRequestDTO, List<T> dtoList) {
        this.totalCount = totalCount;
        this.pageRequestDTO = pageRequestDTO;
        this.dtoList = dtoList;

        this.page = pageRequestDTO.getPage();
        this.pageSize = pageRequestDTO.getSize();

        this.totalPages = (totalCount == 0) ? 1 : (int) Math.ceil((double) totalCount / pageSize);

        this.startPage = ((page - 1) / DEFAULT_BLOCK_SIZE) * DEFAULT_BLOCK_SIZE + 1;
        this.startPage = Math.min(this.startPage, this.totalPages);
        this.startPage = Math.max(1, this.startPage);

        this.endPage = Math.min(startPage + DEFAULT_BLOCK_SIZE - 1, totalPages);
        this.endPage = Math.max(this.startPage, this.endPage);

        this.hasPrev = startPage > 1;
        this.hasNext = endPage < totalPages;
        
        this.hasPrev = startPage > 1;
        this.hasNext = endPage < totalPages;
    }
}