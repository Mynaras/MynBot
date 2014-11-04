package bot;
import org.jibble.pircbot.*;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.*;

public class MynBot extends PircBot {

	private MessageParser mp;
	private int counter =0; // Used to keep track of messages and users
	private Date lastMessage;
	
	public MynBot(){
		this.setName("MynBot");
		addmp();
		lastMessage = new Date();
	}
	public boolean streaming(){
		/**Gson gson = new Gson();
		try{
			URL stream = new URL("https://api.twitch.tv/kraken/streams/radu_hs");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(stream.openStream()));
			Stream s = gson.fromJson(in, Stream.class);
			System.out.println(s.toString());
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}*/
		try{
			URL url = new URL("https://api.twitch.tv/kraken/streams/aydren");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(url.openStream()));
			String json = in.readLine();
			in.close();
			Scanner read = new Scanner(json);
			read.useDelimiter("\"");
			while(read.hasNext()){
				String next = read.next();
				if(next.equalsIgnoreCase("stream")){
					if(read.next().equalsIgnoreCase(":null}")){
						read.close();
						//System.out.println("DEBUG:Not Streaming");
						return false;
					}
				}
			}
			read.close();
			//System.out.println("DEBUG:Streaming");
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	public void onUserList(String channel, User[] users){
		TwitchUser.users=new TwitchUser[users.length];
		for(int i=0;i<users.length;i++){
			TwitchUser.users[i] = new TwitchUser(users[i]);
		}
	}
	public void onMessage(String channel, String sender, String login, String hostname, String message){
		if(new Date().getTime() - lastMessage.getTime() > 3000||counter == 5){
			getUsers(channel);
			TwitchUser.writeUsers();
		}
		else{
			counter++;
		}
		int cmd = mp.parseCommand(message);
		if(cmd==-1){
			//Do nothing
		}
		else if(sender.equalsIgnoreCase("mynbot")){
			//Do nothing
		}
		else{
			execute(cmd, channel, sender);
		}
		lastMessage = new Date();
		addmp();
	}
	private void addmp(){
		try {
			this.mp = new MessageParser(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void execute(int cmd, String channel, String sender){
		if(sender.equalsIgnoreCase("mynbot")){
			cmd=-1;
		}
		switch(cmd){
		case -1: break;
		case 0:
			Date date = new Date();
		    SimpleDateFormat simpDate;
		    simpDate = new SimpleDateFormat("hh:mm:ss a");
			sendMessage(channel, String.format(sender+mp.messages.get(cmd),simpDate.format(date)));
			break;
		case 1:sendMessage(channel, "Hey "+sender+". "+mp.messages.get(cmd));
			break;
		case 4:
			if(sender.equalsIgnoreCase("mynaras")){
				sendMessage(channel, mp.messages.get(cmd));
				try{
					Thread.sleep(100);
				}catch(Exception e){
					e.printStackTrace();
				}
				disconnect();
				System.exit(0);
			}
		case 5:
			sendMessage(channel, String.format(mp.messages.get(cmd), sender, TwitchUser.getLP(sender)));
			break;
		default:
			sendMessage(channel, mp.messages.get(cmd));
			break;
		}
	}
	/**
	private void execute(String command, String channel, String message, String sender){
		if(command.equalsIgnoreCase("!time")){
			//String[] date = new java.util.Date().toString().split(" ");
			Date date = new Date();
		    SimpleDateFormat simpDate;
		    simpDate = new SimpleDateFormat("hh:mm:ss a");
		    System.out.println(simpDate.format(date));
			sendMessage(channel, sender+": Mynaras's current time is "+simpDate.format(date)+" EDT");
		}
		else if(command.equalsIgnoreCase("!help")){
			String output = "MynBot Help: Available commands are: !time, !loyalty, !schedule, !mypoints";
			sendMessage(channel, output);
		}
		else if(command.equalsIgnoreCase("!schedule")){
			String output = "Mynaras has a schedule! Check out his Google calendar at http://goo.gl/ypMbkf";
			sendMessage(channel, output);
		}
		else if(command.equalsIgnoreCase("!loyalty")){
			String output = "Viewers gain 1 loyalty point every 5 minutes while Mynaras is live! Check your total with !mypoints";
			sendMessage(channel, output);
		}
		else if(command.equalsIgnoreCase("!killbot")&&sender.equalsIgnoreCase("mynaras")){
			sendMessage(channel, "I'll be back.");
			TwitchUser.writeLP();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			disconnect();
			System.exit(0);
		}
		else if(command.equalsIgnoreCase("!mypoints")){
			TwitchUser.readLP();
			String output = "Hi "+sender+", you have "+TwitchUser.getLP(sender)+" loyalty points!.";
			sendMessage(channel, output);
		}
		else if(command.equalsIgnoreCase("!lp4all")&&sender.equalsIgnoreCase("mynaras")){
			TwitchUser.readLP();
			for(int i=0;i<TwitchUser.users.length;i++){
				TwitchUser.users[i].addLP(1);
			}
			TwitchUser.writeLP();
		}
	}*/
}
