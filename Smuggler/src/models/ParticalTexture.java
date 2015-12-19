package models;

public class ParticalTexture extends ModelTexture {
	
	private int numberofrows;
	private int blendfunc;

	public ParticalTexture(int id,int numberofrows,int blendfunc) {
		super(id);
		this.numberofrows=numberofrows;
		this.blendfunc=blendfunc;
	}
	
	public ParticalTexture(ModelTexture text,int numberofrows,int blendfunc){
		super(text);
		this.numberofrows=numberofrows;
		this.blendfunc=blendfunc;
	}

	public int getNumberofrows() {
		return numberofrows;
	}

	public int getBlendfunc() {
		return blendfunc;
	}
	
	

}
