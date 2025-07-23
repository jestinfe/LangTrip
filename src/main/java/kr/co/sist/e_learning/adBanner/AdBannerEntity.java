package kr.co.sist.e_learning.adBanner;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdBannerEntity {
    private Long bannerId;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private int clickCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}