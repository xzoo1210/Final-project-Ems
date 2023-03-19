package com.ems.api.util;

public interface Constant {
    enum Role {
        USER
    }

    enum ErrorCode {
        SYSTEM_ERROR,
        SYSTEM_ERROR_INVALID_DATA,
        SYSTEM_ERROR_FAILED_TO_LOAD_EMAIL_TEMPLATE,
        WRONG_USERNAME_OR_PASSWORD,//Wrong username or password
        ACCOUNT_NOT_ACTIVE,//Account has not been activated
        ACCOUNT_LOCKED,//Account is Locked
        COMPOSE_EMAIL_ERROR,
        ERROR_EMAIL,//Wrong email format
        ERROR_FIRST_NAME,//Wrong first name format
        ERROR_MIDDLE_NAME,//Wrong middle name format
        ERROR_LAST_NAME,//Wrong last name format
        ERROR_PHONE,//Wrong phone number format
        ERROR_IMAGE_URL,//Wrong image url
        CONTACT_ID_NOT_EXIST, // Contact does not exist
        OTHER_CONTACT_ID_NOT_EXIST, // Contact does not exist
        OTHER_CONTACT_IS_ALREADY_ADDED,
        NOT_ALLOW_DELETE_TEAM,
        NOT_ALLOW_CHANGE_STATUS_BOOTH_ORDER,
        BOOTH_IS_ALREADY_ACCEPTED,
        CONTACT_ALREADY_EXIST, //Contact does already exist
        EMAIL_IS_NULL,// Email is null
        PHONE_IS_NULL,// Phone is null
        ACCOUNT_EXISTED,//account existed
        ACCOUNT_NOT_EXIST,
        WAITING_VALIDATE,//account is waiting for validate
        CONTACT_HAS_CHILDREN,//Not allow change type company->individual
        EVENT_NOT_EXIST,
        EVENT_CODE_EXISTED,
        EVENT_USING_BOOTHS,
        EVENT_USING_TICKETS,
        EVENT_USING_SPONSORS,
        EVENT_USING_POSTS,
        CANNOT_CREATE_FOLDER,
        TEAM_ID_NOT_EXIST,// Contact does not exist
        TASK_ID_NOT_EXIST,// Contact does not exist
        CONTACT_TYPE_IS_NOT_TEAM,
        CONTACT_TYPE_IS_NOT_INDIVIDUAL,
        THIS_CONTACT_IS_NOT_USER_PROFILE,
        POST_ID_NOT_EXIST,
        POST_IS_NOT_POST_TEAM,
        OWNER_BY_EMAIL_JWT_NOT_EXIST,
        CREATOR_BY_EMAIL_JWT_NOT_EXIST,
        CURRENT_USER_NOT_IN_THIS_EVENT,
        CURRENT_USER_NOT_ACCEPT_TO_JOIN_THIS_EVENT_YET,
        CREATOR_NOT_HAS_AUTHOR_TO_EDIT_MEMBER,
        CURRENT_USER_NOT_HAS_AUTHOR,
        CURRENT_USER_IS_NOT_ASSIGNER_OR_ASSIGNEE,
        ADDRESS_ID_NOT_EXIST,
        MEMBER_IS_INVITED_TO_THIS_EVENT,
        MEMBER_IS_JOINED_THIS_EVENT,
        NOT_ENOUGH_REMAINING_TICKET,
        LIMITED_QUANTITY_CANNOT_LESS_THAN_SOLD_QUANTITY,
        WRONG_PASSWORD,
        THIS_EMAIL_HAS_WAITING_BOOTH_ORDER,
        NOT_ALLOW_CHANGE_STATUS_BOOTH_HAS_ORDERS,
        NOT_ALLOW_CHANGE_LOCATION_BOOTH_HAS_ORDERS,
        NOT_ALLOW_DELETE_BOOTH_HAS_ORDERS,
        LOCATION_IN_USE,
        EVENT_HAS_NOT_COMPLETED_TASK,
    }

    interface TypeConstant {
        enum ContactType {
            INDIVIDUAL,
            TEAM
        }

        enum PostType {TEAM, EVENT}

        enum PostStatus {ACTIVE, NOT_ACTIVE}

        enum TicketStatus {ACTIVE, NOT_ACTIVE}


        enum ActivityType {
            BOOTH, SPONSOR_REQUEST,
            SPONSOR, EVENT, POST, TASK
        }

        enum RoleType {
            USER, ADMIN
        }

        enum AccountStatus {
            ACTIVE, NOT_VALIDATE
        }

        enum EventStatus {
            INPROGRESS, CANCEL, DONE,
        }
        enum TaskStatus {
            INPROGRESS, CANCEL, DONE,OPEN,
        }



        enum EmailStatus {
            FAILED, PENDING, DONE,
        }


        enum EventMemberStatus {
            WAITING, ACCEPT, REJECT
        }

        enum EventMemberAccess {
            VIEW, EDIT, NONE
        }

