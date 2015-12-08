package render.shaders;

import math3d.Matrix4f;

public class GUIshader extends ShaderProgram {
	
	private static final String VERTEXFILE ="GUIvertex.txt";
	private static final String FRAGFILE="GUIfrag.txt";

	private int matloc;
	private int rotloc;

	public GUIshader() {
		super(GUIshader.class.getResourceAsStream(VERTEXFILE), GUIshader.class.getResourceAsStream(FRAGFILE));
	}

	@Override
	protected void getAllUniforms() {
		matloc=super.GetUniFormL("transform");
		rotloc=super.GetUniFormL("rotform");
	}

	@Override
	protected void bindAtributes() {
		super.bindAtrib(0, "position");
		super.bindAtrib(1, "texturecoords");

	}
	
	public void loadmatices(Matrix4f trans,Matrix4f rot){
	super.loadmatrix(matloc, trans);
	super.loadmatrix(rotloc, rot);
	}
	

}
