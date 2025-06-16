package com.boot.loiteMsBack.product.general;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTagId implements Serializable {

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "TAG_ID")
    private Long tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductTagId)) return false;
        ProductTagId that = (ProductTagId) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, tagId);
    }
}
