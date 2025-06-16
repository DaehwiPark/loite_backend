package com.boot.loiteMsBack.product.tag.entity;

import com.boot.loiteMsBack.product.general.ProductTagId;
import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_product_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTagEntity {

    @EmbeddedId
    private ProductTagId productTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "PRODUCT_ID")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "TAG_ID")
    private TagEntity tag;
}
