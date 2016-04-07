package com.pik.base

import com.pik.MeetJamApplication
import com.pik.model.Account
import com.pik.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

@SpringApplicationConfiguration(classes = MeetJamApplication)
@Ignore
class IntegrationSpec extends Specification {
    private static List<Account> testAccounts = [
            new Account('Adam', 'adam123', 'adam@gmail.com'),
            new Account('Zosia', 'zosia123', 'zosia@gmail.com')
    ]

    @Autowired
    AccountRepository accountRepository

    @Shared
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    void setup(){
        addSampleAccounts()
    }

    void cleanup(){
        testAccounts.each { acc ->
            accountRepository.deleteAll()
        }
    }



    protected addSampleAccounts(){
        testAccounts.each { acc ->
            acc.setPassword(encoder.encode(acc.getPassword()))
            accountRepository.save(acc)
        }
    }


}
