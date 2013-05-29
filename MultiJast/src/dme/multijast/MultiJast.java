package dme.multijast;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultEditorKit;

public class MultiJast extends JFrame implements ActionListener, KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int PORT = 8888;
	private static final String IPADDR = "224.0.0.1";
	
	/* Menu */
	JMenuBar mb;
	JMenu Menu, Edit;
	JMenuItem item_settings, item_clean, item_about, item_exit,
			  item_copy, item_cut, item_paste; 
	/* Menu - end */
	
	static MultiJastSettingsData settings;
	static boolean settingsChanged = false;
	
	JPanel center, south;
	JTextField jtf;
	static JTextPane jtp;
	JScrollPane jsp;
	JButton bt_send;
	
	static ArrayList<String> log = new ArrayList<String>();
	
	public MultiJast(){
		
		super("Multi Jast");
		
		/* System Look and Feel */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | 
				 IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.println("Error: "+e.getMessage());
		} 
		/* System Look and Feel - end */
		
		settings = new MultiJastSettingsData();
		
		/* Menu */
		Menu = new JMenu("Menu");
		item_settings = new JMenuItem("Settings");
		item_settings.addActionListener(this);
		Menu.add(item_settings);
		
		item_clean = new JMenuItem("Clear");
		item_clean.addActionListener(this);
		Menu.add(item_clean);
		
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
		jtp.setEditable(false);
		jsp = new JScrollPane(jtp);
		jsp.setBorder( new MatteBorder(1,5,1,1, Color.DARK_GRAY) );
		jsp.setAutoscrolls(true);
		center.add(jsp);
		center.setBorder( new TitledBorder("Recieve") );
		getContentPane().add(center, BorderLayout.CENTER);
		
		south = new JPanel();
		jtf = new JTextField(27);
		jtf.setBorder( new MatteBorder(1,5,1,1, Color.DARK_GRAY) );
		jtf.addKeyListener(this);
		south.add(jtf);
		bt_send = new JButton("Send");
		bt_send.addActionListener(this);
		south.add(bt_send);
		south.setBorder( new TitledBorder("Send") );
		getContentPane().add(south, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(320, 240);
		setLocationRelativeTo(null);
		
	}
	
	
	public static String sendMulticast(String msg){
		
		 DatagramSocket socket = null;
		 DatagramPacket outPacket = null;
		 byte[] outBuf;
		 
		    try {
		        socket = new DatagramSocket();
		        
		        msg = msg.trim();
		        outBuf = msg.getBytes();
		        System.out.println("send: "+IPADDR+":"+PORT);
		        //Send to multicast IP address and port
		        InetAddress address = InetAddress.getByName(IPADDR);
		        
		        outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
		 
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
	
	public static void recieveMulticast(){
        //System.out.println(IPADDR+" "+PORT);

		MulticastSocket socket = null;
	    DatagramPacket inPacket = null;
	    byte[] inBuf = new byte[256];
	    try {
	      //Prepare to join multicast group
	      socket = new MulticastSocket(PORT);
	      InetAddress address = InetAddress.getByName(IPADDR);
	      socket.joinGroup(address);
	    	
	      while (true) {
	   
	        inPacket = new DatagramPacket(inBuf, inBuf.length);
	        socket.receive(inPacket);
	        String msg = new String(inBuf, 0, inPacket.getLength());
	        //System.out.println("From " + inPacket.getAddress() + " Msg : " + msg);
	        jtp.setText(jtp.getText()+""+inPacket.getAddress()+" : " + msg+"\n");
	        log.add(""+inPacket.getAddress()+" : " + msg);
	  
	        //JOptionPane.showMessageDialog(null, ""+inPacket.getAddress() + " : " + msg);
	      }
	    } catch (IOException ioe) {
	    	ioe.printStackTrace();
	      System.out.println(ioe.getMessage());
	    }
		
	}
	
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

	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==10)
			if(!jtf.getText().isEmpty()){
				sendMulticast(jtf.getText());
				jtf.setText(" ");
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
				sendMulticast(jtf.getText());
				jtf.setText(" ");
			}
		
		if(e.getSource()==item_settings){
			new MultiJastSettingsWindow();
			settingsChanged = true;
		}
		
		if(e.getSource()==item_clean)
			jtp.setText("");
		
		if(e.getSource()==item_about)
			new MultiJastAbout();
		
		if(e.getSource()==item_exit){
			closeApp("log.txt");
			System.exit(0);
		}
		
	}
	
	
	public static void main(String [] args){
		
		MultiJast app = new MultiJast();
		app.setVisible(true);
		recieveMulticast();
		
	}

}
