package com.ems.api.configuration;

import com.ems.api.dto.request.*;
import com.ems.api.dto.response.*;
import com.ems.api.entity.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        // Tạo object và cấu hình
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setAmbiguityIgnored(true);
        modelMapper.addMappings(new EventResponseMapper());
        modelMapper.addMappings(new EventShortResponseMapper());
        modelMapper.addMappings(new ContactShortResponseMapper());
        modelMapper.addMappings(new PublicTicketResponseMapper());
        modelMapper.addMappings(new EventContactResponseMapper());
        modelMapper.addMappings(new TaskMapper());
        modelMapper.addMappings(new TaskShortResponseMapper());
        modelMapper.addMappings(new TaskCommentResponseMapper());
        modelMapper.addMappings(new ActivityLogResponseMapper());
        modelMapper.addMappings(new BoothLocationMapper());
        modelMapper.addMappings(new BoothLocationResponseMapper());
        modelMapper.addMappings(new BoothMapper());
        modelMapper.addMappings(new BoothResponseMapper());
        modelMapper.addMappings(new PublicEventPostResponseMapper());
        modelMapper.addMappings(new TeamPostResponseMapper());
        modelMapper.addMappings(new PostCommentResponseMapper());
        modelMapper.addMappings(new BoothOrderMapper());
        modelMapper.addMappings(new AgendaMapper());
        modelMapper.addMappings(new PublicBoothResponseMapper());
        modelMapper.addMappings(new SponsorMapper());
        modelMapper.addMappings(new TicketOrderDetailMapper());
