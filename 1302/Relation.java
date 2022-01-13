import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

//mport javax.management.NotCompliantMBeanException;
public class Relation {
  // Name of the relation.
  private String name;

  // Attribute names for the relation
  private ArrayList<String> attributes;

  // Domain classes or types of attributes; possible values: INTEGER, DECIMAL, VARCHAR
  private ArrayList<String> domains;

  // Actual data storage (list of tuples) for the relation.
  public ArrayList<Tuple> table;
  

  // METHODS

  // Constructor; set instance variables
  public Relation (String name, ArrayList<String> attributes, ArrayList<String> dNames) {
      
      this.name = name; 
      this.attributes = attributes; 
      this.domains = dNames; 
      table = new ArrayList<Tuple>(); 

  }

  public ArrayList<String> getAttributes () {
    return this.attributes; 
  }

  public ArrayList<String> getDomains () {
    return this.domains; 
  }

  // returns true if attribute with name aname exists in relation schema
  // false otherwise
  public boolean attributeExists(String aname) {

    return attributes.contains(aname); // returns true if attributes contains aname

  }

  // returns domain type of attribute aname; return null if not present
  public String attributeType(String aname) {

    if (attributes.contains(aname)) {
        int index = attributes.indexOf(aname); // find the index of 'aname' which is corresponds to the row number of the tuple 
        return domains.get(index); // return the String in domains at the corresponding index of 'aname's postion in attributes
    } else {
        return null; 
    }
  }

  // Print relational schema to screen.
  public void displaySchema() {

    //this method should specify the set of attributes and data type for each attribute
    //using this format: R(A1 : D1, A2 : D2, . . . , An : Dn)

    String temp = ""; 
    for (int i = 0; i < this.attributes.size(); i ++) {
        temp += (this.attributes.get(i) + ":" + this.domains.get(i));
        if (i < this.attributes.size() - 1) {
            temp += (",");
        }
    }
    System.out.println(name + "(" + temp + ")"); // print the name of the relational schema   
  }

  // Set name of relation to rname
  public void setName(String rname) {
      name = rname; 

  }

  // Add tuple tup to relation; Duplicates are fine.
  public void addTuple(Tuple tup) {
      //table = new ArrayList<>(); 
      table.add(tup); // add the tuple to table

  }

  // Print relation to screen (see output of run for formatting)
  public void displayRelation() {

    displaySchema();

    System.out.println("Number of tuples: " + table.size());
    
    
    for(int i = 0 ; i < table.size() ; ++i)
    {
        System.out.println(table.get(i).toString());
    }

    System.out.println();

  }

  // Return String version of relation; See output of run for format.
  public String toString() {

    // create the displaySchema () method as a string 
    String temp = this.name + "(";
    
    for (int i = 0; i < this.attributes.size(); i ++) {
        temp += (this.attributes.get(i) + ":" + this.domains.get(i)); 
        if (i < this.attributes.size() -1) {
            temp += ","; 
        }
    }

    temp += ")" + "\nNumber of tuples: " + table.size() + "\n"; 

    for (int j = 0; j < table.size(); j ++) {
        temp += table.get(j) + "\n"; 
    }
    temp += "\n"; 
    return temp; 

  }

// WEEK 3 --------

// Remove duplicate tuples from this relation
public void removeDuplicates() {

    for (int i =0; i < table.size() - 1; i++) {
      for (int j = i + 1; j < table.size(); j++) {
        if (table.get(i).equals(table.get(j))) {
          table.remove(j); 
          --j; 
        }
      }
    } 

    /** Set<Tuple> temp = new LinkedHashSet<>();
    temp.addAll(this.table); 
    this.table.clear();
    this.table.addAll(temp); */

    /**ArrayList<Tuple> temp = new ArrayList<Tuple>();
    for (Tuple t : this.table) {
      if (!temp.contains(t)) {
        temp.add(t); 
      }
    }
    this.table.clear();
    this.table.addAll(temp);  */
    
}

    
  
// -- WEEK 4 

// This method returns true if tuple t is present in relation and false otherwise.
public boolean member(Tuple t) {
    for (int i =0; i < this.table.size(); i++ ) {
      Tuple s = this.table.get(i); 
      if (s.equals(t))
        return true; 
    }
    return false; 
  }

  //This method returns the union of two relations (this and r2); 
  //It should remove duplicates before returning. 
  //Clone the tuples from the input relations and then add to output.

