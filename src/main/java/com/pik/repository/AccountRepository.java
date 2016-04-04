package com.pik.repository;

import com.pik.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;


public interface AccountRepository extends MongoRepository<Account, BigInteger> {
    Account findByLogin(String login);
}
