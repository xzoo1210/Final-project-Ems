package com.ems.api.controller;

import com.ems.api.ApiApplication;
import com.ems.api.configuration.security.MyUserDetails;
import com.ems.api.dto.request.CreateEventRequest;
import com.ems.api.dto.request.RegisterRequest;
import com.ems.api.dto.request.SearchEventRequest;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.dto.response.SearchEventResponse;
import com.ems.api.entity.Event;
import com.ems.api.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = ApiApplication.class)
public class ActivityLogControllerTest {   private static final String URL_CREATE = "/event/create";
    private static final String URL_SEARCH = "/event/search";
    private static final String URL_FIND_BY_CODE = "/event/find-by-code";
    private static final String URL_GET_BY_ID = "/event/%s";
    private static final String EVENT_CODE = "EVENT-TEST";
    private static final String EVENT_NAME = "Su kien test";
    private static final String EVENT_DESCRIPTION = "Su kien test description";
    private static final String EVENT_NOTE = "UT localhost";
    private static final String EVENT_LOGO_PATH = "https://www.google.com/";
    private static final Long ID = 1L;
    private static final Long CREATOR_ID = 1L;
    private static final Date EVENT_B_DATE = new Date(Date.UTC(122, 10, 10, 12, 30, 0));
    private static final Date EVENT_E_DATE = new Date(Date.UTC(122, 11, 10, 12, 30, 0));
    private CreateEventRequest createRequest;
    private RegisterRequest registerRequest;
    private SearchEventRequest searchRequest;
    private Event createResponse;
    private SearchEventResponse searchResponse;
    private Event getByIdResponse;
    private MyUserDetails appUserDetails;
    @Captor
    private ArgumentCaptor<CreateEventRequest> createRequestCaptor;
    private ArgumentCaptor<SearchEventRequest> searchRequestCaptor;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventService eventService;
    private Authentication authentication;
    private SecurityContext scContext;
    @Captor private ArgumentCaptor<Class<Event>> classCaptor;
    private Page<Event> eventPage;
    @BeforeEach
    void initData() {
        createRequest = CreateEventRequest.builder()
                .code(EVENT_CODE)
                .name(EVENT_NAME)
                .beginDate(EVENT_B_DATE)
                .endDate(EVENT_E_DATE)
                .autoConfirm(true)
                .description(EVENT_DESCRIPTION)
                .note(EVENT_NOTE)
                .logoPath(EVENT_LOGO_PATH)
                .build()
        ;
        createResponse = Event.builder()
                .id(ID)
                .creatorId(CREATOR_ID)
                .code(EVENT_CODE)
                .name(EVENT_NAME)
                .beginDate(EVENT_B_DATE)
                .endDate(EVENT_E_DATE)
                .autoConfirm(true)
                .description(EVENT_DESCRIPTION)
                .note(EVENT_NOTE)
                .logoPath(EVENT_LOGO_PATH)
                .build();
        appUserDetails = new MyUserDetails();
//        AppUserDetails.AppUser appUser = new AppUserDetails.AppUser();
//        appUser.setCountryId(COUNTRY_ID);
//        appUserDetails.setAppUser(appUser);
        authentication = mock(Authentication.class);
        scContext = mock(SecurityContext.class);
        when(authentication.getPrincipal()).thenReturn(appUserDetails);
        when(scContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(scContext);
        searchRequest=SearchEventRequest.builder()
                .searchKey(EVENT_CODE)
                .build();
        searchRequest.setPage(0);
        searchRequest.setSize(10);
        eventPage=new PageImpl<>(Collections.singletonList(createResponse));
        searchResponse=SearchEventResponse.<Event>builder().eventPage(eventPage).build();
//        var province = ProvinceResponse.builder().provinceName(PROVINCE_NAME).build();
//        var district = DistrictResponse.builder().districtName(DISTRICT_NAME).build();
//        var ward = WardResponse.builder().wardName(WARD_NAME).build();
//        var address =
//                AddressResponse.builder()
//                        .id(ID)
//                        .province(province)
//                        .district(district)
//                        .ward(ward)
//                        .detail(DETAIL)
//                        .build();
//
//        var area =
//                AreaResponse.builder()
//                        .id(ID)
//                        .areaCode("TT_A1")
//                        .areaName("TT_A1")
//                        .maxCapacity(100)
//                        .availableCapacity(0)
//                        .build();
//
//        var warehouseResponse =
//                WarehouseResponse.builder()
//                        .id(ID)
//                        .warehouseCode(AREA_CODE)
//                        .warehouseName(AREA_NAME)
//                        .description(DESCRIPTION)
//                        .address(address)
//                        .area(singletonList(area))
//                        .build();
//
//        areaRequest =
//                AreaRequest.builder()
//                        .areaCode(AREA_CODE)
//                        .warehouseId(ID)
//                        .areaName(AREA_NAME)
//                        .capacity(CAPACITY)
//                        .build();
//
//        areaResponse =
//                AreaResponse.builder()
//                        .id(ID)
//                        .areaCode(AREA_CODE)
//                        .areaName(AREA_NAME)
//                        .maxCapacity(CAPACITY)
//                        .availableCapacity(CAPACITY)
//                        .warehouse(warehouseResponse)
//                        .isEnabled(true)
//                        .isDeleted(false)
//                        .build();
//
//        areaResponsePage = new PageImpl<>(singletonList(areaResponse));
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void test_create_normalCase_01() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<Event>builder().data(createResponse).build();

        when(eventService.create(any(), any())).thenReturn(createResponse);

        mockMvc
                .perform(
                        post(URL_CREATE)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.logoPath", is(EVENT_LOGO_PATH)));


        verify(eventService, times(1)).create(createRequestCaptor.capture(), any());
        assertThat(createRequestCaptor.getValue(), samePropertyValuesAs(createRequest));
    }

    @Test
    void test_search_normalCase_01() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<SearchEventResponse>builder().data(searchResponse).build();

        when(eventService.search(any(), any())).thenReturn(searchResponse);

        mockMvc
                .perform(
                        post(URL_SEARCH)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.eventPage.content[0].id", is(1)))
                .andExpect(jsonPath("$.data.eventPage.content[0].name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.eventPage.content[0].code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.eventPage.content[0].autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.eventPage.content[0].description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.eventPage.content[0].note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.eventPage.content[0].logoPath", is(EVENT_LOGO_PATH)));


//        verify(eventService, times(1)).search(searchRequestCaptor.capture(), any());
    }
    @Test
    void test_findByCode_normalCase_01() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<SearchEventResponse>builder().data(searchResponse).build();

        when(eventService.findByCode(any())).thenReturn(createResponse);

        mockMvc
                .perform(
                        post(URL_FIND_BY_CODE)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.logoPath", is(EVENT_LOGO_PATH)));


//        verify(eventService, times(1)).search(searchRequestCaptor.capture(), any());
    }
    @Test
    void test_findByCode_normalCase_02() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<SearchEventResponse>builder().data(searchResponse).build();

        when(eventService.findByCode(any())).thenReturn(createResponse);

        mockMvc
                .perform(
                        post(URL_FIND_BY_CODE)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.logoPath", is(EVENT_LOGO_PATH)));


//        verify(eventService, times(1)).search(searchRequestCaptor.capture(), any());
    }
    @Test
    void test_findByCode_normalCase_03() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<SearchEventResponse>builder().data(searchResponse).build();

        when(eventService.findByCode(any())).thenReturn(createResponse);

        mockMvc
                .perform(
                        post(URL_FIND_BY_CODE)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.logoPath", is(EVENT_LOGO_PATH)));


//        verify(eventService, times(1)).search(searchRequestCaptor.capture(), any());
    }
    @Test
    void test_findByCode_normalCase_04() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<SearchEventResponse>builder().data(searchResponse).build();

        when(eventService.findByCode(any())).thenReturn(createResponse);

        mockMvc
                .perform(
                        post(URL_FIND_BY_CODE)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.logoPath", is(EVENT_LOGO_PATH)));


//        verify(eventService, times(1)).search(searchRequestCaptor.capture(), any());
    }
    @Test
    void test_findByCode_normalCase_05() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<SearchEventResponse>builder().data(searchResponse).build();

        when(eventService.findByCode(any())).thenReturn(createResponse);

        mockMvc
                .perform(
                        post(URL_FIND_BY_CODE)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.logoPath", is(EVENT_LOGO_PATH)));


//        verify(eventService, times(1)).search(searchRequestCaptor.capture(), any());
    }
    @Test
    void test_findByCode_normalCase_06() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<SearchEventResponse>builder().data(searchResponse).build();

        when(eventService.findByCode(any())).thenReturn(createResponse);

        mockMvc
                .perform(
                        post(URL_FIND_BY_CODE)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.logoPath", is(EVENT_LOGO_PATH)));


//        verify(eventService, times(1)).search(searchRequestCaptor.capture(), any());
    }
    @Test
    void test_findByCode_normalCase_07() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<SearchEventResponse>builder().data(searchResponse).build();

        when(eventService.findByCode(any())).thenReturn(createResponse);

        mockMvc
                .perform(
                        post(URL_FIND_BY_CODE)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.logoPath", is(EVENT_LOGO_PATH)));


//        verify(eventService, times(1)).search(searchRequestCaptor.capture(), any());
    }
    @Test
    void test_findByCode_normalCase_08() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<SearchEventResponse>builder().data(searchResponse).build();

        when(eventService.findByCode(any())).thenReturn(createResponse);

        mockMvc
                .perform(
                        post(URL_FIND_BY_CODE)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.logoPath", is(EVENT_LOGO_PATH)));


//        verify(eventService, times(1)).search(searchRequestCaptor.capture(), any());
    }
    @Test
    void test_findByCode_normalCase_09() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<SearchEventResponse>builder().data(searchResponse).build();

        when(eventService.findByCode(any())).thenReturn(createResponse);

        mockMvc
                .perform(
                        post(URL_FIND_BY_CODE)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.logoPath", is(EVENT_LOGO_PATH)));


//        verify(eventService, times(1)).search(searchRequestCaptor.capture(), any());
    }
    @Test
    void test_findByCode_normalCase_10() throws Exception {
        MessagesResponse responseBody = MessagesResponse.<SearchEventResponse>builder().data(searchResponse).build();

        when(eventService.findByCode(any())).thenReturn(createResponse);

        mockMvc
                .perform(
                        post(URL_FIND_BY_CODE)
                                .characterEncoding("utf-8")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(asJsonString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").doesNotExist())
                .andExpect(jsonPath("$.messages").doesNotExist())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is(EVENT_NAME)))
                .andExpect(jsonPath("$.data.code", is(EVENT_CODE)))
//                .andExpect(jsonPath("$.data.beginDate", is(asJsonString(EVENT_B_DATE))))
//                .andExpect(jsonPath("$.data.endDate", is(asJsonString(EVENT_E_DATE))))
                .andExpect(jsonPath("$.data.autoConfirm", is(true)))
                .andExpect(jsonPath("$.data.description", is(EVENT_DESCRIPTION)))
                .andExpect(jsonPath("$.data.note", is(EVENT_NOTE)))
                .andExpect(jsonPath("$.data.logoPath", is(EVENT_LOGO_PATH)));


//        verify(eventService, times(1)).search(searchRequestCaptor.capture(), any());
    }
}
