package combattest;

public class HeightMap {
    int y, x;
    public TerrainType hMap[][];
    
    public HeightMap (int width, int height) {
        x = width;
        y = height;
        hMap = new TerrainType[x][y];
        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++){
                hMap[i][j] = new TerrainType("plains0");
            }
        }
    }
    
    //changes cell type to input
    public void editCell(int x, int y, String a){
        hMap[x][y] = new TerrainType(a);
    }
    
    //access terrain
    public TerrainType terrain (int x, int y) {
        return hMap[x][y];
    }
    
    //prints selected cell code
    public void cellPrint(int x, int y){
        System.out.println(hMap[x][y].desc());
    }
    
    //prints coded terrain map
    public void showMap() {
        for (int i = 0; i < y; i++){
            for (int j = 0; j < x; j++){
                System.out.print("["+hMap[j][i].code+"]");
            }
            System.out.println();
        }
        System.out.println();
    }
}