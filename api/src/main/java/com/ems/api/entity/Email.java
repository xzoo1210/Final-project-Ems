package com.ems.api.entity;

import com.ems.api.util.Constant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "EMAIL")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_id_seq")
    @SequenceGenerator(name = "email_id_seq", sequenceName = "email_id_seq", allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String sendTo;

    private String senderEmail;

    private String senderDisplayName;

    @Column(columnDefinition = "TEXT")
    private String cc;

    @Column(columnDefinition = "TEXT")
    private String bcc;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Date sendDate;

    @Enumerated(EnumType.STRING)
    private Constant.EmailType emailType;

    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.EmailStatus status;

    private Integer retryCount = 0;
}
