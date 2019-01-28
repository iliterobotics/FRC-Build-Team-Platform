package us.ilite.robot;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.function.Consumer;

import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;

import edu.wpi.first.networktables.ConnectionInfo;
import edu.wpi.first.networktables.ConnectionNotification;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * This class is a helper class that resolves commonly-needed information based
 * upon the 3 different networks the FRC bot can see: - Ethernet (10.18.85) -
 * USB (172 network) - Wireless (10.18.85)
 * 
 */
public class Network {
    private final ILog sLOG = Logger.createLog(Network.class);
    private static final Network INFO = new Network();
    private ConnectionInfo mConnectionInfo = null;

    public static Network getInstance() {
        return INFO;
    }

    /**
     * @return the current connection information. This will be NULL during
     *         robotInit() but NOT NULL during disabledInit(), auton, and teleop
     */
    public ConnectionInfo getConnectionInfo() {
        return mConnectionInfo;
    }

    private Network() {
        Consumer<ConnectionNotification> listener = conn -> {
            mConnectionInfo = conn.conn;
            sLOG.info("=== Remote Connection Info ===");
            sLOG.info(mConnectionInfo.remote_ip + " : " + mConnectionInfo.remote_port);
        };
        NetworkTableInstance.getDefault().addConnectionListener(listener, true);
    }

    public void printConnections() throws SocketException {
        Enumeration<NetworkInterface> connections = NetworkInterface.getNetworkInterfaces();
        while(connections.hasMoreElements()) {
          NetworkInterface ni = connections.nextElement();
          sLOG.info("Network Interface: " + ni);
          Enumeration<InetAddress> addrs = ni.getInetAddresses();
          while(addrs.hasMoreElements()) {
            InetAddress addr = addrs.nextElement();
            sLOG.info("Address - " + addr);
            if(addr.isLoopbackAddress() == false && addr instanceof Inet4Address) {
              System.out.println("IPV4 - " + addr.getHostAddress());
            }
          }
        }
    }

	public static Network getinstance() {
		return null;
	}
}