package com.boot.loiteBackend.admin.product.tag.entity;

import com.boot.loiteBackend.admin.product.general.AdminProductTagId;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_product_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductTagEntity {

    @EmbeddedId
    private AdminProductTagId adminProductTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "PRODUCT_ID")
    private AdminProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "TAG_ID")
    private AdminTagEntity tag;
}
