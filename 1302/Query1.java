import java.util.*;

public class Query1 {
  public static void main(String args[]) {
    Database db = new Database();
    db.initializeDatabase(args[0]);

    // Retrieve the name and address of employees who work for the "Research" department.
    //   project[fname,lname,address](
    //       (rename[dname,dno,mgrssn,mgrstartdate](
    //           select[dname='Research'](department)) 
    //        join 
    //        employee
    //       )
    // );
    Relation department = db.getRelation("DEPARTMENT");
    Relation employee = db.getRelation("EMPLOYEE");
    
    //Relation r1 = employee.join(department.select("col","DNAME","=","str","Research")
    //                      .rename(new ArrayList<String>(Arrays.asList("DNAME","DNO","MGRSSN","MGRSTARTDATE"))))
    //                      .project(new ArrayList<String>(Arrays.asList("FNAME","LNAME","ADDRESS")));
    Relation r1 = department.select("col","DNAME","=","str","Research");
    System.out.println("TEST: " + r1);
    ArrayList<String> cols = new ArrayList<String>();
    cols.add("DNAME");
    cols.add("DNO");
    cols.add("MGRSSN");
    cols.add("MGRSTARTDATE");
    Relation r2 = r1.rename(cols);
    Relation r3 = r2.join(employee);
    cols.clear();
    cols.add("FNAME");
    cols.add("LNAME");
    cols.add("ADDRESS");
    Relation r4 = r3.project(cols);
    r4.setName("ANSWER");
    System.out.println(r4);
  }
}