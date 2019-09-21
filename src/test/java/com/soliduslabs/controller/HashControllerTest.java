package com.soliduslabs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soliduslabs.TestConfiguration;
import com.soliduslabs.model.HashMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
//@ContextConfiguration(classes = {TestConfiguration.class})
public class HashControllerTest {

    @InjectMocks
    private HashController hashController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(this.hashController).build();
    }

    @Test
    public void testGetHashByString() throws Exception {
        String url = "/messages";
        String expectedResult = "136ffddfbcd209d8ab01a794e2fa1f6b88f223d409d97c799c7a7c46494a2d6a";

        MvcResult actualResult = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(stringAsJson(new HashMessage("testGetHashByString")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Response does not match", actualResult.getResponse().getContentAsString(), expectedResult);
    }

    @Test
    public void testFailedToGetHashByString() throws Exception {
        String url = "/messages";
        String expectedResult = "Could not get hash from null ,error: java.lang.NullPointerException";

        MvcResult actualResult = mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(stringAsJson(new HashMessage(null)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("Response does not match", actualResult.getResponse().getContentAsString(), expectedResult);
        assertEquals("Response status code does not match", actualResult.getResponse().getStatus(), 400);
    }

    public static String stringAsJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Could not parse string to json. Error message: " + e.getMessage());
        }
    }
}
