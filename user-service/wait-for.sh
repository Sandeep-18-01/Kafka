#!/bin/sh

echo "Waiting for MySQL to be ready..."
while ! nc -z mysql 3306; do
  sleep 1
done

echo "Waiting for Kafka to be ready..."
while ! nc -z kafka 9092; do
  sleep 1
done

echo "MySQL & Kafka are up. Starting app..."
exec java -jar app.jar
