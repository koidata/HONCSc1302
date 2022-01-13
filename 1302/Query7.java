import java.util.ArrayList;
import java.util.Arrays;

public class Query7 {
    public static void main(String[] args) {
    /**
    Query 7: List the names of managers who have at least one dependent.

    project[lname,fname](
    ((rename[ssn](project[mgrssn](department))
        join
        rename[ssn](project[essn](dependent))
    )
    join
    employee
    )
    );
    */

    Database db = new Database(); 
    db.initializeDatabase(args[0]);

    Relation department = db.getRelation("DEPARTMENT");
    Relation employee = db.getRelation("EMPLOYEE"); 
    Relation dependent = db.getRelation("DEPENDENT"); 

    Relation r1 = department.project(new ArrayList<String>(Arrays.asList("MGRSSN"))).rename(new ArrayList<String>(Arrays.asList("SSN"))); 
    Relation r2 = dependent.project(new ArrayList<String>(Arrays.asList("ESSN"))).rename(new ArrayList<String>(Arrays.asList("SSN"))); 
    Relation r3 = r1.join(r2).join(employee); 
    Relation r4 = r3.project(new ArrayList<String>(Arrays.asList("LNAME","FNAME"))); 
    r4.setName("ANSWER");
    System.out.println(r4);
    }
}
