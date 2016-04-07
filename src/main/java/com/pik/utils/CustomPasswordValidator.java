package com.pik.utils;

import edu.vt.middleware.password.*;

import java.util.ArrayList;
import java.util.List;

public class CustomPasswordValidator {
    private PasswordValidator validator;
    private List<String> passwordIssues;

    public CustomPasswordValidator(){
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
        List<Rule> ruleList = new ArrayList<>();
        ruleList.add(lengthRule);
        ruleList.add(whitespaceRule);
        ruleList.add(charRule);
        ruleList.add(alphaSeqRule);
        ruleList.add(numSeqRule);
        ruleList.add(qwertySeqRule);
        ruleList.add(repeatRule);
        validator = new PasswordValidator(ruleList);
    }

    public boolean validatePassword(String password){
        RuleResult result = validator.validate(new PasswordData(new Password(password)));
        passwordIssues = validator.getMessages(result);
        return result.isValid();
    }

    public List<String> getLastPasswordIssues()
    {
        return passwordIssues;
    }
}
