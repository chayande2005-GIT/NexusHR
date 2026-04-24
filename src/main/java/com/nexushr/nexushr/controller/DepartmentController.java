package com.nexushr.nexushr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nexushr.nexushr.entity.Department;
import com.nexushr.nexushr.repository.DepartmentRepository;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentRepository repo;

    @PostMapping
    public Department add(@RequestBody Department dept) {
        return repo.save(dept);
    }

    @GetMapping
    public List<Department> getAll() {
        return repo.findAll();
    }
}