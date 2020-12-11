package com.steelerose.crm.service.impl;

import com.steelerose.crm.model.Staff;
import com.steelerose.crm.repository.StaffRepository;
import com.steelerose.crm.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository repository;

    @Override
    public List<Staff> getAllStaffs() {
        return repository.findAll();
    }
}
