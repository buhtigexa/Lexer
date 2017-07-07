package symboltable;

public class RowConst extends Row{

	
	public String type;
	
	
		
	public RowConst(String token) {
		super(token);

	}
	
	public RowConst(String token, String lexeme, String type){
		super(token);
		this.token=token;
		this.lexeme=lexeme;
		this.type=type;
	}

	@Override
	public String getLexeme() {
		// TODO Auto-generated method stub
		return lexeme;
	}
	
	public String getType() {
		return type;
	}

}

/*

No ME respondas esto ( es para vos ).

No importa lo que yo piense , importa lo que vos creés.

1- Confías en mi ?
2- Creés/ que yo puedo quedarme con vos mucho tiempo ( el que vos necesitás ) ?
3- Pensás que ... podés superar dificultades/desencuentros/discusiones/distancias conmigo ? ( porque vas a querer... )
4- Soy yo un candidato que puede someterse al estilo de vida que vos quisieras ? Soy capaz de hacerlo en corto plazo ?
5- Hay algo que te dicta que YO realmente quiero estar con vos , de verdad ? ( de ahí en adelante.... lo que yo haría ).



Y .... esto es lo que yo SÉ :

1- No me esperaba encontrar a alguien así; sentí como si te conocía de antes.
2- Tengo ganas de estar con vos toda mi vida; sé que eso no va a ser fácil ( x 4HoR4 ), pero si las ganas, preferentemente
los cambios se hacen rápido.


*/

