package com.learncamel.file;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.SimpleRegistry;

import java.util.Set;


public class CopyFileCamel {

    public static void main(String[] args) {

        SimpleRegistry reg = new SimpleRegistry();
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        reg.bind("mongoClient", mongoClient);
        CamelContext context = new DefaultCamelContext(reg);

        try {
            /*context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("file:data/input?noop=true")
                            .to("log:?level=INFO&showBody=true&showHeaders=true")
                            .to("file:data/output?");
                }
            });*/
            context.addRoutes(new kafkaToMongoDBRoute());
            context.start();
            Thread.sleep(6000000);
            context.stop();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
