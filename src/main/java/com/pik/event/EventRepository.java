package com.pik.event;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.List;

public interface EventRepository extends MongoRepository<MusicEvent, BigInteger> {
    List<MusicEvent> findByOwner(String owner);
}