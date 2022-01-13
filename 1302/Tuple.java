import java.util.*;

public class Tuple {

  private ArrayList<String> attributes;
  private ArrayList<String> domains;
  private ArrayList<Comparable> tuple;

  // METHODS
  
  // Constructor; set instance variables
  public Tuple (ArrayList<String> attr, ArrayList<String> dom) {
      this.attributes = attr; 
      this.domains = dom; 
      this.tuple = new ArrayList<Comparable>(); 
  }

  // Add String s at the end of the tuple
  public void addStringComponent(String s) { 
    tuple.add(s); //adds String s to the end via last index in tuple
  }

  // Add Double d at the end of the tuple
  public void addDoubleComponent(Double d) {
    tuple.add(d); //adds Double d to the end via last index in tuple
  }

  // Add Integer i at the end of the tuple
  public void addIntegerComponent(Integer i) {
    tuple.add(i); //adds Integer i to the end via last index in tuple
  }

  // return String representation of tuple; See output of run for format.
  public String toString() {
    String temp = "" ; 
    for (int i = 0; i < tuple.size(); i++) {
        temp += tuple.get(i);
        if (i < tuple.size() -1) {
            temp += ":"; 
        }

    }
    return temp; 
  }

//WEEK 3 -----

  public Iterator<Tuple> iterator() {
    return null;
}

  // Return true if this tuple is equal to compareTuple; false otherwise
  public boolean equals(Tuple compareTuple) {
    for (int i =0; i < this.tuple.size(); i++)
      if ((this.tuple.get(i).compareTo(compareTuple.tuple.get(i)) != 0))
        return false; 
    return true; 
  }

  // --- WEEK 4 

  public Tuple clone (ArrayList<String> attr) {

    // create a new Domian arryalist 
    ArrayList <String> dom = new ArrayList<String>(); 
      // for this.domains 
      for (int i =0; i < this.domains.size(); i ++) {
        dom.add(this.domains.get(i)); 
      }
    //create the new tuple with given attr and domains
    Tuple newTuple = new Tuple(attr, dom); 
    //make a new tuple ArrayList
    //ArrayList <Comparable> newTuple = new ArrayList<Comparable>(); 
      for (int j =0; j < this.tuple.size(); j++) {
        // create an if for VARCHAR 
        if (this.domains.get(j).equals("VARCHAR") ) {
          newTuple.addStringComponent((String)this.tuple.get(j)); // add each thing in this.tuple to newTuple
        }
        // create an if for INTEGER ...
        if (this.domains.get(j).equals("INTEGER") ) {
          newTuple.addIntegerComponent((Integer)this.tuple.get(j)); // add each thing in this.tuple to newTuple
        }
        // create an if for DECIMAL ...
        if (this.domains.get(j).equals("DECIMAL") ) {
          newTuple.addDoubleComponent((Double)this.tuple.get(j)); // add each thing in this.tuple to newTuple
        }
      }
    return newTuple; 
    }
    // -- WEEK 5

// This method combines two tuples into one and assigns a new schema to the 
// result tuple; the method returns the new tuple
// e.g. t1 = <"jones",20,200> and t2=<1,2,2.5>
// then t1.concatenate(t2,attr,dom) will be <"jones",20,200,1,2,2.5>
// with schema attr = <A, R.B, R.C, S.B, S.C, D>
// and dom = <VARCHAR,INTEGER,INTEGER,INTEGER,INTEGER,DECIMAL>
public Tuple concatenate(Tuple t, ArrayList<String> attr, ArrayList<String> dom) {
  //Construct new Tuple object, tup, with attrs and doms;
  Tuple tup = new Tuple(attr, dom); 
  //for each component in the "this" tuple:
  for (int i =0; i < this.tuple.size(); i ++) {
    //add the component to tup; // look at how you cloned a tuple for how to do this step
    // create an if for VARCHAR 
    if (this.domains.get(i).equals("VARCHAR") ) {
      tup.addStringComponent((String)this.tuple.get(i)); // add each thing in this.tuple to newTuple
    }
    // create an if for INTEGER ...
    if (this.domains.get(i).equals("INTEGER") ) {
      tup.addIntegerComponent((Integer)this.tuple.get(i)); // add each thing in this.tuple to newTuple
    }
    // create an if for DECIMAL ...
    if (this.domains.get(i).equals("DECIMAL") ) {
      tup.addDoubleComponent((Double)this.tuple.get(i)); // add each thing in this.tuple to newTuple
    }      
  }
  //for each component in the Tuple t:
  for (int j =0; j < t.tuple.size(); j++) {
    //add the component to tup; // look at how you cloned a tuple for how to do this step
    if (t.domains.get(j).equals("VARCHAR") ) {
      tup.addStringComponent((String)t.tuple.get(j)); // add each thing in this.tuple to newTuple
    }
    // create an if for INTEGER ...
    if (t.domains.get(j).equals("INTEGER") ) {
      tup.addIntegerComponent((Integer)t.tuple.get(j)); // add each thing in this.tuple to newTuple
    }
    // create an if for DECIMAL ...
    if (t.domains.get(j).equals("DECIMAL") ) {
      tup.addDoubleComponent((Double)t.tuple.get(j)); // add each thing in this.tuple to newTuple
    }
  }
  //return tup;
  return tup; 
  }

