/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.test;

import java.io.IOException;

import com.arcaner.warlock.network.Connection;
import com.arcaner.warlock.network.IConnectionListener;

/**
 * @author Marshall
 *
 * A simple Connection test.
 */
public class ConnectionTest {

	public static void main (String args[])
		throws Exception
	{
		String host = args[0];
		String port = args[1];
		final String key = args[2];
		
		System.out.println("host="+host+",port="+port+",key="+key);
		
		Connection c = new Connection ();
		c.addConnectionListener( new IConnectionListener () {
			private boolean sendFirstData = false;
			
			public void connected(Connection connection) {
				/*try {
					System.out.println("------------------CONNECTED------------------");
					connection.sendLine(key);
					connection.sendLine(StormFrontProtocol.PROTOCOL_ID);
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}
			
			public void dataReady(Connection connection, String line) {
				try {
					if (!sendFirstData) {
						connection.sendLine("look");
						sendFirstData = true;
					}
					System.out.println(line);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			public void disconnected(Connection connection) {
				System.out.println("-----------DISCONNECTED----------------");
				
			}
		});
		
		System.out.println("Connecting...");
		c.connect(host, Integer.parseInt(port));
		
		while (true)
		{
			Thread.sleep(200);
		}
	}
}
