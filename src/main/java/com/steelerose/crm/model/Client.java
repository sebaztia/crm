package com.steelerose.crm.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "client")
public @Data
class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "ref_number")
    private String refNumber;

    @Column(name = "record_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date recordDate;

    @Column(name = "call_sheet")
    private String callSheet;

    @Column(name = "call_id")
    private Long callId;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "staff_name")
    private String staffName;


}
