package com.ems.api.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagesResponse<T> implements java.io.Serializable {

    private String errorCode;

    private String messages;

    private T data;

}
