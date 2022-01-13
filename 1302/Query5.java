import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Query5 {
/**Query 5: List the names of all employees with two or more dependents.

project[lname,fname](
(rename[ssn](
 project[essn1](
  select[essn1=essn2 and dname1<>dname2](
   (rename[essn1,dname1](project[essn,dependent_name](dependent))
    times
    rename[essn2,dname2](project[essn,dependent_name](dependent)))
   )
  )
 )
join
employee)
); */

    public static void main(String[] args) {

        Database db = new Database(); 
        db.initializeDatabase(args[0]);

        Relation employee = db.getRelation("EMPLOYEE"); 
        Relation dependent = db.getRelation("DEPENDENT");
        
        Relation r1 = dependent.project(new ArrayList<String>(Arrays.asList("ESSN","DEPENDENT_NAME"))).rename(new ArrayList<String>(Arrays.asList("ESSN1","DNAME1")));
        Relation r2 = dependent.project(new ArrayList<String>(Arrays.asList("ESSN","DEPENDENT_NAME"))).rename(new ArrayList<String>(Arrays.asList("ESSN2","DNAME2"))); 

        Relation r3 = r1.times(r2);
        Relation r4 = r3.select("col", "DNAME1", "<>", "col", "DNAME2").select("col", "ESSN1", "=", "col", "ESSN2").project(new ArrayList<String>(Arrays.asList("ESSN1"))).rename(new ArrayList<String>(Arrays.asList("SSN")));
        Relation r5 = r4.join(employee).project(new ArrayList<String>(Arrays.asList("LNAME","FNAME"))); 
        r5.setName("ANSWER");
        System.out.println(r5);

    } 
}