  // -- Week 6 -- 

// This method takes as input an array list of column names, each of which
// belonging to this.attributes, and returns a new tuple with only those
// components that correspond to the column names in cnames.
public Tuple project(ArrayList<String> cnames) {
  /** Create an array list, doms, with domains values for column names in cnames
  (Note: you would need .indexOf() method of ArrayList to determine the index where the column name is stored)
  Create a new Tuple object, tup;
  for each column name in cnames:
    add corresponding component of this.tuple to tup;
    */ 
    ArrayList<String> attr = new ArrayList<String>(); 
    ArrayList<String> dom = new ArrayList<String>(); 

    attr.addAll(cnames); 
    //search through each String s in cnames
      //find the index of s in this.attributes
      //use that index to find the corresponding domain for s in cnames
    for (String s : attr) {
      if (this.attributes.contains(s)) {
        int index = this.attributes.indexOf(s); 
        dom.add(this.domains.get(index)); 
      }
    }

    Tuple tup = new Tuple(attr, dom); 
    //Search through each String s in cnames
      //find the index of s in this.attributes
      //use that index to find the corresponding tuple in this.tuple
      //use that index to check Data type of its domain
    for (String s : attr) {
      int index = this.attributes.indexOf(s); 
      if (this.domains.get(index).equals("INTEGER")) {
        tup.addIntegerComponent((Integer)this.tuple.get(index));
      }
      if (this.domains.get(index).equals("VARCHAR")) {
        tup.addStringComponent((String)this.tuple.get(index));
      }
      if (this.domains.get(index).equals("DOUBLE")) {
        tup.addDoubleComponent((Double)this.tuple.get(index));
      }
    }
    return tup; 
  }
  // Week 7 -------------
  // This method takes a comparison condition in the 5 parameters and
  // returns true if the tuple satisfies the condition and false otherwise.
  //
  // The comparison condition is coded in the 5 parameters as follows:
  //
  // lopType/ropType can take one of three values: "col", "num", "str"
  // indicating that the operand is either a name of a column, or a number,
  // or a string respectively.
  //
  // lopValue/ropValue will contain the name of the column if the lopType/ropType
  // is "col" and will contain a numeric value if lopType/ropType is "num" and
  // will contain a string value if lopType/ropType is "str".
  //
  // comparison will have one of six values: "<", "<=","=",">",">=", or "<>"
  //
  // As an example, if we want to express the comparison, SNAME = "Jones", the 5 parameters will be:
  // lopType="col", lopValue="SNAME", comparison="=", ropType="str", ropValue="Jones"
  //
  // As another example, if we want to express the condition GPA > 3.0, the 5 parameters will be:
  // lopType="col", lopValue="GPA", comparison=">", ropType="num", ropValue="3.0"
  //
  public boolean select(String lopType, String lopValue, String comparison, String ropType, String ropValue) {
    if (lopType.equals("num") && ropType.equals("num")) {
      switch (comparison) {
        case "<":  return (Double.parseDouble(lopValue) <  Double.parseDouble(ropValue));
        case "<=": return (Double.parseDouble(lopValue) <= Double.parseDouble(ropValue));
        case "=":  return (Double.parseDouble(lopValue) == Double.parseDouble(ropValue));
        case ">":  return (Double.parseDouble(lopValue) >  Double.parseDouble(ropValue));
        case ">=": return (Double.parseDouble(lopValue) >= Double.parseDouble(ropValue));
        case "<>": return (Double.parseDouble(lopValue) != Double.parseDouble(ropValue));
      }
    } else if (lopType.equals("str") && ropType.equals("str")) {
      switch (comparison) {
        case "<":  return (lopValue.compareTo(ropValue) < 0);
        case "<=": return (lopValue.compareTo(ropValue) <= 0);
        case "=":  return (lopValue.compareTo(ropValue) == 0);
        case "<>": return (lopValue.compareTo(ropValue) != 0);
        case ">":  return (lopValue.compareTo(ropValue) > 0);
        case ">=": return (lopValue.compareTo(ropValue) >= 0);
      }
    } else if (lopType.equals("col") && ropType.equals("num")) { 
      int i = this.attributes.indexOf(lopValue);
      Double temp = ((Number) this.tuple.get(i)).doubleValue();
      switch (comparison) {
        case "<":  return (temp < Double.parseDouble(ropValue));
        case "<=": return (temp <= Double.parseDouble(ropValue));
        case "=":  return (temp == Double.parseDouble(ropValue));
        case "<>": return (temp != Double.parseDouble(ropValue));
        case ">":  return (temp > Double.parseDouble(ropValue));
        case ">=": return (temp >= Double.parseDouble(ropValue));
      }
    }
    else if (lopType.equals("col") && ropType.equals("str")) {
      int i = this.attributes.indexOf(lopValue);
      String  temp = ((String) this.tuple.get(i));
      switch (comparison) {
        case "<":  return (temp.compareTo(ropValue) < 0);
        case "<=": return (temp.compareTo(ropValue) <= 0);
        case "=":  return (temp.compareTo(ropValue) == 0);
        case "<>": return (temp.compareTo(ropValue) != 0);
        case ">":  return (temp.compareTo(ropValue) > 0);
        case ">=": return (temp.compareTo(ropValue) >= 0);
      }
    } else if (lopType.equals("num") && ropType.equals("col")) {
      int i = this.attributes.indexOf(ropValue);
      Double temp = ((Number) this.tuple.get(i)).doubleValue();
      switch (comparison) {
        case "<":  return (Double.parseDouble(lopValue) < temp);
        case "<=": return (Double.parseDouble(lopValue) <= temp);
        case "=":  return (Double.parseDouble(lopValue) == temp);
        case "<>": return (Double.parseDouble(lopValue) != temp);
        case ">":  return (Double.parseDouble(lopValue) > temp);
        case ">=": return (Double.parseDouble(lopValue) >= temp);
      }
    } else if (lopType.equals("str") && ropType.equals("col")) {
      int i = this.attributes.indexOf(ropValue);
      String temp = ((String) this.tuple.get(i));
      switch (comparison) {
        case "<":  return (lopValue.compareTo(temp) < 0);
        case "<=": return (lopValue.compareTo(temp) <= 0);
        case "=":  return (lopValue.compareTo(temp) == 0);
        case "<>": return (lopValue.compareTo(temp) != 0);
        case ">":  return (lopValue.compareTo(temp) > 0);
        case ">=": return (lopValue.compareTo(temp) >= 0);
      }
    } else if (lopType.equals("col") && ropType.equals("col")) {
      int i1 = this.attributes.indexOf(lopValue);
      int i2 = this.attributes.indexOf(ropValue);
      Comparable temp1 = this.tuple.get(i1);
      Comparable temp2 = this.tuple.get(i2);
      switch (comparison) {
        case "<":  return (temp1.compareTo(temp2) < 0);
        case "<=": return (temp1.compareTo(temp2) <= 0);
        case "=":  return (temp1.compareTo(temp2) == 0);
        case "<>": return (temp1.compareTo(temp2) != 0);
        case ">":  return (temp1.compareTo(temp2) > 0);
        case ">=": return (temp1.compareTo(temp2) >= 0);
      }
    } else
        return false; 
    return false;
  }