  public Relation union(Relation r2) {
    // A OR B or for this code: this UNION r2 
    // r2 is passed in 

    //clone r2
    //create a new Relation object
    Relation uRelation = new Relation(this.name, this.attributes, this.domains);
     
    //for each tuple in this RElation
    for (Tuple t : this.table) {
      //clone the tuple and add it to the new Relation
      uRelation.addTuple(t.clone(this.attributes));
    }  
    //for each tuple in r2
      for (Tuple t : r2.table) {
        //clone the tuple and add it to the new Relation
        uRelation.addTuple(t.clone(r2.attributes));
      }
      //remove duplicate Tuples
      uRelation.removeDuplicates();
      return uRelation; 
  }

  //This method returns the intersection of two relations (this and r2); 
  //Clone the tuples from the input relations and then add to output.
  public Relation intersect (Relation r2) {
    // A AND B 
    // only add tuple if it is a member of both A and B's table
    //Create a new Relation object iRelation
    Relation iRelation = new Relation(name, attributes, domains);
    //for each tuple in r2 
      for (Tuple t : r2.table){
        // if t is a member of this
        if(this.member(t)) {
          //add t.clone to iRelation
          iRelation.addTuple(t.clone(r2.attributes));
        }
      }
      //for each Tuple tup in iRelation
      for (Tuple tup : iRelation.table){
        //if tup is !member of r2
        if (r2.member(tup) == false) {
          //remove tup from iRelation
          iRelation.table.remove(tup); 
        }
      }
      //return iRelation
      return iRelation; 
  }
  //This method returns the difference of two relations (this and r2); Clone the tuples from the input relations and then add to output.
  public Relation minus(Relation r2) {
    //return a Relation containing all the Tuples that are not common between this and r2 
    //create a new Relation mRelation
    ArrayList <String> attr = new ArrayList<String>();
    ArrayList <String> dom = new ArrayList<String>();

    attr.addAll(this.attributes); 
    dom.addAll(this.domains); 

    Relation mRelation = new Relation(null, attr, dom); 
    // for each Tuple t in r2 
    for (Tuple t : this.table) {
      //if t is a !member of this.table
      if (!r2.member(t)) {
        //add t to mRelation
        mRelation.addTuple(t.clone(attr));
      } 
    }
    return mRelation;
  }
  //-- WEEK 5 
  // The rename method takes as parameter an array list of new column names, cnames, 
  // and returns a new relation object that contains the same set of tuples, but with
  // new columns names. We can assume that the size of cnames is same as size of this.attributes
  
