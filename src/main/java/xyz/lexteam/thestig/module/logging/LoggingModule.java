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
package xyz.lexteam.thestig.module.logging;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.lib.net.engio.mbassy.listener.Handler;
import xyz.lexteam.thestig.data.db.Database;
import xyz.lexteam.thestig.module.IModule;

/**
 * The logging module.
 */
public class LoggingModule implements IModule {

    @Handler
    public void onMessageEvent(ChannelMessageEvent event) {
        try {
            Document chatDocument = new Document();
            chatDocument.put("network", event.getClient().getServerInfo().getNetworkName().get());
            chatDocument.put("channel", event.getChannel().getName());
            chatDocument.put("type", LogType.MESSAGE);
            chatDocument.put("message", event.getMessage());

            MongoCollection chats = Database.INSTANCE.getMongoDatabase().getCollection("logs");
            chats.insertOne(chatDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Handler
    public void onUserJoin(ChannelJoinEvent event) {
        try {
            Document chatDocument = new Document();
            chatDocument.put("network", event.getClient().getServerInfo().getNetworkName().get());
            chatDocument.put("channel", event.getChannel().getName());
            chatDocument.put("type", LogType.JOIN);
            chatDocument.put("message", event.getUser().getNick());

            MongoCollection chats = Database.INSTANCE.getMongoDatabase().getCollection("logs");
            chats.insertOne(chatDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Handler
    public void onUserPart(ChannelJoinEvent event) {
        try {
            Document chatDocument = new Document();
            chatDocument.put("network", event.getClient().getServerInfo().getNetworkName().get());
            chatDocument.put("channel", event.getChannel().getName());
            chatDocument.put("type", LogType.PART);
            chatDocument.put("message", event.getUser().getNick());

            MongoCollection chats = Database.INSTANCE.getMongoDatabase().getCollection("logs");
            chats.insertOne(chatDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "logging";
    }

    @Override
    public void onEnable() {
    }

    public enum LogType {
        MESSAGE,
        JOIN,
        PART
    }
}
