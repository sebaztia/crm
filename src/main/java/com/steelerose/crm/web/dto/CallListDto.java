package com.steelerose.crm.web.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public @Data class CallListDto {

    private long id;

    @NotEmpty
    private String contactName;

    @NotEmpty
    private String contactNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime recordDate;

    @NotEmpty
    private String query;

    @NotEmpty
    private String staffName;

    private String reference;

   // private List<Staff> staffList;
    private boolean emailCheck;
    private boolean callCheck;
    private boolean emailDone;
    private boolean callDone;
}
