package org.kde.kdeconnect.Plugins.PyExtPlugin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;

import org.kde.kdeconnect.Plugins.PyExtPlugin.PluginsManager;

public class PluginsService extends Service {
    private final PluginsManager manager;
    private Messenger messenger;

    public PluginsService(PluginsManager manager) {
        this.manager = manager;
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (this.messenger == null) {
            synchronized (PluginsService.class) {
                if (this.messenger == null) {
                    this.messenger = new Messenger(new IncomingMessageHandler(manager));
                }
            }
        }
        return this.messenger.getBinder();
    }
}