        enum EventFeature {
            EVENT_DETAIL, ORGANIZATION, AGENDA, TICKET, BOOTH, SPONSOR, POST, PARTICIPANT
        }

        enum EventRole {
            HEADER, SUB_HEADER, MEMBER
        }

        enum TeamRole {
            LEADER, MEMBER
        }

        enum ParticipantStatus {
            WAITING, ACCEPT
        }

        enum BoothStatus {
            NOT_ACTIVE, ACTIVE
        }

        enum BoothOrderStatus {
            WAITING, ACCEPT, REJECT,
        }

        enum PublicSponsor {
            ACTIVE,NOT_ACTIVE
        }

        enum SaleTicket {
            ENABLE,DISABLE
        }

        enum RentBooth {
            ENABLE,DISABLE
        }
    }

    interface ControllerMapping {
        String AUTHENTICATION = "/auth";
        String AGENDA = "/agenda";
        String CONTACT = "/contact";
        String EVENT = "/event";
        String EVENT_CONTACT = "/event-member";
        String TASK = "/task";
        String TASK_COMMENT = "/task-comment";
        String POST = "/post";
        String SPONSOR = "/sponsor";
        String BOOTH = "/booth";
        String FILE = "/file";
        String TEAM = "/team";
        String TICKET = "/ticket";
        String ORDER = "/order";
        String ACTIVITY_LOG = "/activity-log";
        String PARTICIPANT = "/participant";
        String ALL = "/**";
    }

    enum EmailVariable{
        RECEIVER_NAME("receiverName"),
        DOMAIN_WEB("http://103.75.186.211"),
        EVENT_NAME("eventName"),
        EVENT_CODE("eventCode"),
        EVENT_DESCRIPTION("eventDescription"),
        SENDER_NAME("senderName"),
        LOGO_URL("logoUrl"),
        ACCEPT_EVENT_INVITE("acceptEventUrl"),
        REJECT_EVENT_INVITE("rejectEventUrl"),
        ACTIVATE_ACCOUNT("activateAccountUtl"),
        CONTENT("content"),
        QR_CODE("qrCode"),
        TICKET_NAME("ticketName"),
        TICKET_DESCRIPTION("ticketDescription"),
        BOOTH_NAME("boothName"),
        BOOTH_DESCRIPTION("boothDescription"),
        QUANTITY("quantity"),
        AMOUNT("amount"),
        TICKET_ORDERS("ticketOrders"),
        START_DATE("startDate"),
        NEW_START_DATE("newStartDate"),
        END_DATE("endDate"),
        NEW_END_DATE("newEndDate"),
        TASK_DESCRIPTION("taskDescription"),
        NEW_TASK_DESCRIPTION("newTaskDescription"),
        TASK_NAME("taskName"),
        NEW_TASK_NAME("newTaskName"),
        PARENT_TASK_NAME("parentTaskName"),
        NEW_PARENT_TASK_NAME("newparentTaskName"),
        ASSIGNER_NAME("assignerName"),
        ASSIGNEE_NAME("assigneeName"),
        NEW_ASSIGNEE_NAME("newAssigneeName"),
        PROGRESS("progress"),
        NEW_PROGRESS("newProgress"),
        STATUS("status"),
        NEW_STATUS("newStatus"),
        USERNAME("username"),
        ;
        public String value;
        EmailVariable(String value){
            this.value=value;
        };
    }
    enum EmailType {
        ACTIVATE_REGISTER("activate-register-subject.html","activate-register-content.html"),
        FORGOT_PASSWORD("",""),
        TASK_ASSIGNED("assigned-task-subject.html","assigned-task-content.html"),
        TASK_UPDATED("updated-task-subject.html","updated-task-content.html"),
        TASK_LATE("",""),
        INVITE_EVENT("invite-event-subject.html","invite-event-content.html"),
        TICKET_EVENT("ticket-subject.html","ticket-content.html"),
        BUY_TICKET_EVENT("buy-ticket-subject.html","buy-ticket-content.html"),
        INVITE_PARTICIPANT("invite-participant-subject.html","invite-participant-content.html"),
        ACCEPT_BOOTH_ORDER("accept-booth-order-subject.html","accept-booth-order-content.html"),
        REJECT_BOOTH_ORDER("reject-booth-order-subject.html","reject-booth-order-content.html"),
        CANCEL_EVENT("cancel-event-subject.html","cancel-event-content.html"),
        DONE_EVENT("done-event-subject.html","done-event-content.html"),
        ;
        public String content;
        public String subject;
        EmailType(String subject,String content){
            this.subject=subject;
            this.content =content;
        };
    }
    enum EmailTemplate {
        TICKET("template-ticket.html"),
        TICKET_ORDER("template-ticket-order.html"),
        PARTICIPANT("template-participant.html"),
        ;
        public String template;
        EmailTemplate(String template){
            this.template=template;
        };
    }
}
