package bot;

import org.jibble.pircbot.User;
import java.io.*;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;

public class TwitchUser{
	private String nick, prefix;
	private int lp;
	protected static TwitchUser[] users;
	private static TwitchUser[] offlineUsers = new TwitchUser[0];
	
	public TwitchUser(User user){
		nick = user.getNick();
	}
	public TwitchUser(String nick, int lp){
		this.nick = nick;
	}
	public static void writeUsers(){
		try{
			File userList = new File("C:\\MynBot\\userList.txt");
			Writer out = new FileWriter(userList, true);
			for(int i =0; i<users.length;i++){
				out.write(System.lineSeparator());
				out.write(users[i].nick);
			}
			out.close();
			clean(userList);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void clean(File userList){
		try {
			Scanner keyboard = new Scanner(userList);
			ArrayList<String> users = new ArrayList<String>(1);
			users.add(keyboard.nextLine());
			while(keyboard.hasNextLine()){ //Removes Duplicate entries
				String temp = keyboard.nextLine();
				boolean dupe = false;
				for(int i=0;i<users.size();i++){
					if(users.get(i).equalsIgnoreCase(temp)){
						dupe = true;
					}
				}
				if(!dupe){
					users.add(temp);
				}
			}
			keyboard.close();
			Collections.sort(users);
			users.trimToSize();
			Writer out = new FileWriter(userList, false);
			out.write(users.get(0));
			for(int i=1; i<users.size();i++){
				out.write(System.lineSeparator());
				out.write(users.get(i));
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addLP(int value){
		this.lp+=value;
	}
	/**
	 * Reads lp values from file, overwrites current values.
	 * @param users
	 */
	public static void readLP(){
		File loyaltypoints = new File("C:\\MynBot\\LoyaltyPoints.csv");
		/**
		 * Fields: nick, loyalty points
		 */
		String[][] table;
		int repeaters=0;
		try{
			int lines = countLines("C:\\MynBot\\LoyaltyPoints.csv");
			table = new String[lines][2];Scanner in = new Scanner(loyaltypoints);
			for(int i=0;i<lines;i++){
				if(in.hasNextLine()){
					table[i] = in.nextLine().split(",");
				}
			}
			in.close();
			for(int i=0;i<users.length;i++){
				for(int j=0;j<lines;j++){
					if(users[i].nick.equalsIgnoreCase(table[j][0])){
						users[i].lp = Integer.parseInt(table[j][1]);
						repeaters++;
					}
					else{
						users[i].lp = 0;
					}
				}
			}
			if(lines>repeaters){
				offlineUsers = new TwitchUser[lines-repeaters];
				int index = 0;
				for(int i=0;i<lines;i++){
					for(int j=0;j<users.length;j++){
						if(users[j].nick.equalsIgnoreCase(table[i][0])){
							//Do nothing
						}
						else{
							offlineUsers[index] = new TwitchUser(table[i][0], Integer.parseInt(table[i][1]));
						}
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	/**
	 * Writes the current lp values to file. WARNING: Will overwrite previous
	 * file, ensure there is backup.
	 * Needs to maintain for offline users as well.
	 * @param users - an array of current users
	 */
	public static void writeLP(){
		File loyaltypoints = new File("C:\\MynBot\\LoyaltyPoints.csv");
		try {
			Writer out = new FileWriter(loyaltypoints);
			for(int i=0;i<users.length;i++){
				out.write(users[i].nick+","+users[i].lp+System.lineSeparator());
			}
			for(int i=0; i<offlineUsers.length;i++){
				out.write(offlineUsers[i].nick+","+offlineUsers[i].lp+System.lineSeparator());
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static int getLP(String sender){
		for(int i=0;i<users.length;i++){
			if(sender.equalsIgnoreCase(users[i].nick)){
				return users[i].lp;
			}
		}
		return 0;
	}
}
