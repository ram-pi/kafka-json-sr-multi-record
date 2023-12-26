# Kafka JSON Schema Clients with RecordStategyName

## Overview

Example of using Kafka JSON Schema Clients with RecordStategyName.

## Setup

Start the Confluent Platform:

```
docker-compose up -d
sleep 10
kafka-topics --bootstrap-server localhost:9092 --create --topic users --partitions 1 --replication-factor 1
```

## Build

```
mvn clean package
```

## Run

```
# RecordNamingStrategy
java -cp target/kafka-json-sr-multi-record-1.0-SNAPSHOT.jar kafka.client.CloudUserProducer
java -cp target/kafka-json-sr-multi-record-1.0-SNAPSHOT.jar kafka.client.PlatformUserProducer
java -cp target/kafka-json-sr-multi-record-1.0-SNAPSHOT.jar kafka.client.UserConsumer
# TopicNamingStrategy with one container object
java -cp target/kafka-json-sr-multi-record-1.0-SNAPSHOT.jar kafka.client.CombinedRequestResponseProducer
java -cp target/kafka-json-sr-multi-record-1.0-SNAPSHOT.jar kafka.client.GenericCombinedRequestResponseConsumer
java -cp target/kafka-json-sr-multi-record-1.0-SNAPSHOT.jar kafka.client.SpecificCombinedRequestResponseConsumer

```

## Command Line Utils

Check the schema id used by each message in the topic:

```
kafka-json-schema-console-consumer --bootstrap-server localhost:9092 --topic users --from-beginning --property print.schema.ids=true 
```