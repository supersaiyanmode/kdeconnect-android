package org.kde.kdeconnect.Plugins.PyExtPlugin;

import android.content.Intent;

import org.kde.kdeconnect.Device;

import java.util.Map;

/**
 * Created by thrustmaster on 8/15/16.
 */
public interface PyExtEventHandler {
    public void onEvent(Device device, Script script, Map<String, String> map);
}
