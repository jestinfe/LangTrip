package kr.co.sist.e_learning.adBanner;

import java.util.List;

public interface AdBannerService {
    List<AdBannerEntity> getTop5Banners();
    List<AdBannerEntity> getNext5Banners(); 
    void recordClick(Long bannerId);
}
