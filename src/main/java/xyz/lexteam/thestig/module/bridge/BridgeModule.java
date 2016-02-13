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
package xyz.lexteam.thestig.module.bridge;

import com.google.common.base.Preconditions;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.element.Channel;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.channel.ChannelCTCPEvent;
import org.kitteh.irc.lib.net.engio.mbassy.listener.Handler;
import xyz.lexteam.thestig.Main;
import xyz.lexteam.thestig.module.IModule;

public class BridgeModule implements IModule {

    @Handler
    public void onMessageEvent(ChannelMessageEvent event) {
        if (!isBot(event.getActor())) {
            BridgeData data = new BridgeData(event.getClient(), event.getChannel());
            if (data.isBridged()) {
                Channel channel = data.getSecondaryChannel();
                if (channel != null) {
                    channel.sendMessage("[" + event.getActor().getNick() + "] " + event.getMessage());
                }
            }
        }
    }

    @Handler
    public void onAction(ChannelCTCPEvent event) {
        if (!isBot(event.getActor()) && event.getMessage().startsWith("ACTION ")) {
            String actionMessage = getActionMessage(event.getMessage());
            BridgeData data = new BridgeData(event.getClient(), event.getChannel());
            if (data.isBridged()) {
                Channel channel = data.getSecondaryChannel();
                if (channel != null) {
                    sendAction(channel, event.getActor().getNick() + " " + actionMessage);
                }
            }
        }
    }

    private static void sendAction(Channel channel, String action) {
        channel.sendCTCPMessage("ACTION " + action);
    }

    private static String getActionMessage(String raw) {
        return raw.substring("ACTION ".length());
    }

    private static boolean isBot(User user) {
        return user.getNick().equalsIgnoreCase(user.getClient().getNick());
    }

    @Override
    public String getName() {
        return "bridge";
    }

    @Override
    public void onEnable() {

    }

    public static class BridgeData {

        private final String key;
        private String[] secondaryData;

        public BridgeData(Client client, Channel channel) {
            this.key = client.getServerInfo().getNetworkName().get() + "-" + channel.getName();
        }

        public boolean isBridged() {
            return Main.INSTANCE.getConfig().getBridges().containsKey(key);
        }

        public Client getSecondaryNetwork() {
            return Main.INSTANCE.getServers().get(getSecondaryData()[0]);
        }

        public Channel getSecondaryChannel() {
            Client client = getSecondaryNetwork();
            if (client == null) return null;
            return client.getChannel(getSecondaryData()[1]).orElse(null);
        }

        private String[] getSecondaryData() {
            Preconditions.checkState(isBridged(), "%s is not bridged", key);
            if (secondaryData == null) {
                String secondaryChannel = Main.INSTANCE.getConfig().getBridges().get(this.key);
                secondaryData = secondaryChannel.split("-");
            }
            return secondaryData;
        }
    }
}
