package com.demo.poc.commons;

import com.demo.poc.commons.properties.ApplicationProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mockserver.integration.ClientAndServer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MockServerInitializer {

    @Getter
    private ClientAndServer mockServer;

    private final List<MockRuleProvider> ruleProviders;
    private final ApplicationProperties properties;

    @PostConstruct
    public void startServer() {
        mockServer = ClientAndServer.startClientAndServer(properties.getMockPort());
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
