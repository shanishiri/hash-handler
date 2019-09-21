package com.soliduslabs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soliduslabs.dao.DynamoDbClient;
import com.soliduslabs.exceptions.ResourceNotFoundException;
import com.soliduslabs.model.HashMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class HashControllerTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    private MockMvc mockMvc;
    private final String HASH_KEY = "fcde2b2edba56bf408601fb721fe9b5c338d10ee429ea04fae5511b68fbf8fb9";

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HashController(dynamoDbClient))
                .build();
    }

    @Test
    public void testGetHashByString() throws Exception {
        String url = "/messages";
        MvcResult actualResult = getPostResult(url);
        assertEquals("Response does not match", actualResult.getResponse().getContentAsString(), HASH_KEY);
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

    @Test
    public void testGetMessageByHash() throws Exception {
        HashMessage hashMessage = new HashMessage(HASH_KEY, "bar");
        when(dynamoDbClient.getStringByHashKey(HASH_KEY)).thenReturn(hashMessage);

        String getUrl = "/messages/" + HASH_KEY;
        MvcResult actualResult = mockMvc.perform(MockMvcRequestBuilders
                .get(getUrl)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Response does not match", actualResult.getResponse().getContentAsString(), "bar");
    }

    @Test
    public void testFailedToGetMessageByHash() throws Exception {
        when(dynamoDbClient.getStringByHashKey(HASH_KEY)).thenReturn(null);

        String getUrl = "/messages/" + HASH_KEY;
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .get(getUrl)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andReturn();
        } catch (Exception e) {
            assertTrue(e instanceof ResourceNotFoundException);
        }
    }

    private MvcResult getPostResult(String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(stringAsJson(new HashMessage("bar")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    public static String stringAsJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Could not parse string to json. Error message: " + e.getMessage());
        }
    }
}
