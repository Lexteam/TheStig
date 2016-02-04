package xyz.lexteam.thestig.command;

import com.google.common.collect.Maps;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.lib.net.engio.mbassy.listener.Handler;
import xyz.lexteam.thestig.Main;

import java.util.Map;

/**
 * The command manager.
 * It handles all the commands :D
 */
public class CommandManager {

    private Map<String, CommandCallable> commands = Maps.newHashMap();

    public void registerCommand(CommandCallable callable, String... aliases) {
        for (String alias : aliases) {
            this.commands.put(alias, callable);
        }
    }

    @Handler
    public void onMessageEvent(ChannelMessageEvent event) {
        if (event.getMessage().startsWith(Main.INSTANCE.getConfig().getCommandPrefix())) {
            String[] messageSplit = event.getMessage().split(" ");
            String command = messageSplit[0].substring(1);

            if (this.commands.containsKey(command)) {
                this.commands.get(command).call(messageSplit);
            }
        }
    }
}