  // Week 8

  // This method attempts to construct a "joined" tuple out of this.tuple and t2.tuple
  // If the two tuples can join, the joined tuple is returned; otherwise "null" is returned.
  // Let ENROLL(SID:INTEGER,SNAME:VARCHAR,PHONE:INTEGER,MAJOR:VARCHAR,GPA:DECIMAL)
  // and STUDENT(SID:INTEGER,COURSE:VARCHAR,GRADE:VARCHAR)
  // let this.tuple = 1111:Robert Adams:1234:Computer Science:4.0:
  // and   t2.tuple = 1111:Database Systems:A:
  // Then, joined tuple will be: 1111:Robert Adams:1234:Computer Science:4.0:Database Systems:A:
  // As another example,
  // let this.tuple = 1111:Robert Adams:1234:Computer Science:4.0:
  // and   t2.tuple = 1114:Database Systems:B:
  // These two tuples do not join because in the first tuple SID=1111 and in the second
  // tuple SID=1114; So, the result should be "null"
  public Tuple join(Tuple t2, ArrayList attr, ArrayList dom) {
    
    //Collect information about join columns in the following 4 array lists:
    //leftJoinColumns, leftJoinDomains, rightJoinColumns, rightJoinDomains
    //Verify if the two tuples can join; if not return null
    //If tuples can join then produce the joined tuple and return it.
    
    ArrayList<String> joinColumns = new ArrayList<String>(); 
    ArrayList<String> joinDomains = new ArrayList<String>(); 

    //Tuple t = new Tuple(attr, dom); // attr and dom contain all the values for the relation 
    //there is no need to further append the left or right arraylists
    
    //add all the columns that are duplicates between the two columns in left 
    for (int i =0; i < this.attributes.size();i++) {
      String cName = this.attributes.get(i); 
      if (t2.attributes.contains(cName)) {
        joinColumns.add(cName); 
        joinDomains.add(this.domains.get(i)); 
      }
    } // this stores all the duplicate values in joinColumns

//System.out.println("joinColumns="+joinColumns);
//System.out.println("joinDomains="+joinDomains);
    //verify if they can join 
    for (int i = 0; i < joinColumns.size(); i ++) {
      //store attr
      String cName = joinColumns.get(i);
      //find the index
      int index1 = this.attributes.indexOf(cName);
      int index2 = t2.attributes.indexOf(cName);  
      //verifies if they can join 
      if (!(this.tuple.get(index1).equals(t2.tuple.get(index2)))) {
        return null; 
      } 
    }

    Tuple t = new Tuple(attr, dom); 
    //add tuples from this and t2
    for (int x = 0; x < this.tuple.size(); x++) {
      t.tuple.add(this.tuple.get(x)); 
    }
    for (int x = 0; x < t2.tuple.size(); x++ ) {
      if (!(this.attributes.contains(t2.attributes.get(x)))) {
        t.tuple.add(t2.tuple.get(x)); 
      }
    }
    return t; 
  }
}
