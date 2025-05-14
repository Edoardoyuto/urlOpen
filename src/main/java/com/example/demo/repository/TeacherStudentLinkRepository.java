package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TeacherStudentLink;

@Repository
public interface TeacherStudentLinkRepository extends JpaRepository<TeacherStudentLink, String> {
    // 先生IDから、その先生に紐づく生徒一覧を取得
    List<TeacherStudentLink> findByTeacherId(String teacherId);

    // 生徒IDと先生IDの組み合わせが存在するかチェック
    boolean existsByStudentIdAndTeacherId(String studentId, String teacherId);
    
    List<TeacherStudentLink> findByStudentId(String studentId);
}
