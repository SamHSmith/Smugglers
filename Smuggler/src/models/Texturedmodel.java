package models;


public class Texturedmodel {
	
	private RawModel model;
	private ModelTexture text;

	public Texturedmodel(RawModel model, ModelTexture text) {
		this.model=model;
		this.text=text;
	}

	public RawModel getModel() {
		return model;
	}

	public void setModel(RawModel model) {
		this.model = model;
	}

	public int getTextID() {
		return text.getId();
	}

	public void setText(ModelTexture text) {
		this.text = text;
	}

}
