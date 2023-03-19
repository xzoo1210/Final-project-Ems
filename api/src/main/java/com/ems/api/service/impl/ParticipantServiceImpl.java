package com.ems.api.service.impl;

import com.ems.api.dto.request.ParticipantRequest;
import com.ems.api.dto.request.SearchParticipantRequest;
import com.ems.api.dto.request.SendMailRequest;
import com.ems.api.dto.response.ParticipantResponse;
import com.ems.api.dto.response.SearchResponse;
import com.ems.api.dto.response.TicketOrderResponse;
import com.ems.api.entity.Contact;
import com.ems.api.entity.Event;
import com.ems.api.entity.Participant;
import com.ems.api.entity.Ticket;
import com.ems.api.repository.ContactRepo;
import com.ems.api.repository.EventRepo;
import com.ems.api.repository.ParticipantRepo;
import com.ems.api.repository.TicketRepo;
import com.ems.api.service.EmailService;
import com.ems.api.service.ParticipantService;
import com.ems.api.service.TicketService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.PageUtil;
import com.ems.api.util.StringUtils;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ParticipantServiceImpl implements ParticipantService {
    @Autowired
    private ParticipantRepo participantRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ContactRepo contactRepo;
    @Value("${export.qr.path}")
    private String exportQrPath;
    @Value("${export.path}")
    private String exportPath;
    @Value("${server.web}")
    private String domainWeb;
    @Value("${server.api}")
    private String domainApi;
    @Value("${logo.path}")
    private String logoPath;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private TicketService ticketService;

    @Transactional
    public List<Participant> addParticipantFromOrder(TicketOrderResponse orderResponse) {
        Long eventId = orderResponse.getTicketOrder().getEventId();
        Event event = eventRepo.getOne(eventId);
        boolean autoCheck = event.getAutoConfirm() != null ? event.getAutoConfirm() : false;
        List<Participant> participants = orderResponse.getTicketOrderDetails().stream()
                .map(tod -> {
                    Participant p = new Participant();
                    p.setEventId(eventId);
                    p.setTicketId(tod.getTicketId());
                    p.setStatus(autoCheck ? Constant.TypeConstant.ParticipantStatus.ACCEPT
                            : Constant.TypeConstant.ParticipantStatus.WAITING);
                    p.setName(tod.getName());
                    p.setEmail(tod.getEmail());
                    p.setPhone(tod.getPhone());
                    if (autoCheck) {
                        p.setConfirmedDate(new Date());
                    }

                    return p;
                }).collect(Collectors.toList());

        return participantRepo.saveAll(participants);
    }

    @Override
    public SearchResponse<ParticipantResponse> search(SearchParticipantRequest request) {
        SearchResponse<ParticipantResponse> response = new SearchResponse<>();
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Page<Participant> participantPage = participantRepo.search(request.getEventId(), request.getTicketId(), request.getKeySearch(), pageableRequested);
        List<ParticipantResponse> foundParticipants = participantPage.getContent().stream().map(
                l -> modelMapper.map(l, ParticipantResponse.class)
        ).collect(Collectors.toList());
        response.setPage(new PageImpl<>(foundParticipants, pageableRequested, participantPage.getTotalElements()));
        return response;
    }

    @Override
    @Transactional
    public ParticipantResponse inviteParticipant(ParticipantRequest request, String userEmail) throws AppException {
        Contact inviter = contactRepo.findByAccountEmail(userEmail);
        Participant participant = new Participant();
        participant.setInviterId(inviter.getId());
        participant.setStatus(Constant.TypeConstant.ParticipantStatus.WAITING);
        participant.setTicketId(request.getTicketId());
        participant.setName(request.getName());
        participant.setEmail(request.getEmail());
        participant.setPhone(request.getPhone());
        participant.setEventId(request.getEventId());
        ticketService.inviteTicket(request.getTicketId());
        return modelMapper.map(participantRepo.save(participant), ParticipantResponse.class);
    }

    @Override
    @Transactional
    public void sendInviteParticipantMail(Long participantId) throws AppException {
        Participant participant = participantRepo.getOne(participantId);
        SendMailRequest sendMailRequest = new SendMailRequest();
        Event event = eventRepo.getOne(participant.getEventId());
        Ticket ticket = ticketRepo.getOne(participant.getTicketId());
        sendMailRequest.setTo(Collections.singletonList(participant.getEmail()));
        sendMailRequest.setEmailType(Constant.EmailType.INVITE_PARTICIPANT);
        Map<String, Object> rawData = new HashMap<>();
        rawData.put(Constant.EmailVariable.RECEIVER_NAME.value, participant.getName());
        rawData.put(Constant.EmailVariable.CONTENT.value, event.getContentInviteMail());
        rawData.put(Constant.EmailVariable.LOGO_URL.value, logoPath);
        rawData.put(Constant.EmailVariable.QR_CODE.value,
                StringUtils.updateToCloudinary(exportQrPath, domainWeb + "/qr-check/" + event.getId() + "/" + participant.getId()));
        rawData.put(Constant.EmailVariable.TICKET_NAME.value, ticket.getName());
//            rawData.put(Constant.EmailVariable.TICKET_DESCRIPTION.value, tickets.get(participant.getTicketId()).getDescription());
        rawData.put(Constant.EmailVariable.EVENT_NAME.value, event.getName());
        sendMailRequest.setRawData(rawData);
        emailService.composeEmail(Collections.singletonList(sendMailRequest));
    }

    @Override
    public ParticipantResponse getById(Long id) {
        return modelMapper.map(participantRepo.getOne(id),ParticipantResponse.class);
    }

    @Override
    public ParticipantResponse confirmJoined(Long id, String userEmail) {
        Participant participant = participantRepo.getOne(id);
        Contact verifier = contactRepo.findByAccountEmail(userEmail);
        participant.setVerifierId(verifier.getId());
        participant.setConfirmedDate(new Date());
        participant.setStatus(Constant.TypeConstant.ParticipantStatus.ACCEPT);
        return modelMapper.map(participantRepo.save(participant), ParticipantResponse.class);
    }

    @Override
    public ResponseEntity getFile(Long eventId) throws AppException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Người tham dự");
        Map<Constant.TypeConstant.ParticipantStatus, String> participantStatus  = new HashMap<Constant.TypeConstant.ParticipantStatus, String>() {{
            put(Constant.TypeConstant.ParticipantStatus.ACCEPT, "Đã tham gia");
            put(Constant.TypeConstant.ParticipantStatus.WAITING, "Chưa tham gia");
        }};
        int countRow = 0;
        Row row;
        //header
        row = sheet.createRow(countRow++);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setWrapText(true);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Times New Roman");
        List<Participant> participants = participantRepo.findAllByEventId(eventId);
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        String[] headerNames = {"ID", "Người thêm", "Loại vé", "Trạng thái",
                "Tên người tham dự", "Email", "Số điện thoại",
                "Ngày thêm", "Ngày xác nhận", "Người xác nhận"};
        for (int i = 0; i < headerNames.length; i++) {
            {
                Cell headerCell = row.createCell(i);
                headerCell.setCellValue(headerNames[i]);
                headerCell.setCellStyle(headerStyle);
            }
        }
        Event event = eventRepo.getOne(eventId);
        Cell cell;
        for (Participant participant : participants) {
            row = sheet.createRow(countRow++);

            cell = row.createCell(0);
            cell.setCellValue(participant.getId());

            cell = row.createCell(1);
            cell.setCellValue(participant.getInviter() == null ? "" : participant.getInviter().getFirstName());


            cell = row.createCell(2);
            cell.setCellValue(participant.getTicket().getName());


            cell = row.createCell(3);
            cell.setCellValue(participantStatus.get(participant.getStatus()));


            cell = row.createCell(4);
            cell.setCellValue(participant.getName());


            cell = row.createCell(5);
            cell.setCellValue(participant.getEmail());


            cell = row.createCell(6);
            cell.setCellValue(participant.getPhone());


            cell = row.createCell(7);
            cell.setCellValue(com.ems.api.util.DateUtil.formatDate(participant.getAddedDate()));


            cell = row.createCell(8);
            cell.setCellValue(com.ems.api.util.DateUtil.formatDate(participant.getConfirmedDate()));


            cell = row.createCell(9);
            cell.setCellValue(participant.getVerifier() == null ? "" : participant.getVerifier().getFirstName());

        }
        for (int i = 0; i < 10; i++) {
            sheet.autoSizeColumn(i);
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
                "attachment; filename=" + event.getCode() + ".xlsx");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(returnFileBytes);
    }

    @Override
    public void sendTicketToParticipants(List<Participant> participants) throws AppException {
        List<SendMailRequest> sendMailRequests = new LinkedList<>();
        Event event = eventRepo.getOne(participants.get(0).getEventId());
        Map<Long, Ticket> tickets = ticketRepo.findAllByEventId(event.getId()).stream().collect(Collectors.toMap(Ticket::getId, Function.identity()));
        for (Participant participant : participants) {
            SendMailRequest sendMailRequest = new SendMailRequest();
            sendMailRequest.setTo(Collections.singletonList(participant.getEmail()));
            sendMailRequest.setEmailType(Constant.EmailType.TICKET_EVENT);
            Map<String, Object> rawData = new HashMap<>();
            rawData.put(Constant.EmailVariable.RECEIVER_NAME.value, participant.getName());
            rawData.put(Constant.EmailVariable.CONTENT.value, event.getContentTicketMail());
            rawData.put(Constant.EmailVariable.LOGO_URL.value, logoPath);
            rawData.put(Constant.EmailVariable.QR_CODE.value,
                    StringUtils.updateToCloudinary(exportQrPath, domainWeb + "/qr-check/" + participant.getEventId() + "/" + participant.getId()));
            rawData.put(Constant.EmailVariable.TICKET_NAME.value, tickets.get(participant.getTicketId()).getName());
//            rawData.put(Constant.EmailVariable.TICKET_DESCRIPTION.value, tickets.get(participant.getTicketId()).getDescription());
            rawData.put(Constant.EmailVariable.EVENT_NAME.value, event.getName());
            sendMailRequest.setRawData(rawData);
            sendMailRequests.add(sendMailRequest);
        }


        emailService.composeEmail(sendMailRequests);
    }

}
