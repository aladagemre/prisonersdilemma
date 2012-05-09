package tr.edu.boun.cmpe.cmpe59e.agent.frame;

import java.util.Collections;
import java.util.Vector;

public class GameResult {
	
	private Vector<Result> results = new Vector<Result>();
	
	public GameResult(){
	}
	public void addResult(int agentID,String agentName){
		results.add(new Result(agentID,agentName));
	}
	public void add (int index , int more ){
		if ( results.get(index).getAgentID()!= index )
			throw new RuntimeException("Wrong index..");
		results.get(index).add(more);
	}
	public void printResults(){
		System.out.println("\n\n     *****   PRISONER'S DILEMMA GAME RESULTS   *****\n");
		Collections.sort(results);
		int i = 1;
		for(Result r: results){
			System.out.println(i++ +") Agent : " + r.getAgentName() + " - #Played Game : " + r.getGamePlayed() + " - Result : " + r.getNormalizedResult() );
		}	
		System.out.println("\n     *****   PRISONER'S DILEMMA GAME RESULTS   *****\n");
	}
	private class Result implements Comparable<Result>{
		
		int result = 0;
		int agentId = 0;
		int gamePlayed = 0;
		String agentName = "";
		
		public Result (int id,String name){
			agentId = id ;
			agentName= name;
		}
		public int getAgentID(){
			return agentId ;
		}
		public String getAgentName(){
			return agentName;
		}
		public int getGamePlayed(){
			return gamePlayed;
		}
		private  void add(int more){
			result += more;
			gamePlayed++;
		}
		public double getNormalizedResult(){
			if(gamePlayed!= 0 && result != 0)
				return (double)result/gamePlayed;
			return 0.0;
		}
		public int compareTo(Result o) {
			return Double.compare(this.getNormalizedResult(),o.getNormalizedResult());
		}
	}
}