//        modelMapper.addMappings(new AddressMapper());
        return modelMapper;
    }

    class AddressMapper extends PropertyMap<AddressRequest, Address> {
        @Override
        protected void configure() {
            map().setCommune(source.getCommune());
            map().setProvince(source.getProvince());
            map().setDistrict(source.getDistrict());
            map().setStreet1(source.getStreet1());
            map().setStreet2(source.getStreet2());
        }
    }

    class TicketOrderDetailMapper extends PropertyMap<TicketOrderDetailRequest, TicketOrderDetail> {
        @Override
        protected void configure() {
            map().setName(source.getName());
            map().setEmail(source.getEmail());
            map().setPhone(source.getPhone());
            map().setTicketId(source.getTicketId());
        }
    }

    class EventMemberRequestMapper extends PropertyMap<EventMemberRequest, EventContact> {
        @Override
        protected void configure() {
            skip(destination.getCreator());
            skip(destination.getEvent());
            skip(destination.getMember());
            skip(source.getIsSubHeader());
            map().setEventId(source.getEventId());
            map().setMemberId(source.getMemberId());
            map().setOrganizationAccess(source.getOrganizationAccess());
            map().setAgendaAccess(source.getAgendaAccess());
            map().setTicketAccess(source.getTicketAccess());
            map().setBoothAccess(source.getBoothAccess());
            map().setSponsorAccess(source.getSponsorAccess());
            map().setPostAccess(source.getPostAccess());
            map().setParticipantAccess(source.getParticipantAccess());
        }
    }

    class EventResponseMapper extends PropertyMap<Event, EventResponse> {
        @Override
        protected void configure() {

            map().setId(source.getId());
            map().setName(source.getName());
            map().setCode(source.getCode());
            map().setLogoPath(source.getLogoPath());
            map().setBeginDate(source.getBeginDate());
            map().setEndDate(source.getEndDate());
            map().setDescription(source.getDescription());
            map().setContentInviteMail(source.getContentInviteMail());
            map().setContentTicketMail(source.getContentTicketMail());
            map().setContentBuyTicketMail(source.getContentBuyTicketMail());
            map().setAutoConfirm(source.getAutoConfirm());
            map().setStatus(source.getStatus());
        }
    }

    class EventShortResponseMapper extends PropertyMap<Event, EventShortResponse> {
        @Override
        protected void configure() {

            map().setId(source.getId());
            map().setName(source.getName());
            map().setCode(source.getCode());
            map().setLogoPath(source.getLogoPath());
            map().setStatus(source.getStatus());
        }
    }

    class TeamShortResponseMapper extends PropertyMap<Contact, TeamShortResponse> {
        @Override
        protected void configure() {

            map().setId(source.getId());
            map().setName(source.getFirstName());
            map().setImagePath(source.getImagePath());
            map().setDescription(source.getDescription());
        }
    }

    class TaskShortResponseMapper<T extends TaskShortResponse> extends PropertyMap<Task, T> {
        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setName(source.getName());
            map().setCode(source.getCode());
            map().setStartDate(source.getStartDate());
            map().setEndDate(source.getEndDate());
            map().setProgress(source.getProgress());
            map().setCreateDate(source.getCreateDate());
            map().setStatus(source.getStatus());
        }
    }

    class TaskCommentResponseMapper extends PropertyMap<TaskComment, TaskCommentResponse> {
        @Override
        protected void configure() {
            map().setContent(source.getContent());
            map().setCreateDate(source.getCreateDate());
        }
    }

    class TaskMapper extends PropertyMap<TaskRequest, Task> {
        @Override
        protected void configure() {
            map().setName(source.getName());
            map().setEventId(source.getEventId());
            map().setStartDate(source.getStartDate());
            map().setEndDate(source.getEndDate());
            map().setParentTaskId(source.getParenTaskId());
            map().setAssigneeId(source.getAssigneeId());
            map().setDescription(source.getDescription());
        }
    }

    class ContactShortResponseMapper extends PropertyMap<Contact, ContactShortResponse> {
        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setEmailAccount(source.getEmailAccount());
            map().setFirstName(source.getFirstName());
            map().setImagePath(source.getImagePath());
        }
    }

    class PublicTicketResponseMapper extends PropertyMap<Ticket, PublicTicketResponse> {
        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setSaleBeginDate(source.getSaleBeginDate());
            map().setSaleEndDate(source.getSaleEndDate());
            map().setRemainingQuantity(source.getRemainingQuantity());
            map().setName(source.getName());
            map().setDescription(source.getDescription());
            map().setPrice(source.getPrice());
        }
    }

    class EventContactResponseMapper extends PropertyMap<EventContact, EventContactResponse> {
        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setStatus(source.getStatus());
            map().setEventRole(source.getEventRole());
            map().setOrganizationAccess(source.getOrganizationAccess());
            map().setAgendaAccess(source.getAgendaAccess());
            map().setSponsorAccess(source.getSponsorAccess());
            map().setPostAccess(source.getPostAccess());
            map().setParticipantAccess(source.getParticipantAccess());
            map().setCreateDate(source.getCreateDate());
        }
    }

    class ActivityLogResponseMapper extends PropertyMap<ActivityLog, ActivityLogResponse> {
        @Override
        protected void configure() {
            map().setCreateDate(source.getCreateDate());
            map().setNote(source.getNote());
        }
    }

    class BoothLocationMapper<T extends BoothLocationRequest> extends PropertyMap<T, BoothLocation> {
        @Override
        protected void configure() {
            map().setEventId(source.getEventId());
            map().setDescription(source.getDescription());
            map().setName(source.getName());
        }
    }

    class BoothLocationResponseMapper<T extends PublicBoothLocationResponse> extends PropertyMap<BoothLocation, T> {
        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setDescription(source.getDescription());
            map().setName(source.getName());
        }
    }

    class BoothMapper<T extends BoothRequest> extends PropertyMap<T, Booth> {
        @Override
        protected void configure() {
            map().setEventId(source.getEventId());
            map().setDescription(source.getDescription());
            map().setName(source.getName());
            map().setRentFee(source.getRentFee());
            map().setLocationId(source.getLocationId());
            map().setStartRentDate(source.getStartRentDate());
            map().setEndRentDate(source.getEndRentDate());
        }
    }

    class BoothResponseMapper extends PropertyMap<Booth, BoothResponse> {
        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setDescription(source.getDescription());
            map().setName(source.getName());
            map().setRentFee(source.getRentFee());
            map().setCreateDate(source.getCreateDate());
            map().setStatus(source.getStatus());
            map().setStartRentDate(source.getStartRentDate());
            map().setEndRentDate(source.getEndRentDate());
        }
    }

    class PublicBoothResponseMapper extends PropertyMap<Booth, PublicBoothResponse> {
        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setDescription(source.getDescription());
            map().setName(source.getName());
            map().setRentFee(source.getRentFee());
            map().setStartRentDate(source.getStartRentDate());
            map().setEndRentDate(source.getEndRentDate());
        }
    }

    class PublicEventPostResponseMapper<T extends PublicEventPostShortResponse> extends PropertyMap<Post, T> {
        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setSubject(source.getSubject());
            map().setCreateDate(source.getCreateDate());
            map().setOverviewDescription(source.getOverviewDescription());
            map().setOverviewImagePath(source.getOverviewImagePath());
            map().setViews(source.getViews());
        }
    }

    class TeamPostResponseMapper extends PropertyMap<Post, TeamPostResponse> {
        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setCreateDate(source.getCreateDate());
            map().setContent(source.getContent());
        }
    }

    class PostCommentResponseMapper extends PropertyMap<PostComment, PostCommentResponse> {
        @Override
        protected void configure() {
            map().setContent(source.getContent());
            map().setCreateDate(source.getCreateDate());
            map().setId(source.getId());
        }
    }

    class BoothOrderMapper extends PropertyMap<BoothOrderRequest, BoothOrder> {
        @Override
        protected void configure() {
            map().setBoothId(source.getBoothId());
            map().setEmail(source.getEmail());
            map().setName(source.getName());
            map().setPhone(source.getPhone());

        }
    }

    class AgendaMapper extends PropertyMap<AgendaRequest, Agenda> {
        @Override
        protected void configure() {
            map().setName(source.getName());
            map().setDescription(source.getDescription());
            map().setStartDate(source.getStartDate());
            map().setEndDate(source.getEndDate());
            map().setEventId(source.getEventId());
        }
    }

    class SponsorMapper extends PropertyMap<SponsorRequest, Sponsor> {
        @Override
        protected void configure() {
            map().setName(source.getName());
            map().setPhone(source.getPhone());
            map().setEmail(source.getEmail());
            map().setSponsorItem(source.getSponsorItem());
            map().setMoreInformation(source.getMoreInformation());
            map().setLink(source.getLink());
            map().setLogoPath(source.getLogoPath());
            map().setEventId(source.getEventId());
        }
    }
}
