package vcrms.marcel;

public class AnswerKeywords {
	private String action;
	private String category;
	private String product;
	private boolean negation;
	private String other;
	private boolean help;
	private boolean greeting;
	
	public boolean isHelp() {
		return help;
	}
	public void setHelp(boolean help) {
		this.help = help;
	}
	public boolean isGreeting() {
		return greeting;
	}
	public void setGreeting(boolean greeting) {
		this.greeting = greeting;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public boolean isNegation() {
		return negation;
	}
	public void setNegation(boolean negation) {
		this.negation = negation;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
	
}
