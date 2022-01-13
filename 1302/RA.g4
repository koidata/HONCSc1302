grammar RA;

query returns [RANode value] : 
  e=expr SEMI
  {
    $value = $e.value;
  }
;

expr returns [RANode value] : 
    e1=factor UNION e2=expr
  {
    RANode node = new RANode();
    node.setLchild($e1.value);
    node.setRchild($e2.value);
    node.setRnodetype("union");
    $value = node;
  }
  | e1=factor MINUS e2=expr
  {
    RANode node = new RANode();
    node.setLchild($e1.value);
    node.setRchild($e2.value);
    node.setRnodetype("minus");
    $value = node;
  }
  | e1=factor INTERSECT e2=expr
  {
    RANode node = new RANode();
    node.setLchild($e1.value);
    node.setRchild($e2.value);
    node.setRnodetype("intersect");
    $value = node;
  }
  | e1=factor
  {
    $value = $e1.value;
  }
;

factor returns [RANode value] : 
    e1=term TIMES e2=expr
  {
    RANode node = new RANode();
    node.setLchild($e1.value);
    node.setRchild($e2.value);
    node.setRnodetype("times");
    $value = node;
  }
  | e1=term JOIN e2=expr
  {
    RANode node = new RANode();
    node.setLchild($e1.value);
    node.setRchild($e2.value);
    node.setRnodetype("join");
    $value = node;
  }
  | e1=term
  {
    $value = $e1.value;
  }
;


term returns [RANode value] : 
    n=NAME 
  {
    RANode node = new RANode();
    node.setLchild(null);
    node.setRchild(null);
    node.setRnodetype("relation");
    node.setRelationName($n.text.toUpperCase());
    $value = node;
  }
  | PROJECT LBRACKET as=alist RBRACKET LPAREN e1=expr RPAREN
  {
    RANode node = new RANode();
    node.setLchild($e1.value);
    node.setRnodetype("project");
    node.setAttributes($as.value);
    $value = node;
  }
  | RENAME  LBRACKET as=alist RBRACKET LPAREN e1=expr RPAREN
  {
    RANode node = new RANode();
    node.setLchild($e1.value);
    node.setRnodetype("rename");
    node.setAttributes($as.value);
    $value = node;
  }
  | SELECT LBRACKET c=condition RBRACKET LPAREN e1=expr RPAREN
  {
    RANode node = new RANode();
    node.setLchild($e1.value);
    node.setRnodetype("select");
    node.setConditions($c.value);
    $value = node;
  }
  | LPAREN e=expr RPAREN
  {
    $value = $e.value;
  }
;

alist returns [ArrayList<String> value] :
    n=NAME
  {
    ArrayList<String> a = new ArrayList<String>();
    a.add($n.text.toUpperCase());
    $value = a;
  }
  | as=alist COMMA n=NAME
  {
    $as.value.add($n.text.toUpperCase());
    $value = $as.value;
  }
;


condition returns [ArrayList<Condition> value] :
    sc=simplecondition
  {
    ArrayList<Condition> a = new ArrayList<Condition>();
    a.add($sc.value);
    $value = a;
  }
  | c=condition AND sc=simplecondition 
  {
    $c.value.add($sc.value);
    $value = $c.value;
  }
;

simplecondition returns [Condition value] : 
    o1=operand c=COMPARISON o2=operand
  {
    $value = new Condition($o1.value.substring(3),$o1.value.substring(0,3),
                           $c.text,
                           $o2.value.substring(3),$o2.value.substring(0,3));
  }
;

operand returns [String value] :  
    n=NAME
  {
    $value = "col"+$n.text.toUpperCase();
  }
  | s=STRING
  {
    $value = "str"+$s.text.substring(1,$s.text.length()-1);
  }
  | num=NUMBER
  {
    $value = "num"+$num.text;
  }
;

fragment VALID_NAME_START : ('a'..'z') | ('A'..'Z');
fragment VALID_NAME_CHAR : VALID_NAME_START | ('0'..'9') | '_';
LBRACKET : '[';
RBRACKET : ']';
LPAREN : '(';
RPAREN : ')';
SEMI : ';';
COMMA : ',';
UNION : 'UNION' | 'union';
MINUS : 'MINUS' | 'minus';
INTERSECT : 'INTERSECT' | 'intersect';
TIMES : 'TIMES' | 'times';
JOIN : 'JOIN' | 'join';
PROJECT : 'PROJECT' | 'project';
SELECT : 'SELECT' | 'select';
RENAME : 'RENAME' | 'rename';
AND : 'AND' | 'and';
NUMBER : ('0'..'9')+ | ('0'..'9')* '.' ('0'..'9')+;
STRING : '\'' (~[\r\n'] | '\'\'')* '\'';
COMPARISON : '<' | '<=' | '>' | '>=' | '=' | '<>';
NAME : VALID_NAME_START VALID_NAME_CHAR*;
WS : [ \r\n\t]+ -> skip;
