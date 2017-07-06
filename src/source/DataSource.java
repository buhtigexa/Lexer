
package source;

public abstract class DataSource {

	public abstract void rewind();
	public abstract char get();
	public abstract String readAll();
	public DataSource(){
		
	}
}
