package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Buffs.BuffAction;
import item.ConsumableItem;
import item.EquipableItem;
import item.EquipableSetItem;
import item.ItemFrame;

public class ItemDAO {
	private final Connection sqlConnection = new SQLconnection().getConnection(); 
	private static ItemDAO instance;
	
	private ItemDAO() {
		instance = this;
	}
	
	public static ItemDAO getInstance() {
		return (instance == null) ? new ItemDAO() : instance;
	}
	
	//Get item buffs
	public List<Integer> getItemBuffs() {
		ResultSet rs = null;
		List<Integer> itemBuffs = new ArrayList<Integer>();
		try {
			rs = Queries.getItemBuffs(this.sqlConnection).executeQuery();
			while (rs.next()) {
				itemBuffs.add(rs.getInt("buffs")) ;
			}
		} catch (SQLException e){
			e.printStackTrace();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return itemBuffs;
	}
	
	//Get skill buffs
	public List<Integer> getSkillBuffs() {
		ResultSet rs = null;
		List<Integer> skillBuffs = new ArrayList<Integer>();
		try {
			rs = Queries.getSkillBuffs(this.sqlConnection).executeQuery();
			while (rs.next()) {
				skillBuffs.add(rs.getInt("buffs")) ;
			}
		} catch (SQLException e){
			e.printStackTrace();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return skillBuffs;
	}
	
	// retrieve item data from db and return it as base type of ItemFrame
	public ItemFrame getItem(int id) {
		if (id == 0) return null;
		ResultSet rs = fetchItem(id);
		ItemFrame it = null;
		try {
			if (rs.next()){
				if(rs.getInt("SetEffectID") > 0) {
					it = this.getSetItem(id);
				} else if((rs.getInt("EquipSlot") > 0 && rs.getInt("EquipSlot") < 17) || rs.getInt("Category")==18) {
					it = this.getEquipableItem(id);
				} else if(rs.getInt("EquipSlot") == 20){
					it = this.getConsumableItem(id);
				}else{
					it = new ItemFrame(id);
					setBasicProperties(it, rs);
				}
			}
			if (rs!=null)
				rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return it;
	}
	// instantiate EquipableItem object with proper attributes
	private EquipableItem getEquipableItem(int id){
		EquipableItem it = null;
		if (id == 0) return null;
		ResultSet rs = fetchItem(id);
		try {
			if (rs.next()){
				it = new EquipableItem(id);
				setBasicProperties(it, rs);
				setEquipableProperties(it, rs);
			}
			if (rs!=null)
				rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return it;
	}
	// instantiate EquipableSetItem object with proper attributes
	private EquipableSetItem getSetItem(int id){
		if (id == 0) return null;
		EquipableSetItem it = null;
		ResultSet rs = fetchItem(id);
		try {
			if (rs.next()){
				it = new EquipableSetItem(id);
				setBasicProperties(it, rs);
				setEquipableProperties(it, rs);
				setSetProperties(it, rs);
			}
			if (rs!=null)
				rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return it;
	}
	
	private ConsumableItem getConsumableItem(int id){
		if (id == 0) return null;
		ConsumableItem it = null;
		ResultSet rs = fetchItem(id);
		try {
			if (rs.next()){
				it = new ConsumableItem(id);
				setBasicProperties(it, rs);
				setConsumableProperties(it, rs);
			}
			if (rs!=null)
				rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return it;
	}
	
	private void setConsumableProperties(ConsumableItem it, ResultSet rs) {
		try{
			it.setHealhp((short)rs.getInt("HealHp"));
			it.setHealmana((short)rs.getInt("HealMana"));
			//Add buffs to usable item
			short[] buffid = new short[2], bufftime = new short[2], buffvalue = new short[2];
			for(int i = 1; i <= 2; i++) {
				buffid[i-1] = (short)rs.getInt("BuffId" + i);
				bufftime[i-1] = (short)rs.getInt("BuffTime" + i);
				buffvalue[i-1] = (short)rs.getInt("BuffValue"  + i);
			}
			it.setBuff(buffid, bufftime, buffvalue);
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	// set Set specific properties
	private void setSetProperties(EquipableSetItem it, ResultSet rs) {
		try{
			it.setSetBonusCritSucc(rs.getFloat("BonusCritChance"));
			it.setSetBonusDef(rs.getInt("BonusDefPower"));
			it.setSetBonusDefSucc(rs.getFloat("BonusDefScs"));
			it.setSetBonusHp(rs.getInt("BonusLife"));
			it.setSetBonusAtk(rs.getInt("BonusOffPower"));
			it.setSetBonusMana(rs.getInt("BonusMana"));
			it.setSetBonusStamina(rs.getInt("BonusStam"));
			it.setSetBonusTypeDmg(rs.getInt("BonusTypeDmg"));
			it.setSetPieces(rs.getInt("AmountSetPieces"));
			//str, int, vit, agi, dex
			short [] stats = new short[5];
			stats[0] = rs.getShort("BonusStr");
			stats[1] = rs.getShort("BonusDex");
			stats[2] = rs.getShort("BonusVit");
			stats[3] = rs.getShort("BonusIntl");
			stats[4] = rs.getShort("BonusAgi");
			it.setStats(stats);
		} catch (SQLException e){
			e.printStackTrace();
		}
		
	}
	// set basic item properties
	private void setBasicProperties(ItemFrame it, ResultSet rs){
		try {
			it.setCategory(rs.getInt("Category"));
			it.setHeight(rs.getInt("Height"));
			it.setWidth(rs.getInt("Width"));
			it.setMaxLvl(rs.getInt("MaxLvl"));
			it.setMinLvl(rs.getInt("MinLvl"));
			it.setNpcPrice(rs.getInt("Price"));
			it.setExpirationTime(rs.getInt("TimeToExpire")*1000);
			it.setMaxStack(rs.getInt("isConsumable")*99+1);
		} catch (SQLException e){
			e.printStackTrace();
			
		} catch (Exception e){
			e.printStackTrace();
			
		}
	}
	// set equipable attributes
	private void setEquipableProperties(EquipableItem it, ResultSet rs) {
		try{
			it.setEquipSlot(rs.getShort("EquipSlot"));
			it.setMinDmg(rs.getInt("MinDmg"));
			it.setMaxDmg(rs.getInt("MaxDmg"));
			it.setAtk(rs.getInt("OffPower"));
			it.setAtk(rs.getInt("OffPower"));
			it.setDef(rs.getInt("DefPower"));
			it.setAttSucc(rs.getFloat("AtkScs"));
			it.setCritDmg(rs.getShort("CritDmg"));
			it.setCritSucc(rs.getFloat("CritChance"));
			it.setDefSucc(rs.getFloat("DefScs"));
			it.setFaction(rs.getShort("Faction"));
			it.setHp(rs.getInt("Life"));
			it.setMana(rs.getInt("Mana"));
			it.setSetHash(rs.getInt("SetEffectID"));
			it.setStamina(rs.getInt("Stam"));
			it.setType(rs.getInt("AgainstType"));
			it.setTypeDmg(rs.getInt("TypeDmg"));
			it.setSpeed(rs.getInt("MoveSpeed"));
		
			boolean[] usableClass = new boolean[4];
			usableClass[0] = rs.getInt("WarUsable") == 1;
			usableClass[1] = rs.getInt("SinUsable") == 1;
			usableClass[2] = rs.getInt("MageUsable") == 1;
			usableClass[3] = rs.getInt("MonkUsable") == 1;
			it.setUsableClass(usableClass);
		
			short [] statreg = new short[5];
			statreg[0] = rs.getShort("ReqStr");
			statreg[1] = rs.getShort("ReqDex");
			statreg[2] = rs.getShort("ReqVit");
			statreg[3] = rs.getShort("ReqInt");
			statreg[4] = rs.getShort("ReqAgi");
			it.setStatRequirements(statreg);
		
			short []statbon = new short[5];
			statbon[0] = rs.getShort("Str");
			statbon[1] = rs.getShort("Dex");
			statbon[2] = rs.getShort("Vit");
			statbon[3] = rs.getShort("Intl");
			statbon[4] = rs.getShort("Agi");
			it.setStatBonuses(statbon);
			
		} catch (SQLException e){
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	// return resultset to item in question
	private ResultSet fetchItem(int id) {
		ResultSet rs = null;
		try {
			rs = Queries.getItem(this.sqlConnection, id).executeQuery();
		} catch (SQLException e){
			e.printStackTrace();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return rs;
	}
}
