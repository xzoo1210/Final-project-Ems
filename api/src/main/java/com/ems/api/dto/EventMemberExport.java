package com.ems.api.dto;

import java.util.Date;

public interface EventMemberExport {
     String getName();
     String getRole();
     Integer getNoTeam();
     Date getCreateDate();
     String getCreatorName();
     Integer getTaskOpen();
     Integer getTaskInprogress();
     Integer getTaskDone();
     Integer getTaskCancel();
     Integer getTaskLate();
     Integer getTask();
}
