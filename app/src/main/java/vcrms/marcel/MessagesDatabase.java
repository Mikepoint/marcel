package vcrms.marcel;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessagesDatabase {
	private static ArrayList<String> negations;
	private static ArrayList<String> unnecessaryWords;
	private static ArrayList<String> actions;
	private static ArrayList<String> greetings;
	private static ArrayList<String> helpRequests;
	private static ArrayList<String> categories;
	private Map<String, Product> products;
 
	
	public void initialize() {
		negations = new ArrayList<String>();
		unnecessaryWords = new ArrayList<String>();
		actions = new ArrayList<String>();
		categories = new ArrayList<String>();
		greetings = new ArrayList<String>();
		helpRequests = new ArrayList<String>();
		products = new HashMap<String, Product> ();
		
		//negations.add("don't know");
		negations.add("no");
		negations.add("not");
		negations.add("don't");
		//negations.add("don't care");
		//negations.add("not interested");
		negations.add("skip");
		negations.add("next");
		
		
		unnecessaryWords.add("i");
		unnecessaryWords.add("a");
		unnecessaryWords.add("an");
		unnecessaryWords.add("in");
		unnecessaryWords.add("the");
		unnecessaryWords.add("some");
		unnecessaryWords.add("any");
		unnecessaryWords.add("to");
		//unnecessaryWords.add("");
		
		actions.add("buy");
		actions.add("purchase");
		actions.add("want");
		actions.add("find");
		actions.add("search");
		actions.add("remove");
		actions.add("delete");
		actions.add("back");
		//actions.add("");
		
		greetings.add("hello");
		greetings.add("hye");
		greetings.add("hi ");
		greetings.add("how are you");
		greetings.add("good afternoon");
		greetings.add("good morning");
		greetings.add("good evening");
		
		helpRequests.add("help");
		helpRequests.add("usage");
		helpRequests.add("how do work");
		helpRequests.add("how you work");
		helpRequests.add("how to");
		helpRequests.add("how do you work");
		helpRequests.add("how can i use you");
		helpRequests.add("instructions");
		
		categories.add("laptop");
		categories.add("phone");
		//categories.add("tablet");
		//categories.add("games");
		
		addProducts();
	}
	
	private void addProducts() {
		//String price, String size, String memory, String brand, String platform, String image) {
		products.put("galaxy s7", new Product("phone", "three", "samsung", ""));
		products.put("galaxy s5", new Product("phone", "two", "samsung", ""));
		products.put("iphone 6", new Product("phone", "two", "apple", ""));
		products.put("iphone 7", new Product("phone", "three", "apple", ""));
		products.put("hp probook", new Product("laptop", "8", "hp", ""));

	}

	public boolean isHelpRequests(String message) {
		for (String helpRequest : helpRequests) {
			if (message.contains(helpRequest)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isGreeting(String message) {
		for (String greeting : greetings) {
			if (message.contains(greeting)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isRelevant(String message) {
		if (unnecessaryWords.contains(message)) {
			return false;
		}
		return true;
	}
	
	public boolean isNegation(String message) {
		for (String negation : negations) {
			if (message.contains(negation)) {
				return true;
			}
		}
		return false;
	}
	
	public String isAction(String message) {
		for (String action : actions) {
			if (message.contains(action)) {
				return action;
			}
		}
		return null;
	}
	
	public String isCategory(String message) {
		if (categories.contains(message)) {
			return message;
		}
		return null;
	}
	
	public Map<String, Product> getProducts() {
		return products;
	}
	
	public ArrayList<String> containsProduct(String message) {
		ArrayList<String> matchedProducts = new ArrayList<String>();
		for (String name: products.keySet()) {
			if (name.contains(message)) {
				matchedProducts.add(name);
			}
		}
		return matchedProducts;
	}
	
	public ArrayList<String> productsOfCategory(String message) {
		ArrayList<String> matchedProducts = new ArrayList<String>();
		for (String key: products.keySet()) {
			if (products.get(key).getCategory().equals(message)) {
				matchedProducts.add(key);
			}
		}
		return matchedProducts;
	}

	public ArrayList<String> eliminateProducts(ArrayList<String> possibleMatches, String spec, String answer) {
		if (spec.equals("brand")) {
			for (int i = 0; i< possibleMatches.size(); i++) {
				if (possibleMatches.size() > 1 && !products.get(possibleMatches.get(i)).getBrand().contains(answer)) {
					Log.e("removeB", ""+possibleMatches.get(i));
					possibleMatches.remove(i);
					i--;
				}
			}
		}
		if (spec.equals("memory")) {
			for (int i = 0; i< possibleMatches.size(); i++) {
				if (possibleMatches.size() > 1 && !products.get(possibleMatches.get(i)).getMemory().contains(answer)) {
					Log.e("removeM", ""+possibleMatches.get(i));
					Log.e("removeM", ""+possibleMatches.size());
					possibleMatches.remove(i);
					i--;
				}
			}
		}
		for (int i = 0; i < possibleMatches.size(); i++) {
			Log.e("left", ""+possibleMatches.get(i));
		}
		return possibleMatches;
	}
}
