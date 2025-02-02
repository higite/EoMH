package Database;

import item.ItemInInv;
import item.inventory.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import logging.ServerLogger;
import Player.Character;
import Skills.CharacterSkillbar;
import Skills.CharacterSkills;
import Buffs.Buff;
import Configuration.ConfigurationManager;

public class Queries {
	private static ServerLogger log = ServerLogger.getInstance();
	
	public static PreparedStatement auth(Connection sqlc, String username, String hash) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM accounts WHERE username = ? AND password = ?;");
		st.setString(1, username);
		st.setString(2, hash);		
		return st;
	}
	
	public static PreparedStatement getAccountByIp(Connection sqlc, String ip) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM accounts WHERE Ip = ?;");
		st.setString(1, ip);		
		return st;
	}
	
	public static PreparedStatement newCharacter(Connection sqlc, String name,  byte[] stats, int chClass, short statpoints, short skillpoints, int xCoords, int yCoords, short face, int owner) {
		PreparedStatement st = null;
		try {
			st = sqlc.prepareStatement("INSERT INTO " +
					"characters(charname, commands, GMrank, charClass, face, size, kao, isAbandoned, isDead, faction, level, exp, currentHP, currentMana, currentStamina, fame, fametitle, flags, locationX, locationY, map, intelligence, vitality, agility, strength, dexterity, statpoints, skillpoints, ownerID) " +
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);");
	
		st.setString(1, name);  //character name
		st.setInt(2, 1);  		//commands
		st.setInt(3, 0);  		//GMrank
		st.setInt(4, chClass);  //class
		st.setInt(5, face);	    //face
		st.setInt(6, 0);  		//size
		st.setInt(7, 0);  		//kao
		st.setInt(8, 0);	    //isAbandoned
		st.setInt(9, 0);	    //isDead
		st.setInt(10, 0);	    //faction
		st.setInt(11, 1);	    //level
		st.setLong(12, 0);	    //exp
		st.setInt(13, 10);		//current HP	
		st.setInt(14, 10);		//current mana
		st.setInt(15, 10);		//current stamina
		st.setInt(16, 0);		//fame
		st.setInt(17, 0);		//fametitle
		st.setInt(18, 10);		//flags
		
	
		
		st.setInt(19, xCoords);		//x coords
		st.setInt(20, yCoords);		//y coords
		
		st.setInt(21, 1);			//map
		
		st.setInt(22, (int)stats[0]);		//INT
		st.setInt(24, (int)stats[1]);		//AGI
		st.setInt(23, (int)stats[2]);		//VIT
		st.setInt(26, (int)stats[3]);		//DEX
		st.setInt(25, (int)stats[4]);		//STR
		
		st.setInt(27, (int)statpoints);		//statpoints
		st.setInt(28, (int)skillpoints);	//skillpoints
		st.setInt(29, owner); 				//owner account
		} catch (SQLException e) {
			log.logMessage(Level.WARNING, Queries.class, e.getMessage());
		}
		
		
		return st;
	}
	
	public static PreparedStatement saveCharFace(Connection con, Character ch) throws Exception{
		
		PreparedStatement st = con.prepareStatement("UPDATE characters SET face=? WHERE CharacterID=?");
		
		st.setInt(1, ch.getFace());
		st.setInt(2, ch.getCharID());
		
		return st;
		
	}
	
	public static PreparedStatement saveCharFaction(Connection con, Character ch) throws Exception{
		
		PreparedStatement st = con.prepareStatement("UPDATE characters SET faction=? WHERE CharacterID=?");
		
		st.setInt(1, ch.getFaction());
		st.setInt(2, ch.getCharID());
		
		return st;
		
	}

	public static PreparedStatement saveCharKao(Connection con, Character ch) throws Exception{
	
		PreparedStatement st = con.prepareStatement("UPDATE characters SET kao=? WHERE CharacterID=?");
		
		st.setInt(1, ch.getKao());
		st.setInt(2, ch.getCharID());
		
		return st;
	
	}

	public static PreparedStatement saveCharSize(Connection con, Character ch) throws Exception{
	
		PreparedStatement st = con.prepareStatement("UPDATE characters SET size=? WHERE CharacterID=?");
		
		st.setInt(1, ch.getSize());
		st.setInt(2, ch.getCharID());
		
		return st;
	
	}
	
	public static PreparedStatement saveCharGMrank(Connection con, Character ch) throws Exception{
		
		PreparedStatement st = con.prepareStatement("UPDATE characters SET GMrank=? WHERE CharacterID=?");
		
		st.setInt(1, ch.getGMrank());
		st.setInt(2, ch.getCharID());
		
		return st;
		
	}
	
	public static PreparedStatement saveCharAbandoned(Connection con, Character ch) throws Exception{
		
		PreparedStatement st = con.prepareStatement("UPDATE characters SET isAbandoned=? WHERE CharacterID=?");
		
		if(ch.isAbandoned())
			st.setInt(1, 1);
		else
			st.setInt(1, 0);
		st.setInt(2, ch.getCharID());
		
		return st;
		
	}
	
	public static PreparedStatement getCharacterByName(Connection sqlc, String name) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM characters WHERE charname = ?;");
		st.setString(1, name);
		return st;
	}
	
	public static PreparedStatement getCharacterByID(Connection sqlc, int id) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM characters WHERE CharacterID = ?;");
		st.setInt(1, id);
		return st;
	}
	
	public static PreparedStatement getCharactersByOwnerID(Connection sqlc, int id) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SELECT * FROM characters WHERE ownerID = ?;");
		st.setInt(1, id);
		return st;
	}
	
	public static PreparedStatement deleteCharacter(Connection sqlc, int id) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("DELETE FROM characters WHERE CharacterID = ?;");
		st.setInt(1, id);
		return st;
	}
	
	public static PreparedStatement showTables(Connection sqlc) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SHOW TABLES;");
		return st;
	}
	public static PreparedStatement showDatabases(Connection sqlc) throws Exception {
		PreparedStatement st = sqlc.prepareStatement("SHOW DATABASES;");
		return st;
	}
	public static PreparedStatement createAccountTable(Connection sqlc) throws Exception{
		PreparedStatement st =  sqlc.prepareStatement("CREATE TABLE `accounts` ("+
														"`accountID` int(10) unsigned NOT NULL AUTO_INCREMENT," +
														"`Ip` char(16) NOT NULL DEFAULT '127.0.0.1'," +
														"`username` char(16) NOT NULL," +
														"`password` char(255) NOT NULL," +
														"`flags` int(11) DEFAULT NULL," +
														"PRIMARY KEY (`accountID`), " +
														"UNIQUE KEY `uniqueuser` (`username`) USING HASH " +
														") ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=ascii;");
		return st;
	}
	// some1's gotta write us parser for these -.-
	public static PreparedStatement createCharactersTable(Connection sqlc) throws Exception{
		PreparedStatement st =  sqlc.prepareStatement("CREATE TABLE `characters` (" +
							" `CharacterID` int(10) unsigned NOT NULL AUTO_INCREMENT,"+
							" `commands` tinyint(4) NOT NULL,"+
							" `GMrank` int(10) NOT NULL,"+
							" `charname` char(16) NOT NULL," +
							" `charClass` int(11) NOT NULL, " +
							" `face` tinyint(4) NOT NULL DEFAULT '1',"+
							" `size` smallint(6) NOT NULL DEFAULT '0',"+
							" `kao` int(10) NOT NULL DEFAULT '0',"+
							" `isAbandoned` tinyint(4) NOT NULL DEFAULT '0',"+
							" `isDead` tinyint(4) NOT NULL,"+
							" `faction` int(11) DEFAULT NULL,"+
							" `level` int(11) NOT NULL DEFAULT '1',"+
							" `exp` bigint(20) unsigned NOT NULL, "+
							" `currentHP` int(11) NOT NULL, "+
							" `currentMana` int(11) NOT NULL,"+
							" `currentStamina` int(11) NOT NULL, "+
							" `fame` int(11) NOT NULL,"+
							" `fametitle` tinyint(10) NOT NULL DEFAULT '0',"+
							" `flags` int(11) DEFAULT NULL,"+
							" `locationX` int(11) NOT NULL,"+
							" `locationY` int(11) NOT NULL,"+
							" `map` int(11) NOT NULL,"+
							" `intelligence` int(11) NOT NULL,"+
							" `vitality` int(11) NOT NULL,"+
							" `agility` int(11) NOT NULL,"+
							" `strength` int(11) NOT NULL,"+
							" `dexterity` int(11) NOT NULL,"+
							" `statpoints` int(11) NOT NULL,"+
							" `skillpoints` int(11) NOT NULL,"+
							" `ownerID` int(10) unsigned NOT NULL,"+
							"PRIMARY KEY (`CharacterID`),"+
							" UNIQUE KEY `uniquename` (`charname`) USING HASH,"+
							"KEY `owner` (`ownerID`),"+
							"CONSTRAINT `mapID` FOREIGN KEY (`map`) REFERENCES `maps` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
							"CONSTRAINT `owner` FOREIGN KEY (`ownerID`) REFERENCES `accounts` (`accountID`) ON DELETE CASCADE ON UPDATE CASCADE"+
							") ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=ascii;");
		return st;
	}
	public static PreparedStatement createMapTable(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("CREATE TABLE `maps` ("+
													" `id` int(11) NOT NULL,"+
													"`name` varchar(45) NOT NULL,"+
													"`gridSizeX` int(11) NOT NULL,"+
													"`gridSizeY` int(11) NOT NULL,"+
													"`areaSize` int(11) NOT NULL,"+
													" `mapx` int(11) NOT NULL,"+
													"`mapy` int(11) NOT NULL,"+
													"`poolSize` int(11) NOT NULL,"+
													"PRIMARY KEY (`id`),"+
													"UNIQUE KEY `id_UNIQUE` (`id`)"+
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;

	}
	public static PreparedStatement createMobDataTable(Connection con) throws Exception{
		String s="CREATE TABLE `mobData` ("+
				"`mobID` int(10) unsigned NOT NULL,"+
				"`lvl` int(10) unsigned NOT NULL,"+
				"`skill1` int(10) unsigned NOT NULL,"+
				"`skill2` int(10) unsigned NOT NULL,"+
				"`skill3` int(10) unsigned NOT NULL,"+
				"`minatk` int(11) NOT NULL,"+
				"`maxatk` int(11) NOT NULL,"+
				"`defence` int(11) NOT NULL,"+
				"`maxhp` int(11) NOT NULL,"+
				"`atksuc` int(11) NOT NULL,"+
				"`defsuc` int(11) NOT NULL,"+
				"`basexp` bigint(20) NOT NULL,"+
				"`coins` int(11) NOT NULL,"+
				"`basefame` int(11) NOT NULL,"+
				"`aggroRange` int(11) NOT NULL,"+
				"`attackRange` int(11) NOT NULL,"+
				"`followRange` int(11) NOT NULL,"+
				"`moveRange` int(11) NOT NULL,";
		
		for(int i=0;i<80;i++){
			s+="`drop"+i+"` int(10) unsigned NOT NULL,";
			s+="`dropchance"+i+"` float NOT NULL,";
		}
		
		s+=		"PRIMARY KEY (`mobID`),"+
				"UNIQUE KEY `mobID_UNIQUE` (`mobID`)"+
				") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		PreparedStatement st = con.prepareStatement(s);
													
		return st;
	}
	
	public static PreparedStatement createNpcDataTable(Connection con) throws Exception{
		String s="CREATE TABLE `npcData` ("+
				"`npcID` int(10) unsigned NOT NULL,"+
				"`module` int(10) unsigned NOT NULL,";
		
		for(int i=0;i<60;i++){
			s+="`item"+(i+1)+"` int(10) unsigned NOT NULL DEFAULT '0',";
		}
		
		s+=		"PRIMARY KEY (`npcID`),"+
				"UNIQUE KEY `npcID_UNIQUE` (`npcID`)"+
				") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		PreparedStatement st = con.prepareStatement(s);
													
		return st;
	}
	
	public static PreparedStatement createMobsTable(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("CREATE TABLE `mobs` ("+
													"`id` int(11) NOT NULL AUTO_INCREMENT,"+
													"`mobType` int(11) NOT NULL,"+
													"`map` int(11) NOT NULL,"+
													"`spawnCount` int(11) NOT NULL,"+
													"`spawnWidth` int(11) NOT NULL," +
													"`spawnHeight` int(11) NOT NULL," +
													"`spawnX` int(11) NOT NULL,"+
													"`spawnY` int(11) NOT NULL,"+
													"`respawnTime` int(11) NOT NULL,"+
													"`waypointCount` int(11) NOT NULL,"+
													"`waypointHop` int(11) NOT NULL,"+
													"PRIMARY KEY (`id`),"+
													"UNIQUE KEY `id_UNIQUE` (`id`),"+
													"KEY `type` (`mobType`),"+
													"KEY `map` (`map`),"+
													"CONSTRAINT `map` FOREIGN KEY (`map`) REFERENCES `maps` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION"+
													//"CONSTRAINT `type` FOREIGN KEY (`mobType`) REFERENCES `mobData` (`mobID`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
		
	}
	
	public static PreparedStatement createNpcSpawnsTable(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("CREATE TABLE `npcspawns` ("+
													"`id` int(11) NOT NULL AUTO_INCREMENT,"+
													"`npcType` int(11) NOT NULL,"+
													"`map` int(11) NOT NULL,"+
													"`spawnX` int(11) NOT NULL,"+
													"`spawnY` int(11) NOT NULL,"+
													"PRIMARY KEY (`id`),"+
													"UNIQUE KEY `id_UNIQUE` (`id`),"+
													"KEY `type` (`npcType`),"+
													"KEY `map` (`map`),"+
													"CONSTRAINT `map2` FOREIGN KEY (`map`) REFERENCES `maps` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION"+
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
		
	}
	
	public static PreparedStatement createCharBuffTable(Connection con) throws Exception{
		String query = "CREATE TABLE `charbuffs` (";
		for(int i=0;i<19;i++) { //max of 19 slots
			query +=   "`buffId"+i+"` tinyint(4) DEFAULT '0',"+
					   "`buffTime"+i+"` int DEFAULT '0',"+
					   "`buffValue"+i+"` tinyint(4) DEFAULT '0',";
		}
		query +=  	   "`belongsTo` int(10) unsigned NOT NULL," +
				       "PRIMARY KEY (`belongsTo`)," +
				       "UNIQUE KEY `belongsTo_UNIQUE` (`belongsTo`)," +
				       "KEY `belongsTo_idx` (`belongsTo`), " +
				       "CONSTRAINT `characterID` FOREIGN KEY (`belongsTo`) REFERENCES `accounts` (`accountID`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
				       ") ENGINE=InnoDB DEFAULT CHARSET=ascii;";

		PreparedStatement st = con.prepareStatement(query);
		return st;
	}
	
	public static PreparedStatement dropCharBuffTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE charbuffs;");
			return st;
		}
		throw new Exception();
	}
	
	
	public static PreparedStatement addMap(Connection con, int id, String name, int gridsizex, int gridsizey, int areasize, int x, int y, int pool) throws Exception{
		PreparedStatement st = con.prepareStatement("INSERT INTO maps(id, name, gridSizeX, gridSizeY, areaSize, mapx, mapy, poolSize) VALUES (?, ?, ?, ?, ?, ?, ?,?);");
		st.setInt(1, id);
		st.setString(2, name);
		st.setInt(3, gridsizex);
		st.setInt(4, gridsizey);
		st.setInt(5, areasize);
		st.setInt(6, x);
		st.setInt(7, y);
		st.setInt(8, pool);
		return st;
	}
	public static PreparedStatement addItem(Connection con,int itemid,int baseid,int category,int againsttype,int bonustype,int typedmg,int bonustypedmg,float atkrange,
			int price,int isconsumable,int ispermanent,int equipslot,int width,int height,int minlvl,int maxlvl,int reqstr,int reqdex,int reqvit,int reqint,int reqagi,
			int warusable,int sinusable,int mageusable,int monkusable,int faction,int upgradelvl,int str,int bonusstr,int dex,int bonusdex,int vit,int bonusvit,
			int intl,int bonusintl,int agi,int bonusagi,int healhp,int life,int bonuslife,int healmana,int mana,int bonusmana,int stam,int bonusstam,float atkscs,float bonusatkscs,float defscs,
			float bonusdefscs,float critchance,float bonuscritchance,int critdmg,int bonuscritdmg,int mindmg,int maxdmg,int offpower,int bonusoffpower,
			int defpower,int bonusdefpower,int pvpdmginc,int timetoexpire,int telemap,float telex,float teley,int seteffectid,int amountsetpieces,int movespeed,int buffid1, int bufftime1, int buffvalue1, int buffid2, int bufftime2, int buffvalue2) throws Exception{
		PreparedStatement st = con.prepareStatement("INSERT INTO items(itemid, ItemIDOfBaseItem, Category, AgainstType, BonusType, TypeDmg, BonusTypeDmg, AtkRange,"
				+ " Price, IsConsumable, IsPermanent, Equipslot, Width, Height, MinLvl, MaxLvl, ReqStr, ReqDex, ReqVit, ReqInt, ReqAgi,"
				+ " WarUsable, SinUsable, MageUsable, MonkUsable, Faction, UpgradeLvl, Str, BonusStr, Dex, BonusDex, Vit, BonusVit,"
				+ " Intl, BonusIntl, Agi, BonusAgi, HealHp, Life, BonusLife, HealMana, Mana, BonusMana, Stam, BonusStam, AtkScs, BonusAtkScs, DefScs,"
				+ " BonusDefScs, CritChance, BonusCritChance, CritDmg, BonusCritDmg, MinDmg, MaxDmg, OffPower, BonusOffPower,"
				+ " DefPower, BonusDefPower, PvpDmgInc, TimeToExpire, TeleMap, TeleX, TeleY, SetEffectID, AmountSetPieces, MoveSpeed, BuffId1, BuffTime1, BuffValue1, BuffId2, BuffTime2, BuffValue2)" 
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?);");
		st.setInt(1, itemid);
		st.setInt(2, baseid);
		st.setInt(3, category);
		st.setInt(4, againsttype);
		st.setInt(5, bonustype);
		st.setInt(6, typedmg);
		st.setInt(7, bonustypedmg);
		st.setFloat(8, atkrange);
		st.setInt(9, price);
		st.setInt(10, isconsumable);
		st.setInt(11, ispermanent);
		st.setInt(12, equipslot);
		st.setInt(13, width);
		st.setInt(14, height);
		st.setInt(15, minlvl);
		st.setInt(16, maxlvl);
		st.setInt(17, reqstr);
		st.setInt(18, reqdex);
		st.setInt(19, reqvit);
		st.setInt(20, reqint);
		st.setInt(21, reqagi);
		st.setInt(22, warusable);
		st.setInt(23, sinusable);
		st.setInt(24, mageusable);
		st.setInt(25, monkusable);
		st.setInt(26, faction);
		st.setInt(27, upgradelvl);
		st.setInt(28, str);
		st.setInt(29, bonusstr);
		st.setInt(30, dex);
		st.setInt(31, bonusdex);
		st.setInt(32, vit);
		st.setInt(33, bonusvit);
		st.setInt(34, intl);
		st.setInt(35, bonusintl);
		st.setInt(36, agi);
		st.setInt(37, bonusagi);
		st.setInt(38, healhp);
		st.setInt(39, life);
		st.setInt(40, bonuslife);
		st.setInt(41, healmana);
		st.setInt(42, mana);
		st.setInt(43, bonusmana);
		st.setInt(44, stam);
		st.setInt(45, bonusstam);
		st.setFloat(46, atkscs);
		st.setFloat(47, bonusatkscs);
		st.setFloat(48, defscs);
		st.setFloat(49, bonusdefscs);
		st.setFloat(50, critchance);
		st.setFloat(51, bonuscritchance);
		st.setInt(52, critdmg);
		st.setInt(53, bonuscritdmg);
		st.setInt(54, mindmg);
		st.setInt(55, maxdmg);
		st.setInt(56, offpower);
		st.setInt(57, bonusoffpower);
		st.setInt(58, defpower);
		st.setInt(59, bonusdefpower);
		st.setInt(60, pvpdmginc);
		st.setInt(61, timetoexpire);
		st.setInt(62, telemap);
		st.setFloat(63, telex);
		st.setFloat(64, teley);
		st.setInt(65, seteffectid);
		st.setInt(66, amountsetpieces);
		st.setInt(67, movespeed);
		st.setInt(68, buffid1);
		st.setInt(69, bufftime1);
		st.setInt(70, buffvalue1);
		st.setInt(71, buffid2);
		st.setInt(72, bufftime2);
		st.setInt(73, buffvalue2);
		return st;
	}
	public static PreparedStatement dropCharacterTable(Connection sqlc) throws Exception{
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  sqlc.prepareStatement("DROP TABLE characters;");
			return st;
		}
		throw new Exception();
		//return null;
	}
	public static PreparedStatement dropAccountTable(Connection sqlc) throws Exception{
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  sqlc.prepareStatement("DROP TABLE accounts;");
			return st;
		}
		throw new Exception();
	}

	public static PreparedStatement getMaps(Connection connection) throws Exception{
		PreparedStatement st = connection.prepareStatement("SELECT * FROM maps;");
		return st;
	}

	public static PreparedStatement dropMapTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE maps;");
			return st;
		}
		throw new Exception();
	}

	public static PreparedStatement getMobData(Connection con, int mobID) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT * FROM mobData WHERE mobID = ?;");
		st.setInt(1, mobID);
		return st;
	}
	
	public static PreparedStatement getNpcData(Connection con, int npcID) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT * FROM npcData WHERE npcID = ?;");
		st.setInt(1, npcID);
		return st;
	}
	
	public static PreparedStatement getAllNpcData(Connection con) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT * FROM npcData;");
		return st;
	}
	
	public static PreparedStatement getMobs(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM mobs;");
		return st;
	}
	
	public static PreparedStatement getNpcSpawns(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM npcspawns;");
		return st;
	}
	
	public static PreparedStatement dropNpcSpawnsTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE npcspawns;");
			return st;
		}
		throw new Exception();
	}

	public static PreparedStatement getZones(Connection con) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT * FROM zones;");
		return st;
	}

	public static PreparedStatement dropZonesTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE zones;");
			return st;
		}
		throw new Exception();
	}

	public static PreparedStatement dropMobsTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE mobs;");
			return st;
		}
		throw new Exception();
	}

	public static PreparedStatement dropMobDataTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE mobData;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement dropNpcDataTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE npcData;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement dropItemsTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st =  connection.prepareStatement("DROP TABLE items;");
			return st;
		}
		throw new Exception();
	}

	public static PreparedStatement createZoneTable(Connection connection) throws Exception {
		PreparedStatement st = connection.prepareStatement("CREATE TABLE `zones` ("+
													"`id` int(11) NOT NULL AUTO_INCREMENT,"+
													"`map` int(11) NOT NULL,"+
													"`startx` int(11) NOT NULL,"+
													"`starty` int(11) NOT NULL,"+
													"`width` int(11) NOT NULL,"+
													"`length` int(11) NOT NULL,"+
													"`type` varchar(45) NOT NULL,"+
													"PRIMARY KEY (`id`),"+
													"UNIQUE KEY `id_UNIQUE` (`id`),"+
													"KEY `myMap` (`map`),"+
													"CONSTRAINT `myMap` FOREIGN KEY (`map`) REFERENCES `maps` (`id`) ON DELETE CASCADE ON UPDATE CASCADE"+
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
	}

	public static PreparedStatement CreateUserAccount(Connection connection,String ip, String username, String password, int flags) throws Exception {
		PreparedStatement st = connection.prepareStatement("INSERT INTO accounts(Ip, username, password, flags) VALUES(?, ?, ?, ?);");
		st.setString(1, ip);
		st.setString(2, username);
		st.setString(3, password);
		st.setInt(4, flags);
		return st;
	}
	
	public static PreparedStatement createMobDataEntry(Connection con, int id, int lvl, int minatk, int maxatk, int deff, int skill1, int skill2, int skill3, int hp, int atksuc, int defsuc, long baseExp, int coins, int baseFame, int aggro, int follow, int move, int attrange, int[] drops, float[] dropchances) throws Exception{
		String s="INSERT INTO mobData(mobID, lvl, skill1, skill2, skill3, minatk, maxatk, defence, maxhp, atksuc, defsuc, basexp, coins, basefame, aggroRange, attackRange, followRange, moveRange, ";
		for(int i=0;i<79;i++){
			s+="drop"+i+", ";
			s+="dropchance"+i+", ";
		}
		s+="drop79, dropchance79) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ";
		for(int i=0;i<80;i++){
			s+="?, ?, ";
		}
		s+="?);";
		PreparedStatement st = con.prepareStatement(s);
		st.setInt(1, id);
		st.setInt(2, lvl);
		st.setInt(3, skill1);
		st.setInt(4, skill2);
		st.setInt(5, skill3);
		st.setInt(6, minatk);
		st.setInt(7, maxatk);
		st.setInt(8, deff);
		st.setInt(9, hp);
		st.setInt(10, atksuc);
		st.setInt(11, defsuc);
		st.setLong(12, baseExp);
		st.setInt(13, coins);
		st.setInt(14, baseFame);
		st.setInt(15, aggro);
		st.setInt(16, attrange);
		st.setInt(17, follow);
		st.setInt(18, move);
		for(int i=0;i<80;i++){
			st.setInt(19+i*2, drops[i]);
			st.setFloat(20+i*2, dropchances[i]);
		}
		return st;
	}
	
	public static PreparedStatement createNpcDataEntry(Connection con, int id, int module, int[] items) throws Exception{
		String s="INSERT INTO npcData(npcID, module, ";
		for(int i=0;i<59;i++){
			s+="item"+(i+1)+", ";
		}
		s+="item60) VALUES(?, ?, ";
		for(int i=0;i<59;i++){
			s+="?, ";
		}
		s+="?);";
		PreparedStatement st = con.prepareStatement(s);
		st.setInt(1, id);
		st.setInt(2, module);
		for(int i=0;i<60;i++){
			st.setInt(3+i, items[i]);
		}
		return st;
	}
	
	public static PreparedStatement createMobSpawnEntry(Connection con, int map, int id, int amount, float rx, float ry, float spawnWidth, float spawnHeight) throws Exception{
		String s="INSERT INTO mobs(mobType, map, spawnCount, spawnWidth, spawnHeight, spawnX, spawnY, respawnTime, waypointCount, waypointHop) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement st = con.prepareStatement(s);
		st.setInt(1, id);
		st.setInt(2, map);
		st.setInt(3, amount);
		st.setInt(4, (int)spawnWidth);
		st.setInt(5, (int)spawnHeight);
		st.setInt(6, (int)rx);
		st.setInt(7, (int)ry);
		st.setInt(8, 30000);
		st.setInt(9, 8);
		st.setInt(10, 8);
		return st;
	}
	
	public static PreparedStatement createNpcSpawnEntry(Connection con, int map, int id, float x, float y) throws Exception{
		String s="INSERT INTO npcspawns(npcType, map, spawnX, spawnY) VALUES(?, ?, ?, ?);";
		PreparedStatement st = con.prepareStatement(s);
		st.setInt(1, id);
		st.setInt(2, map);
		st.setInt(3, (int)x);
		st.setInt(4, (int)y);
		return st;
	}
	
	//-----CHARACTER-----
	
	public static PreparedStatement saveCharacterLocation(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("UPDATE characters SET locationX=?, locationY=?, map=? WHERE CharacterID = ?;");
		st.setInt(1, (int)ch.getlastknownX());
		st.setInt(2, (int)ch.getlastknownY());
		st.setInt(3, ch.getCurrentMap());
		st.setInt(4, ch.getCharID());
		return st;
	}
	
	public static PreparedStatement saveCharacterHpMpSp(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("UPDATE characters SET currentHP=?, currentMana=?, currentStamina=? WHERE CharacterID = ?;");
		st.setInt(1, (int)ch.getHp());
		st.setInt(2, (int)ch.getMana());
		st.setInt(3, (int)ch.getStamina());
		st.setInt(4, ch.getCharID());
		return st;
	}
	
	public static PreparedStatement loadCharacterHpMpSp(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT currentHP, currentMana, currentStamina FROM characters WHERE CharacterID = ?;");
		st.setInt(1, ch.getCharID());
		return st;
	}
	
	public static PreparedStatement saveCharacterStats(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("UPDATE characters SET strength=?, dexterity=?, vitality=?, intelligence=?, agility=? WHERE CharacterID = ?;");
		short[] stats=ch.getCStats();
		st.setInt(1, (int)stats[0]);
		st.setInt(2, (int)stats[1]);
		st.setInt(3, (int)stats[2]);
		st.setInt(4, (int)stats[3]);
		st.setInt(5, (int)stats[4]);
		st.setInt(6, ch.getCharID());
		return st;
	}
	
	public static PreparedStatement loadCharacterStats(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT strength, dexterity, vitality, intelligence, agility FROM characters WHERE CharacterID = ?;");
		st.setInt(1, ch.getCharID());
		return st;
	}
	
	public static PreparedStatement saveCharacterLvlexp(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("UPDATE characters SET level=?, exp=? WHERE CharacterID = ?;");
		st.setInt(1, ch.getLevel());
		st.setLong(2, ch.getExp());
		st.setInt(3, ch.getCharID());
		return st;
	}
	
	public static PreparedStatement saveCharacterDead(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("UPDATE characters SET isDead=? WHERE CharacterID = ?;");
		int dead;
		if(ch.isDead())
			dead=1;
		else
			dead=0;
		st.setInt(1, dead);
		st.setInt(2, ch.getCharID());
		return st;
	}
	
	public static PreparedStatement saveCharacterStatpoints(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("UPDATE characters SET statpoints=? WHERE CharacterID = ?;");
		st.setInt(1, ch.getStatPoints());
		st.setInt(2, ch.getCharID());
		return st;
	}
	
	public static PreparedStatement saveCharacterSkillpoints(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("UPDATE characters SET skillpoints=? WHERE CharacterID = ?;");
		st.setInt(1, ch.getSkillPoints());
		st.setInt(2, ch.getCharID());
		return st;
	}
	
	public static PreparedStatement saveCharacterFame(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("UPDATE characters SET fame=? WHERE CharacterID = ?;");
		st.setInt(1, ch.getFame());
		st.setInt(2, ch.getCharID());
		return st;
	}
	
	public static PreparedStatement saveCharacterFameTitle(Connection con, Character ch) throws Exception {
		PreparedStatement st = con.prepareStatement("UPDATE characters SET fametitle=? WHERE CharacterID = ?;");
		st.setInt(1, ch.getFameTitle());
		st.setInt(2, ch.getCharID());
		return st;
	}
	
	//----------

	public static PreparedStatement showGrants(Connection connection) throws Exception {
		PreparedStatement st = connection.prepareStatement("SHOW GRANTS;");
		return st;
	}
	public static PreparedStatement getItem(Connection con, int id) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM items WHERE itemid = ?;");
		st.setInt(1, id);
		return st;
	}
	public static PreparedStatement getItemBuffs(Connection con) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT `BuffId1` as buffs FROM items WHERE BuffId1 > 0 UNION SELECT `BuffId2` FROM items WHERE BuffId2 > 0");
		return st;
	}
	public static PreparedStatement getSkillBuffs(Connection con) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT `effId1` as buffs FROM skills WHERE effId1 > 0 UNION SELECT `effId2` FROM skills WHERE effId2 > 0 UNION SELECT `effId3` FROM skills WHERE effId3 > 0");
		return st;
	}
	public static PreparedStatement createItemsTable(Connection con) throws Exception {
		PreparedStatement st = con.prepareStatement("CREATE TABLE `items` (" +
													"`itemid` int(10) unsigned NOT NULL," +
													"`ItemIDOfBaseItem` int(10) unsigned NOT NULL," +
													"`Category` smallint(6) unsigned NOT NULL," +
													"`AgainstType` smallint(6) unsigned NOT NULL," +
													"`BonusType` smallint(6) unsigned NOT NULL," +
													"`TypeDmg` float NOT NULL," +
													"`BonusTypeDmg` float NOT NULL," +
													"`AtkRange` float DEFAULT NULL," +
													"`Price` int(10) unsigned NOT NULL," +
													"`IsConsumable` tinyint(4) unsigned NOT NULL," +
													"`IsPermanent` tinyint(4) unsigned NOT NULL," +
													"`EquipSlot` smallint(6) unsigned NOT NULL," +
													"`Width` smallint(6) unsigned NOT NULL," +
													"`Height` smallint(6) unsigned NOT NULL," +
													"`MinLvl` smallint(6) unsigned NOT NULL," +
													"`MaxLvl` smallint(6) unsigned NOT NULL," +
													"`ReqStr` smallint(6) unsigned NOT NULL," +
													"`ReqDex` smallint(6) unsigned NOT NULL," +
													"`ReqVit` smallint(6) unsigned NOT NULL," +
													"`ReqInt` smallint(6) unsigned NOT NULL," +
													"`ReqAgi` smallint(6) unsigned NOT NULL," +
													"`WarUsable` tinyint(4) unsigned NOT NULL," +
													"`SinUsable` tinyint(4) unsigned NOT NULL," +
													"`MageUsable` tinyint(4) unsigned NOT NULL," +
													"`MonkUsable` tinyint(4) unsigned NOT NULL," +
													"`Faction` tinyint(4) unsigned NOT NULL," +
													"`UpgradeLvl` tinyint(4) unsigned NOT NULL," +
													"`Str` smallint(6) NOT NULL," +
													"`BonusStr` smallint(6) NOT NULL," +
													"`Dex` smallint(6) NOT NULL," +
													"`BonusDex` smallint(6) NOT NULL," +
													"`Vit` smallint(6) NOT NULL," +
													"`BonusVit` smallint(6) NOT NULL," +
													"`Intl` smallint(6) NOT NULL," +
													"`BonusIntl` smallint(6) NOT NULL," +
													"`Agi` smallint(6) NOT NULL," +
													"`BonusAgi` smallint(6) NOT NULL," +
													"`HealHp` smallint(6) NOT NULL," +
													"`Life` smallint(6) NOT NULL," +
													"`BonusLife` smallint(6) NOT NULL," +
													"`HealMana` smallint(6) NOT NULL," +
													"`Mana` smallint(6) NOT NULL," +
													"`BonusMana` smallint(6) NOT NULL," +
													"`Stam` smallint(6) NOT NULL," +
													"`BonusStam` smallint(6) NOT NULL," +
													"`AtkScs` float NOT NULL, "+
													"`BonusAtkScs` float NOT NULL, "+
													"`DefScs` float NOT NULL, "+
													"`BonusDefScs` float NOT NULL, "+
													"`CritChance` float NOT NULL," +
													"`BonusCritChance` float NOT NULL," +
													"`CritDmg` smallint(6) NOT NULL," +
													"`BonusCritDmg` smallint(6) NOT NULL," +
													"`MinDmg` int(10) unsigned NOT NULL," +
													"`MaxDmg` int(10) unsigned NOT NULL," +
													"`OffPower` int(10) unsigned NOT NULL," +
													"`BonusOffPower` int(10) unsigned NOT NULL," +
													"`DefPower` int(10) unsigned NOT NULL," +
													"`BonusDefPower` int(10) unsigned NOT NULL," +
													"`PvpDmgInc` smallint(6) NOT NULL," +
													"`TimeToExpire` int(10) unsigned NOT NULL," +
													"`TeleMap` int(10) unsigned NOT NULL," +
													"`TeleX` float NOT NULL," +
													"`TeleY` float NOT NULL," +
													"`SetEffectID` int(10) unsigned NOT NULL," +
													"`AmountSetPieces` smallint(6) unsigned NOT NULL," +
													"`MoveSpeed` int(10) unsigned NOT NULL," +
													"`BuffId1` smallint(6) unsigned NOT NULL," +
													"`BuffTime1` int(10) unsigned NOT NULL," +
													"`BuffValue1` smallint(6) unsigned NOT NULL," +
													"`BuffId2` smallint(6) unsigned NOT NULL," +
													"`BuffTime2` int(10) unsigned NOT NULL," +
													"`BuffValue2` smallint(6) unsigned NOT NULL," +
													"PRIMARY KEY (`itemid`)," +
													"UNIQUE KEY `equid_UNIQUE` (`itemid`)" +
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
	}
	public static PreparedStatement createEquipmentTable(Connection con) throws Exception {
		PreparedStatement st = con.prepareStatement("CREATE TABLE `equipments` (" +
													"`cap` int(10) unsigned DEFAULT '0', "+ 
													"`necklace` int(10) unsigned DEFAULT '0'," +
													"`cape` int(10) unsigned DEFAULT '0'," +
													"`jacket` int(10) unsigned DEFAULT '0'," +
													"`pants` int(10) unsigned DEFAULT '0'," +
													"`armor` int(10) unsigned DEFAULT '0'," +
													"`bracelet` int(10) unsigned DEFAULT '0'," +
													"`weapon1` int(10) unsigned DEFAULT '0'," +
													"`weapon2` int(10) unsigned DEFAULT '0'," +
													"`ring1` int(10) unsigned DEFAULT '0'," +
													"`ring2` int(10) unsigned DEFAULT '0'," +
													"`shoes` int(10) unsigned DEFAULT '0'," +
													"`bird` int(10) unsigned DEFAULT '0'," +
													"`tablet` int(10) unsigned DEFAULT '0'," +
													"`famepad` int(10) unsigned DEFAULT '0'," +
													"`mount` int(10) unsigned DEFAULT '0'," +
													"`beed` int(10) unsigned DEFAULT '0'," +
													"`belongsTo` int(10) unsigned NOT NULL," +
													"PRIMARY KEY (`belongsTo`)," +
													"UNIQUE KEY `belongsTo_UNIQUE` (`belongsTo`)," +
													"KEY `belongsTo_idx` (`belongsTo`)," +
													"CONSTRAINT `ownerID1` FOREIGN KEY (`belongsTo`) REFERENCES `characters` (`CharacterID`) ON DELETE CASCADE ON UPDATE CASCADE" +
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;


	}
	public static PreparedStatement getEquipment(Connection con, int id) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM equipments WHERE belongsTo = ?;");
		st.setInt(1, id);
		return st;
	}
	public static PreparedStatement createEquips(Connection con, int id, List<Integer> eq) throws Exception{
		PreparedStatement st = con.prepareStatement("INSERT INTO equipments(cap, necklace, cape, jacket, pants,armor, bracelet, weapon1, weapon2, ring1, ring2," +
													"shoes,bird,tablet,famepad,mount,beed,belongsTo) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		st.setInt(1, eq.get(0));
		st.setInt(2, eq.get(1));
		st.setInt(3, eq.get(2));
		st.setInt(4, eq.get(3));
		st.setInt(5, eq.get(4));
		st.setInt(6, eq.get(5));
		st.setInt(7, eq.get(6));
		st.setInt(8, eq.get(7));
		st.setInt(9, eq.get(8));
		st.setInt(10, eq.get(9));
		st.setInt(11, eq.get(10));
		st.setInt(12, eq.get(11));
		st.setInt(13, eq.get(12));
		st.setInt(14, eq.get(13));
		st.setInt(15, eq.get(14));
		st.setInt(16, eq.get(15));
		st.setInt(17, eq.get(16));
		st.setInt(18, id);
		return st;
	}
	public static PreparedStatement storeEquipments(Connection con, int id, List<Integer> eq) throws Exception{
		PreparedStatement st = con.prepareStatement("UPDATE equipments SET cap = ?, necklace = ?, cape = ?, jacket = ?, pants = ?," +
													"armor = ?, bracelet = ?, weapon1 = ?,weapon2 = ?,ring1 = ?," +
													"ring2 = ?, shoes = ?,bird = ?, tablet = ?,famepad = ?, mount = ?," +
													"beed = ?  WHERE belongsTo = ?;");
		st.setInt(1, eq.get(0));
		st.setInt(2, eq.get(1));
		st.setInt(3, eq.get(2));
		st.setInt(4, eq.get(3));
		st.setInt(5, eq.get(4));
		st.setInt(6, eq.get(5));
		st.setInt(7, eq.get(6));
		st.setInt(8, eq.get(7));
		st.setInt(9, eq.get(8));
		st.setInt(10, eq.get(9));
		st.setInt(11, eq.get(10));
		st.setInt(12, eq.get(11));
		st.setInt(13, eq.get(12));
		st.setInt(14, eq.get(13));
		st.setInt(15, eq.get(14));
		st.setInt(16, eq.get(15));
		st.setInt(17, eq.get(16));
		st.setInt(18, id);
		return st;
	}

	public static PreparedStatement dropEquipmentTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = connection.prepareStatement("DROP TABLE equipments;");
			return st;
		}
		throw new Exception();
	}
	
	//----------INVENTORY----------
	
	public static PreparedStatement createInventoryTable(Connection con) throws Exception {
		
		String s="CREATE TABLE `inventories`(";
		for(int i=0;i<240;i++){
			s+="`item"+i+"` int(10) unsigned DEFAULT '0', ";
			s+="`hash"+i+"` smallint(6) DEFAULT '-1', ";
			s+="`amount"+i+"` smallint(6) unsigned DEFAULT '0', ";
		}
		s+="`coins` int(10) unsigned DEFAULT '0',";
		s+="`vendingpoints` int(10) unsigned DEFAULT '0',";
		s+="`pages` tinyint(4) unsigned DEFAULT '3',";
		s+="`belongsTo` int(10) unsigned NOT NULL,";
		s+="PRIMARY KEY (`belongsTo`)," +
			"UNIQUE KEY `belongsTo_UNIQUE` (`belongsTo`)," +
			"KEY `belongsTo_idx` (`belongsTo`)," +
			"CONSTRAINT `ownerID2` FOREIGN KEY (`belongsTo`) REFERENCES `characters` (`CharacterID`) ON DELETE CASCADE ON UPDATE CASCADE" +
			") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		
		PreparedStatement st = con.prepareStatement(s);
		
		return st;
		
	}
	
	public static PreparedStatement getInventory(Connection con, int id) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM inventories WHERE belongsTo = ?;");
		st.setInt(1, id);
		return st;
	}
	
	public static PreparedStatement createInventories(Connection con, int id, Inventory inv) throws Exception{
		
		String s="INSERT INTO inventories(";
		for(int i=0;i<240;i++){
			s+="item"+i+",";
			s+="hash"+i+",";
			s+="amount"+i+",";
		}
		s+="coins,vendingpoints,pages,belongsTo) VALUES (";
		for(int i=0;i<723;i++){
			s+="?,";
		}
		s+="?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		for(int i=0;i<240;i++){
			st.setInt(i*3+1, 0);
			st.setInt(i*3+2, inv.getSeqSaved().get(i));
			st.setInt(i*3+3, 0);
			if(inv.getSeqSaved().get(i)!=-1){
				ItemInInv item=inv.getInvSaved().get(inv.getSeqSaved().get(i));
				if(item!=null){
					st.setInt(i*3+1, item.getItem().getId());
					st.setInt(i*3+3, item.getAmount());
				}
			}
		}
		st.setInt(721, inv.getCoins());
		st.setInt(722, inv.getVendingPoints());
		st.setInt(723, inv.getPages());
		st.setInt(724, id);
		
		return st;
		
	}
	
	public static PreparedStatement storeInventories(Connection con, int id, Inventory inv) throws Exception{
		
		String s="UPDATE inventories SET ";
		for(int i=0;i<240;i++){
			s+="item"+i+"=?,";
			s+="hash"+i+"=?,";
			s+="amount"+i+"=?,";
		}
		s+="coins=?,vendingpoints=?,pages=?";
		s+="  WHERE belongsTo = ?;";
		
		PreparedStatement st = con.prepareStatement(s);
		
		for(int i=0;i<240;i++){
			st.setInt(i*3+1, 0);
			st.setInt(i*3+2, inv.getSeqSaved().get(i));
			st.setInt(i*3+3, 0);
			if(inv.getSeqSaved().get(i)!=-1){
				ItemInInv item=inv.getInvSaved().get(inv.getSeqSaved().get(i));
				if(item!=null){
					st.setInt(i*3+1, item.getItem().getId());
					st.setInt(i*3+3, item.getAmount());
				}
			}
		}
		//holding item
		int indexHold=inv.getIndexHold();
		if(indexHold!=-1){
			int i=inv.nextFreeSequence();
			ItemInInv it=inv.getHoldingItem();
			st.setInt(i*3+1, it.getItem().getId());
			st.setInt(i*3+2,-1);
			List<Integer> ls;
			int line;
			int row=0;
			boolean found=false;
			while(row<inv.getPages()*5 && found==false){
				line=0;
				while(line<8 && found==false){
					ls=inv.checkBlockingItems(line, row, it);
					if(ls.size()==0){
						st.setInt(i*3+2,(row*100)+line);
						found=true;
					}
					line++;
				}
				row++;
			}
			st.setInt(i*3+3, inv.getHoldingItem().getAmount());
		}
		//equipping item
		if(inv.getSeqSaved().contains(8)){
			ItemInInv it=inv.getInvSaved().get(8);
			List<Integer> ls;
			int line;
			int row=0;
			boolean found=false;
			while(row<inv.getPages()*5 && found==false){
				line=0;
				while(line<8 && found==false){
					ls=inv.checkBlockingItems(line, row, it);
					if(ls.size()==0){
						st.setInt(inv.getSeqSaved().indexOf(8)*3+2,(row*100)+line);
						st.setInt(inv.getSeqSaved().indexOf(8)*3+1, it.getItem().getId());
						st.setInt(inv.getSeqSaved().indexOf(8)*3+2,-1);
						found=true;
					}
					line++;
				}
				row++;
			}
		}
		st.setInt(721, inv.getCoins());
		st.setInt(722, inv.getVendingPoints());
		st.setInt(723, inv.getPages());
		st.setInt(724, id);
		
		return st;
		
	}
	
	public static PreparedStatement dropInventoryTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = connection.prepareStatement("DROP TABLE inventories;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement createSkillsTable(Connection con) throws Exception {
		PreparedStatement st = con.prepareStatement("CREATE TABLE `skills` (" +
													"`skillid` int(10) unsigned NOT NULL," +
													"`skillgroup` int(10) unsigned NOT NULL," +
													"`chclass` smallint(6) unsigned NOT NULL," +
													"`stage` tinyint(4) unsigned NOT NULL," +
													"`effectOnWep` int(10) unsigned NOT NULL," +
													"`reqSkill1` int(10) unsigned NOT NULL," +
													"`reqSkill2` int(10) unsigned NOT NULL," +
													"`reqSkill3` int(10) unsigned NOT NULL," +
													"`skillpoints` int(10) unsigned NOT NULL," +
													"`nextSkillLawful` int(10) unsigned NOT NULL," +
													"`nextSkillEvil` int(10) unsigned NOT NULL," +
													"`lvl` smallint(6) unsigned NOT NULL," +
													"`specificType` smallint(6) unsigned NOT NULL," +
													"`moreSpecificType` int(10) unsigned NOT NULL," +
													"`normalDmgFont` smallint(6) unsigned NOT NULL," +
													"`mobDmgFont` smallint(6) unsigned NOT NULL," +
													"`targets` smallint(6) unsigned NOT NULL," +
													"`generalType` smallint(6) unsigned NOT NULL," +
													"`faction` int(10) unsigned NOT NULL," +
													"`needsWepToCast` int(10) unsigned NOT NULL," +
													"`ultiSetId` int(10) unsigned NOT NULL," +
													"`isCastable` tinyint(4) unsigned NOT NULL," +
													"`isSpecialCast` tinyint(4) unsigned NOT NULL," +
													"`healCost` smallint(6) NOT NULL," +
													"`manaCost` smallint(6) NOT NULL," +
													"`staminaCost` smallint(6) NOT NULL," +
													"`dmg` int(10) unsigned NOT NULL," +
													"`speed` float NOT NULL, "+
													"`effAmount` int(10) unsigned NOT NULL," +
													"`effId1` smallint(6) unsigned NOT NULL," +
													"`effDuration1` smallint(6) unsigned NOT NULL," +
													"`effValue1` smallint(6) NOT NULL," +
													"`effId2` smallint(6) unsigned NOT NULL," +
													"`effDuration2` smallint(6) unsigned NOT NULL," +
													"`effValue2` smallint(6) NOT NULL," +
													"`effId3` smallint(6) unsigned NOT NULL," +
													"`effDuration3` smallint(6) unsigned NOT NULL," +
													"`effValue3` smallint(6) NOT NULL," +
													"PRIMARY KEY (`skillid`)," +
													"UNIQUE KEY `equid_UNIQUE` (`skillid`)" +
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
	}
	
	public static PreparedStatement getAllSkills(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM skills;");
		return st;
	}
	
	public static PreparedStatement getSkill(Connection con, int id) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM skills WHERE skillid = ?;");
		st.setInt(1, id);
		return st;
	}
	
	public static PreparedStatement addSkill(Connection con,int skillid,int skillgroup,int chclass,int stage,int effectOnWep,int reqSkill1,int reqSkill2,int reqSkill3,int skillpoints,int nextSkillLawful,
			int nextSkillEvil,int lvl,int specificType,int moreSpecificType,int normalDmgFont,int mobDmgFont,int targets,int generalType,int faction,
			int needsWepToCast,int ultiSetId,int isCastable,int isSpecialCast,int healCost,int manaCost,int staminaCost,int dmg,float speed,int effAmount,
			int effId1,int effDuration1,int effValue1,int effId2,int effDuration2,int effValue2,int effId3,int effDuration3,int effValue3) throws Exception{
		PreparedStatement st = con.prepareStatement("INSERT INTO skills(skillid,skillgroup,chclass,stage,effectOnWep,reqSkill1,reqSkill2,reqSkill3,skillpoints,nextSkillLawful,"
				+ " nextSkillEvil,lvl,specificType,moreSpecificType,normalDmgFont,mobDmgFont,targets,generalType,faction,"
				+ " needsWepToCast,ultiSetId,isCastable,isSpecialCast,healCost,manaCost,staminaCost,dmg,speed,effAmount,"
				+ " effId1,effDuration1,effValue1,effId2,effDuration2,effValue2,effId3,effDuration3,effValue3) VALUES (?, "
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
		st.setInt(1, skillid);
		st.setInt(2, skillgroup);
		st.setInt(3, chclass);
		st.setInt(4, stage);
		st.setInt(5, effectOnWep);
		st.setInt(6, reqSkill1);
		st.setInt(7, reqSkill2);
		st.setFloat(8, reqSkill3);
		st.setInt(9, skillpoints);
		st.setInt(10, nextSkillLawful);
		st.setInt(11, nextSkillEvil);
		st.setInt(12, lvl);
		st.setInt(13, specificType);
		st.setInt(14, moreSpecificType);
		st.setInt(15, normalDmgFont);
		st.setInt(16, mobDmgFont);
		st.setInt(17, targets);
		st.setInt(18, generalType);
		st.setInt(19, faction);
		st.setInt(20, needsWepToCast);
		st.setInt(21, ultiSetId);
		st.setInt(22, isCastable);
		st.setInt(23, isSpecialCast);
		st.setInt(24, healCost);
		st.setInt(25, manaCost);
		st.setInt(26, staminaCost);
		st.setInt(27, dmg);
		st.setFloat(28, speed);
		st.setInt(29, effAmount);
		st.setInt(30, effId1);
		st.setInt(31, effDuration1);
		st.setInt(32, effValue1);
		st.setInt(33, effId2);
		st.setInt(34, effDuration2);
		st.setInt(35, effValue2);
		st.setInt(36, effId3);
		st.setInt(37, effDuration3);
		st.setInt(38, effValue3);
		return st;
	}
	
	public static PreparedStatement dropSkillsTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = connection.prepareStatement("DROP TABLE charskills;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement createCharSkillsTable(Connection con) throws Exception {
		
		String s="CREATE TABLE `charskills`(";
		for(int i=0;i<200;i++){
			s+="`skill"+i+"` int(10) unsigned DEFAULT '0', ";
		}
		s+="`belongsTo` int(10) unsigned NOT NULL,";
		s+="PRIMARY KEY (`belongsTo`)," +
			"UNIQUE KEY `belongsTo_UNIQUE` (`belongsTo`)," +
			"KEY `belongsTo_idx` (`belongsTo`)," +
			"CONSTRAINT `ownerID3` FOREIGN KEY (`belongsTo`) REFERENCES `characters` (`CharacterID`) ON DELETE CASCADE ON UPDATE CASCADE" +
			") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		
		PreparedStatement st = con.prepareStatement(s);
		
		return st;
		
	}
	
	public static PreparedStatement getCharSkills(Connection con, int id) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM charskills WHERE belongsTo = ?;");
		st.setInt(1, id);
		return st;
	}
	
	public static PreparedStatement createCharSkills(Connection con, int id, CharacterSkills chSkills) throws Exception{
		
		String s="INSERT INTO charskills(";
		for(int i=0;i<200;i++){
			s+="skill"+i+",";
		}
		s+="belongsTo) VALUES (";
		for(int i=0;i<200;i++){
			s+="?,";
		}
		s+="?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		for(int i=0;i<200;i++){
			st.setInt(i+1, 0);
			if(chSkills.getLearnedSkills().containsKey(i))
				st.setInt(i+1, chSkills.getLearnedSkills().get(i));
		}
		st.setInt(201, id);
		
		return st;
		
	}
	
	public static PreparedStatement storeCharSkills(Connection con, int id, CharacterSkills chSkills) throws Exception{
		
		String s="UPDATE charskills SET ";
		for(int i=0;i<199;i++){
			s+="skill"+i+"=?,";
		}
		s+="skill199=?";
		s+="  WHERE belongsTo = ?;";
		
		PreparedStatement st = con.prepareStatement(s);
		
		for(int i=0;i<200;i++){
			st.setInt(i+1, 0);
			if(chSkills.getLearnedSkills().containsKey(i))
				st.setInt(i+1, chSkills.getLearnedSkills().get(i));
		}
		st.setInt(201, id);
		
		return st;
		
	}
	
	public static PreparedStatement dropCharSkillsTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = connection.prepareStatement("DROP TABLE charskills;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement createCharSkillbarsTable(Connection con) throws Exception {
		
		String s="CREATE TABLE `charskillbars`(";
		for(int i=0;i<21;i++){
			s+="`skillbar"+i+"` int(10) DEFAULT '-1', ";
		}
		s+="`belongsTo` int(10) unsigned NOT NULL,";
		s+="PRIMARY KEY (`belongsTo`)," +
			"UNIQUE KEY `belongsTo_UNIQUE` (`belongsTo`)," +
			"KEY `belongsTo_idx` (`belongsTo`)," +
			"CONSTRAINT `ownerID4` FOREIGN KEY (`belongsTo`) REFERENCES `characters` (`CharacterID`) ON DELETE CASCADE ON UPDATE CASCADE" +
			") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		
		PreparedStatement st = con.prepareStatement(s);
		
		return st;
		
	}
	
	public static PreparedStatement getCharSkillbars(Connection con, int id) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM charskillbars WHERE belongsTo = ?;");
		st.setInt(1, id);
		return st;
	}
	
	public static PreparedStatement createCharSkillbars(Connection con, int id, CharacterSkillbar chSkillbar) throws Exception{
		
		String s="INSERT INTO charskillbars(";
		for(int i=0;i<21;i++){
			s+="skillbar"+i+",";
		}
		s+="belongsTo) VALUES (";
		for(int i=0;i<21;i++){
			s+="?,";
		}
		s+="?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		for(int i=0;i<21;i++){
			st.setInt(i+1, -1);
			if(chSkillbar.getSkillBar().containsKey(i))
				st.setInt(i+1, chSkillbar.getSkillBar().get(i));
		}
		st.setInt(22, id);
		
		return st;
		
	}
	
	public static PreparedStatement getCharBuffs(Connection con, int id) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM charbuffs WHERE belongsTo = ?;");
		st.setInt(1, id);
		return st;
	}
	
	public static PreparedStatement createCharBuffs(Connection con, int id) throws Exception{
		
		String s="INSERT INTO charbuffs(";
		for(int i=0;i<19;i++){
			s+="buffId"+i+", buffTime"+i+", buffValue"+i+", ";
		}
		s+="belongsTo) VALUES (";
		for(int i=0;i<19;i++){
			s+="?, ?, ?, ";
		}
		s+="?);";

		PreparedStatement st = con.prepareStatement(s);
		
		for(int i=0;i<19*3;i++) {
			st.setInt(i+1, 0);
		}
		st.setInt(58, id);
		
		return st;
	}
	
	public static PreparedStatement storeCharBuffs(Connection con, int id, Buff[] buffActive) throws Exception {
		String s="UPDATE charbuffs SET ";
		
		for(int i=0;i<19;i++){
			if(i==18)
				s+="buffId"+i+"=?, buffTime"+i+"=?, buffValue"+i+"=? "; // no ", "
			else
				s+="buffId"+i+"=?, buffTime"+i+"=?, buffValue"+i+"=?, ";
		}
		s+="  WHERE belongsTo = ?;";

		PreparedStatement st = con.prepareStatement(s);

		for(int i=0;i<19*3;i+=3) {
			Buff buff;
			if(i == 0)
				buff = buffActive[0];
			else
				buff = buffActive[i/3 - 1];
			
			if (buff != null) {
		        st.setShort(i+1, (short) buff.getId());
		        st.setShort(i+2, (short) buff.getTimeLeft());
		        st.setShort(i+3, (short) buff.getBuffValue());
		    }
			else {
				st.setShort(i+1, (short)0);
		        st.setShort(i+2, (short)0);
		        st.setShort(i+3, (short)0);
			}
		}
		
		st.setInt(58, id);
		
		return st;
	}
	
	public static PreparedStatement storeCharSkillbars(Connection con, int id, CharacterSkillbar chSkillbar) throws Exception{
		
		String s="UPDATE charskillbars SET ";
		for(int i=0;i<20;i++){
			s+="skillbar"+i+"=?,";
		}
		s+="skillbar20=?";
		s+="  WHERE belongsTo = ?;";
		
		PreparedStatement st = con.prepareStatement(s);
		
		for(int i=0;i<21;i++){
			st.setInt(i+1, -1);
			if(chSkillbar.getSkillBar().containsKey(i))
				st.setInt(i+1, chSkillbar.getSkillBar().get(i));
		}
		st.setInt(22, id);
		
		return st;
		
	}
	
	public static PreparedStatement dropCharSkillbarsTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = connection.prepareStatement("DROP TABLE charskillbars;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement createLvlsTable(Connection con) throws Exception {
		PreparedStatement st = con.prepareStatement("CREATE TABLE `lvls` (" +
													"`lvl` int(10) unsigned NOT NULL,"+
													"`exp` bigint(20) unsigned NOT NULL,"+
													"PRIMARY KEY (`lvl`)," +
													"UNIQUE KEY `equid_UNIQUE` (`lvl`)" +
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
	}
	
	public static PreparedStatement getAllLvls(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM lvls;");
		return st;
	}
	
	public static PreparedStatement getLvl(Connection con, int id) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM lvls WHERE lvl = ?;");
		st.setInt(1, id);
		return st;
	}
	
	public static PreparedStatement addLvl(Connection con,int lvl,long exp) throws Exception{
		PreparedStatement st = con.prepareStatement("INSERT INTO lvls(lvl, exp) VALUES (?,?);");
		st.setInt(1, lvl);
		st.setLong(2, exp);
		return st;
	}
	
	public static PreparedStatement dropLvlsTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = connection.prepareStatement("DROP TABLE lvls;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement createGamemasterTable(Connection con) throws Exception{
		PreparedStatement st =  con.prepareStatement("CREATE TABLE `gamemaster` ("+
														"`rank` int(10) unsigned NOT NULL," +
														"`prename` char(16) NOT NULL," +
														"`gotGMname` tinyint(4) DEFAULT NULL," +
														"`commandpower` int(10) NOT NULL," +
														"`allocateGMrank` int(10) NOT NULL," +
														"`isPlayer` tinyint(4) DEFAULT NULL," +
														"PRIMARY KEY (`rank`), " +
														"UNIQUE KEY `rank_unique` (`rank`)" +
														") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
	}
	
	public static PreparedStatement dropGamemasterTable(Connection con) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = con.prepareStatement("DROP TABLE gamemaster;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement addGamemasterRank(Connection con,int rank, String prename, boolean gotGMname, 
			int commandpower, int allocateGMrank, boolean isPlayer) throws Exception{
		PreparedStatement st = con.prepareStatement("INSERT INTO gamemaster(rank, prename, gotGMname, commandpower, "
				+ "allocateGMrank, isPlayer) VALUES (?,?,?,?,?,?);");
		st.setInt(1, rank);
		st.setString(2, prename);
		if(gotGMname)
			st.setInt(3, 1);
		else
			st.setInt(3, 0);
		st.setInt(4, commandpower);
		st.setInt(5, allocateGMrank);
		if(isPlayer)
			st.setInt(6, 1);
		else
			st.setInt(6, 0);
		return st;
	}
	
	public static PreparedStatement getAllGamemasterRanks(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM gamemaster;");
		return st;
	}
	
	public static PreparedStatement getGamemasterRank(Connection con, int rank) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM gamemaster WHERE rank = ?;");
		st.setInt(1, rank);
		return st;
	}
	
	public static PreparedStatement createItemsetsTable(Connection con) throws Exception{
		String s="CREATE TABLE `itemset` ("+
				"`name` char(16) NOT NULL," +
				"`password` char(16) NOT NULL,";
		for(int i=0;i<60;i++){
			s+="`itemid"+(i+1)+"` int(10) unsigned DEFAULT '0',";
			s+="`itemamount"+(i+1)+"` int(10) unsigned DEFAULT '0',";
		}
		s+="PRIMARY KEY (`name`), " +
		"UNIQUE KEY `setitem_unique` (`name`)" +
		") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		PreparedStatement st =  con.prepareStatement(s);
														
		return st;
	}
	
	public static PreparedStatement dropItemsetsTable(Connection con) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = con.prepareStatement("DROP TABLE itemset;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement addItemset(Connection con, String name, String password, LinkedList<Integer> itemIds, LinkedList<Integer> itemAmounts) throws Exception{
		String s="INSERT INTO itemset(name, password, ";
		for(int i=0;i<59;i++){
			s+="itemid"+(i+1)+", ";
			s+="itemamount"+(i+1)+", ";
		}
		s+="itemid60, itemamount60) VALUES (?,?,";
		for(int i=0;i<59;i++){
			s+="?,?,";
		}
		s+="?,?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, name);
		st.setString(2, password);
		for(int i=0;i<60;i++){
			if(!itemIds.isEmpty() && !itemAmounts.isEmpty()){
				st.setInt(3+i*2, itemIds.removeFirst());
				st.setInt(4+i*2, itemAmounts.removeFirst());
			}else{
				st.setInt(3+i*2, 0);
				st.setInt(4+i*2, 0);
			}
		}
		
		return st;
	}
	
	public static PreparedStatement changeItemset(Connection con, String name, LinkedList<Integer> itemIds, LinkedList<Integer> itemAmounts) throws Exception{
		String s="UPDATE itemset SET ";
		for(int i=0;i<59;i++){
			s+="itemid"+(i+1)+"=?, ";
			s+="itemamount"+(i+1)+"=?, ";
		}
		s+="itemid60=?, itemamount60=? WHERE name=?;";
		
		PreparedStatement st = con.prepareStatement(s);
		
		for(int i=0;i<60;i++){
			if(!itemIds.isEmpty() && !itemAmounts.isEmpty()){
				st.setInt(1+i*2, itemIds.removeFirst());
				st.setInt(2+i*2, itemAmounts.removeFirst());
			}else{
				st.setInt(1+i*2, 0);
				st.setInt(2+i*2, 0);
			}
		}
		st.setString(121, name);
		
		return st;
	}
	
	public static PreparedStatement changeItemsetPassword(Connection con, String name, String password) throws Exception{
		PreparedStatement st=con.prepareStatement("UPDATE itemset SET password=? WHERE name=?;");
		st.setString(1, password);
		st.setString(2, name);
		return st;
	}
	
	public static PreparedStatement deleteItemset(Connection con, String name) throws Exception{
		PreparedStatement st=con.prepareStatement("DELETE FROM itemset WHERE name=?;");
		st.setString(1, name);
		return st;
	}
	
	public static PreparedStatement deleteAllItemset(Connection con) throws Exception{
		PreparedStatement st=con.prepareStatement("TRUNCATE itemset;");
		return st;
	}
	
	public static PreparedStatement getAllItemsets(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM itemset;");
		return st;
	}
	
	public static PreparedStatement getItemset(Connection con, String name) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM itemset WHERE name = ?;");
		st.setString(1, name);
		return st;
	}
	
	public static PreparedStatement createMacroTable(Connection con) throws Exception{
		String s="CREATE TABLE `macro` ("+
				"`name` char(16) NOT NULL," +
				"`password` char(16) NOT NULL,"+
				"`content` char(85) NOT NULL,"+
				"PRIMARY KEY (`name`), " +
				"UNIQUE KEY `macro_unique` (`name`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		PreparedStatement st =  con.prepareStatement(s);
														
		return st;
	}
	
	public static PreparedStatement dropMacroTable(Connection con) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = con.prepareStatement("DROP TABLE macro;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement addMacro(Connection con, String name, String password, String content) throws Exception{
		String s="INSERT INTO macro(name, password, content) VALUES (?,?,?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, name);
		st.setString(2, password);
		st.setString(3, content);
		
		return st;
	}
	
	public static PreparedStatement changeMacro(Connection con, String name, String content) throws Exception{
		String s="UPDATE macro SET content=? WHERE name=?;";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, content);
		st.setString(2, name);
		
		return st;
	}
	
	public static PreparedStatement changeMacroPassword(Connection con, String name, String password) throws Exception{
		PreparedStatement st=con.prepareStatement("UPDATE macro SET password=? WHERE name=?;");
		st.setString(1, password);
		st.setString(2, name);
		return st;
	}
	
	public static PreparedStatement deleteMacro(Connection con, String name) throws Exception{
		PreparedStatement st=con.prepareStatement("DELETE FROM macro WHERE name=?;");
		st.setString(1, name);
		return st;
	}
	
	public static PreparedStatement deleteAllMacros(Connection con) throws Exception{
		PreparedStatement st=con.prepareStatement("TRUNCATE macro;");
		return st;
	}
	
	public static PreparedStatement getAllMacros(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM macro;");
		return st;
	}
	
	public static PreparedStatement getMacro(Connection con, String name) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM macro WHERE name = ?;");
		st.setString(1, name);
		return st;
	}
	
	public static PreparedStatement createFilterTable(Connection con) throws Exception{
		String s="CREATE TABLE `filter` ("+
				"`category` char(16) NOT NULL," +
				"`command` char(16) NOT NULL,"+
				"`sqlName` char(16) NOT NULL,"+
				"`minValue` bigint(20) NOT NULL,"+
				"`mxValue` bigint(20) NOT NULL,"+
				"`standardValue` bigint(20) NOT NULL,"+
				"PRIMARY KEY (`category`,`command`), " +
				"UNIQUE KEY `filter_unique` (`category`,`command`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		PreparedStatement st =  con.prepareStatement(s);
														
		return st;
	}
	
	public static PreparedStatement dropFilterTable(Connection con) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = con.prepareStatement("DROP TABLE filter;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement addFilter(Connection con, String category, String command, String sqlName, long minValue, long maxValue, long standardValue) throws Exception{
		String s="INSERT INTO filter(category,command,sqlName,minValue,mxValue,standardValue) VALUES (?,?,?,?,?,?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, category);
		st.setString(2, command);
		st.setString(3, sqlName);
		st.setLong(4, minValue);
		st.setLong(5, maxValue);
		st.setLong(6, standardValue);
		
		return st;
	}
	
	public static PreparedStatement changeFilter(Connection con, String category, String command, String sqlName, long minValue, long maxValue, long standardValue) throws Exception{
		String s="UPDATE filter SET sqlName=?, minValue=?, mxValue=?, standardValue=? WHERE category=? AND command=?;";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, sqlName);
		st.setLong(2, minValue);
		st.setLong(3, maxValue);
		st.setLong(4, standardValue);
		st.setString(5, category);
		st.setString(6, command);
		
		return st;
	}
	
	public static PreparedStatement deleteFilter(Connection con, String category, String command) throws Exception{
		PreparedStatement st=con.prepareStatement("DELETE FROM filter WHERE category=? AND command=?;");
		st.setString(1, category);
		st.setString(2, command);
		return st;
	}
	
	public static PreparedStatement deleteAllFilters(Connection con) throws Exception{
		PreparedStatement st=con.prepareStatement("TRUNCATE filter;");
		return st;
	}
	
	public static PreparedStatement getAllFiltersByCategory(Connection con, String category) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM filter WHERE category=?;");
		st.setString(1, category);
		return st;
	}
	
	public static PreparedStatement getFilter(Connection con, String category, String command) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM filter WHERE category=? AND command=?;");
		st.setString(1, category);
		st.setString(2, command);
		return st;
	}
	
	public static PreparedStatement createDescriptionTable(Connection con) throws Exception{
		String s="CREATE TABLE `valuedescription` ("+
				"`category` char(16) NOT NULL," +
				"`descValue` int(10) NOT NULL,"+
				"`description` char(16) NOT NULL,"+
				"PRIMARY KEY (`category`,`descValue`), " +
				"UNIQUE KEY `filter_unique` (`category`,`descValue`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		PreparedStatement st =  con.prepareStatement(s);
														
		return st;
	}
	
	public static PreparedStatement dropDescriptionTable(Connection con) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = con.prepareStatement("DROP TABLE valuedescription;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement addDescription(Connection con, String category, int descValue, String description) throws Exception{
		String s="INSERT INTO valuedescription(category,descValue,description) VALUES (?,?,?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, category);
		st.setInt(2, descValue);
		st.setString(3, description);
		
		return st;
	}
	
	public static PreparedStatement changeDescription(Connection con, String category, int descValue, String description) throws Exception{
		String s="UPDATE valuedescription SET description=? WHERE category=? AND descValue=?;";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, description);
		st.setString(2, category);
		st.setInt(3, descValue);
		
		return st;
	}
	
	public static PreparedStatement deleteDescription(Connection con, String category, int descValue) throws Exception{
		PreparedStatement st=con.prepareStatement("DELETE FROM valuedescription WHERE category=? AND descValue=?;");
		st.setString(1, category);
		st.setInt(2, descValue);
		return st;
	}
	
	public static PreparedStatement deleteAllDescriptions(Connection con) throws Exception{
		PreparedStatement st=con.prepareStatement("TRUNCATE valuedescription;");
		return st;
	}
	
	public static PreparedStatement getAllDescriptions(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM valuedescription;");
		return st;
	}
	
	public static PreparedStatement getDescription(Connection con, String category, int descValue) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM valuedescription WHERE category=? AND descValue=?;");
		st.setString(1, category);
		st.setInt(2, descValue);
		return st;
	}
	
	public static PreparedStatement createEventTable(Connection con) throws Exception{
		String s="CREATE TABLE `event` ("+
				"`eventName` char(16) NOT NULL," +
				"`exp` float NOT NULL DEFAULT '1',"+
				"`dropr` float NOT NULL DEFAULT '1',"+
				"`coin` float NOT NULL DEFAULT '1',"+
				"`fame` float NOT NULL DEFAULT '1',"+
				"`generalStarrate` float NOT NULL DEFAULT '1',"+
				"`starrate` int(10) NOT NULL DEFAULT '100',"+
				"`superstarrate` int(10) NOT NULL DEFAULT '1666',"+
				"`multihitmobrate` int(10) NOT NULL DEFAULT '200',"+
				"`puzzlemobrate` int(10) NOT NULL DEFAULT '500',"+
				"`mobhp` float NOT NULL DEFAULT '1',"+
				"`description` char(50) NOT NULL,"+
				"PRIMARY KEY (`eventName`), " +
				"UNIQUE KEY `event_unique` (`eventName`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		PreparedStatement st =  con.prepareStatement(s);
														
		return st;
	}
	
	public static PreparedStatement dropEventTable(Connection con) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = con.prepareStatement("DROP TABLE event;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement addEvent(Connection con, String eventName, float exp, float drop, float coin,
			float fame, float generalStarrate, int starrate, int superstarrate, int multihitmobrate,
			int puzzlemobrate, float mobhp, String desc) throws Exception{
		String s="INSERT INTO event(eventName, exp, dropr, coin, fame, generalStarrate, starrate, superstarrate,"
				+ " multihitmobrate, puzzlemobrate, mobhp, description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, eventName);
		st.setFloat(2, exp);
		st.setFloat(3, drop);
		st.setFloat(4, coin);
		st.setFloat(5, fame);
		st.setFloat(6, generalStarrate);
		st.setInt(7, starrate);
		st.setInt(8, superstarrate);
		st.setInt(9, multihitmobrate);
		st.setInt(10, puzzlemobrate);
		st.setFloat(11, mobhp);
		st.setString(12, desc);
		
		return st;
	}
	
	public static PreparedStatement updateEvent(Connection con, String eventName, float exp, float drop, float coin,
			float fame, float generalStarrate, int starrate, int superstarrate, int multihitmobrate,
			int puzzlemobrate, float mobhp, String desc) throws Exception{
		String s="UPDATE event SET exp=? dropr=? coin=? fame=? generalStarrate=? starrate=? superstarrate=? multihitmobrate=? puzzlemobrate=? mobhp=? desc=? WHERE eventName=?;";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setFloat(1, exp);
		st.setFloat(2, drop);
		st.setFloat(3, coin);
		st.setFloat(4, fame);
		st.setFloat(5, generalStarrate);
		st.setInt(6, starrate);
		st.setInt(7, superstarrate);
		st.setInt(8, multihitmobrate);
		st.setInt(9, puzzlemobrate);
		st.setFloat(10, mobhp);
		st.setString(11, desc);
		st.setString(12, eventName);
		
		return st;
	}
	
	public static PreparedStatement deleteEvent(Connection con, String eventName) throws Exception{
		PreparedStatement st=con.prepareStatement("DELETE FROM event WHERE eventName=?;");
		st.setString(1, eventName);
		return st;
	}
	
	public static PreparedStatement getAllEvents(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM event;");
		return st;
	}
	
	public static PreparedStatement getEvent(Connection con, String eventName) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM event WHERE eventName = ?;");
		st.setString(1, eventName);
		return st;
	}
	
	public static PreparedStatement createServerControlTable(Connection con) throws Exception{
		String s="CREATE TABLE `server` ("+
				"`serverName` char(16) NOT NULL," +
				"`actualEvent` char(16) NOT NULL,"+
				"PRIMARY KEY (`serverName`), " +
				"UNIQUE KEY `server_unique` (`serverName`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		PreparedStatement st =  con.prepareStatement(s);
														
		return st;
	}
	
	public static PreparedStatement dropServerControlTable(Connection con) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = con.prepareStatement("DROP TABLE server;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement addServerControl(Connection con, String serverName, String eventName) throws Exception{
		String s="INSERT INTO server(serverName, actualEvent) VALUES (?,?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, serverName);
		st.setString(2, eventName);
		
		return st;
	}
	
	public static PreparedStatement deleteServerControl(Connection con, String serverName) throws Exception{
		PreparedStatement st=con.prepareStatement("DELETE FROM server WHERE serverName=?;");
		st.setString(1, serverName);
		return st;
	}
	
	public static PreparedStatement changeServerControlEvent(Connection con, String serverName, String eventName) throws Exception{
		PreparedStatement st = con.prepareStatement("UPDATE server SET actualEvent=? WHERE serverName = ?;");
		st.setString(1, eventName);
		st.setString(2, serverName);
		return st;
	}
	
	public static PreparedStatement getCurrentEvent(Connection con, String serverName) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT actualEvent FROM server WHERE serverName = ?;");
		st.setString(1, serverName);
		return st;
	}
	
	public static PreparedStatement createUpgradeTable(Connection con) throws Exception {
		PreparedStatement st = con.prepareStatement("CREATE TABLE `upgrade` (" +
													"`upgradeId` int(10) unsigned NOT NULL,"+
													"`oldItem` int(10) unsigned NOT NULL,"+
													"`upgrader` int(10) unsigned NOT NULL,"+
													"`newItem` int(10) unsigned NOT NULL,"+
													"`itemStage` float NOT NULL,"+
													"`upgradeLvl` float NOT NULL,"+
													"`failRate` float NOT NULL,"+
													"`breakOption` float NOT NULL,"+
													"`upgradeSkill` int(10) unsigned NOT NULL,"+
													"PRIMARY KEY (`upgradeId`)," +
													"UNIQUE KEY `upgrid_UNIQUE` (`upgradeId`)" +
													") ENGINE=InnoDB DEFAULT CHARSET=ascii;");
		return st;
	}
	
	public static PreparedStatement getAllUpgrades(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM upgrade;");
		return st;
	}
	
	public static PreparedStatement getUpgradeByItemAndUpgrader(Connection con, int oldItem, int upgrader) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM upgrade WHERE oldItem = ? AND upgrader = ?;");
		st.setInt(1, oldItem);
		st.setInt(2, upgrader);
		return st;
	}
	
	public static PreparedStatement addUpgrade(Connection con,int id, int oldIt, int upgrader, int newIt,
			float itStage, float upgradelvl, float failrate, float breakoption, int upgradeskill) throws Exception{
		PreparedStatement st = con.prepareStatement("INSERT INTO upgrade(upgradeId,oldItem,upgrader,newItem,"
				+ "itemStage,upgradeLvl,failRate,breakOption,upgradeSkill) VALUES (?,?,?,?,?,?,?,?,?);");
		st.setInt(1, id);
		st.setInt(2, oldIt);
		st.setInt(3, upgrader);
		st.setInt(4, newIt);
		st.setFloat(5, itStage);
		st.setFloat(6, upgradelvl);
		st.setFloat(7, failrate);
		st.setFloat(8, breakoption);
		st.setInt(9, upgradeskill);
		return st;
	}
	
	public static PreparedStatement dropUpgradeTable(Connection connection) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = connection.prepareStatement("DROP TABLE upgrade;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement createPuzzlemobTable(Connection con) throws Exception{
		String s="CREATE TABLE `puzzlemob` ("+
				"`question` char(50) NOT NULL," +
				"`answer` char(20) NOT NULL,"+
				"`type` int(10) NOT NULL,"+
				"`coinrate` float NOT NULL,"+
				"`exprate` float NOT NULL,"+
				"`droprate` float NOT NULL,"+
				"`bonusdrop` int(10) NOT NULL,"+
				"PRIMARY KEY (`question`), " +
				"UNIQUE KEY `puzzle_unique` (`question`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		PreparedStatement st =  con.prepareStatement(s);
														
		return st;
	}
	
	public static PreparedStatement dropPuzzlemobTable(Connection con) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = con.prepareStatement("DROP TABLE puzzlemob;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement addPuzzlemob(Connection con, String question, String answer, int type, float coinrate,
			float exprate, float droprate, int bonusdrop) throws Exception{
		String s="INSERT INTO puzzlemob(question,answer,type,coinrate,exprate,droprate,bonusdrop) VALUES (?,?,?,?,?,?,?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, question);
		st.setString(2, answer);
		st.setInt(3, type);
		st.setFloat(4, coinrate);
		st.setFloat(5, exprate);
		st.setFloat(6, droprate);
		st.setInt(7, bonusdrop);
		
		return st;
	}
	
	public static PreparedStatement deleteAllPuzzlemobs(Connection con) throws Exception{
		PreparedStatement st=con.prepareStatement("TRUNCATE puzzlemob;");
		return st;
	}
	
	public static PreparedStatement getAllPuzzlemobs(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM puzzlemob;");
		return st;
	}
	
	public static PreparedStatement createRandomnameTable(Connection con) throws Exception{
		String s="CREATE TABLE `randomname` ("+
				"`name` char(20) NOT NULL," +
				"PRIMARY KEY (`name`), " +
				"UNIQUE KEY `randomname_unique` (`name`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		PreparedStatement st =  con.prepareStatement(s);
														
		return st;
	}
	
	public static PreparedStatement dropRandomnameTable(Connection con) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = con.prepareStatement("DROP TABLE randomname;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement addRandomname(Connection con, String name) throws Exception{
		String s="INSERT INTO randomname(name) VALUES (?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, name);
		
		return st;
	}
	
	public static PreparedStatement deleteAllRandomnames(Connection con) throws Exception{
		PreparedStatement st=con.prepareStatement("TRUNCATE randomname;");
		return st;
	}
	
	public static PreparedStatement getAllRandomnames(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM randomname;");
		return st;
	}
	
	public static PreparedStatement createRandomsentenceTable(Connection con) throws Exception{
		String s="CREATE TABLE `randomsentence` ("+
				"`sentence` char(80) NOT NULL," +
				"PRIMARY KEY (`sentence`), " +
				"UNIQUE KEY `sentence_unique` (`sentence`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=ascii;";
		PreparedStatement st =  con.prepareStatement(s);
														
		return st;
	}
	
	public static PreparedStatement dropRandomsentenceTable(Connection con) throws Exception {
		if (ConfigurationManager.getProcessName().contentEquals("install")){
			PreparedStatement st = con.prepareStatement("DROP TABLE randomsentence;");
			return st;
		}
		throw new Exception();
	}
	
	public static PreparedStatement addRandomsentence(Connection con, String sentence) throws Exception{
		String s="INSERT INTO randomsentence(sentence) VALUES (?);";
		
		PreparedStatement st = con.prepareStatement(s);
		
		st.setString(1, sentence);
		
		return st;
	}
	
	public static PreparedStatement deleteAllRandomsentences(Connection con) throws Exception{
		PreparedStatement st=con.prepareStatement("TRUNCATE randomsentence;");
		return st;
	}
	
	public static PreparedStatement getAllRandomsentences(Connection con) throws Exception{
		PreparedStatement st = con.prepareStatement("SELECT * FROM randomsentence;");
		return st;
	}
	
}
