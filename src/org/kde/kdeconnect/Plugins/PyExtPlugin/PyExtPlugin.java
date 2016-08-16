/*
 * Copyright 2016 Srivatsan Iyer <supersaiyanmode.rox@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License or (at your option) version 3 or any later version
 * accepted by the membership of KDE e.V. (or its successor approved
 * by the membership of KDE e.V.), which shall act as a proxy
 * defined in Section 14 of version 3 of the license.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.kde.kdeconnect.Plugins.PyExtPlugin;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.kde.kdeconnect.Device;
import org.kde.kdeconnect.NetworkPackage;
import org.kde.kdeconnect.Plugins.Plugin;
import org.kde.kdeconnect_tp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PyExtPlugin extends Plugin {
    private Map<String, PyExtEventHandler> listeners;
    private List<Script> scripts;

    @Override
    public String getDisplayName() {
        return context.getResources().getString(R.string.pref_plugin_pyext);
    }

    @Override
    public String getDescription() {
        return context.getResources().getString(R.string.pref_plugin_pyext_desc);
    }

    @Override
    public boolean onCreate() {
        loadScriptsList();
        return true;
    }

    @Override
    public boolean onPackageReceived(NetworkPackage np) {
        if (!np.getType().equals(PyExtConstants.PACKAGE_TYPE_PYEXT)) {
            Log.e("PyExtPlugin", "PyExt plugin should not receive packets other than pings!");
            return false;
        }
        if (!np.has("pyext_type")) {
            return false;
        }

        final String type = np.getString("pyext_type");
        if ("ScriptListResponse".equals(type)) {
            scripts = new ArrayList<>();

            try {
                Log.i("PyExt", np.serialize());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final JSONArray arr = np.getJSONArray("pyext_response");
            for (int i = 0; i < arr.length(); i++) {
                final Script script;
                try {
                    script = Script.fromJSON(arr.getJSONObject(i));
                } catch (JSONException e) {
                    Log.i("PyExt", "Unable to parse script info.", e);
                    continue;
                }



                scripts.add(script);
            }
        }
        return true;
    }

    @Override
    public String getActionName() {
        return context.getString(R.string.pyext_custom_scripts);
    }

    @Override
    public void startMainActivity(Activity parentActivity) {
        Intent intent = new Intent(parentActivity, PyExtActivity.class);
        intent.putExtra("deviceId", device.getDeviceId());
        parentActivity.startActivity(intent);
    }

    @Override
    public boolean hasMainActivity() {
        return true;
    }

    @Override
    public boolean displayInContextMenu() {
        return false;
    }

    @Override
    public String[] getSupportedPackageTypes() {
        return new String[]{PyExtConstants.PACKAGE_TYPE_PYEXT};
    }

    @Override
    public String[] getOutgoingPackageTypes() {
        return new String[]{PyExtConstants.PACKAGE_TYPE_PYEXT};
    }

    public List<Script> getScripts() {
        return scripts;
    }

    private void loadScriptsList() {
        final NetworkPackage np = new NetworkPackage(PyExtConstants.PACKAGE_TYPE_PYEXT);
        np.set("pyext_type", "ScriptListRequest");
        device.sendPackage(np);
    }

    public void executeScript(Script script) {
        final NetworkPackage np = new NetworkPackage(PyExtConstants.PACKAGE_TYPE_PYEXT);
        np.set("pyext_type", "ScriptExecuteRequest");
        np.set("script_guid", script.getGuid());
        np.set("script_entrypoint", CapabilityType.MAIN.getValue());
        device.sendPackage(np);
        Log.i("PyExt", "Sent command to execute script with guid: " + script.getGuid());
    }

    public void onEvent(Intent intent) {
        final CapabilityType type = PyExtConstants.INTENT_TO_CAPABILITY.get(intent.getAction());
        final PyExtEventHandler handler = PyExtConstants.CAPABILITY_TO_HANDLER.get(type);
        for (final Script script: scripts) {
            if (script.getCapabilities().contains(type)) {
                handler.onEvent(device, script, new HashMap<String, String>());
            } else {
                Log.i("PyExt", "Capability " + type + " not found for script: " + script.getName());
            }
        }
    }
}
