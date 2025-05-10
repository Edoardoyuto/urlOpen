package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "teacher_student_link")
@Data
public class TeacherStudentLink {

    @Id
    @Column(name = "id")
    private String id; // ★ここを追加！

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "teacher_id", nullable = false)
    private String teacherId;

    @Column(name = "joined_at")
    private java.sql.Timestamp joinedAt;

    @Column(name = "invite_code")
    private String inviteCode;
    
    @Column(name = "is_enabled")
    private boolean enabled;
}
