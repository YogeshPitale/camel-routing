package com.learncamel.file;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class InsertProcessor implements Processor {
    static int id=0;
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Message Received" + exchange.getIn().getBody());
        exchange.getIn().setBody("["+exchange.getIn().getBody()+"]");
    }
}
