package org.kde.kdeconnect.Plugins.PyExtPlugin;

import android.os.Messenger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Srivatsan on 9/25/16.
 */
public class PluginsManager {
    private final Map<String, Messenger> pluginsMap;

    public PluginsManager() {
        pluginsMap = new ConcurrentHashMap<>();
    }

    public void register(final String key, final Messenger messenger) {
        pluginsMap.put(key, messenger);
    }

    public void unregister(final String key) {
        pluginsMap.remove(key);
    }
}
