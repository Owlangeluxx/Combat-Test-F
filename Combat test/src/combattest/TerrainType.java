package combattest;

public class TerrainType {
    int height;
    int penDex; //penalty to DEX
    int penSpd; //penalty to SPD
    int occ; //terrain occlusion bonus
    int accessible; //0 if this cell cannot be occupied by a unit; ie.: walls or
                    //stones
    int los;    //0 if projectiles can't fly through this cells; ie.: walls or
                //columns
    int vertical;   //determines whether a cell can be accessed and left
                    //vertically. Useful for directional cells such as stairs
    int horizontal; //determines whether a cell can be accessed and left
                    //horizontally. Useful for directional cells such as stairs
    String code; //Terrain type ID

    public TerrainType(String t) {
        height = 0;
        penDex = 0;
        penSpd = 0;
        occ = 0;
        accessible = 1;
        los = 1;
        vertical = 1;
        horizontal = 1;
        code = "Default terrain error code";

        if(null != t) switch (t) {
            case "plains0":
                code = "  ";
                break;
                
            case "swamp0":
                penDex = 1;
                penSpd = 2;
                code = "S0";
                break;
                
            case "forest0":
                penSpd = 1;
                occ = 2;
                code = "F0";
                break;
                
            case "stairsUp0":
                height = 1;
                horizontal = 0;
                code = "SU";
                break;
                
            case "battlement0":
                height = 2;
                code = "B0";
                break;
                
            case "inaccessible0":
                accessible = 0;
                code = "XX";
                break;
                
            case "column0":
                accessible = 0;
                los = 0;
                code = "XX"; 
                break;
                
            default:
                break;
        }
    }
    
    public String desc() {
        String desc = "Height = "+height+"/ DEX penalty = "+penDex+
                "/ SPD penalty = "+penSpd+"/ Occlusion = "+occ+"\n";
        return desc;
    }
}