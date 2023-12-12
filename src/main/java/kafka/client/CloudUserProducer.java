package kafka.client;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import model.CloudUser;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CloudUserProducer {

    @SneakyThrows
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer");
        props.put("schema.registry.url", "http://127.0.0.1:8081");
        props.put("value.subject.name.strategy", "io.confluent.kafka.serializers.subject.RecordNameStrategy");

        Producer<String, CloudUser> producer = new KafkaProducer<String, CloudUser>(props);

        String topic = "users";
        Faker faker = new Faker();

        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("cloud", "cloud".getBytes()));


        for (int i = 0; i < faker.number().numberBetween(1, 10); i++) {
            CloudUser user = new CloudUser(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    (short) faker.number().numberBetween(18, 99),
                    faker.internet().emailAddress(),
                    faker.options().option("free", "premium", "vip"),
                    faker.options().option("free", "premium", "vip"),
                    (short) faker.number().numberBetween(0, 10000)
            );

            ProducerRecord<String, CloudUser> record
                    = new ProducerRecord<String, CloudUser>(topic, null, null, user.tier, user, headers);
            producer.send(record).get();
        }
        producer.close();
    }
}
