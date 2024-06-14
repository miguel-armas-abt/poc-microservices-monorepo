package com.demo.bbq.infraestructure.mockservice.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import org.mockserver.integration.ClientAndServer;
import org.springframework.stereotype.Component;

@Component
public class MockServerInitializer {

    private static final int SERVER_PORT=8080;

    @Getter
    private ClientAndServer mockServer;
    private final List<MockRuleProvider> ruleProviders;
    public MockServerInitializer(List<MockRuleProvider> ruleProviders) {
        this.ruleProviders = ruleProviders;
    }

    @PostConstruct
    public void startServer() {
        mockServer = ClientAndServer.startClientAndServer(SERVER_PORT);
        ruleProviders.forEach(provider -> {
            try {
                provider.loadMocks(mockServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @PreDestroy
    public void stopServer() {
        mockServer.stop();
    }

}
