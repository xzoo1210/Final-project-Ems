package com.ems.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicEventPostResponse extends PublicEventPostShortResponse {
    private ContactShortResponse creator;
    private EventShortResponse event;
    private String content;
}
