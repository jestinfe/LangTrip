package kr.co.sist.e_learning.common;

public interface EmailSender {
    void sendEmail(String to, String subject, String body);
}
