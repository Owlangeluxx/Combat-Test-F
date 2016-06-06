package combattest;

public class Map {
    int x, y;
    //int turn;
    //int factionTurn;
    UnitMap uMap;
    HeightMap hMap;
    
    public Map (int x, int y) {
        this.x = x;
        this.y = y;
        uMap = new UnitMap(x,y);
        hMap = new HeightMap(x,y);
    }
    
    //access Height Map
    public HeightMap hMap() {
        return hMap;
    }
    
    //access Unit Map
    public UnitMap uMap() {
        return uMap;
    }

    //returns array with accessible cells by unit in (x, y)
    public int[][] accessible (int ux, int uy) {
        int map[][];
        map = new int[x][y];
        int steps, aux;
        
        steps = (uMap.unit(ux, uy)).steps;
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (hMap.terrain(j,i).accessible == 0) {
                    map[j][i] = -1;
                }
                if ((!"dummy".equals(uMap.unit(j,i).moniker) &&
                        !(uMap.unit(j,i).invisible &&
                        uMap.isHostile(ux, uy, j, i)))) {
                    map[j][i] = -2;
                }
            }
        }
        
        map[ux][uy] = steps + 1;
        
        for (int l = (steps + 1); l > 1; l--) {
            for (int i = 0; i < y; i++){
                for (int j = 0; j < x; j++){
                    if (map[j][i] == l){
                        if (((j-1) >= 0) && ((map[j-1][i]) == 0 )) {
                            if ((Math.abs(hMap.terrain (j-1, i).height - 
                                    hMap.terrain (j, i).height) < 2) &&
                                    hMap.terrain (j-1, i).horizontal == 1 &&
                                    hMap.terrain (j, i).horizontal == 1) {
                                aux = ((l-1)- hMap.terrain(j-1, i).penSpd);
                                map[j-1][i] = Math.max (aux, 1);                                
                            }
                        }
                        if (((j+1) < this.x) && ((map[j+1][i]) == 0 )) {
                            if ((Math.abs(hMap.terrain (j+1, i).height - 
                                    hMap.terrain (j, i).height) < 2) &&
                                    hMap.terrain (j+1, i).horizontal == 1 &&
                                    hMap.terrain (j, i).horizontal == 1) {
                                aux = ((l-1)- hMap.terrain(j+1, i).penSpd);
                                map[j+1][i] = Math.max (aux, 1);
                            }
                        }
                        if (((i-1) >= 0) && ((map[j][i-1]) == 0 )) {
                            if ((Math.abs(hMap.terrain (j, i-1).height - 
                                    hMap.terrain (j, i).height) < 2) &&
                                    hMap.terrain (j, i-1).vertical == 1 &&
                                    hMap.terrain (j, i).vertical == 1) {
                                aux = ((l-1)- hMap.terrain(j, i-1).penSpd);
                                map[j][i-1] = Math.max (aux, 1);
                            }
                        }
                        if (((i+1) < this.y) && ((map[j][i+1]) == 0 )) {
                            if ((Math.abs(hMap.terrain (j, i+1).height - 
                                    hMap.terrain (j, i).height) < 2) &&
                                    hMap.terrain (j, i+1).vertical == 1 &&
                                    hMap.terrain (j, i).vertical == 1) {
                                aux = ((l-1)- hMap.terrain(j, i+1).penSpd);
                                map[j][i+1] = Math.max (aux, 1);
                            }
                        }
                    }
                }
            }            
        }     
    return map;
    }
    
    //prints access array
    public void printAccessible (int ux, int uy) {
        int map[][];
        map = accessible(ux, uy);
        
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (map[j][i] < 0) {
                    System.out.print("[X]");
                } else if  (map [j][i] == 0) {
                    System.out.print("[ ]");
                } else {
                    System.out.print("["+map[j][i]+"]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    //returns an array with enemies in specified range, with occlusion
    //origin coord should point to unit
    //no min range restriction
    public boolean[][] hostileOcc (int ux, int uy, int range) {
        boolean[][] hostileOcc = new boolean[x][y];
        boolean[][] occ = occlusion(ux, uy, range);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if ((occ[j][i]) && uMap.isHostile(ux, uy, j, i)) {
                    hostileOcc[j][i] = true;
                }
            }
        }
        return hostileOcc;
    }
    
    //returns an array with enemies in specified range, with no occlusion
    //origin coord should point to unit
    //no min range restriction
    public boolean[][] hostileFree (int ux, int uy, int range) {
        boolean[][] hostileOcc = new boolean[x][y];
        int[][] inRange = inRange(ux, uy, range);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if ((inRange[j][i] > 0) && uMap.isHostile(ux, uy, j, i)) {
                    hostileOcc[j][i] = true;
                }
            }
        }
        return hostileOcc;
    }
    
    //returns an array with enemies in specified range, with occlusion
    //origin coord should point to unit
    //min range restriction
    public boolean[][] hostileOcc (int ux, int uy, int range, int minRange) {
        boolean[][] hostileOcc = new boolean[x][y];
        boolean[][] occ = occlusion(ux, uy, range);
        boolean[][] minOcc = occlusion(ux, uy, minRange);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (((occ[j][i]) && uMap.isHostile(ux, uy, j, i)) &&
                        (!minOcc[j][i])){
                    hostileOcc[j][i] = true;
                }
            }
        }
        return hostileOcc;
    }
    
    //returns an array with enemies in specified range, with no occlusion
    //origin coord should point to unit
    //min range restriction
    public boolean[][] hostileFree (int ux, int uy, int range, int minRange) {
        boolean[][] hostileOcc = new boolean[x][y];
        int[][] inRange = inRange(ux, uy, range);
        int[][] mininRange = inRange(ux, uy, minRange);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (((inRange[j][i] > 0) && uMap.isHostile(ux, uy, j, i))
                        && (mininRange[j][i] <= 0)){
                    hostileOcc[j][i] = true;
                }
            }
        }
        return hostileOcc;
    }
    
    //returns an array with enemies in specified range, with occlusion
    //origin can point away from unit, must indicate caster's faction
    //no min range restriction
    public boolean[][] hostileOccAway (int ux, int uy, int range, int uFaction) {
        boolean[][] hostileOcc = new boolean[x][y];
        boolean[][] occ = occlusion(ux, uy, range);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if ((occ[j][i]) && uMap.isHostile(uFaction, j, i)) {
                    hostileOcc[j][i] = true;
                }
            }
        }
        return hostileOcc;
    }
    
    //returns an array with enemies in specified range, with no occlusion
    //origin can point away from unit, must indicate caster's faction
    //no min range restriction
    public boolean[][] hostileFreeAway (int ux, int uy, int range, int uFaction) {
        boolean[][] hostileOcc = new boolean[x][y];
        int[][] inRange = inRange(ux, uy, range);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if ((inRange[j][i] > 0) && uMap.isHostile(uFaction, j, i)) {
                    hostileOcc[j][i] = true;
                }
            }
        }
        return hostileOcc;
    }
    
    //returns an array with enemies in specified range, with occlusion
    //origin can point away from unit, must indicate caster's faction
    //min range restriction
    public boolean[][] hostileOccAway (int ux, int uy, int range, int minRange,
            int uFaction) {
        boolean[][] hostileOcc = new boolean[x][y];
        boolean[][] occ = occlusion(ux, uy, range);
        boolean[][] minOcc = occlusion(ux, uy, minRange);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (((occ[j][i]) && uMap.isHostile(uFaction, j, i)) &&
                        (!minOcc[j][i])){
                    hostileOcc[j][i] = true;
                }
            }
        }
        return hostileOcc;
    }
    
    //returns an array with enemies in specified range, with no occlusion
    //origin can point away from unit, must indicate caster's faction
    //min range restriction
    public boolean[][] hostileFreeAway (int ux, int uy, int range, int minRange,
            int uFaction) {
        boolean[][] hostileOcc = new boolean[x][y];
        int[][] inRange = inRange(ux, uy, range);
        int[][] mininRange = inRange(ux, uy, minRange);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (((inRange[j][i] > 0) && uMap.isHostile(uFaction, j, i))
                        && (mininRange[j][i] <= 0)){
                    hostileOcc[j][i] = true;
                }
            }
        }
        return hostileOcc;
    }
    
    //returns an array with allies in specified range, with occlusion
    //origin coord should point to unit
    public boolean[][] friendlyOcc (int ux, int uy, int range) {
        boolean[][] friendlyOcc = new boolean[x][y];
        boolean[][] occ = occlusion(ux, uy, range);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if ((occ[j][i]) && uMap.isFriendly(ux, uy, j, i)) {
                    friendlyOcc[j][i] = true;
                }
            }
        }
        return friendlyOcc;
    }

    //returns an array with allies in specified range, with no occlusion
    //origin coord should point to unit
    public boolean[][] friendlyFree (int ux, int uy, int range) {
        boolean[][] friendlyOcc = new boolean[x][y];
        int[][] inRange = inRange(ux, uy, range);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if ((inRange[j][i] > 0) && uMap.isFriendly(ux, uy, j, i)) {
                    friendlyOcc[j][i] = true;
                }
            }
        }
        return friendlyOcc;
    }
    
    //returns an array with allies in specified range, with occlusion
    //origin can point away from unit
    public boolean[][] friendlyOccAway (int ux, int uy, int range, int uFaction) {
        boolean[][] friendlyOcc = new boolean[x][y];
        boolean[][] occ = occlusion(ux, uy, range);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if ((occ[j][i]) && uMap.isFriendly(uFaction, j, i)) {
                    friendlyOcc[j][i] = true;
                }
            }
        }
        return friendlyOcc;
    }

    //returns an array with allies in specified range, with no occlusion
    //origin coord can point away from unit
    public boolean[][] friendlyFreeAway (int ux, int uy, int range, int uFaction) {
        boolean[][] friendlyOcc = new boolean[x][y];
        int[][] inRange = inRange(ux, uy, range);
     
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if ((inRange[j][i] > 0) && uMap.isFriendly(uFaction, j, i)) {
                    friendlyOcc[j][i] = true;
                }
            }
        }
        return friendlyOcc;
    }
    
    //returns int array with cells in specified range
    public int[][] inRange (int ux, int uy, int range) {
        int map[][];
        map = new int[x][y];
        map[ux][uy] = 1;
        
        if (range == 0) {
            return map;
        }
        
        range = range + 1;
        
        for (int l = 1; l < range; l++) {
            for (int i = 0; i < y; i++){
                for (int j = 0; j < x; j++){
                    if (map[j][i] == l){
                        if (((j-1) >= 0) && ((map[j-1][i]) == 0 )) {
                            map[j-1][i] = (l+1);
                        }
                        if (((j+1) < this.x) && ((map[j+1][i]) == 0 )) {
                            map[j+1][i] = (l+1);
                        }
                        if (((i-1) >= 0) && ((map[j][i-1]) == 0 )) {
                            map[j][i-1] = (l+1);
                        }
                        if (((i+1) < this.y) && ((map[j][i+1]) == 0 )) {
                            map[j][i+1] = (l+1);
                        }
                    }
                }
            }
        }
        
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (map[j][i] == (range - 1)) {
                    if (((j-1) >= 0) && ((map[j-1][i]) == 0 )) {
                        map[j-1][i] = range;
                    }
                    if (((j+1) < this.x) && ((map[j+1][i]) == 0 )) {
                        map[j+1][i] = range;
                    }
                    if (((i-1) >= 0) && ((map[j][i-1]) == 0 )) {
                        map[j][i-1] = range;
                    }
                    if (((i+1) < this.y) && ((map[j][i+1]) == 0 )) {
                        map[j][i+1] = range;
                    }
                    if (((j-1) >= 0) && ((i-1) >= 0) && ((map[j-1][i-1]) == 0 )) {
                        map[j-1][i-1] = range;
                    }
                    if (((j-1) >= 0) && ((i+1) < this.y) && ((map[j-1][i+1]) == 0 )) {
                        map[j-1][i+1] = range;
                    }
                    if (((j+1) < this.x) && ((i-1) >= 0) && ((map[j+1][i-1]) == 0 )) {
                        map[j+1][i-1] = range;
                    }
                    if (((j+1) < this.x) && ((i+1) < this.y) && ((map[j+1][i+1]) == 0 )) {
                        map[j+1][i+1] = range;
                    }
                }
            }
        }
       
        if (range >= 6) {
            int n = range;
            int t = (range - 6)/2;
            System.out.println(t);
            for (int q = 0; q <= t; q++) {
                for (int i = 0; i < y; i++){
                    for (int j = 0; j < x; j++){
                        if (map[j][i] == (n)) {
                            if (((j-1) >= 0) && ((map[j-1][i]) == 0 )) {
                                if (ux - (j-1) < range) {
                                    map[j-1][i] = n+1;
                                }
                            }
                            if (((j+1) < this.x) && ((map[j+1][i]) == 0 )) {
                                if ((j+1) - ux < range) {
                                    map[j+1][i] = n+1;
                                }
                            }
                            if (((i-1) >= 0) && ((map[j][i-1]) == 0 )) {
                                if (uy - (i-1) < range) {
                                    map[j][i-1] = n+1;
                                }
                            }
                            if (((i+1) < this.y) && ((map[j][i+1]) == 0 )) {
                                if ((i+1) - uy < range) {
                                    map[j][i+1] = n+1;
                                }
                            }
                        }
                    }
                }
            n++;
            }
        }

        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (hMap.terrain(j,i).accessible == 0) {
                    map[j][i] = -1;
                }
            }
        }
    return map;
    }
    
    //prints range array
    public void printRange (int ux, int uy, int range) {
        int map[][];
        map = inRange(ux, uy, range);
        
        System.out.println("Range matrix for range "+range+":");
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (map[j][i] < 0) {
                    System.out.print("[X]");
                } else if  (map [j][i] == 1) {
                    System.out.print("[U]");
                } else if  (map [j][i] == 0) {
                    System.out.print("[ ]");
                } else {
                    System.out.print("["+map[j][i]+"]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    //returns an array with cells obscured by occluded terrain for a given
    //coord and range
    public boolean[][] occlusion (int ux, int uy, int range) {
        boolean[][] occ = new boolean[x][y];
        double slope;

        for (int degree = 89; degree > -90;  degree = degree-5) {
            slope = Math.toRadians(degree);
            slope = Math.tan(slope);
            occ = occAux(slope, occ, ux, uy);
        }
        
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if ((hMap().terrain(j, i)).accessible == 0) {
                    occ[j][i] = false;
                }
            }
        }
        
        int[][] rangecheck = inRange (ux, uy, range);
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (rangecheck[j][i] == 0) {
                    occ[j][i] = false;
                }
            }
        }
    return occ;
    }
    
    //auxiliary method to occlusion
    private boolean[][] occAux (double sl0,
            boolean[][] occ, int ux, int uy) {
        boolean col = false, colh = false;
        int h = hMap.terrain(ux, uy).height;
        
        if ((sl0 < 1) && (sl0 > 0)) {
            for (int i = uy; i < y; i++){
                for (int j = ux; j < x; j++){
                    if (j >= ux) {
                        if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j-0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j+0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) <= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) >= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        }
                    }
                }
            }
            h = hMap.terrain(ux, uy).height;
            colh = false;
            col = false;
            for (int i = y-1; i >= 0; i--){
                for (int j = x-1; j >= 0; j--){
                    if (j <= ux) {
                        if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j-0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j+0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) <= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) >= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        }
                    }
                }
            }
        } else if ((sl0 > -1) && (sl0 < 0)) {
            for (int i = y-1; i >= 0; i--){
                for (int j = ux; j < x; j++){
                    if (j >= ux) {
                        if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j-0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j+0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) <= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) >= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        }
                    }
                }
            }
            h = hMap.terrain(ux, uy).height;
            colh = false;
            col = false;
            for (int i = 0; i < y; i++){
                for (int j = x-1; j >= 0; j--){
                    if (j <= ux) {
                        if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j-0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j+0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) <= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) >= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        }
                    }
                }
            }
        } else if (sl0 > 1) {
            for (int j = ux; j < x; j++){
                for (int i = uy; i < y; i++){
                    if (i >= uy) {
                        if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j-0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j+0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) <= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) >= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        }
                    }
                }
            }
            h = hMap.terrain(ux, uy).height;
            colh = false;
            col = false;
            for (int j = x-1; j >= 0; j--){
                for (int i = y-1; i >= 0; i--){
                    if (i <= uy) {
                        if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j-0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j+0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) <= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) >= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        }
                    }
                }
            }
        } else if (sl0 < -1) {
            for (int j = x-1; j >= 0; j--){
                for (int i = 0; i < y; i++){
                    if (i >= uy) {
                        if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j-0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j+0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) <= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) >= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        }
                    }
                }
            }
            h = hMap.terrain(ux, uy).height;
            colh = false;
            col = false;
            for (int j = 0; j < x; j++){
                for (int i = y-1; i >= 0; i--){
                    if (i <= uy) {
                        if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j-0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j+0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) >= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) <= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        } else if (((sl0*((j-0.5)-ux)) <= (i - 0.5)-uy) &&
                                ((sl0*((j+0.5)-ux)) >= (i + 0.5)-uy)) {
                            if ((hMap().terrain(j, i)).los == 0) {
                                col = true;
                            } else if (!col) {
                                occ[j][i] = true;
                                if (hMap.terrain(j, i).height > h) {
                                    h = hMap.terrain(j, i).height;
                                    colh = true;
                                }
                                if (colh && (hMap.terrain(j, i).height < h)) {
                                    col = true;
                                    occ[j][i] = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return occ;
    }
    
    //prints occlusion array
    public void printOcc (int ux, int uy, int range) {
        boolean map[][];
        map = occlusion (ux, uy, range);
        
        System.out.println("Occlusion matrix for range "+range+":");
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (j == ux && i == uy) {
                    System.out.print("[C]");
                } else if (hMap.terrain(j, i).los == 0) {
                    System.out.print("[X]");
                } else if (map[j][i] == false) {
                    System.out.print("[ ]");
                } else if  (map [j][i] == true) {
                    System.out.print("[O]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    //array that displays hostile targets in attack range,
    //excluding stealthed units
    public boolean[][] availableTargets (int ux, int uy) {
        boolean[][] reach;
        boolean minrange = false;
        int range = uMap.unit(ux, uy).attackRange;
        
        if ((uMap.unit(ux, uy).weap.type == 21) ||
                (uMap.unit(ux, uy).weap.type == 22)) {
            minrange = true;
        }
        
        if (minrange) {
            reach = hostileOcc(ux, uy, range, 1);
        } else {
            reach = hostileOcc(ux, uy, range);
        }
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if ((reach[j][i] == true) && (uMap.unit(j, i).invisible)) {
                    reach[j][i] = false;
                }
            }
        }
        return reach;
    }
    
    //prints hostile targets in attack range, excluding stealthed enemies
    public void printTargets (int ux, int uy) {
        boolean[][] availableTargets = availableTargets(ux, uy);
        
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (availableTargets[j][i]) {
                    System.out.print("[A]");
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    //array that displays hostile cells in range after a single move
    //excludes hostile stealthed units
    public boolean[][] hostileReach (int ux, int uy) {
        boolean[][] reach = new boolean[x][y];
        int[][] steps = accessible(ux, uy);
        boolean[][]aux;
        boolean minrange = false;
        int range = uMap.unit(ux, uy).attackRange;
        int faction = uMap.unit(ux, uy).faction;
        
        if ((uMap.unit(ux, uy).weap.type == 21) ||
                (uMap.unit(ux, uy).weap.type == 22)) {
            minrange = true;
        }
        
        if (minrange) {
            for (int i = 0; i < y; i++){
                for (int j = 0; j < x; j++){
                    if (steps[j][i] > 0) {
                        aux = hostileOccAway(j, i, range, 1, faction);
                        for (int l = 0; l < y; l++){
                            for (int m = 0; m < x; m++){
                                if (aux [m][l]) {
                                    reach[m][l] = true;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < y; i++){
                for (int j = 0; j < x; j++){
                    if (steps[j][i] > 0) {
                        aux = hostileOccAway(j, i, range, faction);
                        for (int l = 0; l < y; l++){
                            for (int m = 0; m < x; m++){
                                if (aux [m][l]) {
                                    reach[m][l] = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if ((reach[j][i] == true) && (uMap.unit(j, i).invisible)) {
                    reach[j][i] = false;
                }
            }
        }
        return reach;
    }
    
    //prints range of movements and hostile targets within a turn's reach
    //excludes hostile stealthed units
    public void printSelected (int ux, int uy) {
        char[][] unitSelected = new char[x][y];
        boolean[][] hostileReach = hostileReach(ux, uy);
        int[][] accessible = accessible(ux, uy);
        
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                unitSelected[j][i] = ' ';
            }
        }
        
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (accessible[j][i] > 0){
                    unitSelected[j][i] = 'M';
                }
            }
        }
        
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                if (hostileReach[j][i]){
                    unitSelected[j][i] = 'A';
                }
            }
        }
        
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                System.out.print("["+unitSelected[j][i]+"]");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    //shows uMap
    public void printuMap() {
        uMap.showMap();
    }
    
    //prints unit map, except for invisible units other than player faction's
    public void printuMap(int player) {
        uMap.showMap(player);
    }
    
    //reports live units
    public void unitReport() {
        uMap.mapReport();
    }
    
    //shows hMap
    public void printhMap() {
        hMap.showMap();
    }
    
    //displays the order in which the unit moves to selected, available cell
    //format is unit origin, unit destination
    //when unit lands on stealthed enemy unit, moves back a square and breaks
    //enemy stealth
    public void moveUnit(int ux, int uy, int dx, int dy){
        int[][] access = accessible(ux, uy);
        int d = access[dx][dy], tx = dx, ty = dy;
        boolean att = true;
        String aux = "";
        
        if (access[dx][dy] > 0) {
            if ("dummy".equals(uMap.unit(dx, dy).moniker)) {
                while (att) {
                    if (access[tx-1][ty] > access [tx][ty]) {
                        aux = "/RIGHT" + aux;
                        tx = tx-1;
                    } else if (access[tx+1][ty] > access [tx][ty]) {
                        aux = "/LEFT" + aux;
                        tx = tx+1;
                    } else if (access[tx][ty-1] > access [tx][ty]) {
                        aux = "/DOWN" + aux;
                        ty = ty-1;
                    } else if (access[tx][ty+1] > access [tx][ty]) {
                        aux = "/UP" + aux;
                        ty = ty+1;
                    } else {
                        System.out.println(aux);
                        System.out.println();
                        att = false;
                    }
                }
                uMap.moveUnit(ux, uy, dx, dy);
            } else if (uMap.unit(dx, dy).invisible) {
                String lastmove = null;
                while (att) {
                    if (access[tx-1][ty] > access [tx][ty]) {
                        aux = "/RIGHT" + aux;
                        tx = tx-1;
                        if (lastmove == null) {
                            lastmove = "/RECOIL! LEFT";
                            uMap.unit(dx, dy).invisible = false;
                            dx = tx;
                            dy = ty;
                        }
                    } else if (access[tx+1][ty] > access [tx][ty]) {
                        aux = "/LEFT" + aux;
                        tx = tx+1;
                        if (lastmove == null) {
                            lastmove = "/RECOIL! RIGHT";
                            uMap.unit(dx, dy).invisible = false;
                            dx = tx;
                            dy = ty;
                        }
                    } else if (access[tx][ty-1] > access [tx][ty]) {
                        aux = "/DOWN" + aux;
                        ty = ty-1;
                        if (lastmove == null) {
                            lastmove = "/RECOIL! UP";
                            uMap.unit(dx, dy).invisible = false;
                            dx = tx;
                            dy = ty;
                        }
                    } else if (access[tx][ty+1] > access [tx][ty]) {
                        aux = "/UP" + aux;
                        ty = ty+1;
                        if (lastmove == null) {
                            lastmove = "/RECOIL! DOWN";
                            uMap.unit(dx, dy).invisible = false;
                            dx = tx;
                            dy = ty;
                        }
                    } else {
                        aux = aux + lastmove;
                        System.out.println(aux);
                        System.out.println("An enemy unit was hiding in"
                                + "selected cell! Stealth was broken.");
                        System.out.println();
                        att = false;
                    }
                }
                uMap.moveUnit(ux, uy, dx, dy);
            } else { //PLACEHOLDER - DELETE!
                System.out.println("DEPURAR ERROR - NO SÉ QUÉ HAS HECHO, ACHO");
            }
        } else {
            System.out.println("Unit cannot access selected cell.");
        }
                
    }
    
    //damage calculator
    //type 0 is physical, 1 is fire, 2 is ice, 3 is nat, 4 is holy, 5 is dark,
    //6 is arcane, 7 is true
    public int damageCalc (Unit attacker, Unit defender, int type, int power) {
        int damage = 0;
        switch (type) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            default:
                System.out.println ("Unsupported damage type received");
            break;
        }
        return damage;
    }
    
    //generic physical damage method
    public int damageCalc (Unit attacker, Unit defender) {
        return damageCalc(attacker, defender, 0, 1);
    }
    
    //damage calculator before defenses
    //type 0 is physical, 1 is magic
    public int rawDamage (Unit attacker, int type, int power) {
        int damage = 0;
        switch (type) {
            case 0:
                break;
            case 1:
                break;
            default:
                System.out.println ("Unsupported damage type received");
            break;
        }
        return damage;
    }
    
    //damage mitigation calculator
    //type 0 is physical, 1 is fire, 2 is ice, 3 is nat, 4 is holy, 5 is dark,
    //6 is arcane
    public int damageMitigated (int raw, Unit defender, int type) {
        int mitigated  = 0;
        switch (type) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            default:
                System.out.println ("Unsupported damage type received");
            break;
        }
        return mitigated;
    }
}