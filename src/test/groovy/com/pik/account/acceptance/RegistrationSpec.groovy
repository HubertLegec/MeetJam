package com.pik.account.acceptance

import com.pik.account.AccountRepository
import com.pik.account.registration.InvalidRegisterParameterError
import com.pik.base.MvcIntegrationSpec
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class RegistrationSpec extends MvcIntegrationSpec {
    @Autowired
    private AccountRepository accountRepository;

    private static final String REGISTRATION_URL = '/api/account/register'

    def 'should return http status ok, when user gives valid registration parameters'(){
        given: 'database with no such user in database'
            def login = 'Wojtek'
            def password = '90mNba12@'
            def email = 'wojtek@gmail.com'
            accountRepository.findByLogin(login) == null
        when: 'register new account'
            def result = registerUser(login, email, password)
        then: 'http status ok is returned'
            result.andExpect(status().isOk())
    }

    def 'should return invalid password message when user gives invalid password'(){
        given:
            def invalidPassword = '!';
            def login = 'test';
            def email = 'test@gmail.com'
        when: 'user gives invalid password'
            def resultActions = registerUser(login, email, invalidPassword)
        then: 'response contains message that inform about invalid password'
            getMessages(resultActions).contains(InvalidRegisterParameterError.INVALID_PASSWORD.message)
    }

    def 'should return incorrect login message when user tries to create account, giving already existing login'(){
        given: 'user with given password already exists'
            def login = 'Zosia'
            def password = '!23uYt45'
            def email = 'zosia12@gmail.com'
            accountRepository.findByLogin(login) != null
        when: 'another user tries to register with the same login'
            def resultActions = registerUser(login,email,password)

        then: 'response contains message that inform about duplicated login'
            getMessages(resultActions).contains(InvalidRegisterParameterError.DUPLICATE_LOGIN.message)
    }

    private ResultActions registerUser(String login, String email, String password) {
        ResultActions resultActions = mockMvc.perform(post(REGISTRATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("login", login)
                .param("password", password)
                .param("email", email))
        return resultActions
    }

    private List<String> getMessages(ResultActions resultActions) {
        def jsonResult = new JsonSlurper().parseText(resultActions.andReturn().response.getContentAsString())
        return jsonResult.messages
    }
}
