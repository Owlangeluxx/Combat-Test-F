package combattest;

public class Unit {
    String name;
    int lvl;
    UnitMask mask;
    String moniker;
    AuraBook auras;     //auras active on character
    SpellBook spells;   //skills available to character
    int unitID;         //unique unit ID
    
    //original arguments, susceptible to mask
    int STR0;
    int DEX0;
    int WIS0;
    int INT0;
    int VIT0;
    int SPD0;
    int armor0;
    int faction0;
    int fireRes0;
    int iceRes0;
    int natRes0;
    int holyRes0;
    int darkRes0;
    boolean shield0;
    
    //displayed arguments
    int STR;
    int DEX;
    int INT;
    int WIS;
    int VIT;
    int SPD;
    int armor;
    boolean shield; //true if shield present
    int resourceType;   //0: mana, 1: rage, 2: energy
    int fireRes;
    int iceRes;
    int natRes;
    int holyRes;
    int darkRes;
    int faction;    //1 is player, 2 to 9 are enemies (mutual and
                        //towards player)
                    //11 is player ally
                    //0 is debug error
                    //10 is destructible elements
    boolean horse;
    boolean rider;  //true if no horse penalty should apply
    Weapon weap;  //equiped weapon
    Weapon auxWeap; //auxiliary weapon for disarmed instances
    int attackRange;
    
    //calculated values
    int maxHp;
    int maxMana;
    int hp;
    int mana;
    int steps;    
        
    //these determine status presence
    boolean silenced; //can't cast INT or WIS affinity spells
    boolean immune; //can't receive damage
    boolean invisible; //can't be seen by enemies, breaks upon receiving damage
    
    //unit affinities, determine castable spells
    boolean affSTR;
    boolean affDEX;
    boolean affINT;
    boolean affWIS;
    int AP; //action points
    int MP; //movement points
    
    public Unit (String name, String type, int lvl, int faction, int id) {
        moniker = name;
        UnitList nUnit = new UnitList(type, lvl);
        unitID = id;
        this.name = nUnit.name;
        this.lvl = nUnit.lvl;
        this.STR = nUnit.STR;
        this.DEX = nUnit.DEX;
        this.INT = nUnit.INT;
        this.WIS = nUnit.WIS;
        this.VIT = nUnit.VIT;
        this.SPD = nUnit.SPD;
        this.armor = nUnit.armor;
        this.shield = nUnit.shield;
        this.resourceType = nUnit.resourceType;
        this.fireRes = nUnit.fireRes;
        this.iceRes = nUnit.iceRes;
        this.natRes = nUnit.natRes;
        this.holyRes = nUnit.holyRes;
        this.darkRes = nUnit.darkRes;
        this.faction = faction;
        this.horse = nUnit.horse;
        this.rider = nUnit.rider;
        this.spells = nUnit.spells;
        this.auras = nUnit.auras;
        weap = nUnit.weapon;
        mask = new UnitMask();
        STR0 = STR;
        DEX0 = DEX;
        WIS0 = WIS;
        INT0 = INT;
        VIT0 = VIT;
        SPD0 = SPD;
        armor0 = armor;
        faction0 = this.faction;
        fireRes0 = fireRes;
        iceRes0 = iceRes;
        natRes0 = natRes;
        holyRes0 = holyRes;
        darkRes0 = darkRes;
        shield0 = shield;
        
        silenced = false;
        immune = false;
        invisible = false;
        AP = 0;
        MP = 0;
    
        this.affSTR = nUnit.affSTR;
        this.affDEX = nUnit.affDEX;
        this.affINT = nUnit.affINT;
        this.affWIS = nUnit.affWIS;
        
        //calculate secondary stats
        maxHp = this.VIT*4;
        if (this.resourceType == 0) {
            maxMana = this.INT*4;
        } else {
            maxMana = 100;
        }
        hp = maxHp;
        if (this.resourceType == 1 || this.resourceType == 3) {
            mana = 0;
        } else {
            mana = maxMana;
        }
        steps = SPD*4/lvl;
        if (horse) {
            steps = steps + 4;
            if (!rider) {
                DEX = (int) (DEX * 0.7);
            }
        }
        equipWeapon(weap);
    }
    
    //lite report of unit info
    public void liteReport () {
        String resource;
        switch (resourceType) {
            case 0:
                resource = "Mana: ";
                break;
            case 1:
                resource = "Rage: ";
                break;
            case 2:
                resource = "Energy: ";
                break;
            default:
                resource = "Missing resource type: ";
                break;
        }
        String report = moniker+" of class "+name+":"+"\n"+
                "HP: "+hp+"/"+maxHp+"\n"+resource+mana+"/"+maxMana+"\n"+
                "Faction: "+faction;
        System.out.println(report);
        System.out.println();
    }
    
