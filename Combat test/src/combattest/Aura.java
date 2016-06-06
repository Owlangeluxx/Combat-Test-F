package combattest;

public class Aura {
    int ID;
    String name;
    String desc;
    int n;  //activations left, -1 if the aura is permanent.
    int k;  //auxiliary ammount method.
    
    public Aura (int id, int charges) {
        this (id, charges, 0);
    }
    
    public Aura (int id, int charges, int k) {
        ID = id;
        this.n  = charges;
        this.k = k;
        switch (id) {
            case 1:
                ID = id;
                name = "Shields up!";
                desc = "Ready to intercept the next attack against a nearby"
                        + " ally.";
                break;
            case 2:
                ID = id;
                name = "Bleeding";
                desc = "Suffering from stunted dexterity and periodically "
                        + "losing health.";
                break;
            case 3:
                ID = id;
                name = "Ready for action";
                desc = "Increases action points for next turn by one.";
                break;
            case 4:
                ID = id;
                name = "Poisonous strikes";
                desc = "Will poison the enemy upon striking them with a weapon.";
                break;
            case 5:
                ID = id;
                name = "Poisoned";
                desc = "Slowly losing health.";
                break;
            case 6:
                ID = id;
                name = "Disarmed";
                desc = "Unable to use a weapon.";
                break;
            case 7:
                ID = id;
                name = "Stealthed";
                desc = "Inivisble to enemy units.";
                break;
            case 100:
                ID = id;
                name = "Hey there, I'm a door";
                desc = "Just doing door things.";
                break;
            default:
                ID = 999;
                name = "Default aura error";
                desc = "Ye dun gooffed.";
                this.n = 0;
                this.k = 0;
                break;
        }
    }
    
    //Reports aura data
    public void report() {
        System.out.println(name);
        System.out.println("Aura ID: "+ID);
        System.out.println(desc);
        System.out.println("Activations remaining: "+n);
        System.out.println("Ammount (aux): "+k);
        System.out.println();
    }
    
    //Reduces activation count
    public void advance() {
        if (n > 0) {
            n--;
        }
    }
}