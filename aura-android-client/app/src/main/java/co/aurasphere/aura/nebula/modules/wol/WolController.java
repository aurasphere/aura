package co.aurasphere.aura.nebula.modules.wol;

import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Donato on 06/01/2016.
 */
public class WolController extends AsyncTask<Void,Void,Void> {


    public static final int PORT = 9;

    /**
     * PC MAC Address.
     */

    public static final String MAC_ADDRESS = "";

    /**
     * PC Network IP address.
     */

    public static final String NETWORK_IP_ADDRESS = "";

    @Override
    protected Void doInBackground(Void... Params) {

        try {
            byte[] macBytes = getMacBytes(MAC_ADDRESS);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }

            InetAddress address = InetAddress.getByName(NETWORK_IP_ADDRESS);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();

            System.out.println("Wake-on-LAN packet sent.");
        } catch (Exception e) {
            Log.d("WolController", "Exception" + e);
            System.out.println("Failed to send Wake-on-LAN packet: " + e);
        }
        return null;
    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }

}
