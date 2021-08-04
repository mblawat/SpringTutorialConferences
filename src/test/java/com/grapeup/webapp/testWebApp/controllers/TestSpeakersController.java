package com.grapeup.webapp.testWebApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grapeup.webapp.testWebApp.models.Session;
import com.grapeup.webapp.testWebApp.models.Speaker;
import com.grapeup.webapp.testWebApp.repositories.SpeakerRepository;
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

@WebMvcTest(value = SpeakersController.class)
public class TestSpeakersController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpeakerRepository mockSpeakerRepository;
    private ObjectMapper mapper = new ObjectMapper();
    private JacksonTester<Speaker> jsonSpeaker;
    private JacksonTester<List<Speaker>> jsonListSpeaker;

    private final Speaker mockSpeaker = new Speaker(1L, "John", "Smith", "mr", "Grape Up", "Bio", null, new ArrayList<Session>());

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void getList() throws Exception {
        // Arrange
        List<Speaker> speakers = Arrays.asList(mockSpeaker);
        Mockito.when(mockSpeakerRepository.findAll()).thenReturn(speakers);

        String url = "/api/v1/speakers";

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        JSONAssert.assertEquals(jsonListSpeaker.write(speakers).getJson(), response.getContentAsString(), false);
    }

    @Test
    public void getOne() throws Exception {
        // Arrange
        final Long speakerId = mockSpeaker.getSpeakerId();
        Mockito.when(mockSpeakerRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(mockSpeaker));

        String url = "/api/v1/speakers/" + speakerId;

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        JSONAssert.assertEquals(jsonSpeaker.write(mockSpeaker).getJson(), response.getContentAsString(), false);
    }

    @Test
    public void getOneNotFound() throws Exception {
        // Arrange
        final Long speakerId = mockSpeaker.getSpeakerId();
        Mockito.when(mockSpeakerRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        String url = "/api/v1/speakers/" + speakerId;

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Map<String, Object> responseBody = mapper.readValue(response.getContentAsString(), Map.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(responseBody.get("message"), String.format("Speaker with Id %d not found", speakerId));
    }

    @Test
    public void create() throws Exception {
        // Arrange
        Mockito.when(mockSpeakerRepository.save(Mockito.any(Speaker.class))).thenReturn(mockSpeaker);

        String url = "/api/v1/speakers";

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url).accept(MediaType.APPLICATION_JSON).content(jsonSpeaker.write(mockSpeaker).getJson()).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(jsonSpeaker.write(mockSpeaker).getJson(), response.getContentAsString());
    }

    @Test
    public void delete() throws Exception {
        // Arrange
        final Long speakerId = mockSpeaker.getSpeakerId();
        String url = "/api/v1/speakers/" + speakerId;

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
        final Long speakerId = mockSpeaker.getSpeakerId();
        Speaker updatedSpeaker = new Speaker(speakerId, "Anna", "Wallace", "mrs", "MS", "Bio 2", null, new ArrayList<Session>());

        Mockito.when(mockSpeakerRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(mockSpeaker));
        Mockito.when(mockSpeakerRepository.save(Mockito.any(Speaker.class))).thenReturn(updatedSpeaker);

        String url = "/api/v1/speakers/" + speakerId;

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url).accept(MediaType.APPLICATION_JSON).content(jsonSpeaker.write(updatedSpeaker).getJson()).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonSpeaker.write(updatedSpeaker).getJson(), response.getContentAsString());
    }

    @Test
    public void updateNotFound() throws Exception {
        // Arrange
        final Long speakerId = mockSpeaker.getSpeakerId();
        Speaker updatedSpeaker = new Speaker(speakerId, "Anna", "Wallace", "mrs", "MS", "Bio 2", null, new ArrayList<Session>());

        Mockito.when(mockSpeakerRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        String url = "/api/v1/speakers/" + speakerId;

        // Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url).accept(MediaType.APPLICATION_JSON).content(jsonSpeaker.write(updatedSpeaker).getJson()).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Map<String, Object> responseBody = mapper.readValue(response.getContentAsString(), Map.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals(responseBody.get("message"), String.format("Speaker with Id %d not found", speakerId));
    }
}


