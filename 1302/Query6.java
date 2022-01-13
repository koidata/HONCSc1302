import java.util.*;

public class Query6 {
  public static void main(String args[]) {
    Database db = new Database();
    db.initializeDatabase(args[0]);
    // Retrieve the names of employees who have no dependents.
    //
    // project[lname,fname](
    //  ( ( project[ssn](employee) 
    //       minus project[essn](dependent)
    //    ) 
    //    join 
    //    employee
    //  )
    // );
    
    Relation employee = db.getRelation("EMPLOYEE");
    Relation dependent = db.getRelation("DEPENDENT");
    ArrayList<String> cols = new ArrayList<String>();
    cols.add("SSN");
    Relation r1 = employee.project(cols);
    cols.clear();
    cols.add("ESSN");
    Relation r2 = dependent.project(cols);
    Relation r3 = r1.minus(r2);
    System.out.println(r3.table.size()); 
    Relation r4 = r3.join(employee);
    //System.out.println(r4.table.size());
    cols.clear();
    cols.add("LNAME"); 
    cols.add("FNAME"); 
    Relation r5 = r4.project(cols);
    r5.setName("ANSWER");
    System.out.println(r5);  
  }
}