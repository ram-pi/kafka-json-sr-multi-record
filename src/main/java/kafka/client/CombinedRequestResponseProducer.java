package kafka.client;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import model.avro.Request;
import model.avro.RequestResponse;
import model.avro.Response;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class CombinedRequestResponseProducer {

    public static String topic = "request-response";

    @SneakyThrows
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put("schema.registry.url", "http://127.0.0.1:8081");
        //props.put("value.subject.name.strategy", "io.confluent.kafka.serializers.subject.RecordNameStrategy");
        props.put("value.subject.name.strategy", io.confluent.kafka.serializers.subject.TopicNameStrategy.class);

        Producer<String, RequestResponse> producer = new KafkaProducer<String, RequestResponse>(props);

        Faker faker = new Faker();

        // Produce Requests
        for (int i = 0; i < faker.number().numberBetween(1, 10); i++) {

            int id = faker.number().numberBetween(1, 100);
            int messageType = faker.number().numberBetween(1, 10);
            String users = faker.internet().emailAddress();
            Request rrequest = Request.newBuilder()
                    .setRequestId(id)
                    .setMessageType(messageType)
                    .setUsers(users)
                    .build();

            RequestResponse rr = RequestResponse.newBuilder().setRequestResponse(rrequest).build();

            ProducerRecord<String, RequestResponse> record
                    = new ProducerRecord<String, RequestResponse>(topic, String.valueOf(rrequest.getMessageType()), rr);
            producer.send(record).get();
        }

        // Produce Responses
        for (int i = 0; i < faker.number().numberBetween(1, 10); i++) {

            int id = faker.number().numberBetween(1, 100);
            int responseCount = faker.number().numberBetween(1, 10);
            // faker string between ["200", "500", "404"]
            String responseCode = faker.options().option("200", "500", "404");
            Response rresponse = Response.newBuilder()
                    .setRequestId(id)
                    .setResponseCode(responseCode)
                    .setResponseCount(responseCount)
                    .setReasonCode("---")
                    .build();

            RequestResponse rr = RequestResponse.newBuilder().setRequestResponse(rresponse).build();

            ProducerRecord<String, RequestResponse> record
                    = new ProducerRecord<String, RequestResponse>(topic, String.valueOf(rresponse.getResponseCode()), rr);
            producer.send(record).get();
        }
        producer.close();
    }
}
