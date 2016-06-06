package combattest;

public class Weapon {
    int id;
    String name;
    int type;
    int raw;
    int crit;
    int weight;
    int range;
    SpellBook spells;
    AuraBook auras;
    
    public Weapon (int id) {
        this.id = id;
        spells = new SpellBook();
        auras = new AuraBook();
        switch (id) {
            case 0:
                name = "Disarmed";
                type = 0;
                raw = 3;
                crit = 0;
                weight = 0;
                range = 0;
                break;
            case 1:
                name = "Iron daggers";
                type = 15;
                raw = 15;
                crit = 10;
                weight = 1;
                range = 1;
                break;
            case 2:
                name = "Iron sword";
                type = 1;
                raw = 10;
                crit = 0;
                weight = 2;
                range = 1;
                break;
            case 3:
                name = "Iron longbow";
                type = 21;
                raw = 8;
                crit = 5;
                weight = 1;
                range = 4;
                break;
            default:
                this.id = 999;
                name = "Error weapon code";
                break;
        }
    }
}
