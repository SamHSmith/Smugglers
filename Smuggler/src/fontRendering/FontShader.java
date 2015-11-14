package fontRendering;

import org.joml.Vector2f;
import org.joml.Vector3f;

import shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "src/fontRendering/fontFragment.txt";
	
	private int location_colour;
	private int location_translation;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
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


}
