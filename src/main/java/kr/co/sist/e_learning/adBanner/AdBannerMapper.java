package kr.co.sist.e_learning.adBanner;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdBannerMapper {
    /** 상위 5개 배너 조회 (노출 순서대로) */
    List<AdBannerEntity> findTop5();
    
    /** 배너 단건 조회 */
    AdBannerEntity findById(@Param("bannerId") Long bannerId);

    /** 노출 수 증가 */
    void incrementView(@Param("bannerId") Long bannerId);

    /** 클릭 수 증가 */
    void incrementClick(@Param("bannerId") Long bannerId);
}
