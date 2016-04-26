package com.pik.base

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Ignore

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebAppConfiguration
@Ignore
class MvcIntegrationSpec extends IntegrationSpec {
    protected static final String LOGIN_URL = '/auth/login'
    protected static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    @Autowired
    protected WebApplicationContext webApplicationContext

    protected MockMvc mockMvc

    void setup() {
        ConfigurableMockMvcBuilder mockMvcBuilder = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        mockMvc = mockMvcBuilder.build()
    }

    protected static String extractTokenFromResponse(String response){
        def jsonResult = new JsonSlurper().parseText(response)
        return jsonResult.token
    }

    protected def sendLoginRequest(String login, String password){
        return mockMvc.perform(post(LOGIN_URL)
                .contentType(APPLICATION_JSON)
                .param('login', login)
                .param('password', password))
    }
}




