package bot;
/**
 * This object acts as a filter to find chat commands, so the bot itself doesn't
 * get bogged down
 * @author Will
 * @version 2.0
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
public class MessageParser {
	//private MynBot bot;
	private File commandFile;
	protected ArrayList<String> commands = new ArrayList<String>();
	protected ArrayList<String> messages = new ArrayList<String>();
	
	public MessageParser(MynBot bot) throws Exception{
		//this.bot = bot;
		this.commandFile = new File("C:\\MynBot\\commands.txt");
		Scanner a = new Scanner(commandFile);
		while(a.hasNextLine()){
			commands.add(a.nextLine());
			messages.add(a.nextLine());
		}
		a.close();
	}
	public int parseCommand(String message){
		for(String foo:commands){
			if(foo.contains("!")){
				String[] words = message.split(" ");
				for(int i=0;i<words.length;i++){
					if(words[i].contains("!")){
						for(int j=0;j<commands.size();j++){
							if(words[i].equalsIgnoreCase(commands.get(j))){
								return j; //Returns the first properly formatted command's number
							}
						}
					}
				}
				return -1; //No command
			}
			else{
				return -1; //No command
			}
		}
		return 0;
	}
	/**
	public String parseCommand(String message){
		if(message.contains("!")){
			String[] words = message.split(" ");
			for(int i=0;i<words.length;i++){
				if(words[i].contains("!")){
					for(int j=0;j<commands.size();j++){
						if(words[i].equalsIgnoreCase(commands.get(j))){
							return commands.get(j); //Returns the first properly formatted command
						}
					}
				}
			}
			return "!!"; //No command
		}
		else{
			return "!!"; //No command
		}
	}
	*/
}
