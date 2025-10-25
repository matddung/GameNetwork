package com.BombTagNet.Backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "game.host")
public class GameHostProperties {
    private String publicAddress = "34.64.149.81";

    private String internalAddress = "10.178.0.2";

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

    public String resolvePublicAddress(String candidate) {
        String normalizedPublic = trim(publicAddress);
        String normalizedInternal = trim(internalAddress);
        String normalizedCandidate = trim(candidate);

        if (normalizedCandidate != null) {
            if (normalizedPublic != null && normalizedCandidate.equals(normalizedPublic)) {
                return normalizedPublic;
            }
            if (normalizedInternal != null && normalizedCandidate.equals(normalizedInternal)) {
                return normalizedPublic != null ? normalizedPublic : normalizedCandidate;
            }
        }

        if (normalizedPublic != null) {
            return normalizedPublic;
        }

        return normalizedCandidate;
    }

    public String resolveInternalAddress(String candidate) {
        String normalizedPublic = trim(publicAddress);
        String normalizedInternal = trim(internalAddress);
        String normalizedCandidate = trim(candidate);

        if (normalizedCandidate != null) {
            if (normalizedInternal != null && normalizedCandidate.equals(normalizedInternal)) {
                return normalizedInternal;
            }
            if (normalizedPublic != null && normalizedCandidate.equals(normalizedPublic)) {
                return normalizedInternal != null ? normalizedInternal : normalizedCandidate;
            }
        }

        if (normalizedInternal != null) {
            return normalizedInternal;
        }

        return normalizedCandidate != null ? normalizedCandidate : normalizedPublic;
    }

    public String resolveAddress(String candidate) {
        return resolvePublicAddress(candidate);
    }

    public int resolvePort(Integer candidate) {
        if (candidate != null && candidate > 0 && candidate == port) {
            return candidate;
        }
        return port;
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}