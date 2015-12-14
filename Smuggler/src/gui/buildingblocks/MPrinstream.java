package gui.buildingblocks;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class MPrinstream extends PrintStream {

	MConsole c;
	/*
	 * This will throw a exception, but don't worry. It was the only way to do it. So just ignore the exception.
	 * 
	 * @param s the String to print to.
	 */
	public MPrinstream(MConsole c) throws FileNotFoundException {
		super("A file");
		this.c=c;
	}
	
	@Override
	public void print(String s){
		c.type(s);
	}
	
	@Override
	public void println(String s){
		c.type(s);
	}

}
