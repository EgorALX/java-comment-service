package ru.comments.commentservice.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.stereotype.Component;
import ru.comments.commentservice.dto.NewCommentDto;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.comments.commentservice.dto.NewsDeletionEvent;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.partition.count}")
    private int partitionCount;

    @Bean
    public TopicPartition createCommentTopicPartition() {
        return new TopicPartition("create-comment-topic", 0);
    }

    @Bean
    public TopicPartition deleteNewsTopicPartition() {
        return new TopicPartition("news-deletion-topic", 0);
    }

    @Bean
    public ConsumerFactory<String, NewCommentDto> createCommentConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "comment-create-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
                new JsonDeserializer<>(NewCommentDto.class));
    }

    @Bean
    public ConsumerFactory<String, String> deleteNewsConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "news-delete-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
                new StringDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NewCommentDto> createCommentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NewCommentDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createCommentConsumerFactory());
        factory.setConcurrency(partitionCount);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> deleteNewsKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(deleteNewsConsumerFactory());
        factory.setConcurrency(partitionCount);
        return factory;
    }
}