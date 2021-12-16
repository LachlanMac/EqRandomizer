package EverquestUtility.discord;

import javax.security.auth.login.LoginException;

import EverquestUtility.Database.EQDao;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class DiscordBot {

	private JDA jda;
	private static DiscordBot instance;
	public static final String TOKEN = "ODIzNjA1NTg0MTAwMTk2NDAy.YFjQeQ.IMlGNYN_KKipCVJc_5hOJo2uaXY";
	private EQDao ref;

	public static DiscordBot GetInstance() {

		if (instance == null) {
			instance = new DiscordBot();

		}

		return instance;

	}

	private DiscordBot() {

	}

	// DiceBot.GetInstance();

	public void initJDA(EQDao ref) {
		this.ref = ref;
		try {
			jda = JDABuilder.createDefault(TOKEN).addEventListeners(new DiscordBotListener()).build();
		} catch (LoginException e) {
			System.out.println(e.getMessage());
		}
	}

	// get JDA Instance
	public JDA getJDA() {
		return jda;
	}

	public EQDao getDAO() {
		return ref;
	}

}
