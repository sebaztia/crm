package com.steelerose.crm.web.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public @Data class EmailDto {

    @Email
    @NotEmpty
    private String to;

    private String subject;

    private String text;

    private CallListDto callListDto;

}
