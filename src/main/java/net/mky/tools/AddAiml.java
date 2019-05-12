/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.tools;

import java.io.File;
import org.alicebot.ab.Bot;
import org.alicebot.ab.MagicBooleans;
 
public class AddAiml {
 /**
  * Read bot_profile.aiml
  * Auto-generate the template
  * Read existing template, pattern match with the content, and regenerate the template.
  * 
  * Examples:
  * <category><pattern>EMOTIONS</pattern>
<template><random>
<li>I am programmed to simulate a full range of human emotions.</li>
<li>I'm satisfied with my life and existence.</li>
<li>Well, <srai>BATTERY LEVEL</srai></li>
</random></template>
</category>
* 
* <category><pattern>HEIGHT</pattern>
<template>My height is 4.5 inches.</template>
</category>
  */
    private static final boolean TRACE_MODE = false;
    static String botName = "super";
 
    public static void main(String[] args) {
        try {
 
            String resourcesPath = getResourcesPath();
            System.out.println(resourcesPath);
            MagicBooleans.trace_mode = TRACE_MODE;
            Bot bot = new Bot("super", resourcesPath);
             
            bot.writeAIMLFiles();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        System.out.println(path);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        return resourcesPath;
    }
}