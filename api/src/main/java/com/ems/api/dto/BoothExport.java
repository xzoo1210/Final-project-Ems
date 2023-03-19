package com.ems.api.dto;

import java.util.Date;

public interface BoothExport {
     String getLocationName();
     String getLocationDescription();
     String getBoothName();
     String getBoothDescription();
     Double getRentFee();
     Date getStartRentDate();
     Date getEndRentDate();
     String getStatus();
     Integer getWaiting();
     Integer getAccept();
     Integer getReject();
     Integer getTotalOrder();
}
