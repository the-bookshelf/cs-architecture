package com.packtpub.maskaccounts;

import com.microsoft.azure.serverless.functions.ExecutionContext;
import org.springframework.cloud.function.adapter.azure.AzureSpringBootRequestHandler;
import reactor.core.publisher.Flux;

public class Handler extends AzureSpringBootRequestHandler<Flux<String>,Flux<String>> {
    public Flux<String> execute(Flux<String>in, ExecutionContext context) {
        return handleRequest(in, context);
    }
}