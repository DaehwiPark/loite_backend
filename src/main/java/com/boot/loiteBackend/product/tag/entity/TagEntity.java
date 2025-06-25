package com.boot.loiteBackend.product.tag.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    private Long tagId;

    @Column(name = "TAG_NAME", nullable = false, length = 100)
    private String tagName;

    @Column(name = "ACTIVE_YN")
    private String activeYn;
}
