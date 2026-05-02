package com.complaint.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.complaint.entity.Complaint;
import com.complaint.entity.User;
import com.complaint.repository.UserRepository;
import com.complaint.service.ComplaintService;
import com.complaint.service.EmailService;

@RestController
@RequestMapping("/complaints")
@CrossOrigin
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService; // 🔥 ADD THIS

   
    @PostMapping
    public Complaint addComplaint(@RequestBody Complaint complaint) {
        return complaintService.addComplaint(complaint);
    }


    @PostMapping("/upload")
    public Complaint uploadComplaint(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {

        Complaint complaint = new Complaint();
        complaint.setTitle(title);
        complaint.setDescription(description);
        complaint.setCategory(category);

        User user = userRepository.findById(userId).orElse(null);
        complaint.setUser(user);


        if (file != null && !file.isEmpty()) {

            String fileName = System.currentTimeMillis() + "_" +
                    file.getOriginalFilename().replace(" ", "_");

            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            File folder = new File(uploadDir);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String filePath = uploadDir + fileName;
            file.transferTo(new File(filePath));

            complaint.setImagePath(fileName);
        }


        Complaint saved = complaintService.addComplaint(complaint);


        if (saved.getUser() != null) {
            String email = saved.getUser().getEmail();
            emailService.sendComplaintCreated(email, saved.getTitle());
        }

        return saved;
    }

    @GetMapping
    public List<Complaint> getAll() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        return complaintService.getStats();
    }

    @GetMapping("/user/{userId}")
    public List<Complaint> getUserComplaints(@PathVariable Long userId) {
        return complaintService.getUserComplaints(userId);
    }

    @GetMapping("/search")
    public List<Complaint> search(@RequestParam String keyword) {
        return complaintService.searchByTitle(keyword);
    }
    
    @GetMapping("/monthly")
    public Map<String, Long> getMonthlyStats() {
        return complaintService.getMonthlyStats();
    }

    @PutMapping("/{id}")
    public Complaint updateStatus(@PathVariable Long id, @RequestParam String status) {
        return complaintService.updateStatus(id, status);
    }
    
    @PutMapping("/remark/{id}")
    public Complaint updateRemark(
            @PathVariable Long id,
            @RequestParam String remark,
            @RequestParam String handledBy) {

        return complaintService.updateRemark(id, remark, handledBy);
    }
    
}