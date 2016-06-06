package combattest;

public class UnitMap {
    int y, x;
    public Unit uMap[][]; //active units map
    int unitCount;
    
    public UnitMap (int width, int height) {
        x = width;
        y = height;
        uMap = new Unit[x][y];
        unitCount = 0;
    }
    
    //creates unit
    public void newUnit (String name, String type, int lvl, int faction,
            int x, int y) {
        uMap[x][y] = new Unit(name, type, lvl, faction, unitCount);
        unitCount++;
    }
    
    //destroys unit
    public void killUnit (int x, int y) {
        uMap[x][y] = null;
    }
    
    //access to unit by position
    public Unit unit (int x, int y) {
        if (uMap[x][y] != null) {
            return uMap[x][y];
        } else {
            Unit dummy = new Unit("dummy","",1,0,999);
            return dummy;
        }
    }
    
    //moves unit
    public void moveUnit (int xi, int yi, int xf, int yf) {
        if (uMap[xi][yi] == null){
            System.out.println("Attempted to move unit from empty cell");
        } else if (uMap[xf][yf] != null) {
            System.out.println("Attempted to move unit to occupied space");
        } else {
            uMap[xf][yf] = uMap[xi][yi];
            uMap [xi][yi] = null;
        }
    }
    
    //clears 0hp characters
    public void undertaker() {
        int rip = 0;
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (uMap[j][i] != null) {
                    if (uMap[j][i].isDead()){
                        killUnit(j,i);
                        System.out.println("Unit located in ("+j+", "+i+") was"+
                                " dead and cleared.");
                        rip++;
                    }
                }
            }
        }
        System.out.println(rip+" dead characters were removed.");
        System.out.println();
    }
    
    //reports all current units
    public void mapReport() { 
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (uMap[j][i] != null) {
                    System.out.print("Cell ("+j+", "+i+") contains:"+"\n");
                    uMap[j][i].liteReport();
                }
            }
        }
        System.out.println();
    }
    
    //full unit report by position
    public void unitReport(int x, int y) {
        if (uMap[x][y] == null) {
            System.out.println("Empty cell selected for unit report");
        } else {
            System.out.print("Cell ("+x+", "+y+") contains:"+"\n");
            uMap[x][y].fullReport();
        }
    }
   
    //prints unit map
    public void showMap() {
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (uMap[j][i] == null) {
                    System.out.print("[ ]");
                } else{
                    System.out.print("["+uMap[j][i].faction+"]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    //prints unit map, except for invisible units other than player faction's
    public void showMap(int player) {
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (uMap[j][i] == null || (unit(j, i).invisible &&
                        isHostile (player, j, i))) {
                    System.out.print("[ ]");
                } else {
                    System.out.print("["+uMap[j][i].faction+"]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    //checks whether a unit is hostile towards another
    //format is (unit 1 coord, unit 2 coord)
    public boolean isHostile (int ax, int ay, int bx, int by) {
        boolean hostile = true;
        Unit a, b;
        a = unit(ax, ay);
        b = unit(bx, by);
        if (a.faction == b.faction) {
            hostile = false;
        } else if (a.faction == 1 || a.faction == 11) {
            if (b.faction == 1 || b.faction == 11) {
                hostile = false;
            }
        }
        if (a.faction == 0 || b.faction == 0) {
            hostile = false;
        }
        return hostile;
    }
    
    //checks whether a unit is hostile towards another
    //format is (unit 1 faction, unit 2 coord)
    public boolean isHostile (int aFaction, int bx, int by) {
        boolean hostile = true;
        Unit b;
        b = unit(bx, by);
        if (aFaction == b.faction) {
            hostile = false;
        } else if (aFaction == 1 || aFaction == 11) {
            if (b.faction == 1 || b.faction == 11) {
                hostile = false;
            }
        }
        if (aFaction == 0 || b.faction == 0) {
            hostile = false;
        }
        return hostile;
    }
    
    //checks whether a unit is friendly towards another
    //format is (unit 1 coord, unit 2 coord)
    public boolean isFriendly (int ax, int ay, int bx, int by) {
        boolean friendly = false;
        Unit a, b;
        a = unit(ax, ay);
        b = unit(bx, by);
        if (a.faction == b.faction) {
            friendly = true;
        } else if (a.faction == 1 || a.faction == 11) {
            if (b.faction == 1 || b.faction == 11) {
                friendly = true;
            }
        }
        if (a.faction == 0 || b.faction == 0) {
            friendly = false;
        }
        return friendly;
    }
    
    //checks whether a unit is friendly towards another
    //format is (unit 1 aFaction, unit 2 coord)
    public boolean isFriendly (int aFaction, int bx, int by) {
        boolean friendly = false;
        Unit b;
        b = unit(bx, by);
        if (aFaction == b.faction) {
            friendly = true;
        } else if (aFaction == 1 || aFaction == 11) {
            if (b.faction == 1 || b.faction == 11) {
                friendly = true;
            }
        }
        if (aFaction == 0 || b.faction == 0) {
            friendly = false;
        }
        return friendly;
    }
    
    //checks for flanked status for attacked unit
    //format is (attacker coord, defender coord)
    public boolean isFlanked (int ax, int ay, int dx, int dy) {
        Unit a, d;
        int xOff, yOff;
        boolean f = false;
        a = unit(ax, ay);
        d = unit(dx, dy);
        if (isHostile (ax, dx, dx, dy)){
            xOff = dx - ax;
            yOff = dy - ay;
            if (inBounds (dx+xOff, dy+yOff)) {
                if (isHostile (dx, dy, dx+xOff, dy+yOff) 
                        && unit(dx+xOff, dy+yOff).faction != 10) {
                    f = true;
                    System.out.println (d.moniker+" is flanked.");
                    return f;
                }
            }
            if (xOff == 0) {
                if (inBounds (dx+1, dy+yOff)) {
                    if (isHostile (dx, dy, dx+1, dy+yOff) 
                            && unit(dx+1, dy+yOff).faction != 10) {
                        f = true;
                        System.out.println (d.moniker+" is flanked.");
                        return f;
                    }
                }
                if (inBounds (dx-1, dy+yOff)) {
                    if (isHostile (dx, dy, dx-1, dy+yOff) 
                            && unit(dx-1, dy+yOff).faction != 10) {
                        f = true;
                        System.out.println (d.moniker+" is flanked.");
                        return f;
                    }
                }
            } else if (yOff == 0) {
                if (inBounds (dx+xOff, dy+1)) {
                    if (isHostile (dx, dy, dx+xOff, dy+1)  
                            && unit(dx+xOff, dy+1).faction != 10) {
                        f = true;
                        System.out.println (d.moniker+" is flanked.");
                        return f;
                    }
                }
                if (inBounds (dx+xOff, dy-1)) {
                    if (isHostile (dx, dy, dx+xOff, dy-1)
                            && unit(dx+xOff, dy-1).faction != 10) {
                        f = true;
                        System.out.println (d.moniker+" is flanked.");
                        return f;
                    }
                }
            } else {
                if (inBounds (dx+xOff, dy)) {
                    if (isHostile (dx, dy, dx+xOff, dy) 
                            && unit(dx+xOff, dy).faction != 10) {
                        f = true;
                        System.out.println (d.moniker+" is flanked.");
                        return f;
                    }
                }
                if (inBounds (dx, dy+yOff)) {
                    if (isHostile (dx, dy, dx, dy+yOff) 
                            && unit(dx, dy+yOff).faction != 10) {
                        f = true;
                        System.out.println (d.moniker+" is flanked.");
                        return f;
                    }
                }
            }
        }
        return f;
    }
    
    //checks outnumber balance for unit in coord (x, y)
    //eg.: if there are 3 enemies and 2 allies in adjacent cells, this returns 0
    public int outnumber (int x, int y) {
        int outnumber = -1;
        if (inBounds (x-1, y-1)) {
            if (isFriendly(x, y, x-1, y-1)) {
                outnumber--;
            } else if (isHostile(x, y, x-1, y-1)) {
                outnumber++;
            }
        }
        if (inBounds (x, y-1)) {
            if (isFriendly(x, y, x, y-1)) {
                outnumber--;
            } else if (isHostile(x, y, x, y-1)) {
                outnumber++;
            }
        }
        if (inBounds (x+1, y-1)) {
            if (isFriendly(x, y, x+1, y-1)) {
                outnumber--;
            } else if (isHostile(x, y, x+1, y-1)) {
                outnumber++;
            }
        }
        if (inBounds (x-1, y)) {
            if (isFriendly(x, y, x-1, y)) {
                outnumber--;
            } else if (isHostile(x, y, x-1, y)) {
                outnumber++;
            }
        }
        if (inBounds (x+1, y)) {
            if (isFriendly(x, y, x+1, y)) {
                outnumber--;
            } else if (isHostile(x, y, x+1, y)) {
                outnumber++;
            }
        }
        if (inBounds (x-1, y+1)) {
            if (isFriendly(x, y, x-1, y+1)) {
                outnumber--;
            } else if (isHostile(x, y, x-1, y+1)) {
                outnumber++;
            }
        }
        if (inBounds (x, y+1)) {
            if (isFriendly(x, y, x, y+1)) {
                outnumber--;
            } else if (isHostile(x, y, x, y+1)) {
                outnumber++;
            }
        }
        if (inBounds (x+1, y+1)) {
            if (isFriendly(x, y, x+1, y+1)) {
                outnumber--;
            } else if (isHostile(x, y, x+1, y+1)) {
                outnumber++;
            }
        }
        return outnumber;
    }
    
    //prevents out of bounds exception
    public boolean inBounds (int x, int y) {
        boolean ib = false;
            if (x >=0 && x < this.x && y >=0 && y < this.y) {
                ib = true;
            }
        return ib;
    }
}