package kr.co.sist.e_learning.mypage;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FundingDAO {
    FundingDTO selectAccountInfo(Long userSeq);
    List<FundingDTO> selectMyDonations(Long userSeq);
    List<FundingDTO> selectReceivedDonations(Long userSeq);
    List<FundingDTO> selectMyPayments(Long userSeq);
}
