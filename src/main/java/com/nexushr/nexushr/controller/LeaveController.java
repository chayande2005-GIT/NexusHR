package com.nexushr.nexushr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.nexushr.nexushr.entity.*;
import com.nexushr.nexushr.repository.*;
import com.nexushr.nexushr.dto.LeaveDTO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // ✅ APPLY LEAVE
    @PostMapping("/apply")
    public LeaveRequest applyLeave(@RequestBody LeaveDTO dto) {

        Employee emp = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(emp);
        leave.setFromDate(LocalDate.parse(dto.getFromDate()));
        leave.setToDate(LocalDate.parse(dto.getToDate()));
        leave.setReason(dto.getReason());
        leave.setStatus("PENDING");

        return leaveRepository.save(leave);
    }

    // ✅ APPROVE LEAVE
    @PutMapping("/approve/{id}")
    public LeaveRequest approveLeave(@PathVariable Long id) {

        LeaveRequest leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setStatus("APPROVED");

        return leaveRepository.save(leave);
    }

    // ❌ REJECT LEAVE
    @PutMapping("/reject/{id}")
    public LeaveRequest rejectLeave(@PathVariable Long id) {

        LeaveRequest leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setStatus("REJECTED");

        return leaveRepository.save(leave);
    }

    // 📊 GET ALL LEAVES
    @GetMapping
    public List<LeaveRequest> getAll() {
        return leaveRepository.findAll();
    }

    // 📊 GET EMPLOYEE LEAVES
    @GetMapping("/employee/{empId}")
    public List<LeaveRequest> getByEmployee(@PathVariable Long empId) {
        return leaveRepository.findByEmployeeId(empId);
    }
}