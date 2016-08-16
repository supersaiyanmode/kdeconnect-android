package org.kde.kdeconnect.Plugins.PyExtPlugin.eventhandlers;

import android.content.Intent;
import android.util.Log;

import org.kde.kdeconnect.Device;
import org.kde.kdeconnect.NetworkPackage;
import org.kde.kdeconnect.Plugins.PyExtPlugin.CapabilityType;
import org.kde.kdeconnect.Plugins.PyExtPlugin.PyExtConstants;
import org.kde.kdeconnect.Plugins.PyExtPlugin.PyExtEventHandler;
import org.kde.kdeconnect.Plugins.PyExtPlugin.Script;

import java.util.Map;

/**
 * Created by thrustmaster on 8/15/16.
 */
public class ScriptLaunchEventHandler implements PyExtEventHandler {
    @Override
    public void onEvent(Device device, Script script, Map<String, String> map) {
        final NetworkPackage np = new NetworkPackage(PyExtConstants.PACKAGE_TYPE_PYEXT);
        np.set("pyext_type", "ScriptExecuteRequest");
        np.set("script_guid", script.getGuid());
        np.set("script_entrypoint", CapabilityType.MAIN.getValue());
        device.sendPackage(np);
        Log.i("PyExt", "Sent MAIN command to execute script with guid: " + script.getGuid());
    }
}
