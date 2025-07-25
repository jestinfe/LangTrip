package kr.co.sist.e_learning.admin.donation;

import kr.co.sist.e_learning.admin.PageRequestDTO_donation;
import kr.co.sist.e_learning.admin.PageResponseDTO_donation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDonationServiceImpl implements AdminDonationService {

    private final AdminDonationMapper adminDonationMapper;

    @Override
    public PageResponseDTO_donation<DonationVO> getDonationList(DonationSearchDTO searchDTO, PageRequestDTO_donation pageRequestDTO) {
       
        int totalCount = adminDonationMapper.countDonations(searchDTO);
        
        List<DonationVO> donations = adminDonationMapper.selectDonations(searchDTO, pageRequestDTO);
      

        return PageResponseDTO_donation.<DonationVO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .list(donations)
                .totalCnt(totalCount)
                .build();
    }

    @Override
    @Transactional
    public boolean deleteDonationMessage(String donationId) {
        String message = adminDonationMapper.selectMessageByDonationId(donationId); // 기존 메시지 조회

        // delete_message 테이블에 이미 기록된 메시지가 있는지 확인
        String existingDeletedMessage = adminDonationMapper.selectMessageByDonationId(donationId);

        if (existingDeletedMessage == null) {
            // 존재하지 않을 때만 새로 삽입
            adminDonationMapper.insertDeletedMessage(donationId, message);
        } 

        int affectedRows = adminDonationMapper.updateDonationMessageDeleted(donationId); // donation 테이블 상태 업데이트
        return affectedRows == 1;
    }
}