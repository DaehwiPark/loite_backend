package com.boot.loiteBackend.web.home.recommend.item.repository;

import com.boot.loiteBackend.domain.home.recommend.item.entity.HomeRecoItemEntity;
import com.boot.loiteBackend.web.home.recommend.item.dto.HomeRecoItemResponseDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeRecoItemRepository extends Repository<HomeRecoItemEntity, Long> {
    boolean existsById(Long id);

    @Query("""
        select new com.boot.loiteBackend.web.home.recommend.item.dto.HomeRecoItemResponseDto(
            ri.sectionId,
            p.productName,
            p.productModelName,
            p.productSummary,
            p.productPrice,
            ri.slotNo,
            p.productId,
            pc.categoryId,
            d1.categoryPath,
            d2.categoryPath,
            pc.categoryPath,
            img.imageUrl
        )
        from HomeRecoItemEntity ri
        join AdminProductEntity p
             on p.productId = ri.productId
        left join p.productCategory pc
        left join pc.categoryParentId d2
        left join d2.categoryParentId d1
        left join AdminProductImageEntity img
               on img.product = p
              and img.activeYn = 'Y'
              and img.imageType = 'THUMBNAIL'
              and img.imageSortOrder = (
                    select min(i.imageSortOrder)
                    from AdminProductImageEntity i
                    where i.product = p
                      and i.activeYn = 'Y'
                      and i.imageType = 'THUMBNAIL'
               )
        where ri.sectionId = :sectionId
          and ri.displayYn = 'Y'
        order by ri.slotNo,
                 d1.categorySortOrder,
                 d2.categorySortOrder,
                 pc.categorySortOrder
    """)
    List<HomeRecoItemResponseDto> findActiveBySectionIdAsDto(@Param("sectionId") Long sectionId);
}