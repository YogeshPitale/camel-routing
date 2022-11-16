package com.learncamel.file;

import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.SimpleRegistry;
import org.apache.camel.support.jndi.JndiBeanRepository;

public class kafkaToMongoDBRoute extends RouteBuilder {

    String topicName = "events";
    String brokers="localhost:9092";
    String zooKeeperHost = "zookeeperHost=localhost&zookeeperPort=2181";
    String serializerClass = "serializerClass=kafka.serializer.StringEncoder";


    @Override
    public void configure() throws Exception {
        String fromKafkaToMongo = new StringBuilder().append("kafka:").append(topicName).append("?")
                .append("brokers=").append(brokers).append("&groupId=").append("CamelConsumerMongo").toString();

        String fromKafkaToPython = new StringBuilder().append("kafka:").append(topicName).append("?")
                .append("brokers=").append(brokers).append("&groupId=").append("CamelConsumerPython").toString();

        try {
            from(fromKafkaToMongo)
                    .process(new InsertProcessor())
                    .unmarshal(new ListJacksonDataFormat())
                    .to("mongodb:mongoClient?database=local&collection=events&operation=insert")
                    .to("log:?level=INFO&showBody=true");

            from(fromKafkaToPython)
                    .to("log:?level=INFO&showBody=true")
                    .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                    .to("http://127.0.0.1:8000/event/");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
