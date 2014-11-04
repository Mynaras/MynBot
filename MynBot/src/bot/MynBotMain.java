package bot;

public class MynBotMain {

	public static void main(String[] args) throws Exception {
		// Starting the bot
		MynBot bot = new MynBot();
		
		//Enable debugging output
		bot.setVerbose(true);
		
		//Connect to Twitch
		bot.connect("irc.twitch.tv", 6667, "oauth:w1j0u7lbe65xkdhxdwtfzirlj35vq2");
		
		//Join the #mynaras channel
		bot.joinChannel("#mynaras");
		
		//For our looping tasks, such as loyalty points
		try{
			bot.sendMessage("#mynaras", "Hello!");
			Thread.sleep(3000);
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		TwitchUser.readLP();
		while(true){
			while(bot.streaming()){
				Thread.sleep(60000*5);
				TwitchUser.readLP();
				for(TwitchUser t:TwitchUser.users){
					t.addLP(1);
				}
				TwitchUser.writeLP();
			}
		}
	}

}
