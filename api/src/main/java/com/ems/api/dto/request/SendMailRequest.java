package com.ems.api.dto.request;

import com.ems.api.util.Constant;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMailRequest {
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
    private String senderEmail;
    private String senderDisplayName;
    private Constant.EmailType emailType;

    private Map<String, Object> rawData = new HashMap<>();
}
