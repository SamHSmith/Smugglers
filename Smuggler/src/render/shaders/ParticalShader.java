package render.shaders;

import math3d.Matrix4f;
import math3d.Vector2f;


public class ParticalShader extends ShaderProgram {

	private static final String vfile = "/render/shaders/particalvertexshader.txt";
	private static final String fragmentfile = "/render/shaders/particalfragmentshader.txt";
	
	private int modelviewmatloc;
	private int rotmatloc;
	private int projmatloc;
	private int numberofrowsloc;
	private int offset1loc;
	private int offset2loc;
	private int blendloc;

	public ParticalShader() {
		super(EntityShader.class.getResourceAsStream(vfile), EntityShader.class
				.getResourceAsStream(fragmentfile));
	}

	@Override
	protected void getAllUniforms() {
		modelviewmatloc=super.GetUniFormL("modelviewMatrix");
		projmatloc=super.GetUniFormL("projmat");
		rotmatloc=super.GetUniFormL("rotmat");
		numberofrowsloc=super.GetUniFormL("numberofrows");
		offset1loc=super.GetUniFormL("offset1");
		offset2loc=super.GetUniFormL("offset2");
		blendloc=super.GetUniFormL("blend");
	}

	@Override
	protected void bindAtributes() {
		super.bindAtrib(0, "position");
	}
	
	public void loadprojmat(Matrix4f mat) {
		super.loadmatrix(projmatloc, mat);
	}
	
	public void loadmodelviewmat(Matrix4f mat) {
		super.loadmatrix(modelviewmatloc, mat);
	}
	
	public void loadrotmat(Matrix4f mat) {
		super.loadmatrix(rotmatloc, mat);
	}
	
	public void loadAtlasData(float numberofrows,float blend,Vector2f offset1,Vector2f offset2) {
		super.loadFloat(numberofrowsloc, numberofrows);
		super.loadVector2f(offset1loc, offset1);
		super.loadVector2f(offset2loc, offset2);
		super.loadFloat(blendloc, blend);
	}

}
