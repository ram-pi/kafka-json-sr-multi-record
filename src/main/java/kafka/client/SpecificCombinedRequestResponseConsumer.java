package kafka.client;

import com.github.javafaker.Faker;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import model.avro.RequestResponse;
import org.apache.kafka.clients.consumer.*;

import java.util.List;
import java.util.Properties;

public class SpecificCombinedRequestResponseConsumer {

    public static String topic = "request-response";

    public static void main(String[] args) {
        Faker faker = new Faker();
        // generate random alphanumeric string
        String randomString = faker.regexify("[a-z1-9]{10}");

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, randomString);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        props.put("schema.registry.url", "http://localhost:8081");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //props.put("value.subject.name.strategy", "io.confluent.kafka.serializers.subject.RecordNameStrategy");
        props.put("value.subject.name.strategy", io.confluent.kafka.serializers.subject.TopicNameStrategy.class);

        // Specific avro deserializer
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        final Consumer<String, RequestResponse> consumer = new KafkaConsumer<String, RequestResponse>(props);
        consumer.subscribe(List.of(topic));

        try {
            while (true) {
                ConsumerRecords<String, RequestResponse> records = consumer.poll(100);
                for (ConsumerRecord<String, RequestResponse> record : records) {
                    // if record.value() is an instance of Request class, then we print the request object
                    // else if record.value() is an instance of Response class, then we print the response object
                    if (record.value().getRequestResponse() instanceof model.avro.Request) {
                        System.out.println("--- Request ---");
                        System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());
                    } else if (record.value().getRequestResponse() instanceof model.avro.Response) {
                        System.out.println("--- Response ---");
                        System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
