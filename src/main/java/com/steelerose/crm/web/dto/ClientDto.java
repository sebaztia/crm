package com.steelerose.crm.web.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

public @Data
class ClientDto {

    private long id;

    @NotEmpty
    private String fullName;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String refNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime recordDate;

    private String callSheet;

    private Long callId;

    private List<ClientDto> referenceList;


}
