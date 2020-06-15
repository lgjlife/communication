package com.communication.io.bio.protocol;


import lombok.Data;

@Data
public class Protocol {

    private static  long header = 0x1A2A3A4A;
    private int length;
    private long id;
    private long timestamp;
    private String version;
    private long crc;
    private Object payLoad;



}
