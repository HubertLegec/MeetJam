package com.pik.account.acceptance

import com.pik.account.Account
import com.pik.account.AccountRepository
import com.pik.account.profiledetails.AccountDetails
import com.pik.account.profiledetails.dto.ChangeEmailDTO
import com.pik.account.profiledetails.dto.ChangePasswordDTO
import com.pik.account.profiledetails.dto.UpdateDetailsDTO
import com.pik.base.MvcIntegrationSpec
import com.pik.common.InstrumentType
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Shared

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ProfileSpec extends MvcIntegrationSpec {
    private static final String PROFILE_DETAILS_URL = '/api/account/details'
    private static final String CHANGE_EMAIL_URL = '/api/account/changeEmail'
    private static final String CHANGE_PASSWORD_URL = '/api/account/changePassword'
    private static final UpdateDetailsDTO DETAILS_DTO = new UpdateDetailsDTO(['Drums'], 'skypeLogin', 'instaLogin', '123456789', 'Description')

    @Autowired
    private AccountRepository accountRepository
    @Shared
    private String token
    @Shared
    private String login = testAccounts[1].username

    def setup() {
        def response = sendLoginRequest(
                testAccounts[1].username,
                testAccounts[1].password)
        token = extractTokenFromResponse(response.andReturn().response.contentAsString)
    }

    def cleanup() {
        accountRepository.deleteAll()
    }

    def 'user profile details should be returned after request'() {
        given: 'account with details in database'
            Account account = accountRepository.findByLogin(login)
            account.details.setDescription(DETAILS_DTO.description)
            account.details.setInstagramLogin(DETAILS_DTO.instagramLogin)
            account.details.setSkypeLogin(DETAILS_DTO.skypeLogin)
            account.details.setPhoneNumber(DETAILS_DTO.phoneNumber)
            DETAILS_DTO.getUserInstruments().each { instrument ->
                account.details.addUserInstruments(InstrumentType.fromString(instrument))
            }
            accountRepository.save(account)
        when:
            def response = sendProfileDetailsRequest(login)
        then:
            response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
            responseProfileDetailsMatchDto(response, DETAILS_DTO)
    }

    def 'profile details should change in database after user update'() {
        given: 'user account in database with some details'
            Account account = accountRepository.findByLogin(login)
            account.details.setPhoneNumber('888777666')
            accountRepository.save(account)
        when: 'user updates profiledetails details'
            def response = sendUpdateDetailsRequest(DETAILS_DTO)
        then: 'details in database have changed'
            response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
            accountDetailsMatchDto(login, DETAILS_DTO)
    }

    def 'after user request e-mail in database should change'() {
        given: 'user account in database with some e-mail address'
            Account account = accountRepository.findByLogin(login)
            account.setEmail('abc@gmail.com')
            accountRepository.save(account)
        when: 'user updates e-mail address'
            def response = sendChangeEmailRequest(new ChangeEmailDTO('cde@gmail.com'))
        then: 'e-mail in database has changed'
            response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
            String updatedEmail = accountRepository.findByLogin(login).getEmail()
            updatedEmail == 'cde@gmail.com'
    }

    def 'after user request password in database should change'() {
        given: 'user account with some password'
            String password = accountRepository.findByLogin(login).getPassword()
        when: 'user updates password'
            def response = sendChangePasswordRequest(new ChangePasswordDTO('!12abCD34', 'zosia123'))
        then: 'password in database has changed'
            response.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
            password != accountRepository.findByLogin(login).getPassword()
    }

    private def sendProfileDetailsRequest(String login) {
        mockMvc.perform(get(PROFILE_DETAILS_URL)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .param('login', login))
    }

    private def sendUpdateDetailsRequest(UpdateDetailsDTO dto) {
        def json = JsonOutput.toJson(dto)
        mockMvc.perform(post(PROFILE_DETAILS_URL)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .content(json))
    }

    private def sendChangeEmailRequest(ChangeEmailDTO dto) {
        def json = JsonOutput.toJson(dto)
        mockMvc.perform(post(CHANGE_EMAIL_URL)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .content(json))
    }

    private def sendChangePasswordRequest(ChangePasswordDTO dto) {
        def json = JsonOutput.toJson(dto)
        mockMvc.perform(post(CHANGE_PASSWORD_URL)
                .contentType(APPLICATION_JSON)
                .header(AUTH_HEADER_NAME, token)
                .content(json))
    }

    private boolean responseProfileDetailsMatchDto(ResultActions response, UpdateDetailsDTO dto) {
        def jsonResult = new JsonSlurper()
                .parseText(response
                .andReturn()
                .response
                .contentAsString)
        if (dto.phoneNumber != jsonResult.phoneNumber ||
            dto.skypeLogin != jsonResult.skypeLogin ||
            dto.instagramLogin != jsonResult.instagramLogin ||
            dto.description != jsonResult.description) {
            return false
        }
        dto.userInstruments.each { instrumentName ->
            if (!jsonResult.userInstruments.contains(instrumentName)) {
                return false
            }
        }
        return true
    }

    private boolean accountDetailsMatchDto(String login, UpdateDetailsDTO dto) {
        Account account = accountRepository.findByLogin(login)
        AccountDetails details = account.details
        if (details.phoneNumber != dto.phoneNumber ||
            details.instagramLogin != dto.instagramLogin ||
            details.skypeLogin != dto.skypeLogin ||
            details.description != dto.description) {
            return false
        }
        dto.userInstruments.each { instrumentName ->
            if (!details.userInstruments.contains(InstrumentType.fromString(instrumentName))) {
                return false
            }
        }
        return true
    }
}
