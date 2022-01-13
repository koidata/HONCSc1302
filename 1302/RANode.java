import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class RANode{
  public RANode lchild;
  public RANode rchild;  // will be null for unary operators: select, project, rename
  public String rnodetype; // select, project, rename, etc
  public String relationName; // used to name interior nodes with tempN; relation name at leaf
  public ArrayList<String> attributes; // used with project and rename
  public ArrayList<Condition> conditions; // used with select 
  public ArrayList<String> joinColumns; // used to remember JOIN columns
  public ArrayList<String> schemaColumns;
  public ArrayList<String> schemaDataTypes;

  public RANode() {
    lchild = null;
    rchild = null;
    rnodetype = "";
    relationName = "";
    attributes = new ArrayList<String>();
    conditions = new ArrayList<Condition>();
    schemaColumns = new ArrayList<String>();
    schemaDataTypes = new ArrayList<String>();
    joinColumns = new ArrayList<String>();
  }

  public ArrayList<String> getAttributes() {
    return attributes;
  }

  public ArrayList<String> getJoinColumns() {
    return joinColumns;
  }

  public RANode getLchild() {
    return lchild;
  }

  public RANode getRchild() {
    return rchild;
  }

  public String getRelationName() {
    return relationName;
  }

  public String getRnodetype() {
    return rnodetype;
  }

  public ArrayList<Condition> getConditions() {
    return conditions;
  }

  public ArrayList<String> getSchema() {
    return schemaColumns;
  }

  public ArrayList<String> getDataTypes() {
    return schemaDataTypes;
  }

  public void setAttributes(ArrayList<String> vector) {
    attributes = vector;
  }

  public void setLchild(RANode node) {
    lchild = node;
  }

  public void setRchild(RANode node) {
    rchild = node;
  }

  public void setRelationName(String string) {
    relationName = string;
  }

  public void setRnodetype(String string) {
    rnodetype = string;
  }

  public void setConditions(ArrayList<Condition> c) {
    conditions = c;
  }

  public void setSchema(ArrayList<String> a) {
    schemaColumns = a;
  }

  public void setDataTypes(ArrayList<String> a) {
    schemaDataTypes = a;
  }

  public void setJoinColumns(ArrayList<String> a) {
    joinColumns = a;
  }

  public int sizeOfTree() {
    if ((lchild==null) && (rchild==null))
      return 1;
    else if (rchild==null)
      return (1+lchild.sizeOfTree());
    else
      return (1+lchild.sizeOfTree()+rchild.sizeOfTree());
  }

  public void printTree() {
    if ((lchild == null) && (rchild == null)) {
      System.out.println("NODE TYPE: " + rnodetype + "  ");
      System.out.println("Relation Name is : " + relationName);
      System.out.println("Schema is : " + schemaColumns);
      System.out.println("Datatypes is : " + schemaDataTypes+"\n");
    }
    else if (rchild == null) { // project or rename or select
      if (rnodetype.equals("project") || rnodetype.equals("rename")) {
        System.out.println("NODE TYPE: " + rnodetype + "  ");
        System.out.println("Atributes are : "+attributes);
        System.out.println("Relation Name is : " + relationName);
        System.out.println("Schema is : " + schemaColumns);
        System.out.println("Datatypes is : " + schemaDataTypes+"\n");
        lchild.printTree();
      } else { // must be select: if (rnodetype.equals("select")) {
          System.out.println("NODE TYPE: " + rnodetype + "  ");
          for (int i=0; i<conditions.size(); i++) {
            System.out.print(conditions.get(i).getLeftOperand());
            System.out.print("--");
            System.out.print(conditions.get(i).getLeftDataType());
            System.out.print("--");
            System.out.print(conditions.get(i).getOperator());
            System.out.print("--");
            System.out.print(conditions.get(i).getRightOperand());
            System.out.print("--");
            System.out.println(conditions.get(i).getRightDataType());
          }
          System.out.println("Relation Name is : " + relationName);
          System.out.println("Schema is : " + schemaColumns);
          System.out.println("Datatypes is : " + schemaDataTypes+"\n");
          lchild.printTree();
      }
    } else {// union, minus, join, times, intersect
        if (rnodetype.equals("union") || rnodetype.equals("minus") || 
            rnodetype.equals("join")  || rnodetype.equals("intersect") || 
            rnodetype.equals("times")) {
          System.out.println("NODE TYPE: "+rnodetype+"  ");
          System.out.println("Relation Name is : " + relationName);
          System.out.println("Schema is : " + schemaColumns);
          System.out.println("Datatypes is : " + schemaDataTypes+"\n");
          lchild.printTree();
          rchild.printTree();
        }
      }
  }

  public String semanticCheckAndSetSchemaAndDataTypes(Database db) {
    // This method checks for Semantic Errors and returns an error message
    // if there is a semantic error. If there are no semantic errors then this method
    // sets the following 3 instance variables for RANode class:
    // public ArrayList<String> joinColumns; // set this only for join
    // public ArrayList<String> schemaColumns;
    // public ArrayList<String> schemaDataTypes;
    // and returns "OK"

    if(rnodetype.equals("relation")){
      // week 11
      // SEMANTIC ERROR CHECK: 
      // (1) Relation not in database
      if (db.relationExists(this.relationName)) {
        Relation r = db.getRelation(this.relationName); 
        schemaColumns = r.getAttributes();//attributes
        schemaDataTypes = r.getDomains();//domains
        return "OK"; 
      }
      return "Relation " + this.relationName + " does not exist in this database."; 
    }
    else if(rnodetype.equals("union") ||
            rnodetype.equals("minus") ||
            rnodetype.equals("intersect")){
      // week 11
      // SEMANTIC ERROR CHECKS:
      // (1) The two relations have different number of attributes
      // (2) The two relations have mismatched attribute datatypes
      String temp = lchild.semanticCheckAndSetSchemaAndDataTypes(db);
      if (!temp.equals("OK")) {
        return temp; 
      }
      temp = rchild.semanticCheckAndSetSchemaAndDataTypes(db);
      if (!temp.equals("OK")) {
        return temp; 
      }
      if (lchild.schemaColumns.size() != rchild.schemaColumns.size()) {
        return "The two relations have different number of attributes"; 
      } 
      if (!lchild.schemaDataTypes.equals(rchild.schemaDataTypes)) {
        return "The two relations have mismatched attribute datatypes"; 
      }
      schemaColumns =lchild.schemaColumns;
      schemaDataTypes =lchild.schemaDataTypes; 
      return "OK";    
    }
    else if(rnodetype.equals("times")){
      // week 12
      // no error checks
      String temp = lchild.semanticCheckAndSetSchemaAndDataTypes(db);
      if (!temp.equals("OK")) {
        return temp; 
      }
      temp = rchild.semanticCheckAndSetSchemaAndDataTypes(db);
      if (!temp.equals("OK")) {
        return temp; 
      }
      ArrayList <String> attrs = new ArrayList<String>(); 
      ArrayList <String> doms = new ArrayList<String>(); 

      //add all lchild cols
      for (String s : lchild.schemaColumns) {
        attrs.add(s); 
      }
      // add rchild cols
      for (String s : rchild.schemaColumns) {
        if (attrs.contains(s)) {
          int index = attrs.indexOf(s); 
          attrs.set(index, this.lchild.relationName + "." + this.lchild.schemaColumns.get(lchild.schemaColumns.indexOf(s))); 
          attrs.add(this.rchild.relationName + "." + s);
        } else {
          attrs.add(s); 
        }
      }
      doms.addAll(this.lchild.getDataTypes());
      doms.addAll(this.rchild.getDataTypes()); 

      this.schemaColumns = attrs;
      this.schemaDataTypes = doms; 
      return "OK";
    }
    else if(rnodetype.equals("join")){
      // week 12
      // no error checks
      String temp = lchild.semanticCheckAndSetSchemaAndDataTypes(db);
      if (!temp.equals("OK")) {
        return temp; 
      }
      temp = rchild.semanticCheckAndSetSchemaAndDataTypes(db);
      if (!temp.equals("OK")) {
        return temp; 
      }
      ArrayList<String> attrs = new ArrayList<String>();
      ArrayList<String> doms = new ArrayList<String>();

      for(int i = 0; i < this.lchild.getSchema().size() ; ++i)
      {
        attrs.add(this.lchild.getSchema().get(i));
      }

      for(int i = 0; i < this.lchild.getDataTypes().size(); ++i)
      {
        doms.add(this.lchild.getDataTypes().get(i));
      }


      for(int i = 0; i < this.rchild.getSchema().size() ; ++i)
      {
        int index = attrs.indexOf(this.rchild.getSchema().get(i));
        if(index == -1)
        {
          attrs.add(this.rchild.getSchema().get(i));
          doms.add(this.rchild.getDataTypes().get(i));
        }
      }

      this.schemaColumns = attrs;
      this.schemaDataTypes = doms; 

      return "OK";
    }
    else if(rnodetype.equals("rename")){
            // Week 13
      // SEMANTIC ERROR CHECKS:
      // (1) Not a valid number of attributes
      String temp = lchild.semanticCheckAndSetSchemaAndDataTypes(db);
      if (!temp.equals("OK")) {
        return temp; 
      }
      this.attributes.addAll(this.schemaColumns); 
      if (this.attributes.size() != lchild.schemaColumns.size()) {
        return "Not a valid number of attributes";
      }
      this.schemaColumns.addAll(this.attributes); 
      this.schemaDataTypes.addAll(lchild.schemaDataTypes); 
      return "OK";
    }
    else if(rnodetype.equals("project")){ //** add a way to tell which attribute is not valid */
      // Week 13
      // SEMANTIC ERROR CHECKS:
      // (1) Not a valid attribute 

        String temp = lchild.semanticCheckAndSetSchemaAndDataTypes(db);
        if (!temp.equals("OK")) {
          return temp; 
        }
        ArrayList <String> t = new ArrayList<String>(); 
        this.attributes.addAll(this.schemaColumns); 
        for (String s: this.attributes) {
          if (!lchild.schemaColumns.contains(s)) {
            return "Not a valid attribute"; 
          } else {
            int index = lchild.schemaColumns.indexOf(s); 
            t.add(lchild.schemaDataTypes.get(index));
          }
        }
        this.schemaColumns = this.attributes;
        this.schemaDataTypes = t;
      return "OK";
    }
    else if(rnodetype.equals("select")){
      // week 13
      String temp = lchild.semanticCheckAndSetSchemaAndDataTypes(db);
        if (!temp.equals("OK")) {
          return temp; 
        }
      // SEMANTIC ERROR CHECKS:
      // (1) Left Operand is not a valid operand
      // (2) Right Operand is not a valid operand
      // (3) Data types do not match in comparison

      for (int i =0; i < conditions.size(); i++) {
        Condition c = conditions.get(i);
        String ltype = c.getLeftDataType();
        String rtype = c.getRightDataType();

        // (1) Left Operand is not a valid operand
        if (c.getLeftDataType().equals("col")) {
          String cname = c.getLeftOperand();
          if (!lchild.schemaColumns.contains(cname)) {
            return "Left Operand is not a valid operand"; 
          } else {
            String x = lchild.schemaDataTypes.get(lchild.schemaColumns.indexOf(cname)); // gets the col DOMAIN
            if (x.equals("VARCHAR")){
              ltype = "str";
            } else {
              ltype = "num"; 
            }
          }
        } 
        // (2) Right Operand is not a valid operand
        if (c.getRightDataType().equals("col")) { 
          String cname = c.getRightOperand();
          if (!lchild.schemaColumns.contains(cname)) {
            return "Right Operand is not a valid operand"; 
          } else {
            String x = lchild.schemaDataTypes.get(lchild.schemaColumns.indexOf(cname)); // gets the col DOMAIN
            if (x.equals("VARCHAR")){
              rtype = "str";
            } else {
              rtype = "num"; 
            }
          }
        } 
        // (3) Data types do not match in comparison
        if (!ltype.equals(rtype)) {
          return "Data types do not match in comparison";
        }
      } 
      //set schemas and dataTypes
      this.schemaColumns = lchild.schemaColumns;
      this.schemaDataTypes = lchild.schemaDataTypes;  
      return "OK";
    }
    else {
      return "SOMETHING STRANGE TOOK PLACE!";
    }
  }

  public void setRelationNames(AtomicInteger globalInt) {
    // set unique relation names for every interior node in the tree
    if (rnodetype.equals("relation")) {
        //no action
    } else if (rnodetype.equals("union") || rnodetype.equals("intersect") || rnodetype.equals("minus") || rnodetype.equals("times") || rnodetype.equals("join")) {
        lchild.setRelationNames(globalInt);
        rchild.setRelationNames(globalInt);
        this.relationName = "temp" + globalInt.get(); 
        globalInt.set(globalInt.get()+1);
    } else if (rnodetype.equals("rename") || rnodetype.equals("project") || rnodetype.equals("select") ) { //unary 
        lchild.setRelationNames(globalInt);
        this.relationName = "temp" + globalInt.get();
        globalInt.set(globalInt.get()+1);
    } else {
        System.out.println("SOMETHING SERIOUSLY WENT WRONG!");
    }
    //   public String relationName; // used to name interior nodes with tempN; relation name at leaf
  }

  public Relation evaluate(Database db) {
    // evaluate and return relation object for node

    if (rnodetype.equals("relation")) {
      return db.getRelation(this.relationName);
  } else if (rnodetype.equals("union")) {
      Relation r1 = this.lchild.evaluate(db);
      Relation r2  = this.rchild.evaluate(db);
      Relation r3 = r1.union(r2); 
      r3.setName(this.relationName);
      return r3;
      //return this.lchild.evaluate(db).union(this.rchild.evaluate(db));
  } else if (rnodetype.equals("intersect")) {
      Relation r1 = this.lchild.evaluate(db).intersect(this.rchild.evaluate(db));
      r1.setName(this.relationName);
      return r1;
  } else if (rnodetype.equals("minus")) {
      Relation r1 = this.lchild.evaluate(db).minus(this.rchild.evaluate(db));
      r1.setName(this.relationName);
      return r1;
  } else if (rnodetype.equals("rename")) {
      Relation r1 = this.lchild.evaluate(db).rename(this.schemaColumns);
      r1.setName(this.relationName);
      return r1;
  } else if (rnodetype.equals("project")) {
      Relation r1 = this.lchild.evaluate(db).project(this.schemaColumns);
      r1.setName(this.relationName);
      return r1;
  } else if (rnodetype.equals("select")) {
      Relation r = lchild.evaluate(db);   
      for (int i =0; i < conditions.size(); i ++) {
          Condition c = conditions.get(i); 
          r = r.select(c.getLeftDataType(), c.getLeftOperand(), c.getOperator(), c.getRightDataType(), c.getRightOperand()); 
      }
      r.setName(this.relationName);
      return r; 
  } else if (rnodetype.equals("times")) {
      Relation r1 = this.lchild.evaluate(db).times(this.rchild.evaluate(db));
      r1.setName(this.relationName);
      return r1;
  } else if (rnodetype.equals("join")) {
      Relation r1 = this.lchild.evaluate(db).join(this.rchild.evaluate(db));
      r1.setName(this.relationName);
      return r1;
  } else {
      System.out.println("SOMETHING SERIOUSLY WENT WRONG!");
  }
    return null; 
  }

}