package vcrms.marcel;

import java.util.ArrayList;
import java.util.Arrays;

public class BrandConstraint implements ValidatorInterface {

    private ArrayList<String> brands = new ArrayList<String>(Arrays.asList("apple", "samsung", "hp"));

    private String value;

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isValid(String value) {
       return brands.contains(value.toLowerCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String askQuestion() {
        return "Which brand do you prefer?";
    }
}
