package vcrms.marcel;

import android.util.Log;

import java.util.ArrayList;

public class AnswerProcessor {
	MessagesDatabase msgDb;

	public AnswerProcessor() {
		super();
		
		msgDb = new MessagesDatabase();
		msgDb.initialize();
	}
	
	public AnswerKeywords generateAnswerKeywords(String recievedMessage) {
		String[] words = recievedMessage.split(" ");
		AnswerKeywords answer = new AnswerKeywords();
		if (msgDb.isHelpRequests(recievedMessage)) {
			answer.setHelp(true);
			return answer;
		}
		if (msgDb.isNegation(recievedMessage)) {
			answer.setNegation(true);
			return answer;
		}
		if (msgDb.isGreeting(recievedMessage)) {
			answer.setGreeting(true);
		}
		ArrayList<String> possibleMatches = new ArrayList<String>();
		boolean processedMessage = false;
		
		for (String word: words) {
			if (!msgDb.isRelevant(word)) {
				continue;
			}
			if (!possibleMatches.isEmpty()) {
				for (String match : possibleMatches) {
					if (!match.contains(word)) {
						if (possibleMatches.size() > 1) {
							possibleMatches.remove(match);
						} else {
							answer.setProduct(match);
							processedMessage = true;
							continue;
						}
					}
				}
				if (possibleMatches.size() == 1) {
					answer.setProduct(possibleMatches.get(0));
					processedMessage = true;
				}
			}

			String action;
			if ((action = msgDb.isAction(word)) != null) {
				answer.setAction(action);
				processedMessage = true;
				continue;
			}
			if (msgDb.isCategory(word) != null) {
				answer.setCategory(word);
				processedMessage = true;
				continue;
			}
			possibleMatches = msgDb.containsProduct(word);
			if (possibleMatches.size() == 1) {
				answer.setProduct(possibleMatches.get(0));
				processedMessage = true;
			}
			
			
		}
		if (!possibleMatches.isEmpty()) {
			answer.setProduct(possibleMatches.get(0));
			processedMessage = true;
		}

		if (processedMessage == false) {
			answer.setOther(recievedMessage);
		}
		return answer;
	}
}
