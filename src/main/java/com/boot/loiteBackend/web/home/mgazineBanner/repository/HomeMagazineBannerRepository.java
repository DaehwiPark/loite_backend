package com.boot.loiteBackend.web.home.mgazineBanner.repository;

import com.boot.loiteBackend.domain.home.magazinebanner.entity.HomeMagazineBannerEntity;
import com.boot.loiteBackend.admin.product.product.enums.ImageType;
import com.boot.loiteBackend.web.home.mgazineBanner.dto.HomeMagazineBannerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeMagazineBannerRepository extends JpaRepository<HomeMagazineBannerEntity, Long> {

    @Query("""
            select new com.boot.loiteBackend.web.home.mgazineBanner.dto.HomeMagazineBannerDto(
                mb.id,
                p.productId,
                mb.videoThumbnailUrl,
                mb.videoUrl,
                p.productName,
                p.productModelName,
                mb.title,
                mb.subtitle,
                mb.buttonText,
                mb.buttonUrl,
                img1.imageUrl,
                img2.imageUrl
            )
            from HomeMagazineBannerEntity mb
            join AdminProductEntity p
              on p.productId = mb.productId
            left join AdminProductImageEntity img1
              on img1.product = p
             and img1.activeYn = 'Y'
             and img1.imageType = :thumbType
             and img1.imageSortOrder = (
                   select min(i.imageSortOrder)
                   from AdminProductImageEntity i
                   where i.product = p
                     and i.activeYn = 'Y'
                     and i.imageType = :thumbType
              )
            left join AdminProductImageEntity img2
              on img2.product = p
             and img2.activeYn = 'Y'
             and img2.imageType = :thumbType
             and img2.imageSortOrder = (
                   select min(i2.imageSortOrder)
                   from AdminProductImageEntity i2
                   where i2.product = p
                     and i2.activeYn = 'Y'
                     and i2.imageType = :thumbType
                     and i2.imageSortOrder > coalesce(img1.imageSortOrder, -1)
              )
            where mb.displayYn = 'Y'
            order by mb.sortOrder asc
            """)
    List<HomeMagazineBannerDto> findActiveMagazineBannersAsDto(@Param("thumbType") ImageType thumbType);
}