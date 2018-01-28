package org.kde.kdeconnect.Plugins.PyExtPlugin.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.kde.kdeconnect.Plugins.PyExtPlugin.PluginsManager;

/**
 * Created by Srivatsan on 9/25/16.
 */

public class IncomingMessageHandler extends Handler {
    private final PluginsManager manager;

    public IncomingMessageHandler(PluginsManager manager) {
        this.manager = manager;
    }

    @Override
    public void handleMessage(final Message message) {
        final Bundle data = message.getData();
        switch (message.what) {
            case MessageConstants.MESSAGE_REGISTER:
                manager.register(data.getString("plugin_name"), message.replyTo);
                return;
            case MessageConstants.MESSAGE_UNREGISTER:
                manager.unregister(data.getString("plugin_name"));
                return;
            default:
                super.handleMessage(message);
        }
    }
}
