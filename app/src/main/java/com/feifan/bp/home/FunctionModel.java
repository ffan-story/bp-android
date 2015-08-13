package com.feifan.bp.home;

import com.feifan.bp.home.command.Command;

/**
 * Created by maning on 15/8/5.
 */
public class FunctionModel {
    private Command command;
    private String text;
    private int resId;
    private String id;

    public FunctionModel(Command cmd, String text, String id, int resId) {
        this.command = cmd;
        this.text = text;
        this.id = id;
        this.resId = resId;
    }

    public Command getCommand() {
        return command;
    }

    public int getResId() {
        return resId;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }
}
