package vcrms.marcel;

public class RamConstraint implements ValidatorInterface {
    private String value;

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isValid(String value) {
        return true;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String askQuestion() {
        return "How manny GB of RAM do you want?";
    }
}
