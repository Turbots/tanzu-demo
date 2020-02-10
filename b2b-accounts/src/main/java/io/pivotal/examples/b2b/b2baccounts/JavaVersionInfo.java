package io.pivotal.examples.b2b.b2baccounts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
class JavaVersionInfo implements InfoContributor {

    @Value("${java.runtime.version}")
    private String version;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> javaVersion = new HashMap<>();

        javaVersion.put("version", version);

        builder.withDetail("java", javaVersion);
    }
}