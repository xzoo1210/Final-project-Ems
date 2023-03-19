package com.ems.api.dto;

import java.util.Date;

public interface EventPostExport {
     String getSubject();
     String getCreatorName();
     Date getCreateDate();
     String getStatus();
     Integer getViews();
}
