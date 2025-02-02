package Mob;

import item.ItemCache;
import item.ItemFrame;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import logging.ServerLogger;
import Buffs.Buff;
import Buffs.BuffMaster;
import ExperimentalStuff.EffectMaster;
import GameServer.ServerMaster;
import GameServer.ServerPackets.ServerMessage;
import Player.Character;
import Player.CharacterMaster;
import Player.CharacterPackets;
import Player.ChatMaster;
import Player.Fightable;
import ServerCore.ServerFacade;
import Skills.CastableSkill;
import Skills.SkillMaster;
import World.Area;
import World.Grid;
import World.Location;
import World.OutOfGridException;
import World.WMap;
import World.Waypoint;
import World.WaypointChain;


/*
 *  Mob.class
 *  Provides basic mob logic functions 
 */
public class Mob implements Location, Fightable{
        
    private int mobID, uid;
	private MobData data;
	private MobController control;
	private List<Integer> iniPackets = Collections.synchronizedList(new ArrayList<Integer>());
	private ServerLogger log = ServerLogger.getInstance();
	private Waypoint spawn;
	private Waypoint location;
	int hp,  aggroID, currentWaypoint;
	private boolean alive, aggro;
	private Area area;
	private Grid grid;
	private long died;
	private Map<Integer, Integer> damage = Collections.synchronizedMap(new HashMap<Integer, Integer>());
	private WMap wmap = WMap.getInstance();
	private WaypointChain waypoints = new WaypointChain();
	private boolean isRegistered = false;
	private long atked;
	private int atkCooldown=1000;
	private float targetX;
	private float targetY;
	private boolean isDeleted=false;
	private Buff[] buffsActive = new Buff[18];
	/*
	 * Initializes the mob
	 * Params:
	 * mobID = type of mob in question
	 * id = unique ID of mob
	 * mdata = pointer to mobs data object 
	 * * cont = pointer to this mobs MobController object
	 */
	private int bonusHits=0;
	private String funName;
	private boolean isPuzzleMob;
	private boolean askedQuestion;
	private Mobpuzzle currentPuzzle;
	private Character puzzleChar;
	private boolean solvedPuzzle;
        
	public Mob(int mobID, int id, MobController cont) {
		this.uid = id;
		this.mobID = mobID;
		this.location = new Waypoint(0,0);
		this.data = cont.getData();
		this.alive = true;
		this.setCurentWaypoint(0);
		this.control = cont;
		resetToSpawn();
		this.wmap.AddMob(id, this);
		atked=0;
		targetX=0;
		targetY=0;
		this.funName=MobMaster.getRandomName(uid);
	}
        
	public int getMobID() {
		return mobID;
	}
	@Override
	public int getuid() {
		return this.uid;
	}
	@Override
	public void setuid(int uid) {
		this.uid = uid;
	}
	
	public String getName(){
		return "mob";
	}
	
	@Override
	public float getlastknownX() {
		return this.location.getX();
	}
	@Override
	public float getlastknownY() {
		return this.location.getY();
	}
	@Override
	public SocketChannel GetChannel() {
		return null;
	}
	@Override
    public short getState() {
		return 0;
	}
	// Join mob into the grid based proximity system 
	private void joinGrid(int grid) throws OutOfGridException {
		this.grid = this.wmap.getGrid(grid);
		if (!this.hasGrid()){ this.log.severe(this, "Mob failed to join grid"); }
		else {
			Area a = this.grid.update(this);
			// System.out.println("Got area " + a.getuid());
			this.setMyArea(a);
			synchronized(this.iniPackets){
				this.iniPackets.addAll(a.addMemberAndGetMembers(this));
				for(Iterator<Integer> it=this.iniPackets.iterator();it.hasNext();){
					Integer i = it.next();
					if (!WMap.getInstance().CharacterExists(i)){
						it.remove();
					}
				}
			}
			if(!isRegistered && !iniPackets.isEmpty()){
				control.register(this);
				isRegistered=true;
			}
			this.reset(true,true);
		}
	}
	private boolean hasGrid() {
	
		return (this.grid != null);
	}

	private void setMyArea(Area a) {
		this.area = a;
	}

