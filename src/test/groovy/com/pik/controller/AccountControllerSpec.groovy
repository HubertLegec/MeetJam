package com.pik.controller

import com.pik.base.IntegrationSpec
import com.pik.model.dto.AccountDTO
import com.pik.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Ignore
import spock.lang.Shared

class AccountControllerSpec extends IntegrationSpec {
    @Autowired
    private AccountController accountController;
    @Autowired
    private AccountRepository accountRepository;

    @Shared protected static final String login = 'test';
    @Shared protected static final String password = '!6adfKrpu';
    @Shared protected static final String email = 'test@gmail.com'

    def "account should have been sent in database"()
    {
        when:
        accountController.registerNewAccount(new AccountDTO(login: login, password: password, email: email))

        then:
        accountRepository.findByLogin(login) != null

        cleanup:
        accountRepository.deleteByLogin(login)

    }

}