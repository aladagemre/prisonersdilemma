package tr.edu.boun.cmpe.cmpe59e.agent.frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.ServiceLoader;

import tr.edu.boun.cmpe.cmpe59e.agent.intf.AgentBase;

public class Frame {

	private int numOfGeneration = 10;

	private int minMax[] = new int[2];

	private Map<Integer, AgentBase> agents = new HashMap<Integer, AgentBase>();

	private GameResult gameResults = new GameResult();

	private int[][] payoffMatrix = new int[4][2];

	private List<Integer> shuffledKeys = null;

	private int firstAgentIndex = 0 ,secondAgentIndex = 0;

	public Frame() {
		loadProperties();
		loadAgents();
		start();
		printResults();
	}

	private int getGameLength() {
		int min = minMax[0];
		int max = minMax[1];
		Random rand = new Random(System.nanoTime());
		int randomNum = rand.nextInt(max - min + 1) + min;
		return randomNum;
	}

	private void printResults() {
		gameResults.printResults();
	}

	private void loadAgents() {
		ServiceLoader<AgentBase> agentServices = ServiceLoader.load(AgentBase.class);
		int i = 0;
		for (AgentBase agent : agentServices) {
			agents.put(i, agent);
			gameResults.addResult(i++, agent.getClass().getName().substring(agent.getClass().getName().lastIndexOf(".")+1));
		}
		if (agents.size() == 0 || agents.size() == 1) {
			System.err.println(agents.size()+ " agent loaded, program will exit.");
			System.exit(1);
		}
		shuffledKeys = new ArrayList<Integer>(agents.keySet());
		Collections.shuffle(shuffledKeys);
	}

	private void start() {
		int i = 0;
		int size = agents.size();
		while (i++ < numOfGeneration) {
			while (firstAgentIndex < size) {//All Agents should play.
				while(secondAgentIndex < size){
					int[] arr = pickTwoAgents();
					playOneGame(arr);
					secondAgentIndex ++;
					if(firstAgentIndex == secondAgentIndex)
						secondAgentIndex ++ ;
				}
				secondAgentIndex = 0;
				firstAgentIndex++;
			}
			firstAgentIndex = 0;
		}
	}

	private void playOneGame(int arr[]) {

		int i = 0;
		int agent1ID = arr[0], agent2ID = arr[1];
		AgentBase agent1 = agents.get(agent1ID);
		AgentBase agent2 = agents.get(agent2ID);
		agent1.setOpponent(agent2ID);
		agent2.setOpponent(agent1ID);
		int gameLength = getGameLength();//different game lenght for each game.
		while (i++ < gameLength) {
			boolean b1 = agent1.play();
			boolean b2 = agent2.play();
			int[] res = lookupPayoffMatrix(b1, b2);
			gameResults.add(arr[0], res[0]);
			gameResults.add(arr[1], res[1]);
			agent1.result(res[0]);
			agent2.result(res[1]);
		}
	}

	/****
	 * c c = 3 3 
	 * c d = 0 5 
	 * d c = 5 0
	 * d d = 1 1
	 */

	private int[] lookupPayoffMatrix(boolean b1, boolean b2) {

		int[] results = new int[2];
		if (b1 && b2) {
			results[0] = payoffMatrix[0][0];
			results[1] = payoffMatrix[0][1];
		} else if (b1 && !b2) {
			results[0] = payoffMatrix[1][0];
			results[1] = payoffMatrix[1][1];
		} else if (!b1 && b2) {
			results[0] = payoffMatrix[2][0];
			results[1] = payoffMatrix[2][1];
		} else {
			results[0] = payoffMatrix[3][0];
			results[1] = payoffMatrix[3][1];
		}
		return results;
	}

	private int[] pickTwoAgents() {
		int[] arr = new int[2];
		arr[0] = shuffledKeys.get(firstAgentIndex);
		arr[1] = shuffledKeys.get(secondAgentIndex);
		return arr;
	}

	public static void main(String[] args) {
		new Frame();
	}

	private void loadProperties() {
		Properties properties = new Properties();
		try {
			properties.load( this.getClass().getClassLoader().getResourceAsStream("frame.properties"));
			numOfGeneration = Integer.valueOf(properties.getProperty("NUMBER_OF_GENERATION"));
			String gameRange = properties.getProperty("GAME_LENGTH");
			String game[] = gameRange.split("~");
			minMax[0] = Integer.parseInt(game[0]);
			minMax[1] = Integer.parseInt(game[1]);
			String payoff = properties.getProperty("PAYOFF_MATRIX");
			String[] result = payoff.split("\\s");
			int x = 0;
			int j = 0;
			int i = 0;
			while (x < result.length) {
				i = 0;
				payoffMatrix[j][i++] = Integer.valueOf(result[x++]);
				payoffMatrix[j][i++] = Integer.valueOf(result[x++]);
				j++;
			}
		} catch (IOException e) {
			System.err.println("Error loading properties");
			System.exit(1);
		}
	}
}

/****
 * c c = 3 3 
 * c d = 0 5 
 * d c = 5 0
 * d d = 1 1
 */
