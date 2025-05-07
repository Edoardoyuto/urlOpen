package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UrlDelivery;

public interface UrlDeliveryRepository extends JpaRepository<UrlDelivery, Long> {
}
