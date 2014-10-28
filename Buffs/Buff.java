package Buffs;

import java.util.Timer;

import Player.Character;
import Player.Fightable;

public abstract class Buff {

	private Character owner;
	private BuffAction action;
	private Timer timer;
	private short value;
	private long buffLength;
	private long timeLeft;
	protected boolean started;

	public Buff(Character owner, short buffId, long buffLength, short buffValue){
		this.owner=owner;
		this.value=buffValue;
		this.buffLength=buffLength;
		this.timeLeft=this.buffLength;
		this.action=BuffMaster.getBuffAction(buffId);
		this.timer=new Timer();
		this.started=false;
	}
	
	public boolean activate() throws BuffsException{
		return startBuff();
	}
	
	protected boolean startBuff() throws BuffsException{
		if(started==false){
			timer.scheduleAtFixedRate(new BuffTimer(this),4000,4000);
			started=true;
        	owner.addBuff(this);
			action.startBuff(owner, value);
			return true;
		}
		return false;
	}
	
	protected boolean endBuff(){
		if(started==true){
			stopTimer();
			owner.removeBuff(this);
			action.endBuff(owner, value);
			return true;
		}
		return false;
	}
	
	public void decreaseTimeLeft(long time){
		timeLeft-=time;
		System.out.println("[decrease time] item " + action.getId() + " has " + timeLeft + " timeleft");
		if(timeLeft<=0){
			endBuff();
		}
	}
	
	public void startTimer() {
		timer = new Timer(); //timer.cancel doesn't allow new tasks on old instance /* Used for relog */
		timer.scheduleAtFixedRate(new BuffTimer(this),4000,4000);
		started=true;
	}
	
	public void stopTimer() {
		timer.cancel(); //cancels all current timer threads?
		System.out.println("Stop timer for buff id" + action.getId());
		started=false;
	}
	
	public short getId(){
		return action.getId();
	}
	
	public long getTimeLeft(){
		return timeLeft;
	}
	
	public Character getOwner(){
			return owner;
	}
	
	public short getBuffValue(){
		return value;
	}
	
	public long getBuffTime() {
		return buffLength;
	}
	
	public BuffAction getAction(){
		return action;
	}
}
