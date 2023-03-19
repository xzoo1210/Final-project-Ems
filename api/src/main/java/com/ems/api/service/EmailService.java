package com.ems.api.service;

import com.ems.api.entity.Email;
import com.ems.api.dto.request.SendMailRequest;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import freemarker.template.Template;

import javax.mail.internet.MimeMessage;
import java.util.List;

public interface EmailService {
     void sendSimpleMessage( String to, String subject, String text);
     void composeEmail(List<SendMailRequest> sendMailRequests) throws AppException;
     MimeMessage createDataMail(Email email) throws Exception;
     void send(MimeMessage mailMessage);
     String templateToString(Template template) throws Exception;
     Template toTemplateUrl(Constant.EmailTemplate emailType) throws Exception;
}
