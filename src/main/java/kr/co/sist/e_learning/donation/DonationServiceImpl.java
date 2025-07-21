package kr.co.sist.e_learning.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sist.e_learning.mileWallet.MileWalletDao;
import kr.co.sist.e_learning.mileWallet.MileWalletDTO;

@Service
public class DonationServiceImpl implements DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private MileWalletDao mileWalletDao;

    @Autowired
    private LectureService lectureService;

    @Override
    @Transactional
    public DonationDTO donate(Long sponsorUserSeq, DonationRequestDTO dto) {

        // 1. 후원자 지갑 및 잔액 확인
        Long sponsorWalletSeq = mileWalletDao.selectWalletSeqByUserSeq(sponsorUserSeq);
        if (sponsorWalletSeq == null) {
            throw new IllegalArgumentException("후원자의 지갑 정보가 없습니다.");
        }

        Long currentUserMiles = getUserMiles(sponsorUserSeq);
        if (currentUserMiles < dto.getAmount()) {
            throw new IllegalArgumentException("보유 마일이 부족합니다.");
        }

        // 2. 강의 정보 및 강사 정보 조회
        LectureDetailDTO lecture = lectureService.getLectureDetail(dto.getLectureId());
        if (lecture == null) {
            throw new IllegalArgumentException("존재하지 않는 강의입니다.");
        }
        Long instructorUserSeq = lecture.getInstructorId();

        // 3. 강사 지갑 없으면 생성
        Long instructorWalletSeq = mileWalletDao.selectWalletSeqByUserSeq(instructorUserSeq);
        if (instructorWalletSeq == null) {
            MileWalletDTO walletDTO = new MileWalletDTO();
            walletDTO.setUserSeq(instructorUserSeq);
            walletDTO.setTotalMiles(0L);
            walletDTO.setDonation_available(0L);
            walletDTO.setDonated_miles(0L);
            walletDTO.setReceived_miles(0L);
            mileWalletDao.insertMileWallet(walletDTO);

            instructorWalletSeq = mileWalletDao.selectWalletSeqByUserSeq(instructorUserSeq);
        }

        long amount = dto.getAmount();

        // 4. 마일 차감 및 적립
        mileWalletDao.updateMileBalance(sponsorWalletSeq, -amount);
        mileWalletDao.updateDonationAvailable(sponsorWalletSeq, -amount);
        mileWalletDao.addDonatedMiles(sponsorWalletSeq, amount);

        mileWalletDao.updateMileBalance(instructorWalletSeq, amount);
        mileWalletDao.addReceivedMiles(instructorWalletSeq, amount);

        // 5. 후원 기록 저장
        DonationEntity donation = new DonationEntity();
        donation.setCourseSeq(dto.getLectureId());  // DB에서는 String 타입
        donation.setSponsorWalletSeq(sponsorWalletSeq);
        donation.setInstructorWalletSeq(instructorWalletSeq);
        donation.setAmount(amount);
        donation.setMessage(dto.getMessage());

        donationRepository.save(donation);

        // 6. 결과 DTO 반환
        return new DonationDTO(lecture.getTitle(), lecture.getInstructorName(), (int) amount, dto.getMessage());
    }

    @Override
    public Long getUserMiles(Long userSeq) {
        Long walletSeq = mileWalletDao.selectWalletSeqByUserSeq(userSeq);
        if (walletSeq == null) {
            return 0L;
        }
        Long miles = mileWalletDao.selectCurrentMiles(walletSeq);
        return miles == null ? 0 : miles;
    }
}
