package combattest;

public class SpellBook {
    int maxElem = 5;
    Spell[] spellBook;
    int length;
    
    public SpellBook(){
        spellBook = new Spell [maxElem];
        length = 0;   
    }
    
    //adds spell by id if it's not already there
    public void addSpell (int id){
        if(!contains(id)){
            if (length == maxElem) {
                Spell[] newSpellBook = new Spell [maxElem*2];
                for (int i = 0; i < maxElem; i++)
                    newSpellBook[i]= spellBook[i];
                maxElem = maxElem *2;
                spellBook = newSpellBook;
                System.gc();
            }
            spellBook[length] = new Spell(id);
            length++;
        } else {
            System.out.println("There is already a spell with that ID.");
            System.out.println();
        }
        reorder();
    }
    
    //removes spell by id if present
    public void removeSpell (int id) {
        if (contains(id)) {
            for(int a = 0; a < length; a++) {
                if(spellBook[a].ID==id) {
                    spellBook[a]=null;
                    length--;
                }
            }
        } else {
            System.out.println("There is no spell with that ID.");
            System.out.println();
        }
        reorder();
    }
    
    //reports all available spells in the book
    public void fullreport (){
        System.out.println("Full SpellBook report:");
        for (int i = 0; i < length; i++){
            spellBook[i].report();
        }
    }
    
    //returns spell by id
    public Spell getSpell (int id){
        for(int i = 0; i < length; i++){
            if(spellBook[i].ID== id){
                return spellBook[i];
            }
        }
        return null;
    }
    
    //true if book contains spell by id
    public boolean contains (int id){
        for(int i = 0; i < length; i++){
            if(id == spellBook[i].ID){
                return true;
            }
        }
        return false;
    }
    
    //reorders book by spell id
    public void reorder() {
        Spell aux;
        for (int i = 0; i < spellBook.length; i++){
            for(int b = 0; b < (spellBook.length - 1); b++){
                if (spellBook[b+1] != null) {
                    if (spellBook[b] == null || spellBook[b].ID > spellBook[b+1].ID) {
                        aux = spellBook[b];
                        spellBook[b] = spellBook[b+1];
                        spellBook[b+1] = aux;
                    }
                }
            }
        }
    }
    
    //Adds Spells from Spellbook s to summoner
    public void addSpellBook (SpellBook s){
        for (int i = 0; i < s.length ; i++){
            addSpell(s.spellBook[i].ID);
        }
    }
    
    //Removes Spells from SpellBook s from summoner
    public void removeSpellBook (SpellBook s){
        for (int i = 0; i < s.length ; i++){
            removeSpell(s.spellBook[i].ID);
        }
    }
}