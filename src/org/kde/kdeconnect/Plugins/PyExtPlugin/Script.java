package org.kde.kdeconnect.Plugins.PyExtPlugin;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Srivatsan on 8/13/16.
 */
public class Script {

    private final String name;
    private final String description;
    private final String guid;
    private final List<String> params;
    private final List<CapabilityType> capabilities;

    private Script(final String guid, final String name, final String description,
                   final List<CapabilityType> capabilities, final List<String> params) {
        this.guid = guid;
        this.name = name;
        this.description = description;
        this.params = params;
        this.capabilities = capabilities;
    }

    public static Script fromJSON(final JSONObject obj) {
        if (!obj.has("name") || !obj.has("guid")) {
            throw new IllegalArgumentException("Invalid JSON object to read from.");
        }

        try {
            final String name = obj.getString("name");
            final String desc = obj.getString("description");
            final String guid = obj.getString("guid");
            final List<String> params = new ArrayList<>();
            final List<CapabilityType> capabilities = new ArrayList<>();
            if (obj.has("params")) {
                final JSONArray arr = obj.getJSONArray("params");
                for (int i = 0; i < arr.length(); i++) {
                    params.add(arr.getString(i));
                }
            }
            if (obj.has("capabilities")) {
                final JSONArray arr = obj.getJSONArray("capabilities");
                for (int i = 0; i < arr.length(); i++) {
                    final CapabilityType type;
                    try {
                        type = CapabilityType.valueOf(arr.getString(i).toUpperCase());
                    } catch (IllegalArgumentException e) {
                        Log.w("PyExt", "Unable to map Capability: " + arr.getString(i));
                        continue;
                    }
                    capabilities.add(type);
                }
            }
            return new Script(guid, name, desc, capabilities, params);
        } catch (JSONException e) {
            Log.e("PyExt", "JSONException", e);
            throw new IllegalArgumentException("Unable to parse Script info.", e);
        }
    }

    public String getName() {
        return name;
    }

    public String getGuid() {
        return guid;
    }

    public List<String> getParams() {
        return params;
    }

    public String getDescription() {
        return description;
    }

    public List<CapabilityType> getCapabilities() {
        return capabilities;
    }
}
