package com.steelerose.crm.service;

import com.steelerose.crm.model.Client;
import com.steelerose.crm.model.Staff;
import com.steelerose.crm.service.impl.StaffServiceImpl;

import java.util.List;

public interface StaffService {

    List<Staff> getAllStaffs();
}
