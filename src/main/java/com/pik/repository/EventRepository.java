package com.pik.repository;

/**
 * Created by Hubert on 01.04.2016.
 */
import com.pik.model.MusicEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface EventRepository extends MongoRepository<MusicEvent, BigInteger> {}