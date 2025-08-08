package com.boot.loiteBackend.web.mileage.total.entity;

import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_mileage_total")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MileageTotalEntity {

    @Id
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", foreignKey = @ForeignKey(name = "fk_summary_user"))
    private UserEntity user;

    @Column(name = "MILEAGE_TOTAL_EARNED", columnDefinition = "int default 0", nullable = false)
    private Integer mileageTotalEarned;

    @Column(name = "MILEAGE_TOTAL_USED", columnDefinition = "int default 0", nullable = false)
    private Integer mileageTotalUsed;

    @Column(name = "MILEAGE_TOTAL_EXPIRED", columnDefinition = "int default 0", nullable = false)
    private Integer mileageTotalExpired;

    @Column(name = "MILEAGE_TOTAL_AMOUNT", columnDefinition = "int default 0", nullable = false)
    private Integer mileageTotalAmount;

    @Column(name = "CREATED_AT", updatable = false, nullable = false,
            columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false,
            columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    private LocalDateTime updatedAt;
}