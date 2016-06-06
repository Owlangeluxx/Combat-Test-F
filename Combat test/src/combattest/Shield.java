package combattest;

public class Shield {
    int id;
    String name;
    int armor;
    int weight;
    double physical;   //1 - (0 to 1 phys mitigation)
    double ranged;     //1 - (0 to 1 ranged mitigation)
    double magical;    //1 - (0 to 1 magical mitigation)
    SpellBook spells;
    AuraBook auras;
    
    public Shield (int id) {
        this.id = id;
        spells = new SpellBook();
        auras = new AuraBook();
        switch (id) {
            case 1:
                name = "Iron shield";
                armor = 10;
                weight = 2;
                physical = 0.15;
                ranged = 0;
                magical = 0.4;
            default:
                this.id = 999;
                name = "Error shield code";
                break;
        }
    }
}
