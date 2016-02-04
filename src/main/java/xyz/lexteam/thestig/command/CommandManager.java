/*
 * This file is part of TheStig, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
                this.commands.get(command).call(event.getChannel(), messageSplit);
            }
        }
    }
}
