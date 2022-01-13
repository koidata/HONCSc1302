public class Driver3 {
    public static void main(String[] args) {
        Database db = new Database();
        db.initializeDatabase(args[0]);
        db.displaySchema();
        Relation r1 = db.getRelation("MALE");
        System.out.println(r1);
        Relation r2 = db.getRelation("MARRIAGES");
        System.out.println(r2);
        
    }
    
}
