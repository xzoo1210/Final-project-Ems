package com.ems.api.service.impl;

import com.ems.api.configuration.DomainConfig;
import com.ems.api.dto.request.SendMailRequest;
import com.ems.api.entity.Email;
import com.ems.api.repository.EmailRepo;
import com.ems.api.service.EmailService;
import com.ems.api.util.Constant;
import com.ems.api.util.Constant.TypeConstant.EmailStatus;
import com.ems.api.util.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class EmailServiceImpl implements EmailService {
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Configuration freemarkerConfig;

    @Autowired
    private DomainConfig domainConfig;

    @Autowired
    private EmailRepo emailRepo;

    @Autowired
    private JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    String emailFrom;
    @Value("${mail.displayName}")
    String displayName;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void send(MimeMessage mailMessage) {
        emailSender.send(mailMessage);
    }

    //    @PostConstruct
//    private void testTicket() throws AppException, MessagingException, UnsupportedEncodingException {
//        SendMailRequest request = new SendMailRequest();
//        request.setTo(Collections.singletonList("dungbdhe140765@fpt.edu.vn"));
//        request.setEmailType(Constant.EmailType.INVITE_EVENT);
//        request.getRawData().put("content","<h3><strong>Đám cưới bạn Lực tổ chức tại Hưng Yên [25/03/2022]</strong></h3><p><br></p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;Despire an event organization or even an individual person that wants to organize an event, a detailed procedure is important to make a successful event, specially for an event organization, which usually organize large-scale events that leads to the need of managing many perspectives of an event like human resources or event’s plans. For individual people who scarcely or never organize an event, they merely do not know what to do in an event, not have any idea of participants, invited guests management or even an event’s plan to determine which step could include risks and how to deal with those risks.</p><p><br></p><p style=\"text-align: center;\"><img src=\"http://res.cloudinary.com/dcgjui0yd/image/upload/v1668877219/o62tran7j6evonuukyzv.jpg\" width=\"300px\" style=\"display: block; margin-left: auto; margin-right: auto;\"> &nbsp; &nbsp; &nbsp;<em>Hình ảnh đám cưới&nbsp;</em><br></p><p><br></p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;Despire an event organization or even an individual person that wants to organize an event, a detailed procedure is important to make a successful event, specially for an event organization, which usually organize large-scale events that leads to the need of managing many perspectives of an event like human resources or event’s plans. For individual people who scarcely or never organize an event, they merely do not know what to do in an event, not have any idea of participants, invited guests management or even an event’s plan to determine which step could include risks and how to deal with those risks.&nbsp;Despire an event organization or even an individual person that wants to organize an event, a detailed procedure is important to make a successful event, specially for an event organization, which usually organize large-scale events that leads to the need of managing many perspectives of an event like human resources or event’s plans. For individual people who scarcely or never organize an event, they merely do not know what to do in an event, not have any idea of participants, invited guests management or even an event’s plan to determine which step could include risks and how to deal with those risks.</p>");
//        send(createDataMail(composeEmail(request)));
//    }
    @SneakyThrows
    public void composeEmail(List<SendMailRequest> sendMailRequests) {
        List<Email> emails = new LinkedList<>();
        for (SendMailRequest request : sendMailRequests) {
            Email email = new Email();
            email.setSendTo(String.join(",", request.getTo()));
            email.setCc(request.getCc() == null || request.getCc().isEmpty() ? null
                    : String.join(",", request.getCc()));
            email.setBcc(request.getBcc() == null || request.getBcc().isEmpty() ? null
                    : String.join(",", request.getBcc()));

            email.setSenderEmail(StringUtils.nvl(request.getSenderEmail(), emailFrom));
            email.setSenderDisplayName(StringUtils.nvl(request.getSenderDisplayName(), displayName));

            email.setEmailType(request.getEmailType());
            email.setStatus(Constant.TypeConstant.EmailStatus.PENDING);
            email.setSubject(templateToString(toSubjectUrl(request.getEmailType()), request.getRawData()));
            email.setContent(templateToString(toContentUrl(request.getEmailType()), request.getRawData()));
            emails.add(email);
        }


        emailRepo.saveAll(emails);
    }

    private Template toSubjectUrl(Constant.EmailType emailType) throws IOException {
        String templateSrc = emailType.subject;
        return freemarkerConfig.getTemplate(templateSrc);
    }

    @Override
    public Template toTemplateUrl(Constant.EmailTemplate emailType) throws IOException {
        String templateSrc = emailType.template;
        return freemarkerConfig.getTemplate(templateSrc);
    }

    private Template toContentUrl(Constant.EmailType emailType) throws IOException {
        String templateSrc = emailType.content;
        return freemarkerConfig.getTemplate(templateSrc);
    }

    //
    private String templateToString(Template template, Map<String, Object> rawData) throws IOException, TemplateException {
        rawData.put(Constant.EmailVariable.DOMAIN_WEB.name(), Constant.EmailVariable.DOMAIN_WEB.value);
//        rawData.put(Constant.TypeConstant.EmailRawKey.MAIL_TO.name(), domainConfig.getContactEmail());
//        rawData.put(Constant.TypeConstant.EmailRawKey.LOGO_URL.name(), domainConfig.getLogoUrl());

        return FreeMarkerTemplateUtils.processTemplateIntoString(template, rawData);
    }

    @Override
    public String templateToString(Template template) throws IOException, TemplateException {
        Map<String, Object> rawData = new HashMap<>();
        return templateToString(template, rawData);
    }

    //schedule
    public MimeMessage createDataMail(Email email) throws MessagingException, UnsupportedEncodingException {

        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
        helper.setFrom(new InternetAddress(emailFrom, displayName));
        helper.setReplyTo(new InternetAddress(email.getSenderEmail(), email.getSenderDisplayName()));
        helper.setText(email.getContent(), true);
        helper.setTo(email.getSendTo().split(","));
        helper.setSubject(email.getSubject());
        if (email.getCc() != null) {
            helper.setCc(email.getCc().split(","));
        }
        if (email.getBcc() != null) {
            helper.setBcc(email.getBcc().split(","));
        }

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(email.getContent(), "text/html; charset=UTF-8");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attPart;

        // adds inline image attachments
//        if (email.getAttachments() != null && !email.getAttachments().isEmpty()) {
//            for (EmailAttachment attachment : email.getAttachments()) {
//
//                try {
//                    switch (attachment.getAttachmentType()) {
//                        case IMAGE_LINK:
//                            attPart = new MimeBodyPart();
//                            attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
//                            attPart.attachFile(attachment.getContent());
//                            attPart.setDisposition(MimeBodyPart.INLINE);
//                            multipart.addBodyPart(attPart);
//                            break;
//                        case IMAGE_BASE64:
//                            attPart = new PreencodedMimeBodyPart("base64");
//                            attPart.setFileName(attachment.getName());
//                            attPart.setText(attachment.getContent().replace("data:image/png;base64,", ""));
//                            attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
//                            attPart.setDisposition(MimeBodyPart.INLINE);
//                            multipart.addBodyPart(attPart);
//                            break;
//                        case CALENDAR:
//                            attPart = new MimeBodyPart();
//                            // attPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
//                            attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
//                            attPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment.getContent(),
//                                    "text/calendar;method=REQUEST;name=\"invite.ics\"")));
//                            attPart.setDisposition(MimeBodyPart.ATTACHMENT);
//                            multipart.addBodyPart(attPart);
//                            break;
//                        case FILE:
//                            attPart = new MimeBodyPart();
//                            attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
//                            attPart.attachFile(attachment.getContent());
//                            attPart.setDisposition(MimeBodyPart.ATTACHMENT);
//                            multipart.addBodyPart(attPart);
//                            break;
//                    }
//                } catch (Exception ex) {
//                    logger.logError("Add Attachment failed for Attachment Type '"
//                            + attachment.getAttachmentType().name() + "' :" + ex.getMessage());
//                }
//            }
//        }

        msg.setContent(multipart);

        return msg;
    }

    private Date lastSendDate = null;
    @Value("${mail.limited}")
    private int limitMailSendPerDay;
    @Value("${mail.maxRetry}")
    private int maxRetrySendMail;
    private int countMailSentInDay = 0;
    private Pageable limitEachQuery = PageRequest.of(0, 100);

    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    @Async
    public void sendEmailInvitation() {
        if (lastSendDate == null) {
            lastSendDate = new Date();
        }
        if (lastSendDate.getDate() != new Date().getDate()) {
            countMailSentInDay = 0;
        }
        if (countMailSentInDay > limitMailSendPerDay) {
            LOGGER.info("Reach limited mail send per day");
            return;
        }
        lastSendDate = new Date();
        String method = "Mail cronjob invitation to organise";
        LOGGER.info("start " + method, lastSendDate.getTime());
        List<Email> emails = emailRepo.findAllByStatusAndEmailTypeOrderByIdDesc(Arrays.asList(EmailStatus.PENDING),
                Constant.EmailType.INVITE_EVENT, limitEachQuery).getContent();
        sendListMail(emails);
        LOGGER.info("end " + method, lastSendDate.getTime());
    }

    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    @Async
    public void sendEmailActiveAccount() {
        if (lastSendDate == null) {
            lastSendDate = new Date();
        }
        if (lastSendDate.getDate() != new Date().getDate()) {
            countMailSentInDay = 0;
        }
        if (countMailSentInDay > limitMailSendPerDay) {
            LOGGER.info("Reach limited mail send per day");
            return;
        }
        lastSendDate = new Date();
        String method = "Mail cronjob invitation to organise";
        LOGGER.info("start " + method, lastSendDate.getTime());
        List<Email> emails = emailRepo.findAllByStatusAndEmailTypeOrderByIdDesc(Arrays.asList(EmailStatus.PENDING),
                Constant.EmailType.ACTIVATE_REGISTER, limitEachQuery).getContent();
        sendListMail(emails);
        LOGGER.info("end " + method, lastSendDate.getTime());
    }

    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    @Async
    public void sendEmailTicket() {
        if (lastSendDate == null) {
            lastSendDate = new Date();
        }
        if (lastSendDate.getDate() != new Date().getDate()) {
            countMailSentInDay = 0;
        }
        if (countMailSentInDay > limitMailSendPerDay) {
            LOGGER.info("Reach limited mail send per day");
            return;
        }
        lastSendDate = new Date();
        String method = "Mail cronjob ticket invitation";
        LOGGER.info("start " + method, lastSendDate.getTime());
        List<Email> emails = emailRepo.findAllByStatusAndEmailTypeOrderByIdDesc(Arrays.asList(EmailStatus.PENDING),
                Constant.EmailType.TICKET_EVENT, limitEachQuery).getContent();
        sendListMail(emails);
        LOGGER.info("end " + method, lastSendDate.getTime());
    }

    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    @Async
    public void sendEmailBoothOrder() {
        if (lastSendDate == null) {
            lastSendDate = new Date();
        }
        if (lastSendDate.getDate() != new Date().getDate()) {
            countMailSentInDay = 0;
        }
        if (countMailSentInDay > limitMailSendPerDay) {
            LOGGER.info("Reach limited mail send per day");
            return;
        }
        lastSendDate = new Date();
        String method = "Mail cronjob accept/reject booth order";
        LOGGER.info("start " + method, lastSendDate.getTime());
        List<Email> emails1 = emailRepo.findAllByStatusAndEmailTypeOrderByIdDesc(Arrays.asList(EmailStatus.PENDING),
                Constant.EmailType.REJECT_BOOTH_ORDER, limitEachQuery).getContent();
        List<Email> emails2 = emailRepo.findAllByStatusAndEmailTypeOrderByIdDesc(Arrays.asList(EmailStatus.PENDING),
                Constant.EmailType.ACCEPT_BOOTH_ORDER, limitEachQuery).getContent();
        sendListMail(emails2);
        sendListMail(emails1);
        LOGGER.info("end " + method, lastSendDate.getTime());
    }

    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    @Async
    public void sendEmailTicketOrder() {
        if (lastSendDate == null) {
            lastSendDate = new Date();
        }
        if (lastSendDate.getDate() != new Date().getDate()) {
            countMailSentInDay = 0;
        }
        if (countMailSentInDay > limitMailSendPerDay) {
            LOGGER.info("Reach limited mail send per day");
            return;
        }
        lastSendDate = new Date();
        String method = "Mail cronjob ticket order";
        LOGGER.info("start " + method, lastSendDate.getTime());
        List<Email> emails = emailRepo.findAllByStatusAndEmailTypeOrderByIdDesc(Arrays.asList(EmailStatus.PENDING),
                Constant.EmailType.BUY_TICKET_EVENT, limitEachQuery).getContent();
        sendListMail(emails);
        LOGGER.info("end " + method, lastSendDate.getTime());
    }
    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    @Async
    public void sendEmailCancelEvent() {
        if (lastSendDate == null) {
            lastSendDate = new Date();
        }
        if (lastSendDate.getDate() != new Date().getDate()) {
            countMailSentInDay = 0;
        }
        if (countMailSentInDay > limitMailSendPerDay) {
            LOGGER.info("Reach limited mail send per day");
            return;
        }
        lastSendDate = new Date();
        String method = "Mail cronjob cancel event";
        LOGGER.info("start " + method, lastSendDate.getTime());
        List<Email> emails = emailRepo.findAllByStatusAndEmailTypeOrderByIdDesc(Arrays.asList(EmailStatus.PENDING),
                Constant.EmailType.CANCEL_EVENT, limitEachQuery).getContent();
        sendListMail(emails);
        LOGGER.info("end " + method, lastSendDate.getTime());
    }
    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    @Async
    public void sendEmailDoneEvent() {
        if (lastSendDate == null) {
            lastSendDate = new Date();
        }
        if (lastSendDate.getDate() != new Date().getDate()) {
            countMailSentInDay = 0;
        }
        if (countMailSentInDay > limitMailSendPerDay) {
            LOGGER.info("Reach limited mail send per day");
            return;
        }
        lastSendDate = new Date();
        String method = "Mail cronjob done event";
        LOGGER.info("start " + method, lastSendDate.getTime());
        List<Email> emails = emailRepo.findAllByStatusAndEmailTypeOrderByIdDesc(Arrays.asList(EmailStatus.PENDING),
                Constant.EmailType.DONE_EVENT, limitEachQuery).getContent();
        sendListMail(emails);
        LOGGER.info("end " + method, lastSendDate.getTime());
    }
    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    @Async
    public void sendEmailTaskAssigned() {
        if (lastSendDate == null) {
            lastSendDate = new Date();
        }
        if (lastSendDate.getDate() != new Date().getDate()) {
            countMailSentInDay = 0;
        }
        if (countMailSentInDay > limitMailSendPerDay) {
            LOGGER.info("Reach limited mail send per day");
            return;
        }
        lastSendDate = new Date();
        String method = "Mail cronjob task assigned";
        LOGGER.info("start " + method, lastSendDate.getTime());
        List<Email> emails = emailRepo.findAllByStatusAndEmailTypeOrderByIdDesc(Arrays.asList(EmailStatus.PENDING),
                Constant.EmailType.TASK_ASSIGNED, limitEachQuery).getContent();
        sendListMail(emails);
        LOGGER.info("end " + method, lastSendDate.getTime());
    }
    @Transactional
    @Scheduled(fixedRate = 60 * 1000)
    @Async
    public void sendEmailTaskUpdated() {
        if (lastSendDate == null) {
            lastSendDate = new Date();
        }
        if (lastSendDate.getDate() != new Date().getDate()) {
            countMailSentInDay = 0;
        }
        if (countMailSentInDay > limitMailSendPerDay) {
            LOGGER.info("Reach limited mail send per day");
            return;
        }
        lastSendDate = new Date();
        String method = "Mail cronjob task updated";
        LOGGER.info("start " + method, lastSendDate.getTime());
        List<Email> emails = emailRepo.findAllByStatusAndEmailTypeOrderByIdDesc(Arrays.asList(EmailStatus.PENDING),
                Constant.EmailType.TASK_UPDATED, limitEachQuery).getContent();
        sendListMail(emails);
        LOGGER.info("end " + method, lastSendDate.getTime());
    }

    void sendListMail(List<Email> emails) {
        MimeMessage mimeMessage;
        for (Email email : emails) {
            try {
                mimeMessage = this.createDataMail(email);
                this.send(mimeMessage);
                email.setSendDate(new Date());
                email.setStatus(EmailStatus.DONE);
                countMailSentInDay++;
                LOGGER.info("Sent mail id: " + email.getId());
            } catch (Exception e) {
                LOGGER.error("Error mail id: " + email.getId(), e);
                email.setRetryCount(email.getRetryCount() + 1);
                if (email.getRetryCount() < maxRetrySendMail) {
                    email.setStatus(EmailStatus.PENDING);
                    LOGGER.error("increased failed Retry_Count=" + email.getRetryCount()
                            + " for email id=" + email.getId());
                } else {
                    email.setStatus(EmailStatus.FAILED);
                    LOGGER.error("exceed max failed " + "(Retry_Count=" + email.getRetryCount()
                            + ") for email id=" + email.getId());
                }
            } finally {
                emailRepo.save(email);
            }
        }
    }
}
