package vcrms.marcel;

public interface ValidatorInterface {
    public boolean isValid(String value);
    public void setValue(String value);
    public String getValue();
    public String askQuestion();
}
