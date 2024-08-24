package com.demo.bbq.commons;

import org.mockserver.integration.ClientAndServer;

import java.io.IOException;

public interface MockRuleProvider {
    void loadMocks(ClientAndServer mockServer) throws IOException;

}
