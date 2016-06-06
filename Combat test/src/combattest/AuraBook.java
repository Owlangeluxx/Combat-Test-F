package combattest;

public class AuraBook {
    int maxElem = 5;
    Aura[] auraBook;
    int length;
    
    public AuraBook(){
        auraBook = new Aura [maxElem];
        length = 0;   
    }
    
    //adds aura by id if it's not already there
    public void addAura (int id, int turns, int k){
        if(!contains(id)){
            if (length == maxElem) {
                Aura[] newAuraBook = new Aura [maxElem*2];
                for (int i = 0; i < maxElem; i++)
                    newAuraBook[i]= auraBook[i];
                maxElem = maxElem *2;
                auraBook = newAuraBook;
                System.gc();
            }
            auraBook[length] = new Aura(id, turns, k);
            length++;
        } else {
            System.out.println("There is already a spell with that ID.");
            System.out.println();
        }
        reorder();
    }
    
    //removes aura by id if present
    public void removeAura (int id) {
        if (contains(id)) {
            for(int a = 0; a < length; a++) {
                if(auraBook[a].ID==id) {
                    auraBook[a]=null;
                    length--;
                }
            }
        } else {
            System.out.println("There is no aura with that ID.");
            System.out.println();
        }
        reorder();
    }
    
    //reports all available aura in the book
    public void fullreport (){
        System.out.println("Full present auras report:");
        for (int i = 0; i < length; i++){
            auraBook[i].report();
        }
    }
    
    //returns aura by id
    public Aura getAura (int id){
        for(int i = 0; i < length; i++){
            if(auraBook[i].ID== id){
                return auraBook[i];
            }
        }
        return null;
    }
    
    //true if book contains aura by id
    public boolean contains (int id){
        for(int i = 0; i < length; i++){
            if(id == auraBook[i].ID){
                return true;
            }
        }
        return false;
    }
    
    //reorders book by aura id
    public void reorder() {
        Aura aux;
        for (int i = 0; i < auraBook.length; i++){
            for(int b = 0; b < (auraBook.length - 1); b++){
                if (auraBook[b+1] != null) {
                    if (auraBook[b] == null || auraBook[b].ID > auraBook[b+1].ID) {
                        aux = auraBook[b];
                        auraBook[b] = auraBook[b+1];
                        auraBook[b+1] = aux;
                    }
                }
            }
        }
    }
    
    //reduces aura by one
    public void proc (int id) {
        if (contains(id)) {
            getAura(id).advance();
            clearZero();
            reorder();
        } else {
            System.out.println("Attempted to lower activation count on "
                    + "nonexistant aura.");
        }
    }
    
    //clears 0 activation auras
    public void clearZero() {
        for (int i = 0; i < length; i++) {
            if (auraBook[i].n == 0) {
                auraBook[i] = null;
            }
        }
    }
    
    //Adds Auras from AuraBook s to summoner
    public void addAuraBook (AuraBook s){
        for (int i = 0; i < s.length ; i++){
            addAura(s.auraBook[i].ID, s.auraBook[i].n, s.auraBook[i].k);
        }
    }
    
    //Removes Auras from AuraBook s from summoner
    public void removeAuraBook (AuraBook s){
        for (int i = 0; i < s.length ; i++){
            removeAura(s.auraBook[i].ID);
        }
    }
}