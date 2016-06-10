package com.pik.account.profiledetails;

import com.pik.account.Account;
import com.pik.account.AccountRepository;
import com.pik.account.profiledetails.dto.ChangeEmailDTO;
import com.pik.account.profiledetails.dto.ChangePasswordDTO;
import com.pik.account.profiledetails.dto.ProfileDTO;
import com.pik.account.profiledetails.dto.UpdateDetailsDTO;
import com.pik.account.utils.CustomPasswordValidator;
import com.pik.common.InstrumentType;
import com.pik.security.TokenHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.ArrayList;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;


public class ProfileService {
    private AccountRepository accountRepository;
    private TokenHandler tokenHandler;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private CustomPasswordValidator passwordValidator = new CustomPasswordValidator();

    public ProfileService(AccountRepository accountRepository, TokenHandler tokenHandler) {
        this.accountRepository = accountRepository;
        this.tokenHandler = tokenHandler;
    }

    ProfileDTO getProfileDetails(String login) {
        Account account = accountRepository.findByLogin(login);
        if (account == null) {
            throw new LoginDoesNotExistException();
        }
        return new ProfileDTO(account);
    }

    void updateProfileDetails(String token, UpdateDetailsDTO dto) {
        String userName = getLoginFromToken(token);
        Account account = accountRepository.findByLogin(userName);
        updateChangedDetails(account, dto);
        accountRepository.save(account);
    }

    void changePassword(String token, ChangePasswordDTO dto) {
        String username = getLoginFromToken(token);
        Account account = accountRepository.findByLogin(username);
        validateOldPassword(dto.getOldPassword(), account);
        validateNewPassword(dto.getNewPassword());
        String hashedPassword = encoder.encode(dto.getNewPassword());
        account.setPassword(hashedPassword);
        accountRepository.save(account);
    }

    void changeEMail(String token, ChangeEmailDTO dto) {
        String username = getLoginFromToken(token);
        Account account = accountRepository.findByLogin(username);
        account.setEmail(dto.getEmail());
        accountRepository.save(account);
    }

    private void validateOldPassword(String oldPassword, Account account) {
        String accountPassword = account.getPassword();
        if (!encoder.matches(oldPassword, accountPassword)) {
            throw new InvalidPasswordException("Invalid old password", UNAUTHORIZED);
        }
    }

    private void validateNewPassword(String password) {
        if (!passwordValidator.validatePassword(password)) {
            throw new InvalidPasswordException("New password does not meet criteria", NOT_ACCEPTABLE);
        }
    }

    private String getLoginFromToken(String token) {
        try {
            return tokenHandler
                    .parseUserFromToken(token)
                    .getUsername();
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }

    private void updateChangedDetails(Account account, UpdateDetailsDTO dto) {
        if(dto.getDescription() != null){
            account.getDetails().setDescription(dto.getDescription());
        }
        if(dto.getUserInstruments() != null && dto.getUserInstruments().size() > 0) {
            account.getDetails().setUserInstruments(new ArrayList<>());
            dto.getUserInstruments().forEach( instrumentName ->
                    account.getDetails().addUserInstruments(InstrumentType.fromString(instrumentName))
            );
        }
        if(dto.getInstagramLogin() != null){
            account.getDetails().setInstagramLogin(dto.getInstagramLogin());
        }
        if(dto.getSkypeLogin() != null){
            account.getDetails().setSkypeLogin(dto.getSkypeLogin());
        }
        if(dto.getPhoneNumber() != null){
            account.getDetails().setPhoneNumber(dto.getPhoneNumber());
        }
    }
}
