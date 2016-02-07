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

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.lib.net.engio.mbassy.listener.Handler;
import xyz.lexteam.thestig.Main;
import xyz.lexteam.thestig.module.IModule;

public class BridgeModule implements IModule {

    @Handler
    public void onMessageEvent(ChannelMessageEvent event) {
        if (!event.getActor().getNick().equalsIgnoreCase("TheStig")) { // TODO: this is shit
            if (Main.INSTANCE.getConfig().getBridges().containsKey(
                    event.getClient().getServerInfo().getNetworkName().get() + "-" + event.getChannel().getName())) {
                String secondaryChannel = Main.INSTANCE.getConfig().getBridges().get(event.getClient().getServerInfo
                        ().getNetworkName().get() + "-" + event.getChannel().getName());
                String[] split = secondaryChannel.split("-");
                Main.INSTANCE.getServers().get(split[0]).getChannel(split[1]).get().sendMessage("[" + event.getActor
                        ().getNick() + "] " +event.getMessage());
            }
        }
    }

    @Override
    public String getName() {
        return "bridge";
    }

    @Override
    public void onEnable() {

    }
}
