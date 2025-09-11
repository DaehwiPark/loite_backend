package com.boot.loiteBackend.web.home.best.repository;

import com.boot.loiteBackend.domain.home.best.entity.HomeBestItemEntity;
import com.boot.loiteBackend.web.home.best.dto.HomeBestItemResponseDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface HomeBestItemRepository extends Repository<HomeBestItemEntity, Long> {

    @Query("""
                select new com.boot.loiteBackend.web.home.best.dto.HomeBestItemResponseDto(
                    p.productName,
                    p.productModelName,
                    p.productSummary,
                    p.productPrice,
                    e.slotNo,
                    p.productId,
                    pc.categoryId,
                    d1.categoryPath,
                    d2.categoryPath,
                    pc.categoryPath,
                    img.imageUrl
                )
                from HomeBestItemEntity e
                join AdminProductEntity p
                     on p.productId = e.productId
                left join p.productCategory pc
                left join pc.categoryParentId d2
                left join d2.categoryParentId d1
                left join AdminProductImageEntity img
                       on img.product = p
                      and img.activeYn = 'Y'
                      and img.imageType = 'MAIN'
                      and img.imageSortOrder = (
                            select min(i.imageSortOrder)
                            from AdminProductImageEntity i
                            where i.product = p
                              and i.activeYn = 'Y'
                              and i.imageType = 'MAIN'
                       )
                where e.displayYn = 'Y'
                order by e.slotNo,
                         d1.categorySortOrder,
                         d2.categorySortOrder,
                         pc.categorySortOrder
            """)
    List<HomeBestItemResponseDto> findActiveBestItemsAsDto();
}
