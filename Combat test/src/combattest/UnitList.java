package combattest;

public class UnitList {
    //arguments
    String name;
    int lvl;
    int STR;
    int DEX;    //armor penalty already applied
    int INT;
    int WIS;
    int VIT;
    int SPD;
    int armor;
    boolean shield; //true if shield present
    int resourceType;   //0: mana, 1: rage, 2: energy, 3: focus
    int fireRes;
    int iceRes;
    int natRes;
    int holyRes;
    int darkRes;
    boolean horse;
    boolean rider;
    boolean affSTR;
    boolean affDEX;
    boolean affINT;
    boolean affWIS;
    SpellBook spells;
    AuraBook auras;
    Weapon weapon;

    public UnitList (String type, int level) {
        name = "DummyUnit";
        lvl = level;
        STR = 0;
        DEX = 0;
        INT = 0;
        WIS = 0;
        VIT = 0;
        SPD = 0;
        armor = 0;
        shield = false;
        resourceType = 0;
        fireRes = 0;
        iceRes = 0;
        natRes = 0;
        holyRes = 0;
        darkRes = 0;
        int powerLevel = 1;
        horse = false;
        rider = false;
        affSTR = false;
        affDEX = false;
        affINT = false;
        affWIS = false;
        spells = new SpellBook();
        auras = new AuraBook();
        
        if(null != type) switch (type) {
            case "testAssassin0":
                name = "Test Assassin";
                STR = (int) (1.2*powerLevel*lvl);
                DEX = (int) (1.4*powerLevel*lvl);
                INT = (int) (0.5*powerLevel*lvl);
                WIS = (int) (0.6*powerLevel*lvl);
                VIT = (int) (0.9*powerLevel*lvl);
                SPD = (int) (1.4*powerLevel*lvl);
                armor = (int) (1.0*powerLevel*lvl);
                resourceType = 2;
                affDEX = true;
                weapon = new Weapon(1);
                break;
            case "testSoldier0":
                name = "Test Soldier";
                STR = (int) (1.5*powerLevel*lvl);
                DEX = (int) (0.9*powerLevel*lvl);
                INT = (int) (0.5*powerLevel*lvl);
                WIS = (int) (1.0*powerLevel*lvl);
                VIT = (int) (1.2*powerLevel*lvl);
                SPD = (int) (0.8*powerLevel*lvl);
                armor = (int) (1.5*powerLevel*lvl);
                shield = true;
                resourceType = 1;
                affSTR = true;
                weapon = new Weapon(2);
            case "testArcher0":
                name = "Test Archer";
                STR = (int) (1.1*powerLevel*lvl);
                DEX = (int) (1.5*powerLevel*lvl);
                INT = (int) (0.5*powerLevel*lvl);
                WIS = (int) (1.0*powerLevel*lvl);
                VIT = (int) (1.0*powerLevel*lvl);
                SPD = (int) (1.2*powerLevel*lvl);
                armor = (int) (1.2*powerLevel*lvl);
                resourceType = 3;
                affDEX = true;
                weapon = new Weapon(3);
            default:
                weapon = new Weapon(0);
                break;
        }
    }
}