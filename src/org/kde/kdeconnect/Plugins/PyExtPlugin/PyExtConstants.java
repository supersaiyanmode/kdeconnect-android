package org.kde.kdeconnect.Plugins.PyExtPlugin;

import android.content.Intent;

import org.kde.kdeconnect.Plugins.PyExtPlugin.eventhandlers.PhoneUnlockEventHandler;
import org.kde.kdeconnect.Plugins.PyExtPlugin.eventhandlers.ScriptLaunchEventHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thrustmaster on 8/15/16.
 */
public class PyExtConstants {

    public final static String PACKAGE_TYPE_PYEXT = "kdeconnect.pyext";

    public static Map<String, CapabilityType> INTENT_TO_CAPABILITY = new HashMap<String, CapabilityType>() {{
        put(Intent.ACTION_USER_PRESENT, CapabilityType.ON_DEVICE_UNLOCK);
    }};

    public static Map<CapabilityType, PyExtEventHandler> CAPABILITY_TO_HANDLER = new HashMap<CapabilityType, PyExtEventHandler>() {{
        put(CapabilityType.ON_DEVICE_UNLOCK, new PhoneUnlockEventHandler());
        put(CapabilityType.MAIN, new ScriptLaunchEventHandler());
    }};
}
