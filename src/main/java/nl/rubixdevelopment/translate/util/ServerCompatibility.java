package nl.rubixdevelopment.translate.util;

import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Server;

/**
 * Server compatibility utility for Folia, Paper, and Spigot
 */
@Log
public class ServerCompatibility {
    
    private static ServerType serverType;
    private static String serverVersion;
    private static boolean isInitialized = false;
    
    public enum ServerType {
        FOLIA("Folia"),
        PAPER("Paper"),
        SPIGOT("Spigot"),
        UNKNOWN("Unknown");
        
        private final String displayName;
        
        ServerType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Initialize server compatibility detection
     */
    public static void initialize() {
        if (isInitialized) {
            return;
        }
        
        Server server = Bukkit.getServer();
        String serverName = server.getName();
        serverVersion = server.getBukkitVersion();
        
        // Detect server type
        if (serverName.contains("Folia")) {
            serverType = ServerType.FOLIA;
            log.info("Detected Folia server");
        } else if (serverName.contains("Paper")) {
            serverType = ServerType.PAPER;
            log.info("Detected Paper server");
        } else if (serverName.contains("Spigot")) {
            serverType = ServerType.SPIGOT;
            log.info("Detected Spigot server");
        } else {
            serverType = ServerType.UNKNOWN;
            log.warning("Unknown server type: " + serverName);
        }
        
        log.info("Server: " + serverType.getDisplayName() + " " + serverVersion);
        isInitialized = true;
    }
    
    /**
     * Get the detected server type
     */
    public static ServerType getServerType() {
        if (!isInitialized) {
            initialize();
        }
        return serverType;
    }
    
    /**
     * Get the server version
     */
    public static String getServerVersion() {
        if (!isInitialized) {
            initialize();
        }
        return serverVersion;
    }
    
    /**
     * Check if the server is Folia
     */
    public static boolean isFolia() {
        return getServerType() == ServerType.FOLIA;
    }
    
    /**
     * Check if the server is Paper
     */
    public static boolean isPaper() {
        return getServerType() == ServerType.PAPER;
    }
    
    /**
     * Check if the server is Spigot
     */
    public static boolean isSpigot() {
        return getServerType() == ServerType.SPIGOT;
    }
    
    /**
     * Check if the server supports async chat events
     */
    public static boolean supportsAsyncChat() {
        return isPaper() || isFolia();
    }
    
    /**
     * Check if the server supports modern inventory features
     */
    public static boolean supportsModernInventory() {
        return isPaper() || isFolia();
    }
    
    /**
     * Check if the server supports modern chat components
     */
    public static boolean supportsModernChat() {
        return isPaper() || isFolia();
    }
    
    /**
     * Get the minimum supported Minecraft version
     */
    public static String getMinimumVersion() {
        switch (getServerType()) {
            case FOLIA:
                return "1.20.1";
            case PAPER:
                return "1.13";
            case SPIGOT:
                return "1.8.8";
            default:
                return "1.8.8";
        }
    }
    
    /**
     * Check if the current server version is supported
     */
    public static boolean isVersionSupported() {
        try {
            String version = getServerVersion();
            if (version.contains("-")) {
                version = version.split("-")[0];
            }
            
            // Parse version (e.g., "1.21.1")
            String[] parts = version.split("\\.");
            if (parts.length >= 2) {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                
                switch (getServerType()) {
                    case FOLIA:
                        return major >= 1 && minor >= 20;
                    case PAPER:
                        return major >= 1 && minor >= 13;
                    case SPIGOT:
                        return major >= 1 && minor >= 8;
                    default:
                        return major >= 1 && minor >= 8;
                }
            }
        } catch (Exception e) {
            log.warning("Could not parse server version: " + getServerVersion());
        }
        
        return false;
    }
    
    /**
     * Get compatibility warnings
     */
    public static String[] getCompatibilityWarnings() {
        if (isVersionSupported()) {
            return new String[0];
        }
        
        return new String[]{
            "Server version " + getServerVersion() + " may not be fully supported",
            "Minimum recommended version: " + getMinimumVersion(),
            "Some features may not work correctly"
        };
    }
}
