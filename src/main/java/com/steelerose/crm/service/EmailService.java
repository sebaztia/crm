package com.steelerose.crm.service;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);
}
