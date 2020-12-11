package com.steelerose.crm.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "staff")
public @Data class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "staff_name")
    private String staffName;
}
