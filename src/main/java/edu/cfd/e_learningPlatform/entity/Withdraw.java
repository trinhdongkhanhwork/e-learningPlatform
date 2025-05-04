package edu.cfd.e_learningPlatform.entity;

import edu.cfd.e_learningPlatform.enums.WithdrawStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Withdraws")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Withdraw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String fullname;

    private BigDecimal price;
    private LocalDateTime requestDate = LocalDateTime.now();
    private String otp;

    @Enumerated(EnumType.STRING) // Để lưu enum dưới dạng chuỗi
    private WithdrawStatus status = WithdrawStatus.PENDING; // Mặc định là PENDING

    private LocalDateTime otpCreationTime; // Thời gian tạo OTP

    private LocalDateTime otpExpirationTime; // thời gian hết hạn OTP
}
