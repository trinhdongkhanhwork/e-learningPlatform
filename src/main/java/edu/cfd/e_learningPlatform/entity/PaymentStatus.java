package edu.cfd.e_learningPlatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Payment_Statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, columnDefinition = "nvarchar(100)")
    private String statusName;

    @OneToMany(mappedBy = "paymentStatus")
    private List<Payment> payments;
}

