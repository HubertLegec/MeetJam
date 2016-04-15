package com.pik.event;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;


@Document
public class MusicEvent {
    @Id
    private BigInteger id;
    String what;
    String where;

}
