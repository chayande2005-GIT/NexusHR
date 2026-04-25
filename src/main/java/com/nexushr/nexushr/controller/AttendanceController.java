package com.nexushr.nexushr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.nexushr.nexushr.entity.Attendance;
import com.nexushr.nexushr.entity.Employee;
import com.nexushr.nexushr.repository.AttendanceRepository;
import com.nexushr.nexushr.repository.EmployeeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // ✅ CHECK-IN
    @PostMapping("/check-in/{empId}")
    public Attendance checkIn(@PathVariable Long empId) {

        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate today = LocalDate.now();

        Optional<Attendance> existing =
                attendanceRepository.findByEmployeeIdAndDate(empId, today);

        // ❌ already checked in today
        if (existing.isPresent()) {
            throw new RuntimeException("Already checked in today.");
        }

        Attendance attendance = new Attendance();
        attendance.setEmployee(emp);
        attendance.setDate(today);
        attendance.setCheckIn(LocalDateTime.now());
        attendance.setStatus("PRESENT");

        return attendanceRepository.save(attendance);
    }

    // ✅ CHECK-OUT
    @PostMapping("/check-out/{empId}")
    public Attendance checkOut(@PathVariable Long empId) {

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndDate(empId, today)
                .orElseThrow(() -> new RuntimeException("No check-in today"));

        if (attendance.getCheckOut() != null) {
            throw new RuntimeException("Already checked out today");
        }

        attendance.setCheckOut(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }
    
    // ✅ GET ALL
    @GetMapping
    public java.util.List<Attendance> getAll() {
        return attendanceRepository.findAll();
    }
}