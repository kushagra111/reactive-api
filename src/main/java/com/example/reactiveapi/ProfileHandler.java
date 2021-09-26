package com.example.reactiveapi;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
class ProfileHandler {
    private final com.example.reactiveapi.ProfileService profileService;

    ProfileHandler(com.example.reactiveapi.ProfileService profileService) {
        this.profileService = profileService;
    }

    Mono<ServerResponse> getById(ServerRequest r) {
        return defaultReadResponse(this.profileService.get(id(r)));
    }

    Mono<ServerResponse> all(ServerRequest r) {
        return defaultReadResponse(this.profileService.all());
    }

    Mono<ServerResponse> deleteById(ServerRequest r) {
        return defaultReadResponse(this.profileService.delete(id(r)));
    }

    Mono<ServerResponse> updateById(ServerRequest r) {
        Flux<com.example.reactiveapi.Profile> id = r.bodyToFlux(com.example.reactiveapi.Profile.class)
                .flatMap(p -> this.profileService.update(id(r), p.getEmail()));
        return defaultReadResponse(id);
    }

    Mono<ServerResponse> create(ServerRequest request) {
        Flux<com.example.reactiveapi.Profile> flux = request
                .bodyToFlux(com.example.reactiveapi.Profile.class)
                .flatMap(toWrite -> this.profileService.create(toWrite.getEmail()));
        return defaultWriteResponse(flux);
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<com.example.reactiveapi.Profile> profiles) {
        return Mono
                .from(profiles)
                .flatMap(p -> ServerResponse
                        .created(URI.create("/profiles/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build()
                );
    }

    private static Mono<ServerResponse> defaultReadResponse(Publisher<com.example.reactiveapi.Profile> profiles) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(profiles, com.example.reactiveapi.Profile.class);
    }

    private static String id(ServerRequest r) {
        return r.pathVariable("id");
    }
}
