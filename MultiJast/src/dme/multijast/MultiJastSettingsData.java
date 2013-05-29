package dme.multijast;

public class MultiJastSettingsData {
	
	/* Settings Data */
	String ipaddr = "";
	int port = 0;
	String nickname = "";
	String group = "";
	/* Settings Data */
	
	public MultiJastSettingsData() {
		this.ipaddr = "224.0.0.1";
		this.port = 8888;
		this.nickname = "";
		this.group = "";
	}
	
	/* Set */
	public void setIpaddr(String s){
		this.ipaddr = s;
	}
	public void setPort(int p){
		this.port = p;
	}
	public void setNickname(String n){
		this.nickname = n;
	}
	public void setGroup(String g){
		this.group = g;
	}
	/* Set - end */
	/* Get */
	public String getIpaddr(){
		return this.ipaddr;
	}
	public int getPort(){
		return this.port;
	}
	public String getNickname(){
		return this.nickname;
	}
	public String getGroup(){
		return this.group;
	}
	/* Get - end */

}
