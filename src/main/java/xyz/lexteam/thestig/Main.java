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
package xyz.lexteam.thestig;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.ClientBuilder;
import xyz.lexteam.thestig.command.CommandManager;
import xyz.lexteam.thestig.data.ConfigModel;
import xyz.lexteam.thestig.module.IModule;
import xyz.lexteam.thestig.module.logging.LoggingModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * The application entry-point.
 */
public final class Main {

    public static final Gson GSON = new Gson();
    public static Main INSTANCE;

    private ConfigModel config;
    private List<IModule> modules = Lists.newArrayList();
    private List<IModule> enabledModules = Lists.newArrayList();
    private CommandManager commandManager;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private List<Client> servers = Lists.newArrayList();

    private Main() throws FileNotFoundException {
        // config
        this.config = GSON.fromJson(new BufferedReader(new FileReader(new File("config.json"))), ConfigModel.class);

        // mongo
        MongoClientURI clientURI = new MongoClientURI(this.config.getDatabase().getUri());
        this.mongoClient = new MongoClient(clientURI);
        this.mongoDatabase = this.mongoClient.getDatabase(clientURI.getDatabase());

        // commands
        this.commandManager = new CommandManager();

        // modules
        this.modules.add(new LoggingModule());

        for (IModule module : this.modules) {
            if (this.config.getEnabledModules().contains(module.getName())) {
                this.enabledModules.add(module);
            }
        }

        // irc
        for (ConfigModel.ServerModel serverModel : this.config.getServers()) {
            ClientBuilder clientBuilder = Client.builder();
            clientBuilder.serverHost(serverModel.getHost());
            clientBuilder.serverPort(serverModel.getPort());
            if (serverModel.getServerPassword().isPresent()) {
                clientBuilder.serverPassword(serverModel.getServerPassword().get());
            }
            clientBuilder.secure(serverModel.isSsl());
            clientBuilder.user(serverModel.getUsername());
            clientBuilder.nick(serverModel.getNickname());
            //clientBuilder.listenOutput(System.out::println);

            Client client = clientBuilder.build();

            for (IModule module : this.enabledModules) {
                client.getEventManager().registerEventListener(module);
            }
            client.getEventManager().registerEventListener(this.commandManager);

            this.servers.add(client);
        }
    }

    public ConfigModel getConfig() {
        return this.config;
    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return this.mongoDatabase;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public static void main(String[] args) throws FileNotFoundException {
        INSTANCE = new Main();
    }
}
