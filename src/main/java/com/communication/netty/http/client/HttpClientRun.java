package com.communication.netty.http.client;

public class HttpClientRun {

    public static void main(String args[]){

        HttpClient client = new HttpClient();
        client.connect("127.0.0.1",8445);
        client.send("12dfghfgfsgsf gs12");

    }
}
