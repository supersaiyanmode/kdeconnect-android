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

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.kde.kdeconnect.BackgroundService;
import org.kde.kdeconnect.Device;
import org.kde.kdeconnect.UserInterface.List.EntryItem;
import org.kde.kdeconnect.UserInterface.List.ListAdapter;
import org.kde.kdeconnect_tp.R;

import java.util.ArrayList;
import java.util.List;

public class PyExtActivity extends ActionBarActivity {

    private String deviceId;

    private void updateView() {
        BackgroundService.RunCommand(this, new BackgroundService.InstanceCallback() {
            @Override
            public void onServiceStart(final BackgroundService service) {

                final Device device = service.getDevice(deviceId);
                final PyExtPlugin plugin = device.getPlugin(PyExtPlugin.class);
                if (plugin == null) {
                    Log.e("PyExtActivity", "Device has no pyextcommand plugin!");
                    return;
                }

                final List<Script> scripts = plugin.getScripts();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reloadScriptsList(plugin, scripts);
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pyext);

        deviceId = getIntent().getStringExtra("deviceId");

        updateView();
    }

    private void reloadScriptsList(final PyExtPlugin plugin, final List<Script> scripts) {
        final ListView view = (ListView) findViewById(R.id.pyext_listview);

        final ArrayList<ListAdapter.Item> commandItems = new ArrayList<>();
        for (final Script script: scripts) {
            commandItems.add(new EntryItem(script.getName(), script.getGuid()));
        }

        final ListAdapter adapter = new ListAdapter(PyExtActivity.this, commandItems);

        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                plugin.executeScript(scripts.get(i));
            }
        });
    }
}
