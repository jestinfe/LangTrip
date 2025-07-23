package kr.co.sist.e_learning.adBanner;

import java.util.List;

public interface AdBannerService {
    List<AdBannerEntity> getTop5Banners();
    void recordView(Long bannerId);
    void recordClick(Long bannerId);
}
