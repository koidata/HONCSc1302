import java.io.*;
import java.util.*;


public class Database {
    

  private Map<String,Relation> relations;

  // METHODS

  // Constructor; creates the empty HashMap object
  public Database() {
    relations = new HashMap<>(); 
  }
  // Add relation r with name rname to HashMap **
  // if relation does not already exists. **
  // Make sure to set the name within r to rname. => ** go into Relation.java and fill out the setName method
  // return true on successful add; false otherwise ** 
  public boolean addRelation(String rname, Relation r) {
    r.setName(rname);
    relations.putIfAbsent(rname, r); // will add the relation if not present 
    return relations.containsValue(r); //returns true if added and false if not 
  }

  // Delete relation with name rname from HashMap **
  // if relation exists. return true on successful delete; false otherwise ** 
  public boolean deleteRelation(String rname) {

    if (relations.containsKey(rname)) { //check to see if the hashmap contains the key first 
        relations.remove(rname); //remove the key (and its value) if its present 
        return true; // returns true only when the if condition is true 
    }

    return false; //return false if the above condition is not executed 

  }

  // Return true if relation with name rname exists in HashMap
  // false otherwise
  public boolean relationExists(String rname) {

    return relations.containsKey(rname); // returns true if they key is present, false if not

  }

  // Retrieve and return relation with name rname from HashMap;
  // return null if it does not exist.
  public Relation getRelation (String rname) { 

    return relations.getOrDefault (rname, null); //will get and return the key 'rname' and its value. Will return null if the key doesnt exist. 

  }

  // Print database schema to screen.
  public void displaySchema() {

    // first display all the schemas in the form of R(A1 : D1, A2 : D2, . . . , An : Dn)
       
    for (Relation temp : relations.values()) {
           temp.displaySchema();
       }

    System.out.println("");
       
    for (Relation temp : relations.values()) {
        temp.toString(); 
    }

    //Create the database object by reading data from several files in directory dir
    
   
       
  }
  // WEEK 3 ------
  public void initializeDatabase(String dir) {

    FileInputStream fin1 = null; // takes an input of file 
    BufferedReader infile1 = null; // reads the input file 
    FileInputStream fin2 = null; // takes an input of file 
    BufferedReader infile2 = null; // reads the input file 
    
      try {
      
      // creates the BufferedReader
      fin1 = new FileInputStream(dir + "/catalog.dat"); // accepts the file of name catalog.dat
      infile1 = new BufferedReader(new InputStreamReader(fin1)); // reads the file

      //get the number of relations
      int numOfRelations = Integer.parseInt(infile1.readLine());

      //loop for that length 
      for (int i = 0; i < numOfRelations; i++) {
        String rname;
        //String line; 
        int numOfCol;
        int numOfRows;
        // get the name
        rname = infile1.readLine();
        //System.out.println(name);  
        // get the number of coloumns
        numOfCol = Integer.parseInt(infile1.readLine()); 
        //System.out.println(numOfCol); 

        ArrayList<String> attributes = new ArrayList<String>();
        ArrayList<String> dNames = new ArrayList<String>();

        for (int y =0; y < numOfCol; y++) {
          String cname = infile1.readLine(); 
          String cdname = infile1.readLine(); 
          attributes.add(cname); 
          dNames.add(cdname); 
        }
        Relation r = new Relation(rname, attributes, dNames); 

        String filename = dir + "/" + rname + ".dat";
        fin2 = new FileInputStream(filename); 
        infile2 = new BufferedReader(new InputStreamReader(fin2)); // reads the file

        // get the number of rows from name.dat 
        numOfRows = Integer.parseInt(infile2.readLine());
        for (int x = 0; x < numOfRows; x++) {
          Tuple t = new Tuple(attributes, dNames); 
          for (int k =0; k < attributes.size(); k ++) {
            String value = infile2.readLine(); 
            if (dNames.get(k).equals("INTEGER")) {
              t.addIntegerComponent(Integer.parseInt(value));
            } else if (dNames.get(k).equals("VARCHAR")) {
              t.addStringComponent(value);
            }else {
              t.addDoubleComponent(Double.parseDouble(value));
            }
          }
          r.addTuple(t);

        }
        infile2.close();
        this.addRelation(rname, r);
         
      }
      infile1.close();

      } catch (IOException e) { // ifs there is an issue with the file it will throw this exception: 
      System.out.println("Error reading file");
      }
    }
}