package com.communication.io.charset;


import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.SortedMap;
import java.util.regex.Pattern;

@Slf4j
public class CharSetDemo {

    private static  String charset = "UTF-8";

    private static  CharsetEncoder charsetEncoder = Charset.forName(charset).newEncoder();
    private static  CharsetDecoder charsetDecoder = Charset.forName(charset).newDecoder();



    public static void main(String args[]){
       // listCharset();
        try{


            encodeAndDecode();
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }
    }


    public static void listCharset(){
        SortedMap<String,Charset> CharsetMap = Charset.availableCharsets() ;

        CharsetMap.forEach((key,charset)->{

            log.info("{} &&  {} ",key,charset.toString());
        });

    }

    public static  void encodeAndDecode() throws Exception{

        CharBuffer charBuffer = CharBuffer.wrap("史蒂夫法f");
        ByteBuffer byteBuffer =  charsetEncoder.encode(charBuffer);


        CharBuffer decode = charsetDecoder.decode(byteBuffer);

        log.info("decode = " + decode);

    }
}
