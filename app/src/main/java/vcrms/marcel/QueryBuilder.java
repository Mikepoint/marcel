package vcrms.marcel;

import java.util.ArrayList;
import java.util.Arrays;

public class QueryBuilder {

    private static ArrayList<String> categories = new ArrayList<String>(Arrays.asList("phone", "laptop"));

    private static String[] dbCategories = {"phone", "phone", "phone", "phone", "laptop", "laptop"};
    private static String[] dbBrands = {"apple", "apple", "samsung", "samsung", "hp", "dell"};
    private static String[] dbRam = {"2", "4", "2", "4", "8", "4"};
    private static String[] dbNames = {"iPhone 6", "iPhone 7", "Galaxy S7", "Galaxy S8", "HP ProBook", "Dell Inspire"};

    private String category;
    private final static int NO_SPECS = 2;
    public ArrayList<ValidatorInterface> validators = new ArrayList<ValidatorInterface>(Arrays.asList(
            new BrandConstraint(),
            new RamConstraint()
    ));
    public int currentValidator = -1;

    public void setCategory(String category) {
        this.category = category;
        this.currentValidator = 0;
    }

    public boolean nextSpec(String text) {
        String words[] = text.split(" ");
        for(String word: words) {
            if (validators.get(currentValidator).isValid(word)) {
                validators.get(currentValidator).setValue(word);
                currentValidator++;

                return true;
            }
        }

        return false;
    }

    public boolean isCompleted() {
        return currentValidator == NO_SPECS;
    }

    public String find() {
        for (int i = 0; i < dbCategories.length; i++) {
            if (dbCategories[i].toLowerCase().compareTo(category.toLowerCase()) == 0) {
                if (dbBrands[i].toLowerCase().compareTo(validators.get(0).getValue().toLowerCase()) == 0) {
                    if (dbRam[i].toLowerCase().compareTo(validators.get(1).getValue().toLowerCase()) == 0) {
                        return dbNames[i];
                    }
                }
            }
        }

        return null;
    }

    public static String factorize(String text) {
        String words[] = text.split(" ");
        for(String word: words) {
            if (categories.contains(word.toLowerCase())) {
                return word;
            }
        }

        return null;
    }

    public static String checkName(String text) {
        for(String name: dbNames) {
            if (text.toLowerCase().contains(name.toLowerCase())) {
                return name;
            }
        }

        return null;
    }

    public boolean isReady() {
        return currentValidator > -1;
    }

    public String custon() {
        return "";
    }
}
