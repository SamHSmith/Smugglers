package render.shaders;

import math3d.Matrix4f;

public class GUIshader extends ShaderProgram {
	
	private static final String VERTEXFILE ="GUIvertex.txt";
	private static final String FRAGFILE="GUIfrag.txt";

	private int matloc;

	public GUIshader() {
		super(GUIshader.class.getResourceAsStream(VERTEXFILE), GUIshader.class.getResourceAsStream(FRAGFILE));
	}

	@Override
	protected void getAllUniforms() {
		matloc=super.GetUniFormL("transform");
	}

	@Override
	protected void bindAtributes() {
		super.bindAtrib(0, "position");
		super.bindAtrib(1, "texturecoords");

	}
	
	public void loadmatices(Matrix4f trans){
		super.loadmatrix(matloc, trans);
	}
	
	@Override
	protected void printErrorMessage(String message) {
		System.err.println("GUI Shader Failed: ");
		System.err.println(message);
	}
	

}
