package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "invite_code")
@Data
public class InviteCode {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "teacher_id", nullable = false)
    private String teacherId;

    @Column(name = "expires_at")
    private java.sql.Timestamp expiresAt;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "usage_count")
    private Integer usageCount;
}
