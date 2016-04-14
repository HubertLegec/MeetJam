package com.pik.utils

import spock.lang.Shared
import spock.lang.Specification

class CustomPasswordValidatorSpec extends Specification {
    @Shared CustomPasswordValidator validator

    def setup(){
        validator = new CustomPasswordValidator()
    }

    def 'validation of empty password string should fail'() {
        given:
            String password = ''
        when:
            boolean result = validator.validatePassword(password)
        then:
            result == false
    }

    def 'validation of password with number sequence should return false'() {
        given:
            String password = '!Abcdef123'
        when:
            boolean result = validator.validatePassword(password)
        then:
            result == false
    }

    def 'validation of password with qwerty sequence should return false'(){
        given:
            String password = 'qwerty!2Avd'
        when:
            boolean result = validator.validatePassword(password)
        then:
            result == false
            validator.getLastPasswordIssues() != null
            validator.getLastPasswordIssues().size() > 0
    }

    def 'validation of valid password should return true'(){
        given:
        String password = 'A1b4ls!^sad'
        when:
        boolean result = validator.validatePassword(password)
        then:
        result == true
        validator.getLastPasswordIssues() != null
        validator.getLastPasswordIssues().size() == 0
    }
}
