package kr.co.sist.e_learning.admin.donation;

import kr.co.sist.e_learning.admin.PageRequestDTO_donation;
import kr.co.sist.e_learning.admin.PageResponseDTO_donation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public boolean deleteDonationMessage(Long donationId) {
        return adminDonationMapper.updateDonationMessageDeleted(donationId) == 1;
    }
}
