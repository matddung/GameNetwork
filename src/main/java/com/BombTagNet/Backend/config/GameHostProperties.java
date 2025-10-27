package com.BombTagNet.Backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "game.host")
public class GameHostProperties {
    private String address = "34.64.149.81";

    private String internalAddress = "10.178.0.2";

    private boolean preferInternal = false;

    private int port = 7777;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInternalAddress() {
        return internalAddress;
    }

    public void setInternalAddress(String internalAddress) {
        this.internalAddress = internalAddress;
    }

    public boolean isPreferInternal() {
        return preferInternal;
    }

    public void setPreferInternal(boolean preferInternal) {
        this.preferInternal = preferInternal;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public HostEndpoint resolveEndpoint(String candidate) {
        String trimmed = trimToNull(candidate);

        String publicAddress = trimToNull(address);
        if (publicAddress == null) {
            publicAddress = trimToNull(internalAddress);
        }
        if (publicAddress == null) {
            publicAddress = trimmed;
        }

        String resolvedInternal = trimToNull(internalAddress);
        if (resolvedInternal == null) {
            resolvedInternal = trimmed;
        }
        if (resolvedInternal == null) {
            resolvedInternal = publicAddress;
        }

        if (publicAddress == null) {
            publicAddress = resolvedInternal;
        }

        return new HostEndpoint(publicAddress, resolvedInternal);
    }

    public String resolveAddress(String candidate) {
        HostEndpoint endpoint = resolveEndpoint(candidate);
        return shouldUseInternalFor(null) ? endpoint.internalAddress() : endpoint.publicAddress();
    }

    public int resolvePort(Integer candidate) {
        if (candidate != null && candidate > 0 && candidate == port) {
            return candidate;
        }
        return port;
    }

    public boolean shouldUseInternalFor(String requesterAddress) {
        if (!preferInternal) {
            return false;
        }

        String trimmed = trimToNull(requesterAddress);
        if (trimmed == null) {
            return true;
        }

        return isInternalNetwork(trimmed);
    }

    public AddressSelection selectForClient(String publicAddress, String internalAddress, String requesterAddress) {
        String normalizedPublic = trimToNull(publicAddress);
        String normalizedInternal = trimToNull(internalAddress);

        if (normalizedPublic == null && normalizedInternal != null) {
            normalizedPublic = normalizedInternal;
        }
        if (normalizedInternal == null && normalizedPublic != null) {
            normalizedInternal = normalizedPublic;
        }

        boolean useInternal = shouldUseInternalFor(requesterAddress) && normalizedInternal != null;
        String preferred = useInternal ? normalizedInternal : normalizedPublic;

        return new AddressSelection(normalizedPublic, normalizedInternal, preferred);
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isInternalNetwork(String address) {
        try {
            java.net.InetAddress inetAddress = java.net.InetAddress.getByName(address);
            return inetAddress.isSiteLocalAddress()
                    || inetAddress.isLoopbackAddress()
                    || inetAddress.isAnyLocalAddress()
                    || inetAddress.isLinkLocalAddress();
        } catch (java.net.UnknownHostException ex) {
            return false;
        }
    }

    public record HostEndpoint(String publicAddress, String internalAddress) {
        public String preferred(boolean preferInternal) {
            return preferInternal ? internalAddress : publicAddress;
        }
    }

    public record AddressSelection(String publicAddress, String internalAddress, String preferredAddress) {
    }
}