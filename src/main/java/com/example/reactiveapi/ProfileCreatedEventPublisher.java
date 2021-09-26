package com.example.reactiveapi;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

@Component
class ProfileCreatedEventPublisher implements
        ApplicationListener<ProfileCreatedEvent>,
        Consumer<FluxSink<ProfileCreatedEvent>> {

    private FluxSink<ProfileCreatedEvent> sink;

    @Override
    public void onApplicationEvent(ProfileCreatedEvent event) {
        FluxSink<ProfileCreatedEvent> sink = this.sink;
        if (sink != null) {
            sink.next(event);
        }
    }

    @Override
    public void accept(FluxSink<ProfileCreatedEvent> sink) {
        this.sink = sink;
        sink.onDispose(() -> this.sink = null);
    }
}