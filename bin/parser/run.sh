./yacc.linux -J grammar.y 
sed -i '1s/^/package parser; /' ParserVal.java
