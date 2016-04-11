package com.pik.acceptance

import com.pik.base.MvcIntegrationSpec
import com.pik.model.errors.InvalidRegisterParameterError
import com.pik.repository.AccountRepository
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Shared

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class RegistrationSpec extends MvcIntegrationSpec {
    @Autowired
    private AccountRepository accountRepository;

    @Shared protected static final String login = 'test';
    @Shared protected static final String email = 'test@gmail.com'
    @Shared protected static final String invalidPassword = '!';
    @Shared protected static final String password = 'A!aBc6j3h';


    def "user gives invalid password"(){
        when:
            ResultActions resultActions = registerUser(login,email,invalidPassword)

        then:
            getMessages(resultActions).contains(InvalidRegisterParameterError.INVALID_PASSWORD.message)

        cleanup:
            accountRepository.deleteByLogin(login)
    }

    def "user tries to create account, giving already existing login"(){
        given: "User with given password already exists."
            registerUser(login,email,password)

        when: "Another user registers with the same login."
            ResultActions resultActions = registerUser(login,email,invalidPassword)

        then:
            getMessages(resultActions).contains(InvalidRegisterParameterError.DUPLICATE_LOGIN.message)

        cleanup:
            accountRepository.deleteByLogin(login)
    }

    private ResultActions registerUser(String login, String email, String password) {
        ResultActions resultActions = mockMvc.perform(post('/api/account/register')
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
