package com.pik.account.controller

import com.pik.account.AccountRepository
import com.pik.account.registration.AccountDTO
import com.pik.account.registration.RegistrationController
import com.pik.base.IntegrationSpec
import org.springframework.beans.factory.annotation.Autowired

class RegistrationControllerSpec extends IntegrationSpec {
    @Autowired
    private RegistrationController accountController;
    @Autowired
    private AccountRepository accountRepository;

    def 'registration should create account in database'() {
        given: 'there is no user with login in database'
        String login = 'test';
        String password = '!6adfKrpu';
        String email = 'test@gmail.com'
        accountRepository.findByLogin(login) == null
        when:
        accountController.registerNewAccount(new AccountDTO(login: login, password: password, email: email))

        then:
        accountRepository.findByLogin(login) != null

        cleanup:
        accountRepository.deleteByLogin(login)
    }

}