package com.feifan.bp.home;

import com.feifan.bp.home.command.Command;

/**
 * Created by maning on 15/8/5.
 */
public class FunctionModel {
    private Command command;
    private String text;
    private int resId;

    public FunctionModel(Command cmd, String text, int resId) {
        this.command = cmd;
        this.text = text;
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
}
