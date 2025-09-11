package com.boot.loiteBackend.web.order.entity;

import com.boot.loiteBackend.web.delivery.enums.DeliveryStatus;
import com.boot.loiteBackend.web.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "ORDER_NUMBER", nullable = false, unique = true, length = 100)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATUS", nullable = false, length = 20)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @Column(name = "ORIGINAL_AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal originalAmount;

    @Column(name = "DISCOUNT_AMOUNT", precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "TOTAL_AMOUNT", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "DELIVERY_FEE", precision = 15, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Column(name = "RECEIVER_NAME", nullable = false, length = 100)
    private String receiverName;

    @Column(name = "RECEIVER_PHONE", nullable = false, length = 20)
    private String receiverPhone;

    @Column(name = "RECEIVER_ADDRESS", nullable = false, length = 255)
    private String receiverAddress;

    @Column(name = "RECEIVER_MEMO", nullable = false, length = 255)
    private String receiverMemo;

    @Enumerated(EnumType.STRING)
    @Column(name = "DELIVERY_STATUS", nullable = false, length = 20)
    private DeliveryStatus deliveryStatus;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

}
