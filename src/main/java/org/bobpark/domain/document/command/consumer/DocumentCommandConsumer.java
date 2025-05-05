package org.bobpark.domain.document.command.consumer;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.malgn.cqrs.consumer.CommandConsumer;
import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.EventPayload;
import com.malgn.cqrs.event.EventType;
import com.malgn.cqrs.event.handler.DelegatingCommandHandler;

import org.bobpark.domain.document.event.DocumentEventType;
import org.bobpark.domain.document.event.DocumentEventType.Topic;

@Slf4j
@RequiredArgsConstructor
@Component
public class DocumentCommandConsumer implements CommandConsumer {

    private static final List<EventType> SUPPORTED_EVENT_TYPES = Arrays.asList(DocumentEventType.values());

    private final DelegatingCommandHandler handler;

    @KafkaListener(topics = Topic.DOCUMENT)
    @Override
    public void listen(String message, Acknowledgment ack) {

        log.debug("received message - {}", message);

        Event<EventPayload> event = Event.fromJson(message, SUPPORTED_EVENT_TYPES);

        if (event == null) {
            return;
        }

        handler.handle(event);

        ack.acknowledge();

    }

}
