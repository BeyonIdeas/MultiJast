package dme.multijast;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultEditorKit;

public class MultiJast extends JFrame implements ActionListener, KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int PORT = 8888;
	private static final String IPADDR = "224.0.0.1";
	//About messages
	private static final String about = 
			"<html>" +
			"<body>" +
			"<hr>" +
			"<h3>&nbsp;MultiJast&nbsp</h3>" +
			"<p>Sends multicast messages to all<br>" +
			"computers in the local network.&nbsp" +
			"<p>&nbsp;It's sent to 224.0.0.1:8888</p>" +
			"<hr>" +
			"<p>&nbsp;Version: 1.0-dev&nbsp</p>" +
			"<p>&nbsp;Date: 2013&nbsp</p>" +
			"<p>&nbsp;Author: David Ervideira&nbsp</p>" +
			"<p>&nbsp;Contact: david.ervideira@beyonideas.com&nbsp</p>" +
			"<hr>" +
			"<p>&nbsp;BeyonIdeas&nbsp<br>" +
			"<p>&nbsp;www.beyonideas.com&nbsp</p>" +
			"<hr>" +
			"</body>" +
			"</html>";
	
	/* Menu */
	JMenuBar mb;
	JMenu Menu, Edit;
	JMenuItem item_clean, item_nick, item_about, item_exit,
			  item_copy, item_cut, item_paste; 
	/* Menu - end */
	String nickname;
	JPanel center, south;
	JTextField jtf;
	static JTextPane jtp;
	JScrollPane jsp;
	JButton bt_send;
	
	static ArrayList<String> log = new ArrayList<String>();
	
	public MultiJast(){
		
		super(""+getComputerName()+" - MultiJast");
		
		/* System Look and Feel */

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
		}
		
		/* System Look and Feel - end */
		
		/* Menu */
		Menu = new JMenu("Menu");
		
		item_clean = new JMenuItem("Clear");
		item_clean.addActionListener(this);
		Menu.add(item_clean);
		
		Menu.addSeparator();
		
		item_nick = new JMenuItem("Set Name");
		item_nick.addActionListener(this);
		Menu.add(item_nick);
		
		Menu.addSeparator();
		
		item_about = new JMenuItem("About");
		item_about.addActionListener(this);
		Menu.add(item_about);
		
		Menu.addSeparator();
		
		item_exit = new JMenuItem("Exit");
		item_exit.addActionListener(this);
		Menu.add(item_exit);
		
        Edit = new JMenu("Edit");
        item_copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        item_copy.setText("Copy");
        item_copy.setMnemonic(KeyEvent.VK_C);
        Edit.add(item_copy);

        item_cut = new JMenuItem(new DefaultEditorKit.CutAction());
        item_cut.setText("Cut");
        item_cut.setMnemonic(KeyEvent.VK_T);
        Edit.add(item_cut);


        item_paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        item_paste.setText("Paste");
        item_paste.setMnemonic(KeyEvent.VK_P);
        Edit.add(item_paste);
		
		mb = new JMenuBar();
		mb.add(Menu);
		mb.add(Edit);
		setJMenuBar(mb);
		/* Menu - end */
		
		center = new JPanel(new GridLayout(1,1));
		jtp = new JTextPane();
		//Update Caret position to follow messages
		DefaultCaret caret = (DefaultCaret)jtp.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		jtp.setEditable(false);
		jtp.setMinimumSize( new Dimension(300, 200) );
		jsp = new JScrollPane(jtp);
		jsp.setBorder( new MatteBorder(1,5,1,1, Color.DARK_GRAY) );
		jsp.setAutoscrolls(true);
		center.add(jsp);
		center.setBorder( new TitledBorder("Recieve") );
		getContentPane().add(center, BorderLayout.CENTER);
		
		south = new JPanel();
		jtf = new JTextField(20);
		jtf.setBorder( new MatteBorder(1,5,1,1, Color.DARK_GRAY) );
		jtf.addKeyListener(this);
		south.add(jtf);
		bt_send = new JButton("Send");
		bt_send.addActionListener(this);
		south.add(bt_send);
		south.setBorder( new TitledBorder("Send") );
		getContentPane().add(south, BorderLayout.SOUTH);
		
		//get focus to sender textfield
		this.addWindowListener( new WindowAdapter() {
		    public void windowOpened( WindowEvent e ){
		    	jtf.requestFocus();
		    }
		});
		
		new Thread(new Runnable() {
			
			public void run() {
				
				while (true) {
					jtp.setText(jtp.getText()+Multicast.recieveMulticast(IPADDR, PORT));
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						System.err.println("Error recieving: "+e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}).start();
		nickname = getComputerName();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(320, 280);
		setLocationRelativeTo(null);
		
	}
	
	
//	public static String sendMulticast(String msg){
//		
//		 DatagramSocket socket = null;
//		 DatagramPacket outPacket = null;
//		 byte[] outBuf;
//		 
//		    try {
//		        socket = new DatagramSocket();
//		        
//		        msg = msg.trim();
//		        outBuf = msg.getBytes();
//		        System.out.println("send: "+IPADDR+":"+PORT);
//		        //Send to multicast IP address and port
//		        InetAddress address = InetAddress.getByName(IPADDR);
//		        
//		        outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
//		 
//		        socket.send(outPacket);
//		 
//		        try {
//		          Thread.sleep(500);
//		        } catch (InterruptedException ie) {
//		        }
//		        
//		    } catch (IOException ioe) {
//		      return ioe.getMessage();
//		    } finally {
//		    	socket.close();
//		    }
//		    
//		    return "Server sends : " + msg;
//	}//sendMulticast - end
//	
//	public static void recieveMulticast(){
//        //System.out.println(IPADDR+" "+PORT);
//
//		MulticastSocket socket = null;
//	    DatagramPacket inPacket = null;
//	    byte[] inBuf = new byte[256];
//	    try {
//	      //Prepare to join multicast group
//	      socket = new MulticastSocket(PORT);
//	      InetAddress address = InetAddress.getByName(IPADDR);
//	      socket.joinGroup(address);
//	    	
//	      while (true) {
//	   
//	        inPacket = new DatagramPacket(inBuf, inBuf.length);
//	        socket.receive(inPacket);
//	        String msg = new String(inBuf, 0, inPacket.getLength());
//	        jtp.setText(jtp.getText()+""+inPacket.getAddress()+" : " + msg+"\n");
//	        log.add(""+inPacket.getAddress()+" : " + msg);
//	  
//	      }
//	    } catch (IOException ioe) {
//	    	ioe.printStackTrace();
//	      System.out.println(ioe.getMessage());
//	    }
//		
//	}
	
	public static void writeFile(String file, String data) {
		BufferedWriter out = null;
		FileWriter fs = null;
		try {
			 fs = new FileWriter(file);
			 out = new BufferedWriter(fs);
		     out.write(data);
		} catch (IOException e) {
			System.err.println("Error writing file: "+e.getMessage());
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				System.err.println("Error closing file: "+e.getMessage());
			}
		}
	}
	
	public static void closeApp(String file){
		String data = "";
		for(int i=0; i<log.size(); i++)
			data += log.get(i);
		writeFile(file, data);		
	}
	
	public static String getComputerName(){
		String hostname = "Unknown";

		try {
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		    return hostname;
		}
		catch (UnknownHostException ex) {
		    System.err.println("Hostname can not be resolved");
		    return "";
		}
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==10)
			if(!jtf.getText().isEmpty()){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm");
						String time = date.format(new Time(System.currentTimeMillis()));
						String os = System.getProperty("os.name");
						Multicast.sendMulticast(nickname+" with "+os+" at "+time+":\n"+jtf.getText()+"\n", IPADDR, PORT);
						jtf.setText(" ");
						
					}
				}).start();
				
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==bt_send)
			if(!jtf.getText().isEmpty()){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm");
						String time = date.format(new Time(System.currentTimeMillis()));
						String os = System.getProperty("os.name");
						Multicast.sendMulticast(nickname+" with "+os+" at "+time+"\n"+jtf.getText()+"\n", IPADDR, PORT);
						jtf.setText(" ");
						
					}
				}).start();
			}
		
		if(e.getSource()==item_nick){
			nickname = JOptionPane.showInputDialog("Name to set:");
			this.setTitle(nickname+" - MultiJast");
		}
		
		if(e.getSource()==item_clean)
			jtp.setText("");
		
		if(e.getSource()==item_about)
			JOptionPane.showMessageDialog(null, about, "About", JOptionPane.INFORMATION_MESSAGE);
		
		if(e.getSource()==item_exit){
			closeApp("log.txt");
			System.exit(0);
		}
		
	}
	
	
	public static void main(String [] args){
		
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	        	MultiJast app = new MultiJast();
	            app.setVisible(true);
	            app.addWindowListener( new WindowAdapter() {
	            	
	            	public void windowClosing(WindowEvent e)
	            	{
	            			closeApp("log.txt");
	            			System.exit(0);
	            	}//windowCloing
				});//addWindowListener
	         }//run
	      });//invokeLater
		
	}

}
