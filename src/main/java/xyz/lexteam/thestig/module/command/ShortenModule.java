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
package xyz.lexteam.thestig.module.command;

import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.kitteh.irc.client.library.element.Channel;
import xyz.lexteam.thestig.Main;
import xyz.lexteam.thestig.command.CommandCallable;
import xyz.lexteam.thestig.module.IModule;

/**
 * The shorten command / module.
 */
public class ShortenModule implements IModule, CommandCallable {

    @Override
    public String getName() {
        return "shorten";
    }

    @Override
    public void onEnable() {
        Main.INSTANCE.getCommandManager().registerCommand(this, "shorten");
    }

    @Override
    public void call(Channel channel, String[] args) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("longUrl", args[1]);

        try {
            HttpResponse<JsonNode> response = Unirest.post("https://www.googleapis.com/urlshortener/v1/url/")
                    .header("accept", "application/json")
                    .body(Main.GSON.toJson(jsonObject))
                    .asJson();
            channel.sendMessage("Short URL - " + response.getBody().getObject().getString("id"));
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
