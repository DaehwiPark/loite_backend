package com.boot.loiteMsBack.support.counsel.dto;

import com.boot.loiteMsBack.support.counsel.entity.SupportCounselEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportCounselDto {

    private Long counselId;
    private Long counselUserId;
    private String counselTitle;
    private String counselContent;
    private String counselEmail;
    private String counselStatus;
    private String counselReplyContent;
    private LocalDateTime counselRepliedAt;
    private Long counselRepliedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String delYn;
}
