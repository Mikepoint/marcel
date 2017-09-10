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
        }

        return null;
    }
}
