package com.complaint.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.complaint.entity.Complaint;
import com.complaint.entity.User;
import com.complaint.repository.ComplaintRepository;
import com.complaint.repository.UserRepository;

@Service
public class ComplaintService {
	@Autowired
	private EmailService emailService;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;


    public Complaint addComplaint(Complaint complaint) {

      
        Long userId = complaint.getUser().getId();

 
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

    
        complaint.setUser(user);

        
        complaint.setStatus("Pending");

        return complaintRepository.save(complaint);
    }

 
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

 
    public List<Complaint> getUserComplaints(Long userId) {
        return complaintRepository.findByUserId(userId);
    }

 
    public Complaint updateStatus(Long id, String status) {

        Complaint c = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        c.setStatus(status);

        // 🔥 GET USER EMAIL
        String email = c.getUser().getEmail();

        // 🔥 SEND EMAIL
        emailService.sendStatusUpdate(email, status, c.getTitle());

        return complaintRepository.save(c);
    }
    
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("total", complaintRepository.count());
        stats.put("pending", complaintRepository.countByStatus("Pending"));
        stats.put("resolved", complaintRepository.countByStatus("Resolved"));

        return stats;
    }
    
    public List<Complaint> searchByTitle(String keyword) {
        return complaintRepository.findByTitleContainingIgnoreCase(keyword);
    }
    
    public Complaint updateRemark(Long id, String remark, String handledBy) {

        Complaint c = complaintRepository.findById(id).orElse(null);

        if (c != null) {
            c.setRemark(remark);
            c.setHandledBy(handledBy);
        }

        return complaintRepository.save(c);
    }
    
    public Map<String, Long> getMonthlyStats() {

        List<Object[]> data = complaintRepository.getMonthlyStats();

        Map<String, Long> map = new HashMap<>();

        for (Object[] row : data) {
            int month = (int) row[0];
            long count = (long) row[1];

            map.put(String.valueOf(month), count);
        }

        return map;
    }
    
    
}