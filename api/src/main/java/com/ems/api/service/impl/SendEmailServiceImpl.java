package com.ems.api.service.impl;

import com.ems.api.util.AppException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = AppException.class)
public class SendEmailServiceImpl {
//
//    @Autowired
//    private EmailServiceImpl emailCompose;
//    @Autowired
//    private EmailRepo emailRepo;
//    private final JavaMailSender javaMailSender;
//    @Autowired
//    private EmailConfig emailConfig;
//
//    public SendEmailServiceImpl() {
//        javaMailSender = new JavaMailSenderImpl();
//    }
//
//    public ResponseEntity<Object> queueEmailToSend(SendMailRequest sendMailRequest) {
//        int repicientCount = sendMailRequest.getTo().size();
//        if (sendMailRequest.getCc() != null)
//            repicientCount += sendMailRequest.getCc().size();
//        if (sendMailRequest.getBcc() != null)
//            repicientCount += sendMailRequest.getBcc().size();
//        try {
//            Email email = emailCompose.composeEmail(sendMailRequest);
//            emailRepo.save(email);
//        } catch (IOException | TemplateException e) {
//            return new ResponseEntity<>("ERROR on queuing email to send: ", HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>("API Success", HttpStatus.OK);
//    }
//
//
//    public void send(MimeMessage mailMessage) {
//        javaMailSender.send(mailMessage);
//    }
//
//    public MimeMessage createMimeMessage() {
//        return javaMailSender.createMimeMessage();
//    }
//
//    public MimeMessage createDataMail(Email email) throws MessagingException, UnsupportedEncodingException {
//
//        MimeMessage msg = createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
//        helper.setFrom(new InternetAddress(emailConfig.getSenderEmail(), emailConfig.getSenderDisplayName()));
//        helper.setReplyTo(new InternetAddress(email.getSenderEmail(), email.getSenderDisplayName()));
//        helper.setText(email.getContent(), true);
//        helper.setTo(email.getSendTo().split(","));
//        helper.setSubject(email.getSubject());
//        if (email.getCc() != null) {
//            helper.setCc(email.getCc().split(","));
//        }
//        if (email.getBcc() != null) {
//            helper.setBcc(email.getBcc().split(","));
//        }
//
//        // creates message part
//        MimeBodyPart messageBodyPart = new MimeBodyPart();
//        messageBodyPart.setContent(email.getContent(), "text/html; charset=UTF-8");
//
//        // creates multi-part
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
//
//        MimeBodyPart attPart;
//
//        // adds inline image attachments
////        if (email.getAttachments() != null && !email.getAttachments().isEmpty()) {
////            for (EmailAttachment attachment : email.getAttachments()) {
////
////                try {
////                    switch (attachment.getAttachmentType()) {
////                        case IMAGE_LINK:
////                            attPart = new MimeBodyPart();
////                            attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
////                            attPart.attachFile(attachment.getContent());
////                            attPart.setDisposition(MimeBodyPart.INLINE);
////                            multipart.addBodyPart(attPart);
////                            break;
////                        case IMAGE_BASE64:
////                            attPart = new PreencodedMimeBodyPart("base64");
////                            attPart.setFileName(attachment.getName());
////                            attPart.setText(attachment.getContent().replace("data:image/png;base64,", ""));
////                            attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
////                            attPart.setDisposition(MimeBodyPart.INLINE);
////                            multipart.addBodyPart(attPart);
////                            break;
////                        case CALENDAR:
////                            attPart = new MimeBodyPart();
////                            // attPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
////                            attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
////                            attPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment.getContent(),
////                                    "text/calendar;method=REQUEST;name=\"invite.ics\"")));
////                            attPart.setDisposition(MimeBodyPart.ATTACHMENT);
////                            multipart.addBodyPart(attPart);
////                            break;
////                        case FILE:
////                            attPart = new MimeBodyPart();
////                            attPart.setHeader("Content-ID", "<" + attachment.getName() + ">");
////                            attPart.attachFile(attachment.getContent());
////                            attPart.setDisposition(MimeBodyPart.ATTACHMENT);
////                            multipart.addBodyPart(attPart);
////                            break;
////                    }
////                } catch (Exception ex) {
////                    logger.logError("Add Attachment failed for Attachment Type '"
////                            + attachment.getAttachmentType().name() + "' :" + ex.getMessage());
////                }
////            }
////        }
//
//        msg.setContent(multipart);
//
//        return msg;
//    }
}
