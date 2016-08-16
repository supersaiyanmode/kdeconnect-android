package org.kde.kdeconnect.Plugins.PyExtPlugin;

/**
 * Created by Srivatsan on 8/15/16.
 */
public enum CapabilityType {
    MAIN("main"),
    ON_DEVICE_UNLOCK("on_device_unlock");

    private final String value;
    CapabilityType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
