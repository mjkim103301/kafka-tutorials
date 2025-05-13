package com.example.chapter06.service

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaListenerService {

    @KafkaListener(topics = ["peter-test05"], groupId = "peter-consumer-01")
    fun listen(record: ConsumerRecord<String, String>) {
        println("Topic: ${record.topic()}, Partition: ${record.partition()}, Offset: ${record.offset()}, Key: ${record.key()}, Value: ${record.value()}")
    }
}