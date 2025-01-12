package com.jpcchaves.authservice.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@EnableKafka
@Configuration
public class KafkaConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    private static final Integer PARTITION_COUNT = 1;
    private static final Integer REPLICATION_FACTOR = 1;

    private final DefaultListableBeanFactory beanFactory;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.topic.registration-initiated}")
    private String registrationInitiatedTopic;

    @Value("${spring.kafka.topic.registration-completed}")
    private String registrationCompletedTopic;

    public KafkaConfig(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProps());
    }

    private Map<String, Object> producerProps() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(
            ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public Set<NewTopic> buildTopics() {
        Map<String, String> topics = extractTopicProperties();
        return topics.values().stream().map(this::createTopic).collect(Collectors.toSet());
    }

    private Map<String, String> extractTopicProperties() {
        Map<String, String> topics = new HashMap<>();

        ReflectionUtils.doWithFields(
                this.getClass(),
                field -> {
                    field.setAccessible(true);
                    Object value = field.get(this);

                    if (value instanceof String) {
                        topics.put(field.getName(), (String) value);
                    }
                },
                field ->
                        field.isAnnotationPresent(Value.class)
                                && field.getAnnotation(Value.class)
                                        .value()
                                        .startsWith("${spring.kafka.topic."));

        return topics;
    }

    private NewTopic createTopic(String topicName) {
        logger.info("Creating topic {}", topicName);

        BeanDefinitionBuilder beanDefinitionBuilder =
                BeanDefinitionBuilder.genericBeanDefinition(NewTopic.class)
                        .addConstructorArgValue(topicName)
                        .addConstructorArgValue(PARTITION_COUNT)
                        .addConstructorArgValue(REPLICATION_FACTOR);

        registerTopicBean(topicName, beanDefinitionBuilder.getBeanDefinition());

        return buildTopic(topicName);
    }

    private void registerTopicBean(String topicName, BeanDefinition beanDefinition) {
        beanFactory.registerBeanDefinition(topicName, beanDefinition);
    }

    private NewTopic buildTopic(String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(PARTITION_COUNT)
                .replicas(REPLICATION_FACTOR)
                .build();
    }
}
