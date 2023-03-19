package com.ems.api.dto;

import java.util.Date;

public interface TicketExport {
     String getName();
     String getCreatorName();
     Date getCreateDate();
     Integer getLimited();
     Double getPrice();
     Integer getSold();
     Integer getInvited();
     Integer getRemain();
     String getStatus();
}
