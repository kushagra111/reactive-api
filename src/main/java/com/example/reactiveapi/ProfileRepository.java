package com.example.reactiveapi;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
}
