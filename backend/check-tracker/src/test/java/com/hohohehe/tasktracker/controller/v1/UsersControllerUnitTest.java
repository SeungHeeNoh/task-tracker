package com.hohohehe.tasktracker.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohohehe.tasktracker.config.SecurityConfig;
import com.hohohehe.tasktracker.config.jwt.JWTAuthenticationFilter;
import com.hohohehe.tasktracker.common.WithMockCustomUser;
import com.hohohehe.tasktracker.model.dto.UserProfile;
import com.hohohehe.tasktracker.model.dto.request.ModifyUserRequest;
import com.hohohehe.tasktracker.service.UsersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsersController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
class UsersControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsersService usersService;

    @Test
    @DisplayName("사용자 정보 수정 - 성공")
    @WithMockCustomUser
    void modifyUser_Success() throws Exception {
        ModifyUserRequest request = new ModifyUserRequest("newName", "newAvatar");

        mockMvc.perform(post("/api/v1/users/{userSeq}/modify", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SC"));
    }
}
