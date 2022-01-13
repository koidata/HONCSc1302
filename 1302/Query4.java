import java.util.ArrayList;
import java.util.Arrays;

public class Query4 {
    public static void main(String[] args) {
        /**Instructions: 
            Query 4: Make a list of project numbers for projects that involve an employee whose 
            last name is "Smith", either as a worker or as a manager of the department that 
            controls the project.

            ( project[pno](
   (rename[essn](project[ssn](select[lname='Smith'](employee))) 
    join 
    works_on
   )
  )
 union
  project[pnumber](
   ( rename[dnum](project[dnumber](select[lname='Smith'](
       (employee 
        join   
        rename[dname,dnumber,ssn,mgrstartdate](department)
       )
       )
       )
     ) 
     join 
     projects
    )
  )
);

         */
        Database db = new Database();
        db.initializeDatabase(args[0]);
        Relation employee = db.getRelation("EMPLOYEE");
        Relation department = db.getRelation("DEPARTMENT");
        Relation workson = db.getRelation("WORKS_ON");
        Relation projects = db.getRelation("PROJECTS");


        Relation r1 = employee.select("col", "LNAME", "=", "str", "Smith").project(new ArrayList<String>(Arrays.asList("SSN"))).rename(new ArrayList<String>(Arrays.asList("ESSN")));
        //System.out.println(r1);
        Relation r2 = department.rename(new ArrayList<String>(Arrays.asList("DNAME", "DNUMBER", "SUPERSSN", "MGRSTARTDATE")));
        //System.out.println(r2);
        Relation r3 = employee.join(r2).select("col", "LNAME", "=", "str", "Smith").project(new ArrayList<String>(Arrays.asList("DNUMBER"))).rename(new ArrayList<String>(Arrays.asList("DNUM")));
        //System.out.println(r3);
        Relation r4 = r3.join(projects);
        Relation r5 = r4.project(new ArrayList<String>(Arrays.asList("PNUMBER")));

        Relation r6 = r1.join(workson).project(new ArrayList<String>(Arrays.asList("PNO")));


        Relation r7 = r6.union(r5);

        //Relation r8 = employee.join(department); 
        //System.out.println(r8); // <--


        r7.setName("ANSWER");
        System.out.println(r7);
        
    } 
}
