package edu.boun.cmpe59.agent.frame;

import java.util.Vector;

public class GameResult {
	
	private Vector<Result> results = new Vector<Result>();
	
	public GameResult(int size){
		initialize(size);
	}
	private void initialize(int size){
		results.setSize(0);
		for (int i = 0 ;i < size ; i++ ){
			results.add(new Result());
		}
	}
	public void add (int index , int more ){
		results.get(index).add(more);
	}
	public Vector<Integer> getResults(){
		Vector<Integer> resultsInt = new Vector<Integer>();
		for (Result r : results) {
			resultsInt.add(r.getInnerResult());
		}
		return resultsInt;
	}
	private class Result{
		int result = 0;
		
		private  void add(int more){
			result += more;
		}
		private void reset(){
			result= 0; 
		}
		public int getInnerResult(){
			return result;
		}
	}
}

