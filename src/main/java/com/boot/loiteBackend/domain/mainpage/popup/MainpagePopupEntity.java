package com.boot.loiteBackend.domain.mainpage.popup;

import jakarta.persistence.*;
import lombok.*;

import java.lang.annotation.Target;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_mainpage_popup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainpagePopupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popup_id")
    private Long popupId;

    @Column(name = "popup_image_url", length = 512, nullable = false)
    private String popupImageUrl;

    @Column(name = "popup_link_url", length = 512, nullable = false)
    private String popupLinkUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "popup_target", nullable = false)
    private Target popupTarget = Target._self;
    public enum Target { _self, _blank }

    @Column(name = "popup_is_active", nullable = false)
    private boolean popupIsActive = true;

    @Column(name = "popup_sort_order", nullable = false)
    private int popupSortOrder = 0;

    @Column(name = "popup_start_at")
    private LocalDateTime popupStartAt;

    @Column(name = "popup_end_at")
    private LocalDateTime popupEndAt;

    @Column(name = "created_at", updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at",
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "popup_deleted_at")
    private LocalDateTime popupDeletedAt;
}
