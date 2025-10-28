package com.BombTagNet.Backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "game.host")
public class GameHostProperties {
    private String publicAddress = "34.64.149.81";

    private String internalAddress = "34.64.149.81";

    private int port = 7777;

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public String getInternalAddress() {
        return internalAddress;
    }

    public void setInternalAddress(String internalAddress) {
        this.internalAddress = internalAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String resolveAddress(String candidate) {
        return resolvePublicAddress(candidate);
    }

    public String resolvePublicAddress(String candidate) {
        String trimmed = candidate == null ? null : candidate.trim();
        if (trimmed != null) {
            if (trimmed.equals(publicAddress)) {
                return publicAddress;
            }
            if (trimmed.equals(internalAddress)) {
                return publicAddress;
            }
            if ("localhost".equalsIgnoreCase(trimmed) || "127.0.0.1".equals(trimmed) || "::1".equals(trimmed)) {
                return publicAddress;
            }
        }
        return publicAddress;
    }

    public String resolveInternalAddress(String candidate) {
        String trimmed = candidate == null ? null : candidate.trim();
        if (trimmed != null && !trimmed.isEmpty()) {
            if ("localhost".equalsIgnoreCase(trimmed) || "127.0.0.1".equals(trimmed) || "::1".equals(trimmed)) {
                return internalAddress;
            }
            if (trimmed.equals(publicAddress)) {
                return internalAddress;
            }
            return trimmed;
        }
        return internalAddress;
    }

    public int resolvePort(Integer candidate) {
        if (candidate != null && candidate > 0 && candidate == port) {
            return candidate;
        }
        return port;
    }
}