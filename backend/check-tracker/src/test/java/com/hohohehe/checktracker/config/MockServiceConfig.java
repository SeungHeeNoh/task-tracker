package com.hohohehe.checktracker.config;

import com.hohohehe.checktracker.service.CheckListService;
import com.hohohehe.checktracker.service.CheckLogService;
import com.hohohehe.checktracker.service.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockServiceConfig {
    @Bean
    public CheckListService checkListService() {
        return Mockito.mock(CheckListService.class);
    }

    @Bean
    public CheckLogService checkLogService() {
        return Mockito.mock(CheckLogService.class);
    }

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }
}
