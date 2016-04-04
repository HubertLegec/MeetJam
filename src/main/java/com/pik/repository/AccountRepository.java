package com.pik.repository;

import com.pik.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

/**
 * Created by Hubert on 01.04.2016.
 */
public interface AccountRepository extends MongoRepository<Account, BigInteger> {
    Account findByLogin(String login);
}
