package dme.multijast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Multicast {
	
	public static String sendMulticast(String msg, String ipaddr, int port){
		
		 DatagramSocket socket = null;
		 DatagramPacket outPacket = null;
		 byte[] outBuf;
		 
		    try {
		        socket = new DatagramSocket();
		        
		        msg = msg.trim();
		        outBuf = msg.getBytes();
		        System.out.println("send: "+ipaddr+":"+port);
		        //Send to multicast IP address and port
		        InetAddress address = InetAddress.getByName(ipaddr);
		        
		        outPacket = new DatagramPacket(outBuf, outBuf.length, address, port);
		 
		        socket.send(outPacket);
		 
		        try {
		          Thread.sleep(500);
		        } catch (InterruptedException ie) {
		        }
		        
		    } catch (IOException ioe) {
		      return ioe.getMessage();
		    } finally {
		    	socket.close();
		    }
		    
		    return "Server sends : " + msg;
	}//sendMulticast - end

	public static String recieveMulticast(String ipaddr, int port){

		MulticastSocket socket = null;
	    DatagramPacket inPacket = null;
	    byte[] inBuf = new byte[256];
	    try {
	      //Prepare to join multicast group
	      socket = new MulticastSocket(port);
	      InetAddress address = InetAddress.getByName(ipaddr);
	      socket.joinGroup(address);
	      
	      while (true) {
	   
	        inPacket = new DatagramPacket(inBuf, inBuf.length);
	        socket.receive(inPacket);
	        String msg = new String(inBuf, 0, inPacket.getLength());
//	        return ""+inPacket.getAddress()+" : " + msg+"\n";
	        return msg;
	  
	      }
	      
	    } catch (IOException ioe) {
	    	ioe.printStackTrace();
	    	System.out.println(ioe.getMessage());
	    	return "Error: "+ioe.getMessage();
	    } finally {
	    	socket.close();
	    }
		
	}


}
