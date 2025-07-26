package kr.co.sist.e_learning.mypage;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DailyStudyTimeDTO {

    @JsonProperty("studyDate")
    private String studyDate;

    @JsonProperty("totalMinutes")
    private int totalMinutes;
}