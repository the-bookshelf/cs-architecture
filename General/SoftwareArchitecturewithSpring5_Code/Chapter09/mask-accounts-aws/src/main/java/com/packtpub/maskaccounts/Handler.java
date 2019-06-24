package com.packtpub.maskaccounts;

import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import reactor.core.publisher.Flux;

public class Handler extends SpringBootRequestHandler<Flux<String>, Flux<String>> {

}