package com.kikopolis.wordcloudworker.config;

import com.kikopolis.wordcloudworker.resource.HealthResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(HealthResource.class);
    }
}
