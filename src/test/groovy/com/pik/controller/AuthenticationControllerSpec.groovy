package com.pik.controller

import com.pik.base.MvcIntegrationSpec
import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthenticationControllerSpec extends MvcIntegrationSpec{

    public static final String LOGIN_URL = '/auth/login'

    def 'success login when user exist in database'(){
        when:
        def response = mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param('login', 'Adam')
                .param('password', 'adam123'))
        then:
        response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
    }


}
