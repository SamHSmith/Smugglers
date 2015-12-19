package models;

public class ModelTexture {
	
	int id;
	
	public ModelTexture(int id) {
		this.id=id;
	}
	
	public ModelTexture(ModelTexture texture) {
		this.id=texture.getId();
	}

	public int getId() {
		return id;
	}

}