	// update our area
	private void updateArea() throws OutOfGridException {
		Area a = this.grid.update(this);
		if (this.getMyArea() != a){
			this.getMyArea().moveTo(this, a);
			this.setMyArea(a);
			// a.addMember(this);
			List<Integer> ls;
		    ls = this.area.addMemberAndGetMembers(this);
		    synchronized(this.iniPackets){
		    	Iterator<Integer> it = this.iniPackets.iterator();
		    	while (it.hasNext()){
		    		Integer i = it.next();
		    		if (!WMap.getInstance().CharacterExists(i)){
		    			it.remove();
		    		}
		    		else if (!ls.contains(i)){
		    			it.remove();
		    			//if(!wmap.getCharacter(i).isBot())
		    			//	ServerFacade.getInstance().addWriteByChannel(this.wmap.getCharacter(i).GetChannel(), MobPackets.getDeathPacket(this.uid, this,false));
		    		}
		    	}
		    	ls.removeAll(iniPackets);
		    	this.sendInitToList(ls);
		    	this.iniPackets.addAll(ls);
		    
	
		    	if(this.iniPackets.isEmpty() && this.isRegistered) {
		    		this.control.unregister(this);
		    		this.isRegistered = false;
		    	} else if(!this.isRegistered && !this.iniPackets.isEmpty()) {
		    		this.control.register(this);
		    		this.isRegistered = true;
		    	}
		    }
		}
	}
	private void sendInitToList(List<Integer> ls) {
		Iterator<Integer> it = ls.iterator();
		Integer t = null;
		while(it.hasNext()){
			t = it.next();
			if (this.wmap.CharacterExists(t) && t != this.getuid() && !wmap.getCharacter(t).isBot()){
				Character tmp = this.wmap.getCharacter(t);
				SocketChannel sc = tmp.GetChannel();
				if(sc!=null)
					ServerFacade.getInstance().getConnectionByChannel(sc).addWrite(this.getInitPacket());
			}
			else {
				it.remove();
			}
		}
		
	}
	private Area getMyArea() {
		return this.area;
	}

	// remove mob from its current area
	private void rmAreaMember() {
		this.area.rmMember(this);
	}
	
	private void addAreaMember() {
		this.area.addMember(this);
	}
	

	private void generateNewChain(int count){
		Random r = new Random();
		List<Waypoint> ls = new ArrayList<Waypoint>();
		for (int i=0; i<count; i++){
			int startx = -1* this.data.getMoveSpeed();
			int starty = -1* this.data.getMoveSpeed();
			int x = startx + r.nextInt(2 * this.data.getMoveSpeed());
			int y = starty + r.nextInt(2 * this.data.getMoveSpeed());
			float newX=this.getlastknownX() + (float)x;
			float newY=this.getlastknownY() + (float)y;
			if(newX<control.getSpawnx()-control.getSpawnWidth())
				newX=control.getSpawnx()-control.getSpawnWidth();
			else if(newX>control.getSpawnx()+control.getSpawnWidth())
				newX=control.getSpawnx()+control.getSpawnWidth();
			if(newY<control.getSpawny()-control.getSpawnHeight())
				newY=control.getSpawny()-control.getSpawnHeight();
			else if(newY>control.getSpawny()+control.getSpawnHeight())
				newY=control.getSpawny()+control.getSpawnHeight();
			
			ls.add(new Waypoint(newX, newY));
			for(int u=0;u<this.data.getWaypointDelay();u++) {
				ls.add(null);
			}
		}
		this.waypoints.populate(ls);			
	}
	
