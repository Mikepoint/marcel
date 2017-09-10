package vcrms.marcel;

public class Product {
	private String category;
	private String memory;
	private String brand;
	private String image;
	
	public Product() {
		
	}
	
	public Product(String category, String memory, String brand, String image) {
		super();
		//this.price = price;
		this.category = category;
		this.memory = memory;
		this.brand = brand;
		//this.platform = platform;
		this.image = image;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
}
