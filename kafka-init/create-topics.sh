#!/bin/bash

echo ">>> Creating Kafka topics..."

kafka-topics.sh --create --if-not-exists \
  --topic instructions.inbound \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 1

kafka-topics.sh --create --if-not-exists \
  --topic instructions.outbound \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 1

echo ">>> Kafka topics created."
