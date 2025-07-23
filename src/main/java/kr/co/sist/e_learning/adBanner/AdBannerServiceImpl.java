package kr.co.sist.e_learning.adBanner;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdBannerServiceImpl implements AdBannerService {
    private final AdBannerMapper mapper;

    public AdBannerServiceImpl(AdBannerMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<AdBannerEntity> getTop5Banners() {
        return mapper.findTop5();
    }

    @Override
    @Transactional
    public void recordView(Long bannerId) {
        mapper.incrementView(bannerId);
    }

    @Override
    @Transactional
    public void recordClick(Long bannerId) {
        mapper.incrementClick(bannerId);
    }
}
