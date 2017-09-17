byaccj -J grammar.y 
byaccj -v grammar.y 
sed -i '1s/^/package parser; /' ParserVal.java
