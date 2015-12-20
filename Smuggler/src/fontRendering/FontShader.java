package fontRendering;

import math3d.Vector2f;
import math3d.Vector3f;
import render.shaders.GUIshader;
import render.shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "fontVertex.txt";
	private static final String FRAGMENT_FILE = "fontFragment.txt";
	
	private int location_colour;
	private int location_translation;
	
	public FontShader() {
		super(FontShader.class.getResourceAsStream(VERTEX_FILE), FontShader.class.getResourceAsStream(FRAGMENT_FILE));
	}

	@Override
	protected void getAllUniforms() {
		location_colour = super.GetUniFormL("colour");
		location_translation = super.GetUniFormL("translation");
	}

	@Override
	protected void bindAtributes() {
		super.bindAtrib(0, "position");
		super.bindAtrib(1, "textureCoords");
	}
	
	protected void loadColour(Vector3f colour){
		super.loadVector3f(location_colour, colour);
	}
	
	protected void loadTranslation(Vector2f translation){
		super.loadVector3f(location_translation, new Vector3f(translation,0));
	}
	
	@Override
	protected void printErrorMessage(String message) {
		System.err.println("Font Shader Failed: ");
		System.err.println(message);
	}


}
