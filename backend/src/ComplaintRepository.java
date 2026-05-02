package com.complaint.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.complaint.entity.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    
    List<Complaint> findByUserId(Long userId);
    long countByStatus(String status);
    List<Complaint> findByTitleContainingIgnoreCase(String keyword);
    
    @Query("SELECT FUNCTION('MONTH', c.createdAt), COUNT(c) FROM Complaint c GROUP BY FUNCTION('MONTH', c.createdAt)")
    List<Object[]> getMonthlyStats();
}