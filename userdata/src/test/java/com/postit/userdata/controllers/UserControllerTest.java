package com.postit.userdata.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postit.userdata.UserdataApplication;
import com.postit.userdata.models.Subreddit;
import com.postit.userdata.models.User;
import com.postit.userdata.models.UserSubs;
import com.postit.userdata.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserdataApplication.class)
@WithMockUser(username = "username")
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;
    List<User> userList = new ArrayList<>();

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        User u1 = new User("username", "password");
        u1.setUserid(1);

        userList.add(u1);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAll() throws Exception{
        String apiUrl = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList);
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList);
        assertEquals(er, tr);
    }

    @Test
    public void getMyInfo() throws Exception{
        String apiUrl = "/users/myinfo";
        Mockito.when(userService.getCurrentUserInfo()).thenReturn(userList.get(0));
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(0));
        assertEquals(er, tr);
    }

    @Test
    public void updateUserFull() throws Exception {
        String apiUrl = "/users/user/1";
        User b3 = new User("meep-morp", "password");

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(b3);

        Mockito.when(userService.save(any(User.class))).thenReturn(b3);
        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(userString);
        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUser() throws Exception{
        String apiUrl = "/users/user/1";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl);
        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void saveSub() throws Exception{
        String apiUrl = "/users/user/1";
        User b3 = new User("meep-morp", "password");
        Subreddit s1 = new Subreddit("Title", "Desc");
        b3.getUsersubs().add(new UserSubs(b3, s1));

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(b3);

        Mockito.when(userService.save(any(User.class))).thenReturn(b3);
        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(userString);
        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}