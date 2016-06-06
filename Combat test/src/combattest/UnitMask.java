package combattest;

public class UnitMask {
    //modifiers of values (+-)
    int STR;
    int DEX;
    int INT;
    int WIS;
    int VIT;
    int SPD;
    int armor;
    int rangeMod;
    int stepsMod;
    
    //modifiers of magic school resistance
    int fireMod;
    int iceMod;
    int natMod;
    int holyMod;
    int darkMod;
    
    //allows for in-game faction changes
    boolean factionChange;
    int faction;    //1 is player, 2 to 9 are enemies (mutual and
                        //towards player)
                    //11 is player ally
                    //0 is debug error
                    //10 is destructible elements
    
    public UnitMask () {
        STR = 0;
        DEX = 0;
        INT = 0;
        WIS = 0;
        VIT = 0;
        SPD = 0;
        armor = 0;
        rangeMod = 0;
        stepsMod = 0;
        fireMod = 0;
        iceMod = 0;
        natMod = 0;
        holyMod = 0;
        darkMod = 0;
        factionChange = false;
        faction = 0;
    }
}