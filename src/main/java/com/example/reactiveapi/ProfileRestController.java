package com.example.reactiveapi;

import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(value = "/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Profile("classic")
class ProfileRestController {

    private final MediaType mediaType = MediaType.APPLICATION_JSON;
    private final com.example.reactiveapi.ProfileService profileService;

    ProfileRestController(com.example.reactiveapi.ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    Publisher<com.example.reactiveapi.Profile> getAll() {
        return this.profileService.all();
    }


    @GetMapping("/{id}")
    Publisher<com.example.reactiveapi.Profile> getById(@PathVariable("id") String id) {
        return this.profileService.get(id);
    }

    @PostMapping
    Publisher<ResponseEntity<com.example.reactiveapi.Profile>> create(@RequestBody com.example.reactiveapi.Profile profile) {
        return this.profileService
                .create(profile.getEmail())
                .map(p -> ResponseEntity.created(URI.create("/profiles/" + p.getId()))
                        .contentType(mediaType)
                        .build());
    }

    @DeleteMapping("/{id}")
    Publisher<com.example.reactiveapi.Profile> deleteById(@PathVariable String id) {
        return this.profileService.delete(id);
    }

    @PutMapping("/{id}")
    Publisher<ResponseEntity<com.example.reactiveapi.Profile>> updateById(@PathVariable String id, @RequestBody com.example.reactiveapi.Profile profile) {
        return Mono
                .just(profile)
                .flatMap(p -> this.profileService.update(id, p.getEmail()))
                .map(p -> ResponseEntity
                        .ok()
                        .contentType(this.mediaType)
                        .build());
    }
}
