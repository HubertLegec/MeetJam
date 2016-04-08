package com.pik.controller

import com.pik.base.MvcIntegrationSpec
import com.pik.model.dto.AuthenticationResponseDTO
import com.pik.model.dto.ResultMessageDTO
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthenticationControllerIntegrSpec extends MvcIntegrationSpec{

    public static final String LOGIN_URL = '/auth/login'
    public static final String PING_URL = '/ping'
    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    def 'access to secured url with valid token should be successful'(){
        when:
        def response = mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param('login', 'Zosia')
                .param('password', 'zosia123'))
        String body = response.andReturn().response.contentAsString
        AuthenticationResponseDTO responseDTO = mapper.readValue(body, AuthenticationResponseDTO.class)

        def response2 = mockMvc.perform(get(PING_URL).header(AUTH_HEADER_NAME, responseDTO.token)
                .header('Content-type', 'application/json'))

        then:
        response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        responseDTO.messages.size() == 0
        responseDTO.token != null
        responseDTO.token.size() > 0

        response2
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        ResultMessageDTO response2DTO = mapper.readValue(response2.andReturn().response.contentAsString, ResultMessageDTO.class)

        response2DTO.messages.first() == 'PONG'
    }

    def 'login fails when user not exist in database'(){
        when:
        def response = mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param('login', 'Tomek')
                .param('password', 'tomek123'))

        then:
        response
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        String body = response.andReturn().response.contentAsString
        AuthenticationResponseDTO responseDTO = mapper.readValue(body, AuthenticationResponseDTO.class)

        responseDTO.messages.size() > 0
        responseDTO.token == null
    }

    def 'login fails when user in database but password is incorrect'(){
        when:
        def response = mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param('login', 'Zosia')
                .param('password', 'dsfgdfs123'))

        then:
        response
                .andExpect(status().is(401))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        String body = response.andReturn().response.contentAsString
        AuthenticationResponseDTO responseDTO = mapper.readValue(body, AuthenticationResponseDTO.class)

        responseDTO.messages.size() > 0
        responseDTO.token == null
    }
}
