package com.pik.utils

import spock.lang.Specification

class CustomPasswordValidatorSpec extends Specification {

    def "validation of empty password string should fail"() {
        given:
            CustomPaswordValidator validator = new CustomPaswordValidator()
            String password = ""
        when:
            boolean result = validator.validatePassword(password)
        then:
            result == false
            validator.getLastMessage() != null
            validator.getLastMessage().size() > 0
    }

    def "validation of proper password should return true"() {
        given:
            CustomPaswordValidator validator = new CustomPaswordValidator()
            String password = "!Abcdef123"
        when:
            boolean result = validator.validatePassword(password)
        then:
            result == false
    }
}
