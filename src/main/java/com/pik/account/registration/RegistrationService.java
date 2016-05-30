package com.pik.account.registration;

import com.pik.account.Account;
import com.pik.account.AccountRepository;
import com.pik.account.utils.CustomPasswordValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;



public class RegistrationService {
    private AccountRepository accountRepository;
    private CustomPasswordValidator passwordValidator = new CustomPasswordValidator();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public RegistrationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createNewAccount(AccountDTO accountDTO){
        validate(accountDTO);
        String hashedPassword = generateEncodedPassword(accountDTO.getPassword());
        Account newAccount = new Account(
                accountDTO.getLogin(),
                hashedPassword,
                accountDTO.getEmail()
        );
        return accountRepository.save(newAccount);
    }

    private void validate(AccountDTO accountDTO) {
        List<InvalidRegisterParameterError> errors = new ArrayList<>();
        if(accountRepository.findByLogin(accountDTO.getLogin()) != null) {
            errors.add(InvalidRegisterParameterError.DUPLICATE_LOGIN);
        }

        if(!EmailValidator.getInstance().isValid(accountDTO.getEmail())) {
            errors.add(InvalidRegisterParameterError.INVALID_EMAIL);
        }

        if (!passwordValidator.validatePassword(accountDTO.getPassword())) {
            errors.add(InvalidRegisterParameterError.INVALID_PASSWORD);
        }

        if(!errors.isEmpty()) {
            throw new InvalidRegisterParametersException(errors, passwordValidator.getLastPasswordIssues());
        }
    }

    public String generateEncodedPassword(String passwordToEncode){
        return encoder.encode(passwordToEncode);
    }

}
