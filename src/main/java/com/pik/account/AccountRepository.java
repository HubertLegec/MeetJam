package com.pik.account;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;


public interface AccountRepository extends MongoRepository<Account, String> {
    Account findByLogin(String login);

    void deleteByLogin(String login);
}
