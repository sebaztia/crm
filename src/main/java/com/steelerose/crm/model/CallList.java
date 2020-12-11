package com.steelerose.crm.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "call_list")
public @Data
class CallList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "record_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date recordDate;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "ref_number")
    private String refNumber;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "query")
    private String query;

    @Column(name = "email_check")
    private Boolean emailCheck;
    @Column(name = "call_check")
    private Boolean callCheck;
    @Column(name = "email_done")
    private Boolean emailDone;
    @Column(name = "call_done")
    private Boolean callDone;

    @Column(name = "archive")
    private Boolean archive;
}
