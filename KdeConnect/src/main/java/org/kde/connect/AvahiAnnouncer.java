package org.kde.connect;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import org.kde.connect.Announcer;
import org.kde.connect.ComputerLink;
import org.kde.connect.Types.NetworkPackage;
import org.kde.connect.UdpComputerLink;

import java.lang.Override;

/**
 * Created by vaka on 6/10/13.
 */
public class AvahiAnnouncer implements Announcer {

    UdpComputerLink computerLink;

    NsdManager mNsdManager;
    NsdServiceInfo serviceInfo;
    NsdManager.RegistrationListener mRegistrationListener = new NsdManager.RegistrationListener() {

        @Override
        public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
            Log.e("RegistrationListener","registered");
            // Save the service name.  Android may have changed it in order to
            // resolve a conflict, so update the name you initially requested
            // with the name Android actually used.
            //mServiceName = NsdServiceInfo.getServiceName();
        }

        @Override
        public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e("RegistrationListener","Registration error "+errorCode);
            // Registration failed!  Put debugging code here to determine why.
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo arg0) {
            Log.e("RegistrationListener","unregistered");
            // Service has been unregistered.  This only happens when you call
            // NsdManager.unregisterService() and pass in this listener.
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e("RegistrationListener","Unregistration error "+errorCode);
            // Unregistration failed.  Put debugging code here to determine why.
        }
    };

    ComputerLink.PackageReceiver mPackageReceiver = new ComputerLink.PackageReceiver() {
        @Override
        public void onPackageReceived(NetworkPackage np) {
            if (np.getType() == NetworkPackage.Type.ID_REQUEST) {
                Log.i("Avahi announcer","ID_REQUEST");
            } else if (np.getType() == NetworkPackage.Type.PAIR_REQUEST) {
                Log.i("Avahi announcer","PAIR_REQUEST");
            } else {
                Log.i("Avahi announcer","Not paired, ignoring package "+np.getType());
            }
        }
    };

    public AvahiAnnouncer(Context context) {

        serviceInfo = new NsdServiceInfo();
        serviceInfo.setPort(10601);
        serviceInfo.setServiceName("KdeConnect");
        serviceInfo.setServiceType("_device._tcp.");

        computerLink = new UdpComputerLink();

        mNsdManager = (NsdManager)context.getSystemService(Context.NSD_SERVICE);

    }

/*    public boolean startReceiveing() {
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }*/

    @Override
    public boolean startAnnouncing(ConnexionReceiver cr) {
        Log.i("AvahiAnnouncer","startAnnouncing");
        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
        computerLink.addPackageReceiver(mPackageReceiver);
        return true;
    }

    @Override
    public void stopAnnouncing() {
        mNsdManager.unregisterService(mRegistrationListener);
        computerLink.removePackageReceiver(mPackageReceiver);
    }
}