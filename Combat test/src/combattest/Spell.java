package combattest;

public class Spell {
    int ID;
    String name;
    int affinity;       //0 for none, 1 for STR, 2 for DEX, 3 for INT, 4 for WIS
    int type;           //0 for any, 1 for melee, 2 for ranged, 3 for melee/ranged,
                        //4 for shield
    boolean silence;    //1 if susceptible to silence
    String desc;
    
    public Spell (int id) {
        ID = id;
        type = 0;
        silence = false;
        switch (id) {
            case 0:
                name = "Attack";
                affinity = 0;
                desc = "Attacks target enemy in range using equipped weapon.";
                break;
            case 1:
                name = "Shield Wall";
                affinity = 1;
                type = 4;
                desc = "Prepares the caster to intercept the next attack "
                        + "launched against an adjacent ally. Damage received"
                        + " by the user is reduced.";
                break;
            case 2:
                name = "Mount up!";
                affinity = 1;
                desc = "Calls and mounts upon a trusty steed. If used while " +
                        "already mounted, dismounts and dismisses the animal.";
                break;
            case 3:
                name = "Sundering strike";
                affinity = 1;
                type = 1;
                desc = "Powerful strike that aims to rend the flesh of the "
                        + "enemy, causing them to bleed. Bleeding targets will"
                        + " slowly lose health and have their DEX reduced.";
                break;
            case 4:
                name = "Disarm";
                affinity = 1;
                desc = "Takes the opponent's weapon away from him, leaving them"
                        + " unable to use it for the next turn.";
                break;
            case 5:
                name = "Fireball";
                affinity = 3;
                desc = "Launches a ball of flames against target enemy. "
                        + "Range 3.";
                silence = true;
                break;
            case 6:
                name = "Teleport";
                affinity = 3;
                desc = "Teleports the caster to target accessible location. "
                        + "Range 5.";
                silence = true;
                break;
            case 7:
                name = "Summon familiar";
                affinity = 3;
                desc = "Calls upon an elemental spirit to aid the caster in "
                        + "battle for 3 turns. Range 2.";
                silence = true;
                break;
            case 8:
                name = "Wall of flames";
                affinity = 3;
                desc = "Causes the ground to burn in a straight line from the "
                        + "caster, causing enemies standing on the fire to "
                        + "take damage periodically. Range 4.";
                silence = true;
                break;
            case 9:
                name = "Stealth";
                affinity = 2;
                desc = "The unit becomes one with the shadows, concealing his "
                        + "or her position for the next 2 turns.";
                break;
            case 10:
                name = "Preparation";
                affinity = 2;
                desc = "The unit readies itself for action, allowing for the "
                        + "use of two skills or attacks in the following turn";
                break;
            case 11:
                name = "Sprint";
                affinity = 2;
                desc = "Sets the user for an additional movement in the current"
                        + "turn.";
                break;
            case 12:
                name = "Deadly poison";
                affinity = 2;
                type = 3;
                desc = "The user coats his or her weapons in a deadly mixture, "
                        + "poisoning the enemies on strike. Lasts 5 turns.";
                break;
            case 13:
                name = "Piercing shot";
                affinity = 2;
                type = 2;
                desc = "Shoots an arrow in a straight line, damaging all enemies"
                        + " it passes through. Requires a ranged weapon equipped"
                        + ". Range 6.";
                break;
            case 14:
                name = "Hold your breath";
                affinity = 2;
                desc = "The unit concentrates on the flow of battle around him,"
                        + " generating focus. Consumes movement points.";
                break;
            case 15:
                name = "Paralyzing trap";
                affinity = 2;
                desc = "The user lays a trap at his or her feet, which will be"
                        + "triggered upon being step on by an enemy unit";
                break;
            case 16:
                name = "Volley";
                affinity = 2;
                type = 2;
                desc = "The user fires a flurry of arrows directed at target "
                        + "location, dealing damage in a small area and "
                        + "bypassing walls and other terrain impassable "
                        + "terrain";
                break;
            default:
                name = "Default spell error";
                affinity = 0;
                desc = "Ye dun gooffed.";
                ID = 999;
                break;
        }
    }
    
    public void report() {
        System.out.println(name);
        System.out.println("Spell ID: "+ID);
        System.out.println(desc);
        System.out.print("Affinity: "); 
        switch(affinity) {
            case 0:
                System.out.println("None");
                break;
            case 1:
                System.out.println("STR");
                break;
            case 2:
                System.out.println("DEX");
                break;
            case 3:
                System.out.println("INT");
                break;
            case 4:
                System.out.println("WIS");
                break;
            default:
                System.out.println("Error affinity code.");
                break;
        }
        if (silence) {
            System.out.println("Can't be cast while silenced.");
        }
        switch(type) {
            case 0:
                break;
            case 1:
                System.out.println("Melee weapon required.");
                System.out.println();
                break;
            case 2:
                System.out.println("Ranged weapon required.");
                System.out.println();
                break;
            case 3:
                System.out.println("Melee or ranged weapon required.");
                System.out.println();
                break;
            case 4:
                System.out.println("Shield required.");
                System.out.println();
                break;
            default:
                System.out.println("Error weapon type code.");
                System.out.println();
                break;
        }
    }
    
    public void liteReport() {
        System.out.println(name);
        System.out.println("Spell ID: "+ID);
    }
}