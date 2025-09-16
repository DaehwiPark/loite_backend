package com.boot.loiteBackend.admin.manager.notice.repository;

import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AdminManagerNoticeRepository extends org.springframework.data.jpa.repository.JpaRepository<AdminManagerNoticeEntity, Long> {

    @Query("""
        select n
        from AdminManagerNoticeEntity n
        where n.status = :published
          and n.deletedAt is null
          and (n.expiresAt is null or n.expiresAt > :now)
        order by case when n.pinned = true then 0 else 1 end,
                 coalesce(n.publishedAt, n.createdAt) desc
    """)
    Page<AdminManagerNoticeEntity> findVisible(
            Pageable pageable,
            @Param("now") LocalDateTime now,
            @Param("published") AdminManagerNoticeEntity.NoticeStatus published
    );

    @Query("""
        select count(n)
        from AdminManagerNoticeEntity n
        where n.status = :published
          and n.deletedAt is null
          and (n.expiresAt is null or n.expiresAt > :now)
          and not exists (
              select 1 from AdminManagerNoticeRead r
              where r.noticeId = n.id and r.managerId = :managerId
          )
    """)
    long countUnread(
            @Param("managerId") Long managerId,
            @Param("now") LocalDateTime now,
            @Param("published") AdminManagerNoticeEntity.NoticeStatus published
    );
}
