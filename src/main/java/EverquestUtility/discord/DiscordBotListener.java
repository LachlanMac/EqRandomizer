package EverquestUtility.discord;

import java.util.ArrayList;

import EverquestUtility.Database.Helpers.MobItemPacket;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class DiscordBotListener implements EventListener {
	public void sendHelp(MessageChannel channel) {

		channel.sendMessage("Command       arg           description\n"
				+ "-------------------------------------------------------\n"
				+ "!help         [none]                  Displays Command List\n").queue();
	}

	// Function that detects events fired off from the discord server

	public void onEvent(GenericEvent event) {

		// if the event is a message received event
		if (event instanceof MessageReceivedEvent) {
			MessageReceivedEvent rcvMsg = (MessageReceivedEvent) event;
			if (rcvMsg.isFromGuild()) {

				onMessageReceived(rcvMsg);
			}
		}
	}

	public void onMessageReceived(MessageReceivedEvent event) {
		// we don't want to ever respond to other bots
		if (event.getAuthor().isBot())
			return;

		Message message = event.getMessage();

		handleMessage(event.getTextChannel(), message.getContentRaw(), event.getAuthor().getIdLong());

	}

	public void handleMessage(TextChannel chan, String raw, long userId) {

		System.out.println(raw);

		if (raw.charAt(0) != '$' && raw.charAt(0) != '!')
			return;

		String[] tokens = raw.split("\\s+", 2);

		switch (tokens[0].trim().toLowerCase()) {
		case "$find":

			System.out.println(tokens[1]);

			ArrayList<MobItemPacket> results = DiscordBot.GetInstance().getDAO().findItem(tokens[1]);

			ArrayList<MobItemPacket> uniqueResults = new ArrayList<MobItemPacket>();

			for (MobItemPacket mip : results) {
				boolean exists = false;
				for (MobItemPacket mip2 : uniqueResults) {
					if (mip.getM().getId() == mip2.getM().getId() && mip.getZone().equals(mip2.getZone())) {
						exists = true;
					}
				}
				if (!exists) {
					uniqueResults.add(mip);
				}

			}

			StringBuilder sb = new StringBuilder();
			int sendCounter = 0;
			for (MobItemPacket s : results) {

				sb.append(s.getM().getName() + " in " + s.getZone() + " %[" + s.getChance() + "]\n");
				sendCounter++;
				if (sendCounter >= 10) {
					chan.sendMessage(sb.toString()).queue();
					sb = new StringBuilder();
				}
			}
			if (sb != null)
				chan.sendMessage(sb.toString()).queue();
			break;
		case "$help":
			System.out.println("HELP!");
			break;

		case "$test":
			chan.sendMessage("Test confirmed").queue();
			break;
		}

		// System.out.println(tokens.length);
		// #COMMAND [SAVE,EDIT,LIST,ROLL]

		// DiceRoll testRoll = new DiceRoll("8d6+2:hea");

		// chan.sendMessage(testRoll.createEmoteString()).queue();

	}

	public void handleError(TextChannel chan, String msg) {
		chan.sendMessage(msg).queue();
	}
}
