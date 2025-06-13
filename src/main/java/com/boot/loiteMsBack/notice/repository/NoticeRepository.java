package com.boot.loiteMsBack.notice.repository;

import com.boot.loiteMsBack.notice.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
}
