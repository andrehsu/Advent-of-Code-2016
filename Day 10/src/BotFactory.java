import andre.adventofcode.input.Input;

import java.util.*;

/**
 * Created by andre on 12/12/2016.
 */
public class BotFactory {
	public static final List<String> input = Input.readAllLines("Day 10/input.txt");
	
	private final Map<String, Bot> bots = new HashMap<>();
	private final Map<String, List<Integer>> outputs = new HashMap<>();
	private final List<String> instructions;
	
	public BotFactory(List<String> instructions) {
		for (String instruction : instructions) {
			for (StringTokenizer stringTokenizer = new StringTokenizer(instruction); stringTokenizer.hasMoreTokens(); ) {
				String s = stringTokenizer.nextToken();
				
				if (s.equals("bot")) {
					if (!bots.containsKey(s)) {
						bots.put(stringTokenizer.nextToken(), new Bot());
					}
				} else if (s.equals("output")) {
					if (!bots.containsKey(s)) {
						outputs.put(stringTokenizer.nextToken(), new LinkedList<>());
					}
				}
			}
		}
		this.instructions = instructions;
	}
	
	public void run() {
		// add values to bot
		for (Iterator<String> iterator = instructions.iterator(); iterator.hasNext(); ) {
			String next = iterator.next();
			
			if (next.charAt(0) == 'v') {
				String[] tokens = next.trim().split("\\s+");
				
				bots.get(tokens[5]).add(Integer.parseInt(tokens[1]));
				
				iterator.remove();
			}
		}
		
		while (instructions.size() > 0) {
			for (Iterator<String> iterator = instructions.iterator(); iterator.hasNext(); ) {
				String next = iterator.next();
				String[] tokens = next.trim().split("\\s+");
				
				Bot bot = bots.get(tokens[1]);
				if (bot.isComplete()) {
					if (tokens[5].equals("bot"))
						bots.get(tokens[6]).add(bot.getLow());
					else
						outputs.get(tokens[6]).add(bot.getLow());
					
					if (tokens[10].equals("bot"))
						bots.get(tokens[11]).add(bot.getHigh());
					else
						outputs.get(tokens[11]).add(bot.getHigh());
					
					iterator.remove();
				}
			}
		}
	}
	
	public String getBotThatCompares(int value1, int value2) {
		if (value2 < value1) {
			int temp = value1;
			value1 = value2;
			value2 = temp;
		}
		
		for (Map.Entry<String, Bot> botEntry : bots.entrySet()) {
			if (botEntry.getValue().getLow() == value1 && botEntry.getValue().getHigh() == value2) {
				return botEntry.getKey();
			}
		}
		
		return null;
	}
	
	public List<Integer> getOutput(String identifier) {
		return outputs.get(identifier);
	}
}

class Bot {
	private final List<Integer> chips = new ArrayList<>();
	
	Bot() {
		
	}
	
	boolean isComplete() {
		return chips.size() == 2;
	}
	
	int getHigh() {
		if (!isComplete())
			return -1;
		else
			return chips.get(1);
	}
	
	int getLow() {
		if (!isComplete()) {
			return -1;
		} else
			return chips.get(0);
	}
	
	void add(int chip) {
		if (!isComplete())
			chips.add(chip);
		else
			throw new RuntimeException("Too many chips given to bot");
		Collections.sort(chips);
	}
}

class RunDay10_Part1 {
	public static void main(String[] args) {
		BotFactory botFactory = new BotFactory(BotFactory.input);
		
		botFactory.run();
		
		System.out.println(botFactory.getBotThatCompares(61, 17));
	}
}

class RunDay10_Part2 {
	public static void main(String[] args) {
		BotFactory botFactory = new BotFactory(BotFactory.input);
		
		botFactory.run();
		
		List<Integer> outputs = new LinkedList<>();
		outputs.addAll(botFactory.getOutput("0"));
		outputs.addAll(botFactory.getOutput("1"));
		outputs.addAll(botFactory.getOutput("2"));
		
		int product = 1;
		for (Integer integer : outputs) {
			product *= integer;
		}
		System.out.println(product);
	}
}