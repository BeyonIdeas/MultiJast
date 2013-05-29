package dme.multijast;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;




public class MultiJastSettingsWindow extends JDialog implements ActionListener {
	
	/* Dados */
	private static final long serialVersionUID = 1L;
	
	JPanel center, south;
	JLabel lb_nickname, lb_group, lb_ipaddr, lb_port;
	JTextField tf_nickname, tf_group, tf_ipaddr, tf_port;
	JButton bt_ok, bt_cancel;
	
	String [] strduke = new String[] {"duke1.png","duke2.png","duke3.png","duke4.png","duke5.png"};
	ImageIcon meetDuke = new ImageIcon("images/"+strduke[new Random().nextInt(5)]);
	Font font = new Font("Arial", Font.BOLD, 12);
	
	int i=1;
	boolean b=false;
	 
	/* Construtor */
	public MultiJastSettingsWindow(){
		
		/* System Look and Feel */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | 
				 IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err.println("Error: "+e.getMessage());
		} 
		/* System Look and Feel - end */
		
		center = new JPanel(new GridLayout(4,2));
		lb_nickname = new JLabel("Nickname:");
		center.add(lb_nickname);
		tf_nickname = new JTextField(MultiJast.settings.nickname);
		center.add(tf_nickname);
		lb_group = new JLabel("Group:");
		center.add(lb_group);
		tf_group = new JTextField(MultiJast.settings.group);
		center.add(tf_group);
		lb_ipaddr = new JLabel("IP ADdress:");
		center.add(lb_ipaddr);
		tf_ipaddr = new JTextField(MultiJast.settings.ipaddr);
		center.add(tf_ipaddr);
		lb_port = new JLabel("Port:");
		center.add(lb_port);
		tf_port = new JTextField(String.valueOf(MultiJast.settings.port));
		center.add(tf_port);
		getContentPane().add(center);
		
		south = new JPanel(new FlowLayout(FlowLayout.CENTER));	
		bt_ok = new JButton("Ok");
		bt_ok.addActionListener(this);
		south.add(bt_ok);
		bt_cancel = new JButton("Cancel");
		bt_cancel.addActionListener(this);
		south.add(bt_cancel);
		getContentPane().add(south, BorderLayout.SOUTH);
			
		setResizable(false);
		setTitle("Settings");
		setVisible(true);
		setSize(280,220);
		setLocationRelativeTo(null);
		
	}
	
	/* Metodos */
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==bt_cancel){
			this.dispose();
		}
		if(e.getSource()==bt_ok){
			MultiJast.settings.setIpaddr(tf_ipaddr.getText().trim());
			MultiJast.settings.setPort(Integer.parseInt(tf_port.getText()));
			MultiJast.settings.setNickname(tf_nickname.getText().trim());
			MultiJast.settings.setGroup(tf_group.getText().trim());
			this.dispose();
		}
	}

	}//class - END