	protected boolean run() throws OutOfGridException {
		if(!this.hasGrid()) {
			this.joinGrid(this.data.getGridID());
		}
		boolean hasPlayers = !this.iniPackets.isEmpty();
		
		if(this.isAlive()) {
			
			if(isPuzzleMob && ((int)(Math.random()*1000))==0)
				area.sendToMembers(0, ChatMaster.getChatPacket(-1, funName+"["+data.getLvl()+"]", MobMaster.getRandomSentence(), (byte)6));
			
			if (WMap.distance(this.location.getX(), this.location.getY(), this.getSpawnx(), this.getSpawny()) > this.data.getMoveRange()){
				//System.out.println(this.uid + " is too far from spawn");
				this.reset(true,false);
			}
			
			//If mob has stun or freeze buff
			if(getBuffById((short)43) != null || getBuffById((short)49) != null)
			{
				return hasPlayers;
			}
			
			// logic if mob has been aggroed
			else if(this.isAggro()){
				//System.out.println(this.uid + " is aggroed by " + this.getAggroID());
				if (this.wmap.CharacterExists(this.getAggroID())){
						Character loc = this.wmap.getCharacter(this.getAggroID());
						if(WMap.distance(this.location.getX(), this.location.getY(), loc.getlastknownX(), loc.getlastknownY())<data.getFollowRange() && !loc.isDead()){
							// attack target and/or move towards it
							if (WMap.distance(this.location.getX(), this.location.getY(), loc.getlastknownX(), loc.getlastknownY()) < this.data.getAttackRange()){
								// System.out.println(this.uid + " is attacking " + loc.getuid());
								this.attack(loc);
							}else{//if(targetX!=loc.getLocation().getX() || targetY!=loc.getLocation().getY()){
								this.setLocation(getAggroLocation(loc));
							}
						}else{
							Map <Integer, Integer>mp = this.damage;
							synchronized(mp) {
								mp.remove(loc.getuid());
							}
							this.setAggro(false);
							this.setAggroID(0);
						}
				}
				else {
					this.reset(true,false);
				}	
			}
			// mob hasn't been aggroed
			else {
			
				// move mob to it's next waypoint
				this.checkAggro();
				if (hasPlayers){
					if (!this.hasNextWaypoint()){
						this.generateNewChain(20);
					}
					Waypoint wp = this.waypoints.pop();
					if(wp != null) {
						this.setLocation(wp);
					}
				}
				
			}
		}else{
			
			//respawn
			if(System.currentTimeMillis()>died+data.getRespawnTime()){
				alive=true;
				addAreaMember();
				reset(true,true);
			}
			
		}
		return hasPlayers;
	}
	
	private Waypoint getAggroLocation(Location loc){
		
		targetX=loc.getLocation().getX();
		targetY=loc.getLocation().getY();
		float tmpx, tmpy;
		if(location.getX()>targetX)
			tmpx=targetX+5;
		else
			tmpx=targetX-5;
		if(location.getY()>targetY)
			tmpy=targetY+5;
		else
			tmpy=targetY-5;
		
		tmpx+=Math.random()*10-5;
		tmpy+=Math.random()*10-5;
		
		return new Waypoint(tmpx, tmpy);
		
	}
	

	// attack target
	private void attack(Character ch) {
		
		if(atked+atkCooldown<System.currentTimeMillis()){
			CastableSkill skill=data.getSkills()[(int)(Math.random()*data.getSkills().length)];
			int dmg=skill.getDmg()+((int)((Math.random()*(data.getMaxatk()-data.getMinatk()))+data.getMinatk()));
			dmg-=ch.getDefence();
			if(dmg<0)
				dmg=0;
			//MISS
			byte dmgtype=SkillMaster.skillCastDmgTypeCalculations(this, ch, false);
			dmg=(int)(dmg*SkillMaster.getDmgFactorByType(this, dmgtype));
			
			ch.damaged(dmg);
			//ch.subtractHp(dmg);
			
			int[] chs={ch.getuid()};
			send(MobPackets.getSkillPacket(uid, skill.getId(), dmgtype, dmg, chs, ch.getHp(), ch.getMana()));
			
			atked=System.currentTimeMillis();
		}
		
	}
	
	// resets mobs data
	private void reset(boolean sendMove, boolean resetHp) throws OutOfGridException {
		this.resetDamage();
		if(resetHp){
			this.setHp((int)(this.data.getMaxhp()*ServerMaster.getCurrentEvent().getMobhp()));
			//puzzlemob
			resetPuzzle(true);
			if(((int)(Math.random()*ServerMaster.getCurrentEvent().getPuzzlemobrate()))==0)
				isPuzzleMob=true;
			else
				isPuzzleMob=false;
		}
		this.setCurentWaypoint(0);
		this.resetToSpawn();
		this.setAggro(false);
		this.setAggroID(0);
		this.waypoints.clearAll();
		this.updateArea();
		if (sendMove){
			this.send(MobPackets.getInitialPacket(mobID, uid, this.location, hp));        
		}
	}
	private void setHp(int maxhp) {
		this.hp = maxhp;
		
	}

