package dme.multijast;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;




public class MultiJastAbout extends JDialog implements ActionListener {
	
	/* Dados */
	private static final long serialVersionUID = 1L;
	
	JTabbedPane tabs;
	JPanel center, centerbottom, centertop, south, panelApp, panelLang, panelDuke;
	JLabel lb1, lb2duke, lb2logo, lb3, lbApp, lbLang, lbDuke;
	JButton bt;
	
	String [] strAnimation = new String[] {"T1.gif","T2.gif","T3.gif","T4.gif","T5.gif","T6.gif","T7.gif","T8.gif","T9.gif","T10.gif"};
	ImageIcon duke = new ImageIcon("images/duke/wave/"+strAnimation[0]);
	
	ImageIcon logo = new ImageIcon("icon.png");
	
	String [] strduke = new String[] {"duke1.png","duke2.png","duke3.png","duke4.png","duke5.png"};
	ImageIcon meetDuke = new ImageIcon("images/"+strduke[new Random().nextInt(5)]);
	Font font = new Font("Arial", Font.BOLD, 12);
	
	FileReader reader;
	
	int i=1;
	boolean b=false;
	
	Thread trd1, trd2;
	Runnable r1, r2;
	 
	/* Construtor */
	public MultiJastAbout(){
		
		/**/ // System Look and Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {} 
		  catch (InstantiationException e1) {} 
		  catch (IllegalAccessException e1) {} 
		  catch (UnsupportedLookAndFeelException e1) {};
         /**/
		
		tabs = new JTabbedPane();
		
		center = new JPanel(new GridLayout(2,1));
		
		centertop = new JPanel(new GridLayout(1,3));
		lb1 = new JLabel(htmlRead("about/html/about1.html"));
		lb1.setFont(font);
		centertop.add(lb1);
		
		lb2logo = new JLabel(logo, JLabel.CENTER);
		centertop.add(lb2logo);
		lb2duke = new JLabel(duke, JLabel.CENTER);
		centertop.add(lb2duke);
		center.add(centertop);
		
		centerbottom = new JPanel(new GridLayout(1,1));
		lb3 = new JLabel(htmlRead("about/html/about2.html"));
		lb3.setFont(font);
		centerbottom.add(lb3);
		
		center.add(centerbottom);
		
		panelApp = new JPanel(new GridLayout(1,1));
		lbApp = new JLabel(htmlRead("about/html/app.html"));
		panelApp.add(lbApp);
		
		panelLang = new JPanel(new GridLayout(1,1));
		lbLang = new JLabel(htmlRead("about/html/lang.html"));
		panelLang.add(lbLang);
		
		panelDuke = new JPanel(new GridLayout(1,1));
		lbDuke = new JLabel(meetDuke, JLabel.CENTER);
		panelDuke.add(lbDuke);
		
		tabs.setFont( new Font("Arial", Font.BOLD, 12 ));
		tabs.setForeground(Color.DARK_GRAY);
		tabs.setBackground(Color.LIGHT_GRAY);
		
		
		tabs.addTab("Author", center);
		tabs.addTab("Aplication", panelApp);
		tabs.addTab("Language", panelLang);
		tabs.addTab("Meet the Duke", panelDuke);
		getContentPane().add(tabs, BorderLayout.CENTER);
		//getContentPane().add(center, BorderLayout.CENTER);
		
		south = new JPanel();
		
		bt = new JButton("Ok");
		bt.addActionListener(this);
		south.add(bt);
		getContentPane().add(south, BorderLayout.SOUTH);
		
		r1 = new Runnable(){

		    public void run(){
		        while(i<10) {
		        	
		        	duke = new ImageIcon("images/duke/wave/"+strAnimation[i]);
		     	    lb2duke.setIcon(duke);
		     	    //System.out.println(i);
		     	    if(i==9)
		     	    	i=0;
		     	    else
		     	    	i++;
		        	
		     	    try {	
		     	    	if(i==1)
		     	    		Thread.sleep(900);
		     	    	else
		     	    		Thread.sleep(150);
		        	}catch(InterruptedException e){};
		        	
		        }
		    }
			};			

			trd1 = new Thread(r1);
			trd1.start();
		
		addWindowListener(new WindowAdapter() {
		 public void windowClosing(WindowEvent e) {
				trd1.stop();
				dispose();
			  }
		});
			
		setResizable(false);
		setTitle("About");
		setVisible(true);
		setLocationRelativeTo(null);
		setSize(380,280);
		
	}
	
	/* Metodos */
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==bt){
			trd1.stop();
			dispose();
		}
	}
	
	public String htmlRead(String htmlpath){
		
		String strLine ="" ;
		String strOut ="" ;
		try{
			  FileInputStream fstream = new FileInputStream(htmlpath);
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));

			  while ((strLine = br.readLine()) != null)
				   strOut += strLine;

			  in.close();
			    }catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
			  }
		System.out.println(strLine);
		return strOut;
		
	}//htmlRead - END
		

	}//class - END