package com.userservice.web.provider;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailProvider {

    private final JavaMailSender javaMailSender;
    private final String SUBJECT = "[예약 구매 서비스] 인증 메일입니다.";

    public boolean sendCertificationMail(String email, String certificationNumber) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            String htmlContent = getCertificationMessage(certificationNumber);

            messageHelper.setTo(email);
            messageHelper.setSubject(SUBJECT);
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private String getCertificationMessage(String certificationNumber) {

        StringBuilder certificationMessage = new StringBuilder();
        certificationMessage.append("<h1 style='text-align: center;'>[예약 구매 서비스] 인증 메일</h1>");
        certificationMessage.append("<h3 style='text-align: center;'>인증 코드 : <strong style='font-size: 32px; letter-spacing: 8px;'>")
                .append(certificationNumber).append("</strong><h3>");
        return certificationMessage.toString();
    }
}
