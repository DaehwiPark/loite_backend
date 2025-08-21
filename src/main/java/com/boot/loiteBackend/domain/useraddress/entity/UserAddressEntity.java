package com.boot.loiteBackend.domain.useraddress.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;

@Entity
@Table(name = "tb_user_address",
        indexes = {
                @Index(name = "IDX_USER_ID", columnList = "USER_ID"),
                @Index(name = "IDX_ZIP_CODE", columnList = "USER_ADDRESS_ZIP_CODE")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ADDRESS_ID")
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "USER_ADDRESS_ALIAS", length = 50)
    private String alias;

    @Column(name = "USER_ADDRESS_RECIPIENT_NAME", nullable = false, length = 50)
    private String recipientName;

    @Column(name = "USER_ADDRESS_RECIPIENT_PHONE", nullable = false, length = 20)
    private String recipientPhone;

    @Column(name = "USER_ADDRESS_ZIP_CODE", nullable = false, length = 10)
    private String zipCode;

    @Column(name = "USER_ADDRESS_LINE1", nullable = false, length = 200)
    private String addressLine1;

    @Column(name = "USER_ADDRESS_LINE2", length = 200)
    private String addressLine2;

    @Column(name = "USER_ADDRESS_DELIVERY_REQUEST", length = 200)
    private String deliveryRequest;

    @Column(name = "IS_DEFAULT", nullable = false)
    private boolean isDefault;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean isDeleted;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT", nullable = false)
    private Instant updatedAt;
}
