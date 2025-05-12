package com.demo.poc.commons.config;

import com.demo.poc.commons.properties.ApplicationProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mockserver.integration.ClientAndServer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockServerInitializer {

    @Getter
    private ClientAndServer clientAndServer;

    private final List<MockService> mockServices;
    private final ApplicationProperties properties;

    @PostConstruct
    public void startServer() {
        clientAndServer = ClientAndServer.startClientAndServer(properties.getMockPort());
        mockServices.forEach(provider -> {
            try {
                provider.loadMocks(clientAndServer);
            } catch (IOException e) {
                log.error("Error starting mock service: {}", e.getMessage());
            }
        });
    }

    @PreDestroy
    public void stopServer() {
        clientAndServer.stop();
    }

}
