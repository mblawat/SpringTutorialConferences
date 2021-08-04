package com.grapeup.webapp.testWebApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grapeup.webapp.testWebApp.models.Session;
import com.grapeup.webapp.testWebApp.models.Speaker;
import com.grapeup.webapp.testWebApp.repositories.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(value = SessionsController.class)
public class TestSessionsController {

    private final Session mockSession = new Session(1L, "Test Session", "Test Description", 60, new ArrayList<Speaker>());
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SessionRepository mockSessionRepository;
    private ObjectMapper mapper = new ObjectMapper();
    private JacksonTester<Session> jsonSession;
    private JacksonTester<List<Session>> jsonListSession;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void getList() throws Exception {
        // Arrange
        List<Session> sessions = Arrays.asList(mockSession);
        Mockito.when(mockSessionRepository.findAll()).thenReturn(sessions);

        String url = "/api/v1/sessions";

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        JSONAssert.assertEquals(jsonListSession.write(sessions).getJson(), response.getContentAsString(), false);
    }

    @Test
    public void getOne() throws Exception {
        // Arrange
        final Long sessionId = mockSession.getSessionId();
        Mockito.when(mockSessionRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(mockSession));

        String url = "/api/v1/sessions/" + sessionId;

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        JSONAssert.assertEquals(jsonSession.write(mockSession).getJson(), response.getContentAsString(), false);
    }

    @Test
    public void getOneNotFound() throws Exception {
        // Arrange
        final Long sessionId = mockSession.getSessionId();
        Mockito.when(mockSessionRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        String url = "/api/v1/sessions/" + sessionId;

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Map<String, Object> responseBody = mapper.readValue(response.getContentAsString(), Map.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(responseBody.get("message"), String.format("Session with Id %d not found", sessionId));
    }

    @Test
    public void create() throws Exception {
        // Arrange
        Mockito.when(mockSessionRepository.save(Mockito.any(Session.class))).thenReturn(mockSession);

        String url = "/api/v1/sessions";

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url).accept(MediaType.APPLICATION_JSON).content(jsonSession.write(mockSession).getJson()).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(jsonSession.write(mockSession).getJson(), response.getContentAsString());
    }

    @Test
    public void delete() throws Exception {
        // Arrange
        final Long sessionId = mockSession.getSessionId();
        String url = "/api/v1/sessions/" + sessionId;

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertEquals("", response.getContentAsString());
    }

    @Test
    public void update() throws Exception {
        // Arrange
        final Long sessionId = mockSession.getSessionId();
        Session updatedSession = new Session(sessionId, "Updated Session", "Updated Description", 90, new ArrayList<Speaker>());

        Mockito.when(mockSessionRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(mockSession));
        Mockito.when(mockSessionRepository.save(Mockito.any(Session.class))).thenReturn(updatedSession);

        String url = "/api/v1/sessions/" + sessionId;

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url).accept(MediaType.APPLICATION_JSON).content(jsonSession.write(updatedSession).getJson()).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonSession.write(updatedSession).getJson(), response.getContentAsString());
    }

    @Test
    public void updateNotFound() throws Exception {
        // Arrange
        final Long sessionId = mockSession.getSessionId();
        Session updatedSession = new Session(sessionId, "Updated Session", "Updated Description", 90, new ArrayList<Speaker>());

        Mockito.when(mockSessionRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        String url = "/api/v1/sessions/" + sessionId;

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url).accept(MediaType.APPLICATION_JSON).content(jsonSession.write(updatedSession).getJson()).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Map<String, Object> responseBody = mapper.readValue(response.getContentAsString(), Map.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(responseBody.get("message"), String.format("Session with Id %d not found", sessionId));
    }
}


