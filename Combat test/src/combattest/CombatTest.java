package combattest;

public class CombatTest {

    public static void main(String[] args) {
        Map map = new Map(20,20);
        map.hMap.editCell(7, 7, "column0");
        map.hMap.editCell(8, 7, "column0");
        map.hMap.editCell(9, 7, "column0");
        map.hMap.editCell(10, 7, "column0");
        map.hMap.editCell(8, 5, "column0");
        map.hMap.editCell(7, 11, "column0");
        map.hMap.editCell(8, 11, "column0");
        map.hMap.editCell(7, 9, "column0");
        map.hMap.editCell(7, 10, "column0");    
        map.hMap.editCell(12, 4, "column0");
        map.hMap.editCell(11, 9, "column0");
        map.hMap.editCell(11, 10, "column0");
        map.hMap.editCell(11, 7, "stairsUp0");
        map.hMap.editCell(11, 6, "battlement0");
        map.hMap.editCell(12, 6, "battlement0");
        map.printhMap();
        map.uMap.newUnit("Jalajar", "testArcher0", 10, 1, 4, 13);
        map.uMap.newUnit("Jalajar", "testAssassin0", 10, 2, 8, 4);
        map.uMap.newUnit("Jalajar", "testAssassin0", 10, 2, 7, 6);
        map.uMap.newUnit("Jalajar", "testAssassin0", 10, 2, 6, 11);
        map.printuMap(1);
        map.uMap.unit(6, 11).invisible = true;
        map.printuMap(1);
        map.moveUnit(4, 13, 6, 11);
        map.printuMap(1);
        SpellBook grimoire = new SpellBook();
        grimoire.addSpell(001);
        grimoire.fullreport();
        grimoire.removeSpell(001);
        grimoire.fullreport();
    }
}