package com.pik.service;

import com.pik.model.Account;
import com.pik.model.dto.AccountDTO;
import com.pik.repository.AccountRepository;
import edu.vt.middleware.password.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;


    public String createNewAccount(AccountDTO accountDTO){
        if(accountRepository.findByLogin(accountDTO.getLogin())!=null)
            return "User with this login already exists!";

        if(!EmailValidator.getInstance().isValid(accountDTO.getEmail()))
            return "User email is invalid!";

        PasswordValidator validator = getPasswordValidator();
        PasswordData passwordData = new PasswordData(new Password(accountDTO.getPassword()));
        RuleResult result = validator.validate(passwordData);
        if (!result.isValid()) {
            return String.join("\n",validator.getMessages(result));
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Account newAccount = new Account(accountDTO.getLogin(), encoder.encode(accountDTO.getPassword()), accountDTO.getEmail());
        accountRepository.save(newAccount);

        return "Success";
    }



    private PasswordValidator getPasswordValidator()
    {
        // password must be between 8 and 16 chars long
        LengthRule lengthRule = new LengthRule(8, 16);
        // don't allow whitespace
        WhitespaceRule whitespaceRule = new WhitespaceRule();
        // control allowed characters
        CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
        // require at least 1 digit in passwords
        charRule.getRules().add(new DigitCharacterRule(1));
        // require at least 1 non-alphanumeric char
        charRule.getRules().add(new NonAlphanumericCharacterRule(1));
        // require at least 1 upper case char
        charRule.getRules().add(new UppercaseCharacterRule(1));
        // require at least 1 lower case char
        charRule.getRules().add(new LowercaseCharacterRule(1));
        // require at least 3 of the previous rules be met
        charRule.setNumberOfCharacteristics(3);
        // don't allow alphabetical sequences
        AlphabeticalSequenceRule alphaSeqRule = new AlphabeticalSequenceRule();
        // don't allow numerical sequences of length 3
        NumericalSequenceRule numSeqRule = new NumericalSequenceRule(3,true);
        // don't allow qwerty sequences
        QwertySequenceRule qwertySeqRule = new QwertySequenceRule();
        // don't allow 4 repeat characters
        RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(4);
        // group all rules together in a List
        List<Rule> ruleList = new ArrayList<Rule>();
        ruleList.add(lengthRule);
        ruleList.add(whitespaceRule);
        ruleList.add(charRule);
        ruleList.add(alphaSeqRule);
        ruleList.add(numSeqRule);
        ruleList.add(qwertySeqRule);
        ruleList.add(repeatRule);
        return new PasswordValidator(ruleList);
    }

}
