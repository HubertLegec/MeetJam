package com.pik.event;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface EventRepository extends MongoRepository<MusicEvent, BigInteger> {}