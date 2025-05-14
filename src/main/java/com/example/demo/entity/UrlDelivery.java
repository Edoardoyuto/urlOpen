package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "url_deliveries")
@Data
public class UrlDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT対応
    @Column(name = "id")
    private Long id;

    @Column(name = "url_id", nullable = false)
    private Long urlId;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "delivered_at", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp deliveredAt;

    @Column(name = "opened", nullable = false)
    private Boolean opened = false;
}
