package Buffs.BuffActions;

import Buffs.BuffAction;
import Player.Character;

public class BuffActionExample implements BuffAction{
	
	short buffId;
	
	public BuffActionExample(short buffId){
		this.buffId=buffId;
	}

	public void startBuff(Character ch,Object value){
		//no action except for updating character stats
		ch.calculateCharacterStats();
	}
	
	public void endBuff(Character ch,Object value){
		ch.calculateCharacterStats();
	}
	
	public short getId(){
		return buffId;
	}
	
	public String getValueType(){
		return "maxhp";
	}
	
}
