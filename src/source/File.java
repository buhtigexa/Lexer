package source;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class File extends DataSource{

	
	protected RandomAccessFile myFile;
	protected long position;
	
	public File(String path){
		
		super();
		position=0;
		
		try {
			myFile=new RandomAccessFile(path, "r");
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void rewind() {
		
		
		position--;
		try {
			myFile.seek(position);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	
	public String readAll() {
					
		byte b = 0;
		String str="";
		try {
			while ( (b=myFile.readByte()) != -1 ){
				str+=(char)b;
			}
		} 
		
		catch( EOFException e){
			str+="EOF";
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		String x="";
	   	return x;
	}

	
	public char get() {
		
		
		
		return 0;
		
	}
}
