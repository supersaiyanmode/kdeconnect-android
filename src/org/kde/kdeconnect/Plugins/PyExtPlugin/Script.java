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
    private final String guid;
    private final List<String> params;

    private Script(final String guid, final String name, final List<String> params) {
        this.guid = guid;
        this.name = name;
        this.params = params;
    }

    public static Script fromJSON(final JSONObject obj) {
        if (!obj.has("name") || !obj.has("guid")) {
            throw new IllegalArgumentException("Invalid JSON object to read from.");
        }

        try {
            final String name = obj.getString("name");
            final String guid = obj.getString("guid");
            final List<String> params = new ArrayList<>();
            if (obj.has("params")) {
                final JSONArray arr = obj.getJSONArray("params");
                for (int i = 0; i < arr.length(); i++) {
                    params.add(arr.getString(i));
                }
            }
            return new Script(guid, name, params);
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
}
