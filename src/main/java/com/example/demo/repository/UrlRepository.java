package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Url;

public interface UrlRepository extends JpaRepository<Url, Long> {
	 List<Url> findByTeacherId(String teacherId); // ★追加
}
