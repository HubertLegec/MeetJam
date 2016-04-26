package com.pik.account.acceptance

import com.pik.base.MvcIntegrationSpec
import groovy.json.JsonSlurper
import org.springframework.test.web.servlet.ResultActions

import static com.pik.account.authentication.InvalidLoginParametersError.INVALID_PASSWORD
import static com.pik.account.authentication.InvalidLoginParametersError.NON_EXISTING_LOGIN
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.http.MediaType.TEXT_PLAIN
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthenticationSpec extends MvcIntegrationSpec{

    public static final String PING_URL = '/ping'


    def 'login with valid credentials should be successful'(){
        given: 'account in database'
            def acc = accountRepository.findByLogin('Zosia')
            acc.getUsername() == 'Zosia'
        when: 'login request sent'
            def response = sendLoginRequest('Zosia', 'zosia123')
        then: 'token returned'
            response
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
            responseContainsToken(response.andReturn().response.contentAsString)
    }

    def 'access to secured url with valid token should be successful'(){
        given: 'valid token'
            def loginResponse = sendLoginRequest('Zosia', 'zosia123')
            def token = extractTokenFromResponse(loginResponse.andReturn().response.contentAsString)
        when: 'request to secured url sent'
            def response = mockMvc.perform(get(PING_URL).header(AUTH_HEADER_NAME, token)
                .header('Content-type', 'application/json'))
        then: 'http status ok returned'
            response
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_PLAIN))
    }

    def 'login fails when user does not exist in database'(){
        given: 'there is no account with login Tomek in database'
            accountRepository.findByLogin('Tomek') == null
        when: 'login request sent'
            def response = sendLoginRequest('Token', 'tomek123')
        then: 'http status UNAUTHORIZED and error message returned'
            response
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(APPLICATION_JSON))
            getMessages(response).contains(NON_EXISTING_LOGIN.message)
    }

    def 'login fails when user in database but password is incorrect'(){
        given:
            accountRepository.findByLogin('Zosia') != null
        when:
            def response = sendLoginRequest('Zosia', 'dsfgdfs123')
        then:
            response
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(APPLICATION_JSON))
            getMessages(response).contains(INVALID_PASSWORD.message)
    }

    private static boolean responseContainsToken(String response){
        def token = extractTokenFromResponse(response)
        return token != null && token.size() > 0
    }

    private List<String> getMessages(ResultActions resultActions) {
        def jsonResult = new JsonSlurper().parseText(resultActions.andReturn().response.getContentAsString())
        return jsonResult.messages
    }
}
