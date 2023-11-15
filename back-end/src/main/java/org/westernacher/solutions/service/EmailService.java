package org.westernacher.solutions.service;

public interface EmailService
{
    void sendSimpleMessage(String to, String subject, String text);
}
