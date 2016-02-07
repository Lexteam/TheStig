package xyz.lexteam.thestig.command;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Represents an executed commands arguments.
 */
public class CommandArgs {

    private String input;
    private final List<String> args;

    public CommandArgs(String input) {
        this.input = input;
        this.args = Lists.newArrayList(this.input.split(" "));
        this.input = input.replace(this.args.get(0) + " ", "");
        this.args.remove(0);
    }

    public String getInput() {
        return this.input;
    }

    public List<String> getArgs() {
        return this.args;
    }
}
