package kr.co.sist.e_learning.admin.payments;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminMileageStatsDTO {
    private Long chargedMileage;
    private Long donatedMileage;
    private Long receivedMileage;
    private Long availableMileage;
}
