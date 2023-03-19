package com.ems.api.dto;

import java.util.Date;

public interface ParticipantExport {
     String getTicketName();
     String getInviterName();
     String getParticipantName();
     String getEmail();
     String getPhone();
     Date getAddedDate();
     Date getConfirmedDate();
     String getStatus();
     String getVerifierName();
}
