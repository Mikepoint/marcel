package vcrms.marcel;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Custom {
    private static ArrayList<String> jokes = new ArrayList<String>(Arrays.asList(
            "Two tabels sit in a bar. A query walks in and asks them: Can I join you?",
            "What does a programmer shout when he's sinking? ... F1! F1!",
            "What's the name of Irina Login's sister? ... Irina Logout."
    ));

    public static String match(String text) {
        if (text.toLowerCase().contains("joke")) {
            Random rand = new Random();
            return jokes.get(rand.nextInt(jokes.size()));
        } else if (text.toLowerCase().contains("hello") || text.toLowerCase().contains("hi") || text.toLowerCase().contains("greetings")) {
            return "Hi!";
        } else if (text.toLowerCase().contains("help")) {
            return "I am here to help you find products you need. You can ask me about a certain product or we can find the best choice together.";
        } else if (text.toLowerCase().contains("call") || text.toLowerCase().contains("baby")) {
            return "Seriously? This is how you're trying to be funny?";
        } else if (text.toLowerCase().contains("snow") || text.toLowerCase().contains("snowman") || text.toLowerCase().contains("build")) {
            return "It doesn't have to be a snowman.";
        } else if (text.toLowerCase().contains("programming") || text.toLowerCase().contains("language")) {
            return "My source code is written in Java using Android Studio.";
        }  else if (text.toLowerCase().contains("developers")) {
            return "My developers are VCRMS team.";
        } else if ((text.toLowerCase().contains("developer") || text.toLowerCase().contains("developers")) && text.toLowerCase().contains("one")) {
            return "My developers forbid me to do so because of something called TeamWork. I'm still trying to figure it out.";
        }

        return null;
    }
}