  public Relation rename (ArrayList<String> cnames) {
    //create new empty attr and dom 
    ArrayList <String> attr = new ArrayList<String>();
    ArrayList <String> dom = new ArrayList<String>(); 

    //copy all the cnames to attr and this.dom to dom
    for (String n : cnames) {
      attr.add(n); 
    }
    for (String d : this.domains) {
      dom.add(d); 
    }
    //Construct new Relation object, rel.
    Relation rel = new Relation(name, attr, dom); 
    //for each tuple in this.table:
    for (Tuple t : this.table) {
      //clone the tuple and add to rel.table;
      rel.addTuple(t.clone(attr));
    }
    //return rel;
    return rel; 
  }

// The times method returns the cartesian product of two relations.
// As an example, let R and S be the following two relations:
// R(A:VARCHAR, B:INTEGER, C:INTEGER) and S(B:INTEGER, C:INTEGER, D:DECIMAL)
// and let R contain the tuples {<jones",20,200>, <smith",30,300> and
// let S contian the tuples {<1,2,2.5>, <100,200,3.86>}
// The R times S would have the schema 
// R_TIMES_S(A:VARCHAR, R.B:INTEGER, R.C:INTEGER, S.B:INTEGER, S.C:INTEGER, D:DECIMAL)
// and the tuples: {<jones",20,200,1,2,2.5>, <jones",20,200,100,200,3.86>,
//                  <smith",30,300,1,2,2.5>, <smith",30,300,100,200,3.86>}
// Notice the tuples in the output are formed by combining tuples in the
// input relations in all possible ways, maintaining the order of columns
public Relation times(Relation r2) { 
  //Construct new empty attrs and doms array lists;
  ArrayList<String> attrs = this.attributes;
  ArrayList<String> doms = this.domains;
  
    for (int i =0; i < r2.attributes.size(); i ++) {
      //Copy all column names from r2.attributes to attrs, renaming any attribute that
      //also appears in this.attributes; copy corresponding r2.domains value to doms;
      if (this.attributes.contains(r2.attributes.get(i))) {
        // rename r2 attribute to the format r2.name + A : DOMAIN 
        attrs.add(r2.name + "." + r2.attributes.get(i));
        // rename this attribute that is shared with r2 to the format this.name + A : DOMAIN
        attrs.set(attrs.indexOf(r2.attributes.get(i)), this.name + "." + r2.attributes.get(i));

      } else {
        attrs.add(r2.attributes.get(i)); 
      } 
      //add the domains
      doms.add(r2.domains.get(i)); 
    }
  //Construct new Relation object, rel; 
  Relation rel = new Relation(name, attrs, doms); 
  //Using nested for-loops, get tuple t1 from this.table and t2 from r2.table and
    for (Tuple t1 : this.table) {
      for (Tuple t2 : r2.table) {
        rel.table.add(t1.concatenate(t2, attrs, doms)); 
      }
    }
  //return rel;
  return rel; 
  }
// Week 6 ------
// This methods takes as input an array list of column names, each of which
// belonging to this.attributes, and returns a relation whose tuples are
// formed by projecting the columns from cnames.
// Example: R(A:INTEGER, B:INTEGER, C:DECIMAL) with tuples
// {(10,20,3.5),(11,22,7.8),(10,25,3.5)}
// Then, with cnames = {A,C}, the output relation should
// have schema (A:INTEGER, C:DECIMAL) and tuples
// {(10,3.5),(11,7.8)}
// Note that after projection one may get duplicate tuples, which should
// be deleted.
public Relation project(ArrayList<String> cnames) {
  //Create new attrs and doms from cnames and this.domains;
  ArrayList<String> attr = new ArrayList<String>();
  ArrayList<String> dom = new ArrayList<String>();
  
  attr.addAll(cnames); 

  for (String s : attr) {
    int index = this.attributes.indexOf(s);
    dom.add(this.domains.get(index)); 
  }
  //Create a new Relation object, rel;
  Relation rel = new Relation(this.name, attr, dom); 
  //for each tuple in this.table:
  for (Tuple t : this.table) {
    //call the project() method of Tuple class and get a new tuple;
    //add the new tuple to this.table;
    rel.table.add(t.project(attr)); 
  }
  //Remove duplicates from rel;
  rel.removeDuplicates();
  //return rel;
  return rel; 
  }
  // WEEK 7 
  // This method takes as input a comparison condition as explained earlier and returns
  // a new relation that contains only those tuples that satisfies the comparison condition.
  public Relation select (String lopType, String lopValue, String comparison,
  String ropType, String ropValue) {
    Relation r = new Relation(this.name, this.attributes, this.domains);
    for (int i =0; i < this.table.size(); i++) {
      if (this.table.get(i).select(lopType, lopValue, comparison, ropType, ropValue)) {
        r.table.add(this.table.get(i)); 
      }
    }
    return r;  
  }

  // WEEK 8 

  // The join operator combines two relations into one based on common columns in the two relations
  // The schema of the join relation contains all columns of the first relation followed by all columns
  // of the second relation, somewhat like the times operator, except that the common columns appear only
  // once in the join relation
  // Two tuples join with each other only if they have the same values under the common columns.
  public Relation join(Relation r2) {
  
    ArrayList<String> attributes = new ArrayList<String>();
    ArrayList<String> domains = new ArrayList<String>();

    attributes.addAll(this.attributes); 
    domains.addAll(this.domains); 

    //populate new AL's for return Relation with all col names and respective domain types
    for (String s : r2.attributes) {
      int index = attributes.indexOf(s);
      if (index == -1) {
        attributes.add(s);
        domains.add(r2.domains.get(r2.attributes.indexOf(s)));
      }
    }

    // Now create a new relation that is populated with our AL's 
    Relation rel = new Relation(this.name, attributes, domains); 

    for (Tuple t1: this.table) {
      for (Tuple t2: r2.table) {
        Tuple temp = t1.join(t2, attributes, domains); 
        if (temp != null) {
          rel.addTuple(temp);
        }
      }
    }
    return rel;
  }
}
