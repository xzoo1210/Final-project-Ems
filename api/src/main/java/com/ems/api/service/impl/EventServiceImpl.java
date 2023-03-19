package com.ems.api.service.impl;

import com.ems.api.dto.*;
import com.ems.api.dto.request.*;
import com.ems.api.dto.response.EventResponse;
import com.ems.api.dto.response.SearchEventResponse;
import com.ems.api.entity.*;
import com.ems.api.repository.*;
import com.ems.api.service.ActivityLogDetailService;
import com.ems.api.service.ActivityLogService;
import com.ems.api.service.EmailService;
import com.ems.api.service.EventService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.DateUtil;
import com.ems.api.util.PageUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = AppException.class)
public class EventServiceImpl implements EventService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private EventContactRepo eventContactRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private ParticipantRepo participantRepo;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private BoothRepo boothRepo;
    @Autowired
    private TicketOrderRepo ticketOrderRepo;
    @Autowired
    private SponsorRepo sponsorRepo;
    @Autowired
    private BoothOrderRepo boothOrderRepo;
    @Value("${logo.path}")
    private String logoPath;
    @Autowired
    private ActivityLogService activityLogService;
    @Autowired
    private ActivityLogDetailService activityLogDetailService;
    @Value("${export.path}")
    private String exportPath;

    @Override
    public Event create(CreateEventRequest input, String creator) throws AppException {
        Contact creatorContact = contactRepo.findByAccountEmail(creator);
        Event newEvent = new Event();

        newEvent.setId(null);
        newEvent.setCode(input.getCode().toUpperCase());
        newEvent.setName(input.getName());
        newEvent.setBeginDate(input.getBeginDate());
        newEvent.setEndDate(input.getEndDate());
        newEvent.setAutoConfirm(input.getAutoConfirm());
        newEvent.setDescription(input.getDescription());
        newEvent.setNote(input.getNote());

        newEvent.setLogoPath(input.getLogoPath());
        newEvent.setCreatorId(creatorContact.getId());
        //set template
        try {
            newEvent.setContentTicketMail(emailService.templateToString(emailService.toTemplateUrl(Constant.EmailTemplate.TICKET)));
            newEvent.setContentBuyTicketMail(emailService.templateToString(emailService.toTemplateUrl(Constant.EmailTemplate.TICKET_ORDER)));
            newEvent.setContentInviteMail(emailService.templateToString(emailService.toTemplateUrl(Constant.EmailTemplate.PARTICIPANT)));
        } catch (Exception e) {
            throw new AppException(Constant.ErrorCode.SYSTEM_ERROR_FAILED_TO_LOAD_EMAIL_TEMPLATE.name());
        }
        newEvent = save(newEvent);
        //add creator to event
        EventContact newEventMember = new EventContact();
        newEventMember.setEventId(newEvent.getId());
        newEventMember.setMemberId(creatorContact.getId());
        newEventMember.setStatus(Constant.TypeConstant.EventMemberStatus.ACCEPT);
        newEventMember.setEventRole(Constant.TypeConstant.EventRole.HEADER);
        newEventMember.setOrganizationAccess(Constant.TypeConstant.EventMemberAccess.EDIT);
        newEventMember.setAgendaAccess(Constant.TypeConstant.EventMemberAccess.EDIT);
        newEventMember.setTicketAccess(Constant.TypeConstant.EventMemberAccess.EDIT);
        newEventMember.setBoothAccess(Constant.TypeConstant.EventMemberAccess.EDIT);
        newEventMember.setSponsorAccess(Constant.TypeConstant.EventMemberAccess.EDIT);
        newEventMember.setPostAccess(Constant.TypeConstant.EventMemberAccess.EDIT);
        newEventMember.setParticipantAccess(Constant.TypeConstant.EventMemberAccess.EDIT);
        newEventMember.setCreatorId(creatorContact.getId());
        eventContactRepo.save(newEventMember);
        return newEvent;
    }

    @Transactional
    protected Event save(Event e) {
        return eventRepo.save(e);
    }

    @Override
    public Event update(CreateEventRequest input) throws AppException {
        Event currentEvent = eventRepo.getOne(input.getId());
        currentEvent.setName(input.getName());
        currentEvent.setCode(input.getCode());
        currentEvent.setNote(input.getNote());
        currentEvent.setDescription(input.getDescription());
        currentEvent.setAutoConfirm(input.getAutoConfirm());
        currentEvent.setBeginDate(input.getBeginDate());
        currentEvent.setEndDate(input.getEndDate());
        currentEvent.setLogoPath(input.getLogoPath());
        return eventRepo.save(currentEvent);
    }

    @Override
    public SearchEventResponse<EventResponse> search(SearchEventRequest request, String user) {
        SearchEventResponse<EventResponse> searchEventResponse = new SearchEventResponse();
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Page<Event> foundPageEvents = eventRepo.search(request.getSearchKey(), user,
                Constant.TypeConstant.EventMemberStatus.ACCEPT,
                request.getStatus(), pageableRequested);

        List<EventResponse> foundEvents = foundPageEvents.getContent().stream()
                .map(e -> {
                            EventResponse er = modelMapper.map(e, EventResponse.class);
                            er.setNoMembers(eventContactRepo.findAllByEventIdAndStatus(e.getId(), Constant.TypeConstant.EventMemberStatus.ACCEPT).size());
                            return er;
                        }
                ).collect(Collectors.toList());
        searchEventResponse.setEventPage(new PageImpl<>(foundEvents, pageableRequested, foundPageEvents.getTotalElements()));
        return searchEventResponse;
    }


    @Override
    public EventResponse getById(Long id) {
        Optional<Event> optEvent = eventRepo.findById(id);
        if (optEvent.isPresent()) {
            Event e = optEvent.get();
            EventResponse er = modelMapper.map(e, EventResponse.class);
            er.setNoMembers(eventContactRepo.findAllByEventIdAndStatus(er.getId(), Constant.TypeConstant.EventMemberStatus.ACCEPT).size());
            er.setNoBooths(e.getBooths().size());
            er.setNoParticipants(e.getParticipants().size());
            er.setNoSponsors(e.getSponsors().size());
            er.setNoTickets(e.getTickets().size());
            er.getCreator().setId(e.getCreatorId());
            return er;
        } else return null;
    }

    @Override
    public Event findByCode(String code) {
        return eventRepo.findByCode(code);
    }

    @Override
    public List<EventResponse> getAllJoinedEventsShort(String user) {
        Contact userContact = contactRepo.findByAccountEmail(user);
        return eventContactRepo.findAllByMemberIdAndStatus(userContact.getId(), Constant.TypeConstant.EventMemberStatus.ACCEPT).stream().map(e ->
                modelMapper.map(e.getEvent(), EventResponse.class)
        ).collect(Collectors.toList());

    }

    @Override
    public List<Event> getAllJoinedEvents(String user) {
        Contact userContact = contactRepo.findByAccountEmail(user);
        return eventContactRepo.findAllByMemberIdAndStatus(userContact.getId(), Constant.TypeConstant.EventMemberStatus.ACCEPT).stream().map(e ->
                e.getEvent()
        ).collect(Collectors.toList());
    }

    @Override
    public EventResponse updateContentMail(UpdateMailTemplate request, Constant.EmailTemplate template) throws AppException {
        Event event = eventRepo.getOne(request.getEventId());
        switch (template) {
            case PARTICIPANT:
                event.setContentInviteMail(request.getContentMail());
                break;
            case TICKET:
                event.setContentTicketMail(request.getContentMail());
                break;
            case TICKET_ORDER:
                event.setContentBuyTicketMail(request.getContentMail());
                break;
        }
        return modelMapper.map(eventRepo.save(event), EventResponse.class);
    }

    @Override
    public EventResponse changeEventStatus(ChangeEventStatusRequest request, String userEmail) throws AppException {
        Event event = eventRepo.getOne(request.getEventId());
        switch (request.getStatus()) {
            case INPROGRESS:
                break;
            case DONE:
                if (request.isCheckTaskDone() && taskRepo.existsByEventIdAndStatusIn(request.getEventId(),
                        Arrays.asList(Constant.TypeConstant.TaskStatus.INPROGRESS, Constant.TypeConstant.TaskStatus.OPEN))) {
                    throw new AppException(Constant.ErrorCode.EVENT_HAS_NOT_COMPLETED_TASK.name());
                } else {
                    Map<String, String> emailNameMap = new HashMap<>();
                    sponsorRepo.findAllByEventId(event.getId()).forEach(s -> emailNameMap.put(s.getEmail(), s.getName()));
                    eventContactRepo.findAllByEventIdAndStatus(event.getId(), Constant.TypeConstant.EventMemberStatus.ACCEPT).forEach(ec -> {
                        Contact member = ec.getMember();
                        emailNameMap.put(member.getEmailAccount(), member.getFirstName());
                    });
                    participantRepo.findAllByEventIdAndStatus(event.getId(), Constant.TypeConstant.ParticipantStatus.ACCEPT).forEach(p -> emailNameMap.put(p.getEmail(), p.getName()));
                    boothOrderRepo.findAllByEventIdAndStatus(event.getId(), Constant.TypeConstant.BoothOrderStatus.ACCEPT).forEach(bo -> emailNameMap.put(bo.getEmail(), bo.getName()));
                    List<SendMailRequest> sendMailRequests = new LinkedList<>();
                    SendMailRequest sendMailRequest;
                    Map<String, Object> rawData;
                    for (Map.Entry e : emailNameMap.entrySet()) {
                        rawData = new HashMap<>();
                        rawData.put(Constant.EmailVariable.RECEIVER_NAME.value, e.getValue());
                        rawData.put(Constant.EmailVariable.CONTENT.value, request.getContentMail());

                        sendMailRequest = SendMailRequest.builder()
                                .to(Collections.singletonList(e.getKey().toString()))
                                .emailType(Constant.EmailType.DONE_EVENT)
                                .rawData(rawData)
                                .build();
                        sendMailRequests.add(sendMailRequest);
                    }
                    emailService.composeEmail(sendMailRequests);
                }
                break;
            case CANCEL:
                Map<String, String> emailNameMap = new HashMap<>();
                List<Post> posts = postRepo.findByEventId(event.getId());
                posts.stream().forEach(p -> {
                    p.setStatus(Constant.TypeConstant.PostStatus.NOT_ACTIVE);
                });
                postRepo.saveAll(posts);
                participantRepo.findAllByEventIdAndStatus(event.getId(), Constant.TypeConstant.ParticipantStatus.ACCEPT).forEach(p -> emailNameMap.put(p.getEmail(), p.getName()));
                boothOrderRepo.findAllByEventIdAndStatus(event.getId(), Constant.TypeConstant.BoothOrderStatus.ACCEPT).forEach(bo -> emailNameMap.put(bo.getEmail(), bo.getName()));
                sponsorRepo.findAllByEventId(event.getId()).forEach(s -> emailNameMap.put(s.getEmail(), s.getName()));
                List<SendMailRequest> sendMailRequests = new LinkedList<>();
                SendMailRequest sendMailRequest;
                Map<String, Object> rawData;
                for (Map.Entry e : emailNameMap.entrySet()) {
                    rawData = new HashMap<>();
                    rawData.put(Constant.EmailVariable.RECEIVER_NAME.value, e.getValue());
                    rawData.put(Constant.EmailVariable.CONTENT.value, request.getContentMail());
                    sendMailRequest = SendMailRequest.builder()
                            .to(Collections.singletonList(e.getKey().toString()))
                            .emailType(Constant.EmailType.CANCEL_EVENT)
                            .rawData(rawData)
                            .build();
                    sendMailRequests.add(sendMailRequest);
                }
                emailService.composeEmail(sendMailRequests);
                break;
        }
        if (!event.getStatus().equals(request.getStatus())) {
            Contact currentUserContact = contactRepo.findByAccountEmail(userEmail);
            ActivityLog activityLogTask = activityLogService.create(new ActivityLog(currentUserContact.getId(),
                    Constant.TypeConstant.ActivityType.EVENT, event.getId(), request.getNote()));
            List<ActivityLogDetail> activityLogDetails = new ArrayList<>();
            if (!Objects.equals(event.getStatus(), request.getStatus())) {
                activityLogDetails.add(new ActivityLogDetail(activityLogTask.getId(), "status", event.getStatus().name(), request.getStatus().name()));
            }
            activityLogDetailService.saveDetails(activityLogDetails);
        }
        event.setStatus(request.getStatus());
        return modelMapper.map(eventRepo.save(event), EventResponse.class);
    }

    @Override
    public ResponseEntity exportEventInformation(Long eventId) throws AppException {
        Event event = eventRepo.getOne(eventId);
        Workbook workbook = new XSSFWorkbook();
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setWrapText(true);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        //Participant
        List<ParticipantExport> participants = participantRepo.findAllByEventIdForExport(eventId);
        if (!participants.isEmpty()) {
            Sheet sheet = workbook.createSheet("Người tham dự");
            int countRow = 0;
            Row row;
            //header
            row = sheet.createRow(countRow++);
            String[] headerNames = {"Loại vé", "Người thêm",
                    "Tên người tham dự", "Email", "Số điện thoại",
                    "Ngày thêm", "Ngày xác nhận", "Trạng thái", "Người xác nhận"};
            for (int i = 0; i < headerNames.length; i++) {
                {
                    Cell headerCell = row.createCell(i);
                    headerCell.setCellValue(headerNames[i]);
                    headerCell.setCellStyle(headerStyle);
                }
            }
            Cell cell;
            for (ParticipantExport participant : participants) {
                row = sheet.createRow(countRow++);

                cell = row.createCell(0);
                cell.setCellValue(participant.getTicketName());

                cell = row.createCell(1);
                cell.setCellValue(participant.getInviterName());


                cell = row.createCell(2);
                cell.setCellValue(participant.getParticipantName());


                cell = row.createCell(3);
                cell.setCellValue(participant.getEmail());


                cell = row.createCell(4);
                cell.setCellValue(participant.getPhone());


                cell = row.createCell(5);
                cell.setCellValue(com.ems.api.util.DateUtil.formatDate(participant.getAddedDate()));


                cell = row.createCell(6);
                cell.setCellValue(com.ems.api.util.DateUtil.formatDate(participant.getConfirmedDate()));


                cell = row.createCell(7);
                cell.setCellValue(participant.getStatus());


                cell = row.createCell(8);
                cell.setCellValue(participant.getVerifierName());

            }
            for (int i = 0; i < headerNames.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //ticket
        List<TicketExport> tickets = ticketRepo.findAllByEventIdForExport(eventId);
        if (!tickets.isEmpty()) {
            Sheet sheet = workbook.createSheet("Loại vé");
            Map<Constant.TypeConstant.TicketStatus, String> ticketStatus = new HashMap<Constant.TypeConstant.TicketStatus, String>() {{
                put(Constant.TypeConstant.TicketStatus.ACTIVE, "Hoạt động");
                put(Constant.TypeConstant.TicketStatus.NOT_ACTIVE, "Không hoạt động");
            }};
            int countRow = 0;
            Row row;
            //header
            row = sheet.createRow(countRow++);
            String[] headerNames = {"Tên vé", "Người tạo", "Ngày tạo", "Số lượng vé giới hạn",
                    "Giá", "Vé đã bán", "Vé đã mời", "Vé còn lại",
                    "Trạng thái"};
            for (int i = 0; i < headerNames.length; i++) {
                {
                    Cell headerCell = row.createCell(i);
                    headerCell.setCellValue(headerNames[i]);
                    headerCell.setCellStyle(headerStyle);
                }
            }
            Cell cell;
            for (TicketExport ticket : tickets) {
                row = sheet.createRow(countRow++);

                cell = row.createCell(0);
                cell.setCellValue(ticket.getName());

                cell = row.createCell(1);
                cell.setCellValue(ticket.getCreatorName());

                cell = row.createCell(2);
                cell.setCellValue(com.ems.api.util.DateUtil.formatDate(ticket.getCreateDate()));


                cell = row.createCell(3);
                cell.setCellValue(ticket.getLimited());


                cell = row.createCell(4);
                cell.setCellValue(ticket.getPrice());


                cell = row.createCell(5);
                cell.setCellValue(ticket.getSold());


                cell = row.createCell(6);
                cell.setCellValue(ticket.getInvited());


                cell = row.createCell(7);
                cell.setCellValue(ticket.getRemain());


                cell = row.createCell(8);
                cell.setCellValue(ticket.getStatus());

            }
            for (int i = 0; i < headerNames.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //ticket order
        List<TicketOrderExport> ticketOrders = ticketOrderRepo.findAllByEventIdForExport(eventId);
        if (!ticketOrders.isEmpty()) {
            Sheet sheet = workbook.createSheet("Đơn mua vé");
            int countRow = 0;
            Row row;
            //header
            row = sheet.createRow(countRow++);
            String[] headerNames = {"Người mua", "Email", "Số điện thoại",
                    "Số lượng đơn", "Tổng số lượng vé", "Tổng tiền"};
            for (int i = 0; i < headerNames.length; i++) {
                {
                    Cell headerCell = row.createCell(i);
                    headerCell.setCellValue(headerNames[i]);
                    headerCell.setCellStyle(headerStyle);
                }
            }
            Cell cell;
            for (TicketOrderExport ticketExport : ticketOrders) {
                row = sheet.createRow(countRow++);

                cell = row.createCell(0);
                cell.setCellValue(ticketExport.getName());

                cell = row.createCell(1);
                cell.setCellValue(ticketExport.getEmail());

                cell = row.createCell(2);
                cell.setCellValue(ticketExport.getPhone());


                cell = row.createCell(3);
                cell.setCellValue(ticketExport.getTotalOrder());


                cell = row.createCell(4);
                cell.setCellValue(ticketExport.getTotalQuantity());


                cell = row.createCell(5);
                cell.setCellValue(ticketExport.getTotalAmount());


            }
            for (int i = 0; i < headerNames.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //booth
        List<BoothExport> booths = boothRepo.findAllByEventIdForExport(eventId);
        if (!booths.isEmpty()) {
            Sheet sheet = workbook.createSheet("Gian hàng");
            int countRow = 0;
            Row row;
            //header
            row = sheet.createRow(countRow++);
            String[] headerNames = {"Tên vị trí", "Mô tả vị trí", "Tên gian hàng", "Mô tả gian hàng",
                    "Giá thuê", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái",
                    "Số lượng đăng ký đang chờ", "Số lượng đăng ký đã chấp thuận", "Số lượng đăng ký đã từ chối", "Tổng số lượng đăng ký"
            };
            for (int i = 0; i < headerNames.length; i++) {
                {
                    Cell headerCell = row.createCell(i);
                    headerCell.setCellValue(headerNames[i]);
                    headerCell.setCellStyle(headerStyle);
                }
            }
            Cell cell;
            for (BoothExport booth : booths) {
                row = sheet.createRow(countRow++);

                cell = row.createCell(0);
                cell.setCellValue(booth.getLocationName());

                cell = row.createCell(1);
                cell.setCellValue(booth.getLocationDescription());

                cell = row.createCell(2);
                cell.setCellValue(booth.getBoothName());


                cell = row.createCell(3);
                cell.setCellValue(booth.getBoothDescription());


                cell = row.createCell(4);
                cell.setCellValue(booth.getRentFee());


                cell = row.createCell(5);
                cell.setCellValue(com.ems.api.util.DateUtil.formatDate(booth.getStartRentDate()));

                cell = row.createCell(6);
                cell.setCellValue(com.ems.api.util.DateUtil.formatDate(booth.getEndRentDate()));

                cell = row.createCell(7);
                cell.setCellValue(booth.getStatus());

                cell = row.createCell(8);
                cell.setCellValue(booth.getWaiting());

                cell = row.createCell(9);
                cell.setCellValue(booth.getAccept());

                cell = row.createCell(10);
                cell.setCellValue(booth.getReject());

                cell = row.createCell(11);
                cell.setCellValue(booth.getTotalOrder());

            }
            for (int i = 0; i < headerNames.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //post
        List<EventPostExport> posts = postRepo.findAllByEventIdForExport(eventId);
        if (!posts.isEmpty()) {
            Sheet sheet = workbook.createSheet("Bài viết");
            int countRow = 0;
            Row row;
            //header
            row = sheet.createRow(countRow++);
            String[] headerNames = {"Tiêu đề", "Người tạo", "Ngày tạo","Trạng thái","Lượt xem"};
            for (int i = 0; i < headerNames.length; i++) {
                {
                    Cell headerCell = row.createCell(i);
                    headerCell.setCellValue(headerNames[i]);
                    headerCell.setCellStyle(headerStyle);
                }
            }
            Cell cell;
            for (EventPostExport post : posts) {
                row = sheet.createRow(countRow++);

                cell = row.createCell(0);
                cell.setCellValue(post.getSubject());

                cell = row.createCell(1);
                cell.setCellValue(post.getCreatorName());

                cell = row.createCell(2);
                cell.setCellValue(com.ems.api.util.DateUtil.formatDate(post.getCreateDate()));

                cell = row.createCell(3);
                cell.setCellValue(post.getStatus());

                cell = row.createCell(4);
                cell.setCellValue(post.getViews());

            }
            for (int i = 0; i < headerNames.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //sponsor
        List<SponsorExport> sponsors = sponsorRepo.findAllByEventIdForExport(eventId);
        if (!sponsors.isEmpty()) {
            Sheet sheet = workbook.createSheet("Nhà tài trợ");
            int countRow = 0;
            Row row;
            //header
            row = sheet.createRow(countRow++);
            String[] headerNames = {"Tên", "Email", "Số điện thoại","Website","Tài trợ","Thông tin thêm"};
            for (int i = 0; i < headerNames.length; i++) {
                {
                    Cell headerCell = row.createCell(i);
                    headerCell.setCellValue(headerNames[i]);
                    headerCell.setCellStyle(headerStyle);
                }
            }
            Cell cell;
            for (SponsorExport sponsor : sponsors) {
                row = sheet.createRow(countRow++);

                cell = row.createCell(0);
                cell.setCellValue(sponsor.getName());

                cell = row.createCell(1);
                cell.setCellValue(sponsor.getEmail());

                cell = row.createCell(2);
                cell.setCellValue(sponsor.getPhone());

                cell = row.createCell(3);
                cell.setCellValue(sponsor.getLink());

                cell = row.createCell(4);
                cell.setCellValue(sponsor.getItem());
                cell = row.createCell(5);
                cell.setCellValue(sponsor.getInformation());

            }
            for (int i = 0; i < headerNames.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        //member
        List<EventMemberExport> members = eventContactRepo.findAllByEventIdForExport(eventId);
        if (!members.isEmpty()) {
            Sheet sheet = workbook.createSheet("Thành viên");
            int countRow = 0;
            Row row;
            //header
            row = sheet.createRow(countRow++);
            String[] headerNames = {"Tên", "Vai trò", "Số nhóm tham gia","Ngày tham gia","Người thêm","Công việc chưa làm",
            "Công việc đang làm","Công việc hoàn thành", "Công việc đã hủy", "Công việc trễ hạn","Tổng số công việc"};
            for (int i = 0; i < headerNames.length; i++) {
                {
                    Cell headerCell = row.createCell(i);
                    headerCell.setCellValue(headerNames[i]);
                    headerCell.setCellStyle(headerStyle);
                }
            }
            Cell cell;
            for (EventMemberExport  member: members) {
                row = sheet.createRow(countRow++);

                cell = row.createCell(0);
                cell.setCellValue(member.getName());

                cell = row.createCell(1);
                cell.setCellValue(member.getRole());

                cell = row.createCell(2);
                cell.setCellValue(member.getNoTeam());

                cell = row.createCell(3);
                cell.setCellValue(DateUtil.formatDate(member.getCreateDate()));

                cell = row.createCell(4);
                cell.setCellValue(member.getCreatorName());
                cell = row.createCell(5);
                cell.setCellValue(member.getTaskOpen());
                cell = row.createCell(6);
                cell.setCellValue(member.getTaskInprogress());
                cell = row.createCell(7);
                cell.setCellValue(member.getTaskDone());
                cell = row.createCell(8);
                cell.setCellValue(member.getTaskCancel());
                cell = row.createCell(9);
                cell.setCellValue(member.getTaskLate());
                cell = row.createCell(10);
                cell.setCellValue(member.getTask());

            }
            for (int i = 0; i < headerNames.length; i++) {
                sheet.autoSizeColumn(i);
            }
        }


        byte[] returnFileBytes = new byte[0];
        try {
            File currDir = new File(exportPath + "/.");
            String path = currDir.getAbsolutePath();
            long currentMl = System.currentTimeMillis();
            String fileLocation = path.substring(0, path.length() - 1) + "temp" + currentMl;
            File newFile = File.createTempFile(fileLocation, ".xlsx");
            newFile.deleteOnExit();
            FileOutputStream outputStream = new FileOutputStream(newFile);
            workbook.write(outputStream);
            workbook.close();
            returnFileBytes = Files.readAllBytes(newFile.toPath());
            outputStream.close();
        } catch (Exception e) {
            throw new AppException(e, Constant.ErrorCode.SYSTEM_ERROR.name());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + event.getCode()+"-"+event.getName() + ".xlsx");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(returnFileBytes);
    }

}
