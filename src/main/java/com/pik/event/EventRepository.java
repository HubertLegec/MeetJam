package com.pik.event;


import com.pik.common.InstrumentType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends MongoRepository<MusicEvent, String> {

    @Query("{owner : ?0 , date : { $gte : ?1, $lte : ?2}}")
    List<MusicEvent> findByOwnerAndDateBetween(String owner, LocalDateTime dateFrom, LocalDateTime dateTo);
    List<MusicEvent> findByOwner(String owner);
    @Query("{participants : ?0 , date : { $gte : ?1, $lte : ?2}}")
    List<MusicEvent> findByParticipantsInAndDateBetween(String participant, LocalDateTime dateFrom, LocalDateTime dateTo);
    List<MusicEvent> findByParticipantsIn(String owner);
    List<MusicEvent> findByInstrumentsNeededIn(InstrumentType instrumentNeeded);
    @Query("{ $and: "+
                "[" +
                "{$or : [ { $where: '?0 == null' }, { city : ?0 }]}," +
                "{$or : [ { $where: '?1 == null' }, { instrumentsNeeded : ?1 } ]}," +
                "{$or : [ { $or : [{$where: '?2 == null'}, {$where: '?3 == null'}] }, {date : { $gte : ?2, $lte : ?3} }]}" +
            "]}")
    List<MusicEvent> findByCityAndInstrumentNeededAndDateBetween(String city,
                                                                 InstrumentType instrumentNeeded,
                                                                 LocalDateTime dateFrom,
                                                                 LocalDateTime dateTo);
    MusicEvent findById(String id);
}