package com.example.chapter05


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.Properties
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

@SpringBootApplication
class Chapter05Application

fun main(args: Array<String>) {
    runApplication<Chapter05Application>(*args)

    val bootstrapServers = "peter-kafka01.foo.bar:9092"
    val props = Properties()
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.getName())
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.getName())
    props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true") // 정확히 한번 전송을 위한 설정
    props.setProperty(ProducerConfig.ACKS_CONFIG, "all") // 정확히 한번 전송을 위한 설정
    props.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5") // 정확히 한번 전송을 위한 설정
    props.setProperty(ProducerConfig.RETRIES_CONFIG, "5") // 정확히 한번 전송을 위한 설정
    props.setProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "peter-transaction-01") // 정확히 한번 전송을 위한 설정

    val producer: Producer<String?, String?> = KafkaProducer<Any?, Any?>(props) as Producer<String?, String?>

    producer.initTransactions() // 프로듀서 트랜잭션 초기화
    producer.beginTransaction() // 프로듀서 트랜잭션 시작
    try {
        for (i in 0..0) {
            val record = ProducerRecord<String?, String?>(
                "peter-test05",
                "Apache Kafka is a distributed streaming platform - " + i
            )
            producer.send(record)
            producer.flush()
            println("Message sent successfully")
        }
    } catch (e: Exception) {
        producer.abortTransaction() // 프로듀서 트랜잭션 중단
        e.printStackTrace()
    } finally {
        producer.commitTransaction() // 프로듀서 트랜잭션 커밋
        producer.close()
    }
}
