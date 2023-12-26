package kafka.client;

import com.github.javafaker.Faker;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.*;

import java.util.List;
import java.util.Properties;

public class GenericCombinedRequestResponseConsumer {

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

        final Consumer<String, GenericRecord> consumer = new KafkaConsumer<String, GenericRecord>(props);
        consumer.subscribe(List.of(topic));

        try {
            while (true) {
                ConsumerRecords<String, GenericRecord> records = consumer.poll(100);
                for (ConsumerRecord<String, GenericRecord> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
