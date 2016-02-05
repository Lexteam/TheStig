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
package xyz.lexteam.thestig.data;

import java.util.List;
import java.util.Optional;

/**
 * Represents the config model.
 */
public class ConfigModel {

    private DatabaseModel database;
    private List<ServerModel> servers;
    private CommandModel commands;
    private List<String> enabledModules;

    public DatabaseModel getDatabase() {
        return this.database;
    }

    public List<ServerModel> getServers() {
        return this.servers;
    }

    public CommandModel getCommands() {
        return this.commands;
    }

    public List<String> getEnabledModules() {
        return this.enabledModules;
    }

    public static class CommandModel {

        private String prefix;
        private List<String> enabled;

        public String getPrefix() {
            return this.prefix;
        }

        public List<String> getEnabled() {
            return this.enabled;
        }
    }

    public static class DatabaseModel {

        private String uri;

        public String getUri() {
            return this.uri;
        }
    }

    public static class ServerModel {

        private String host;
        private String serverPassword;
        private int port;
        private boolean ssl;
        private String username;
        private String nickname;

        public String getHost() {
            return this.host;
        }

        public Optional<String> getServerPassword() {
            return Optional.ofNullable(this.serverPassword);
        }

        public int getPort() {
            return this.port;
        }

        public boolean isSsl() {
            return this.ssl;
        }

        public String getUsername() {
            return this.username;
        }

        public String getNickname() {
            return this.nickname;
        }
    }
}