	private void resetToSpawn() {
		Random r = new Random();
		int x=control.getSpawnx() + r.nextInt(control.getSpawnWidth());
		int y=control.getSpawny() + r.nextInt(control.getSpawnHeight());
		this.setX(x);
		this.setY(y);
		spawn=new Waypoint(x, y);
		
	}

	// handle damages receiving
	public synchronized void recDamage(int uid, int dmg) throws OutOfGridException {
		if(dmg>0 && this.hp>0){
			if (this.hasPlayerDamage(uid)){
				int tmp = this.getPlayerDamage(uid);
				tmp += dmg;
				this.setDamage(uid, tmp);
			}
			else{
				this.setDamage(uid, dmg);
			}
			
			Map <Integer, Integer>mp = this.damage;
			synchronized(mp) {  //synchronized iteration for thread safe operations
				Iterator <Map.Entry<Integer, Integer>> it = mp.entrySet().iterator();
				int key;
				int value = 0;
				int hiDmg = 0;
				int hiID = 0;
				while (it.hasNext()) {
					Map.Entry<Integer, Integer> pairs = it.next();
					key = pairs.getKey();
					value = pairs.getValue();
					if (value > hiDmg){
						hiDmg = value;
						hiID = key;
					}
				}
				this.setAggro(true);
				this.setAggroID(hiID);
			}   
			
			this.reduceHp(dmg);
			if (this.hp <= 0){
				this.die();
			}else if(isPuzzleMob && !askedQuestion && hp<data.getMaxhp()/5){
				currentPuzzle=MobMaster.getRandomMobpuzzle();
				if(WMap.getInstance().CharacterExists(aggroID)){
					Character ch=WMap.getInstance().getCharacter(aggroID);
					puzzleChar=ch;
					ch.addPuzzleMob(this);
					ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), ChatMaster.getChatPacket(-1, funName+"["+data.getLvl()+"]", "Please dont kill me! I have a question for you!", (byte)6));
					ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), ChatMaster.getChatPacket(-1, funName+"["+data.getLvl()+"]", currentPuzzle.getQuestion(), (byte)6));
					EffectMaster.spawnEffects(control.getMap(), getlastknownX(), getlastknownY(), 0);
				}
				askedQuestion=true;
			}
		}
	}
	
	public void dieByDespawn() throws OutOfGridException{
		//some control stuff and packet
		this.rmAreaMember();
		this.setDied(System.currentTimeMillis());
		this.setAlive(false);
		this.send(MobPackets.getDeathPacket(this.uid, this, false));
		
		//reset
		if(control.isTemp()){
			setDeleted(true);
		}else{
			this.reset(false,false);
		}
	}
	
	public void resetPuzzle(boolean informChar){
		askedQuestion=false;
		currentPuzzle=null;
		if(puzzleChar!=null){
			if(informChar)
				puzzleChar.removePuzzleMob(this);
			puzzleChar=null;
		}
		solvedPuzzle=false;
		isPuzzleMob=false;
	}
	
	public void solvePuzzle(Character ch){
		try {
			solvedPuzzle=true;
			ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), ChatMaster.getChatPacket(-1, funName+"["+data.getLvl()+"]", "Correct answer! Cya! :)", (byte)6));
			die();
			resetPuzzle(true);
		} catch (OutOfGridException e) {
			System.out.print(e.getMessage());
		}
	}
	
	public void failPuzzle(Character ch){
		resetPuzzle(true);
		ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), ChatMaster.getChatPacket(-1, funName+"["+data.getLvl()+"]", "Wrong answer! Im sorry! :(", (byte)6));
	}
	
	// perform actions needed to finalize mob's death
	private void die() throws OutOfGridException {
		
		//character aggro
		Character ch;
		if(wmap.CharacterExists(getAggroID()))
			ch=wmap.getCharacter(getAggroID());
		else
			ch=null;
		
		float multihitmobrate=ServerMaster.getCurrentEvent().getMultihitmobrate();
		
		if(!solvedPuzzle){
			//hp reset
			if(bonusHits!=0){
				hp=1;
				bonusHits--;
			}else{
				//bonushits
				if((int)(Math.random()*multihitmobrate)==0){
					bonusHits=(int)(5+Math.random()*10);
					EffectMaster.spawnEffects(control.getMap(), location.getX(), location.getY(), 2);
					if(ch.getPt()!=null)
						ch.getPt().sendMessageToMembers("Bonus Hits! :D");
					else
						new ServerMessage().execute("Bonus Hits! :D", ServerFacade.getInstance().getConnectionByChannel(ch.GetChannel()));
					hp=1;
				}else{
					hp=0;
				}
			}
		}else{
			bonusHits=0;
			hp=0;
		}
		
		//factor the multiplies coins and exp
		float factor=(float)(Math.random()/10+0.9);
		boolean star=false;
		float coinfactor=ServerMaster.getCurrentEvent().getCoin();
		float expfactor=control.expFactor()*ServerMaster.getCurrentEvent().getExp();
		float famerate=ServerMaster.getCurrentEvent().getFame();
		float starrate=ServerMaster.getCurrentEvent().getStarrate()*ServerMaster.getCurrentEvent().getGeneralStarrate();
		float superstarrate=ServerMaster.getCurrentEvent().getSuperstarrate()*ServerMaster.getCurrentEvent().getGeneralStarrate();
		float bonusDroprate=ServerMaster.getCurrentEvent().getDrop();
		int bonusdrop=-1;
		
		if(!solvedPuzzle){
			//stars
			if(!control.onlyStars() && ((int)(Math.random()*superstarrate))==0){
				star=true;
				expfactor*=166;
				coinfactor*=16;
				EffectMaster.spawnEffects(control.getMap(), location.getX(), location.getY(), 3);
				if(ch.getPt()!=null)
					ch.getPt().sendMessageToMembers("WOW! Gz for super starmob!");
				else
					new ServerMessage().execute("WOW! Gz for super starmob!", ServerFacade.getInstance().getConnectionByChannel(ch.GetChannel()));
			}else
				if(control.onlyStars() || ((int)(Math.random()*starrate))==0){
					star=true;
					expfactor*=45;
					coinfactor*=10;
				}
		}else{
			//puzzle bonus
			expfactor*=currentPuzzle.getExprate();
			coinfactor*=currentPuzzle.getCoinrate();
			bonusDroprate*=currentPuzzle.getDroprate();
			bonusdrop=currentPuzzle.getBonusdrop();
			star=true;
		}
		
		ItemFrame it;
		if(ch!=null && ch.getLevel()<getLevel()+9){
			//drops
			if(bonusHits!=0){
				bonusDroprate*=0.25;
			}
			for(int i=0;i<data.getDrops().length;i++){
				if(data.getDropchances()[i]!=0 && ((int)(Math.random()/data.getDropchances()[i]/bonusDroprate))==0){
					it = (ItemFrame)ItemCache.getInstance().getItem(data.getDrops()[i]);
					if(it!=null)
						it.dropItem(grid.getuid(), getLocation(),1);
				}
			}
			
			//bonusdrop
			if(bonusdrop!=-1){
				it = (ItemFrame)ItemCache.getInstance().getItem(bonusdrop);
				if(it!=null)
					it.dropItem(grid.getuid(), getLocation(),1);
			}
			
			//exp
			if(ch.getPt()!=null){
				ch.getPt().killMob(ch,(long)(data.getBasexp()*factor*expfactor));
			}else{
				if(ch.getLevel()+8<getLevel())
					expfactor/=2;
				ch.gainExp((long)(data.getBasexp()*factor*expfactor),true);
			}
			
		}
		
		//coin
		if(bonusHits==0 || ((int)(Math.random()*4))==0){
			it = (ItemFrame)ItemCache.getInstance().getItem(217000501);
			it.dropItem(grid.getuid(), getLocation(),(int)(data.getCoins()*factor*coinfactor));
		}
		
		//fame
		if(bonusHits==0){
			if(ch!=null && ch.getLevel() >= 36 && ch.getFaction() != 0 && this.data.getLvl() >= 36 && ch.getLevel()<getLevel()+9)	{
				if(Math.random() < 0.04) { // 3% chance
					int fame = (int)(this.data.getBasefame()*(Math.random()*0.4+0.8));
					fame*=famerate;
					ch.addFame(fame);
					ch.setFameTitle(CharacterMaster.getFameTitle(ch.getFame()));
					this.send(MobPackets.famepacket(this.uid, this.aggroID, fame));
				}
			}
		}
		
		if(bonusHits==0){
			//some control stuff and packet
			this.rmAreaMember();
			this.setDied(System.currentTimeMillis());
			this.setAlive(false);
			this.send(MobPackets.getDeathPacket(this.uid, this, star));
			
			//reset
			if(control.isTemp()){
				setDeleted(true);
			}else{
				this.reset(false,false);
			}
		}
	}
	// check if mob is close enough to player to aggro it
	private boolean checkAggro() {
		//System.out.println("Mob" + this.uid +" aggrocheck"  + Thread.currentThread());
		boolean hasPlayers = false;
        synchronized(this.iniPackets){
        	
        	Iterator<Integer> iter = this.iniPackets.iterator();
        	while(iter.hasNext()) {
        		Integer it =  iter.next();
        		Location loc = this.wmap.getCharacter(it);
        		if (loc != null){
        			if (loc.GetChannel() != null){
        				hasPlayers = true;
        				if (this.wmap.getCharacter(it).getLevel()<getLevel()+9 && WMap.distance(this.location.getX(), this.location.getY(), loc.getlastknownX(), loc.getlastknownY()) < this.data.getAggroRange()){
        					this.setAggro(true);
        					this.setAggroID(loc.getuid());
        					break;
        				}                 
        			}
        		}
        		else{
        			iter.remove();
        		}
       		}
        }
        return hasPlayers;
    }

	// send packet to all nearby players
	private void send(byte[] buf) {		
		synchronized(this.iniPackets) {
			Iterator<Integer> iter = this.iniPackets.iterator();
			while(iter.hasNext()) {
				Integer plUid = iter.next();               
				Character ch = this.wmap.getCharacter(plUid.intValue());
				if(ch != null && !ch.isBot()) {
					ServerFacade.getInstance().addWriteByChannel(ch.GetChannel(), buf);
				}	
			}
		}
	}
	
	// set mobs location on the map and send move packet to players
	private void setLocation(Waypoint wp) throws OutOfGridException {
		this.setX(wp.getX());
		this.setY(wp.getY());
		this.updateArea();
		this.send(MobPackets.getMovePacket(this.uid, this.location.getX(), this.location.getY()));
	}
	// return reference to this mob's controller
	public MobController getControl() {
		return control;
	}
	private byte[] getInitPacket(){
		return MobPackets.getInitialPacket(this.mobID, this.uid, this.getLocation(), this.hp);
	}
	// update near by objects, called by area
	// receive updated list for nearby objects
	public synchronized void updateEnvironment(Integer player, boolean add) {
		synchronized(this.iniPackets){
			if (this.iniPackets.contains(player) && !add){
				this.iniPackets.remove(player);
				//if(!wmap.getCharacter(player).isBot())
				//	ServerFacade.getInstance().addWriteByChannel(this.wmap.getCharacter(player).GetChannel(), MobPackets.getDeathPacket(this.uid, this, false));
				if(this.iniPackets.isEmpty() && this.isRegistered) {
					this.control.unregister(this);
					this.isRegistered = false;
				}
			}
			if (add && !this.iniPackets.contains(player)){
				this.iniPackets.add(player);
				this.sendInit(player);
				if(!this.isRegistered) {
					this.control.register(this);
					this.isRegistered = true;
				}
				if(!this.control.isActive()) {
					this.grid.getThreadPool().executeProcess(this.control);
				}
			}
		}
	}
	
	// send initial packets to players who don't already have ours
	private void sendInit(Integer tmp) {
		if (this.wmap.CharacterExists(tmp)){
			Character t = this.wmap.getCharacter(tmp);
			SocketChannel sc = t.GetChannel();
			if(!t.isBot() && sc!=null) {
				ServerFacade.getInstance().getConnectionByChannel(sc).addWrite(this.getInitPacket());
				activateBuffs();
			}
		}
	}
	
	private void activateBuffs() {
		
	}
	
	public boolean isAlive() {
		return alive;
	}
	private void setAlive(boolean alive) {
		this.alive = alive;
	}
	private void setX(float x) {
		this.location.setX(x);
	}
	private void setY(float y) {
		this.location.setY(y);
	}
	private float getSpawnx() {
		return spawn.getX();
	}
	private float getSpawny() {
		return spawn.getY();
	}
	private void setDamage(int uid, int dmg) {
		this.damage.put(uid, dmg);
	}
	private void resetDamage(){
		this.damage.clear();
	}
	private boolean hasPlayerDamage(int uid){
		return this.damage.containsKey(uid);
	}
	private boolean isAggro() {
		return aggro;
	}
	private void setAggro(boolean aggro) {
		this.aggro = aggro;
	}
	private int getAggroID() {
		return aggroID;
	}
	private void setAggroID(int aggroID) {
		this.aggroID = aggroID;
	}
	@SuppressWarnings("unused")
	private long getDied() {
		return died;
	}
	private void setDied(long died) {
		this.died = died;
	}
	private int getPlayerDamage(int uid) {
		return this.damage.get(uid);
	}
	private void reduceHp(int dmg) {
		this.hp -= dmg;
	}
	private void setCurentWaypoint(int curentWaypoint) {
		this.currentWaypoint = curentWaypoint;
	}

	public Waypoint getLocation() {
		return this.location;
	}

	public Waypoint getAndSetNextWaypoint() {
		this.location = this.waypoints.pop(); 
		return this.location;
	}
	
	public void addNewWaypoints(List<Waypoint> wps) {
		this.waypoints.populate(wps);
	}
	
	public boolean hasNextWaypoint() {
		return !this.waypoints.isEmpty();
	}
	
	public int getDefence(){
		return data.getDefence();
	}
	
	public int getLevel(){
		return data.getLvl();
	}
	
	public int getHp(){
		return hp;
	}
	
	public int getMana(){
		return 0;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getAtkSuc() {
		return data.getAtksuc();
	}

	public int getDefSuc() {
		return data.getDefsuc();
	}

	public int getCritRate() {
		return data.getCritsuc();
	}
	
	public int getCritdmg() {
		return 0;
	}

	@Override
	public void addBuff(Buff buff) {
		short buffSlot=BuffMaster.getBuffSlot((short)buff.getId(), buffsActive);
		
		if(buffSlot > 18) {
			System.out.print("invalid buff slot("+buffSlot+") for mobid " + uid);
		}
		
		if(buffSlot < 0) {
			System.out.print("invalid buff slot("+buffSlot+") for mobid " + uid);
		}
		
		if(getBuffById(buff.getId()) != null) 
			getBuffById(buff.getId()).stopTimer();
		
		buffsActive[buffSlot] = buff;
		//ServerFacade.getInstance().getConnectionByChannel(this.GetChannel()).addWrite(CharacterPackets.getBuffPacket(this, buff.getId(), buffSlot, buff));
		area.sendToMembers(-1, CharacterPackets.getBuffPacket(this, buff.getId(), buffSlot, buff));
	}

	@Override
	public void removeBuff(Buff buff) {
		short buffSlot=BuffMaster.getBuffSlot((short)buff.getId(), buffsActive);
		buffsActive[buffSlot] = null;
		//ServerFacade.getInstance().getConnectionByChannel(this.GetChannel()).addWrite(CharacterPackets.getBuffPacket(this, (short)0, buffSlot, buff));
		area.sendToMembers(-1, CharacterPackets.getBuffPacket(this, (short)0, buffSlot, buff));
	}

	@Override
	public Buff[] getBuffs() {
		return this.buffsActive;
	}
	
	public Buff getBuffById(short id) {
		for(Buff buff : buffsActive) {
			if(buff != null)
				if(buff.getId() == id)
					return buff;
		}
		return null;
	}

	@Override
	public void refreshHpMpSp() {
		// Mobs doesn't have refresh 
	}
	
	public Mobpuzzle getPuzzle(){
		return currentPuzzle;
	}
        
}
