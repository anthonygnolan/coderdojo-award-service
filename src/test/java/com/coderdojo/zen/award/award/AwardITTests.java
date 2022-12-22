package com.coderdojo.zen.award.award;

import com.coderdojo.zen.award.Award;
import com.coderdojo.zen.award.AwardRepository;
import com.coderdojo.zen.award.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AwardITTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        awardRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void givenAwardObject_whenCreateAward_thenReturnSavedAward() throws Exception{

        // given - precondition or setup
        Award award = new Award(1L,"MacBook Pro", Status.IN_PROGRESS);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/v1/awards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(award)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.description",
                        is(award.getDescription())))
                .andExpect(jsonPath("$.status",
                        is(award.getStatus().toString())));

    }

    // JUnit test for Get All awards REST API
    @Test
    @Order(2)
    public void givenListOfAwards_whenGetAllAwards_thenReturnAwardsList() throws Exception{
        // given - precondition or setup
        List<Award> listOfAwards = new ArrayList<>();
        listOfAwards.add(new Award(1L,"MacBook Pro", Status.IN_PROGRESS));
        listOfAwards.add(new Award(2L,"iPhone", Status.IN_PROGRESS));
        awardRepository.saveAll(listOfAwards);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/awards"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfAwards.size())));

    }

    // positive scenario - valid award id
    // JUnit test for GET award by id REST API
    @Test
    @Order(3)
    public void givenAwardId_whenGetAwardById_thenReturnAwardObject() throws Exception{
        // given - precondition or setup
        Award award = new Award(6L,"MacBook Pro", Status.IN_PROGRESS);
        awardRepository.save(award);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/awards/{id}", award.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.description",
                        is(award.getDescription())))
                .andExpect(jsonPath("$.status",
                        is(award.getStatus().toString())));

    }

    // negative scenario - valid award id
    // JUnit test for GET award by id REST API
    @Test
    @Order(4)
    public void givenInvalidAwardId_whenGetAwardById_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        long awardId = 11L;
        Award savedAward = new Award(11L,"MacBook Pro", Status.IN_PROGRESS);
        awardRepository.save(savedAward);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/v1/awards/{id}", savedAward.getId()));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // JUnit test for update award REST API - positive scenario
    @Test
    @Order(5)
    public void givenUpdatedAward_whenUpdateAward_thenReturnUpdateAwardObject() throws Exception{
        // given - precondition or setup
        Award savedAward = new Award(4L,"iPhone", Status.IN_PROGRESS);
        awardRepository.save(savedAward);

        Award updatedAward = new Award(4L,"iPhone", Status.COMPLETED);
        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/v1/awards/{id}", savedAward.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAward)));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.description",
                        is(updatedAward.getDescription())))
                .andExpect(jsonPath("$.status",
                        is(updatedAward.getStatus().toString())));
    }

    // JUnit test for update award REST API - negative scenario
    @Test
    @Order(6)
    public void givenUpdatedAward_whenUpdateAward_thenReturn404() throws Exception{
        // given - precondition or setup
        long awardId = 9L;
        Award savedAward = new Award(9L,"iPhone", Status.IN_PROGRESS);
        awardRepository.save(savedAward);

        Award updatedAward = new Award(9L,"iPhone", Status.COMPLETED);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/v1/awards/{id}", awardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAward)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for delete award REST API
    @Test
    @Order(7)
    public void givenAwardId_whenDeleteAward_thenReturn200() throws Exception{
        // given - precondition or setup
        Award savedAward = new Award(5L,"MacBook Pro", Status.IN_PROGRESS);
        awardRepository.save(savedAward);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/v1/awards/{id}", savedAward.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
