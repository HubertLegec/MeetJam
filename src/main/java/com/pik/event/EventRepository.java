package com.pik.event;


import com.pik.common.InstrumentType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.List;

public interface EventRepository extends MongoRepository<MusicEvent, BigInteger> {
    List<MusicEvent> findByOwner(String owner);
    List<MusicEvent> findByCity(String city);
    List<MusicEvent> findByParticipantsIn(String participant);
    List<MusicEvent> findByInstrumentsNeededIn(InstrumentType instrumentNeeded);
}