    //full report of unit info
    public void fullReport () {
        String resource;
        String weapon;
        String shieldNote;
        switch (resourceType) {
            case 0:
                resource = "Mana: ";
                break;
            case 1:
                resource = "Rage: ";
                break;
            case 2:
                resource = "Energy: ";
                break;
            case 3:
                resource = "Focus: ";
                break;
            default:
                resource = "Missing resource type: ";
                break;
        }
        
        switch (weap.type) {
            case 0:
                weapon = "unarmed";
                break;
            case 1:
                weapon = "1h sword";
                break;
            case 2:
                weapon = "1h axe";
                break;
            case 3:
                weapon = "1h hammer";
                break;
            case 4:
                weapon = "1h spear";
                break;
            case 5:
                weapon = "1h dagger";
                break;
            case 11:
                weapon = "2h sword";
                break;
            case 12:
                weapon = "2h axe";
                break;
            case 13:
                weapon = "2h hammer";
                break;
            case 14:
                weapon = "2h spear";
                break;
            case 15:
                weapon = "dual wield daggers";
                break;
            case 21:
                weapon = "bow";
                break;
            case 22:
                weapon = "crossbow";
                break;
            case 30:
                weapon = "staff";
                break;
            default:
                weapon = "missing weapon type";
                break;
        }
        
        if (shield && weap.type > 5) {
            shieldNote = " - Shield detected with "+weapon+" weapon "
                    + "configuration";
        } else {
            shieldNote = "";
        }
        
        String report = "Report for unit "+moniker+" of class "+name+":"+"\n"+
                "Faction = "+faction+"\n"+
                "HP: "+hp+"/"+maxHp+"\n"+resource+mana+"/"+maxMana+"\n"+
                "STR = "+STR+", DEX = "+DEX+", INT= "+INT+"\n"+
                "WIS = "+WIS+", VIT = "+VIT+", SPD = "+SPD+"\n"+
                "Weapon type = "+weapon+", weapon raw damage = "+weap.raw+"\n"+
                "Shield = "+shield+shieldNote+"\n"+
                "Armor = "+armor+"\n"+
                "Magic school resistances:"+"\n"+"Fire: "+fireRes+", Ice: "+
                iceRes+", Nature: "+natRes+", Holy: "+holyRes+", Dark: "+
                darkRes+"\n";
        System.out.println(report);
        System.out.println();
    }
    
    //hp loss method
    public void hpLost (int hpLost) {
        if (!immune) {
            hp = hp - hpLost;
            invisible = false;
            if (hp < 0) {
                hp = 0;
            }
        }
    }
    
    //hp gain method
    public void hpGained (int hpGained) {
        hp = hp + hpGained;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }
    
    //mana consumption method
    public void manaLost (int manaLost) {
        mana = mana - manaLost;
        if (mana < 0) {
            mana = 0;
        }
    }
    
    //mana gain method
    public void manaGained (int manaGained) {
        mana = mana + manaGained;
        if (mana > maxMana) {
            mana = maxMana;
        }
    }
    
    //return true if unit hp == 0
    public boolean isDead(){
        boolean sixFeetUnder = false;
        if (hp == 0) {
            System.out.println("Unit "+moniker+" of type "+name+" is dead."+"\n");
            sixFeetUnder = true;
        }
        return sixFeetUnder;
    }
    
    //sets AP and MP to 1 after turn start
    public void setPoints() {
        AP = 1;
        MP = 1;
    }
    
    //reduces MP count by 1 if not 0
    public void consumeMP() {
        if (MP > 0) {
            MP--;
        } else {
            System.out.println("MP is already 0.");
        }     
    }
    
    //reduces AP by 1 if not 0. Also sets MP to 0
    public void consumeAP() {
        if (AP > 0) {
            AP--;
            MP = 0;
        } else {
            System.out.println("AP is already 0.");
        }
    }
    
     //return true if AP > 0
    public boolean active() {
        return (AP > 0);
    }
    
    //updates unit information after mask is accessed (must be manually invoked)
    public void maskUpdate() {
        STR = Math.max (0, (STR0 + mask.STR));
        DEX = Math.max (0, (DEX0 + mask.DEX));
        INT = Math.max (0, (INT0 + mask.INT));
        WIS = Math.max (0, (WIS0 + mask.WIS));
        VIT = Math.max (0, (VIT0 + mask.VIT));
        SPD = Math.max (0, (SPD0 + mask.SPD));
        armor = Math.max (0, (armor0 + mask.armor));
        
        fireRes = Math.max (0, (fireRes0 + mask.fireMod));
        iceRes = Math.max (0, (iceRes0 + mask.iceMod));
        natRes = Math.max (0, (natRes0 + mask.natMod));
        holyRes = Math.max (0, (holyRes0 + mask.holyMod));
        darkRes = Math.max (0, (darkRes0 + mask.darkMod));
        
        maxHp = this.VIT*4;
        if (hp > maxHp) {
            hp = maxHp;
        }
        if (this.resourceType == 0) {
            maxMana = this.INT*4;
            if (mana > maxMana) {
                mana = maxMana;
            }
        }
        steps = Math.max (1, ((SPD*4/lvl) + mask.stepsMod));
        if (horse) {
            steps = steps + 4;
            if (!rider) {
                DEX = (int) (DEX0 * 0.7);
            }            
        } else {
            DEX = DEX0;   
        }
        if (mask.factionChange) {
            faction = mask.faction;
        } else {
            faction = faction0;
        }
        if (weap.type == 21 || weap.type == 22) {
            attackRange = Math.max (0, (weap.range + mask.rangeMod));
        }
    }
    
    //equips weapon
    public final void equipWeapon (Weapon w) {
        if (weap != null) {
            unequipWeapon();
        }
        weap = w;
        auras.addAuraBook(weap.auras);
        spells.addSpellBook(weap.spells);
        if (weap.type == 21 || weap.type == 22) {
            attackRange = Math.max (0, (weap.range + mask.rangeMod));
        } else {
            attackRange = weap.range;
        }
    }
    
    //unequips weapon
    public void unequipWeapon () {
        auras.removeAuraBook(weap.auras);
        spells.removeSpellBook(weap.spells);
        weap = new Weapon(0);
        attackRange = weap.range;
    }
}