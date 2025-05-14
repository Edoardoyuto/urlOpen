package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.InviteCode;

public interface InviteCodeRepository extends JpaRepository<InviteCode, String> {
    List<InviteCode> findByTeacherId(String teacherId); // ★追加
}