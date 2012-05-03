package edu.boun.cmpe59.agent.intf;

public interface AgentBase {

	public void setPayoffMatrix(int[][] p);
	public boolean play();//0 defect - 1 cooperate
	public void result(int r);
	public void setOpponent(int a);
}
