package com.emotibot.xychatlib.utils;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by ldy on 2017/2/22.
 */

public class XYlibCommandUtils {
    private static Map<String, Boolean> commands;

    private static Map<String, Boolean> getCommands() {
        if (commands == null) {
            synchronized (commands) {
                if (commands == null) {
                    commands = new Hashtable<>();
                }
            }
        }
        return commands;
    }

    private static void initCommands() {
        //commands.put();
    }
}
