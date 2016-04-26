package com.pik.account.utils

import spock.lang.Shared
import spock.lang.Specification

class CustomPasswordValidatorSpec extends Specification {
    @Shared CustomPasswordValidator validator

    def setup(){
        validator = new CustomPasswordValidator()
    }

    def 'password variants validation'(String password, boolean result) {
        expect:
            validator.validatePassword(password) == result
        where:
            password      | result
            ''            | false
            '!Abcdef123'  | false
            'qwerty!2Avd' | false
            'A1b4ls!^sad' | true

    }
}
