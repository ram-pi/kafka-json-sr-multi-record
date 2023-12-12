package kafka.client;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import model.PlatformUser;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class PlatformUserProducer {

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

        Producer<String, PlatformUser> producer = new KafkaProducer<String, PlatformUser>(props);

        String topic = "users";
        Faker faker = new Faker();
        for (int i = 0; i < faker.number().numberBetween(1, 10); i++) {
            PlatformUser user = new PlatformUser(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    (short) faker.number().numberBetween(18, 99),
                    faker.internet().emailAddress(),
                    faker.options().option("free", "premium", "vip")
            );
            ProducerRecord<String, PlatformUser> record
                    = new ProducerRecord<String, PlatformUser>(topic, user.tier, user);
            producer.send(record).get();
        }
        producer.close();
    }
}
