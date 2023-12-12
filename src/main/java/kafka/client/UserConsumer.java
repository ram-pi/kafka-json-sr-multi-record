package kafka.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import model.CloudUser;
import model.PlatformUser;
import org.apache.kafka.clients.consumer.*;

import java.util.List;
import java.util.Properties;

public class UserConsumer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group2");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer");
        props.put("schema.registry.url", "http://localhost:8081");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put("value.subject.name.strategy", "io.confluent.kafka.serializers.subject.RecordNameStrategy");
        props.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, JsonNode.class.getName());

        String topic = "users";
        final Consumer<String, JsonNode> consumer = new KafkaConsumer<String, JsonNode>(props);
        consumer.subscribe(List.of(topic));

        try {
            while (true) {
                ConsumerRecords<String, JsonNode> records = consumer.poll(100);
                for (ConsumerRecord<String, JsonNode> record : records) {
                    ObjectMapper mapper = new ObjectMapper();

                    if (record.headers().lastHeader("cloud") != null) {
                        CloudUser user = mapper.convertValue(record.value(), new TypeReference<CloudUser>() {
                        });
                        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), user);
                    } else {
                        PlatformUser user = mapper.convertValue(record.value(), new TypeReference<PlatformUser>() {
                        });
                        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), user);
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
