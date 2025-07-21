package kr.co.sist.e_learning.donation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<DonationEntity, Long> {

    

    // 내가 보낸 후원 내역
    List<DonationEntity> findBySponsorWalletSeq(Long sponsorWalletSeq);

    // 내가 받은 후원 내역
    List<DonationEntity> findByInstructorWalletSeq(Long instructorWalletSeq);

    // 강의 기준 후원 내역 (통계용)
    List<DonationEntity> findByCourseSeq(Long courseSeq);

    // 특정 후원자 → 특정 강사에게 보낸 기록
    List<DonationEntity> findBySponsorWalletSeqAndInstructorWalletSeq(Long from, Long to);
}

