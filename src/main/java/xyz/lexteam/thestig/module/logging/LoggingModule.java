package xyz.lexteam.thestig.module.logging;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.lib.net.engio.mbassy.listener.Handler;
import xyz.lexteam.thestig.Main;
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
            chatDocument.put("message", event.getMessage());

            MongoCollection chats = Main.INSTANCE.getMongoDatabase().getCollection("chats");
            chats.insertOne(chatDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "logging";
    }
}
