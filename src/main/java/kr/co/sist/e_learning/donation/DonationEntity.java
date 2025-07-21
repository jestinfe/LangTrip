package kr.co.sist.e_learning.donation;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DONATION")
public class DonationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "donation_seq_gen")
    @SequenceGenerator(
        name = "donation_seq_gen",
        sequenceName = "SEQ_donation",
        allocationSize = 1
    )
    @Column(name = "DONATION_SEQ")
    private Long donationSeq;

    @Column(name = "COURSE_SEQ")
    private Long courseSeq;

    @Column(name = "WALLET_SEQ")
    private Long sponsorWalletSeq;

    @Column(name = "WALLET_SEQ2")
    private Long instructorWalletSeq;

    @Column(name = "DONATION_AMOUNT")
    private Long amount;

    @Column(name = "MESSAGE", length = 1000)
    private String message;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
