package shaders;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class StaticShader extends ShaderProgram {
	
	private static final String VERTEXFILE ="src/shaders/vertexshader.txt";
	private static final String FRAGFILE="src/shaders/fragmentshader.txt";
	
	private int rotxloc;
	private int rotyloc;
	private int rotzloc;
	private int scaleloc;
	private int posloc;
	private int projmatloc;
	private int veiwposloc;
	private int veiwrotxloc;
	private int veiwrotyloc;

	public StaticShader() {
		super(VERTEXFILE, FRAGFILE);
	}

	@Override
	protected void bindAtributes() {
		super.bindAtrib(0, "position");
		super.bindAtrib(1, "texturecoords");
	}

	@Override
	protected void getAllUniforms() {
		rotxloc=super.GetUniFormL("rotx");
		rotyloc=super.GetUniFormL("roty");
		rotzloc=super.GetUniFormL("rotz");
		scaleloc=super.GetUniFormL("scale");
		projmatloc=super.GetUniFormL("projmat");
		veiwposloc=super.GetUniFormL("veiwpos");
		veiwrotxloc=super.GetUniFormL("viewrotx");
		veiwrotyloc=super.GetUniFormL("viewroty");
		posloc=super.GetUniFormL("pos");
	}
	
	public void loadScaleandpos(Matrix4f scale,Vector3f pos){
		super.loadmatrix(scaleloc, scale);
		super.loadVector3f(posloc, pos);
	}
	
	public void loadprojmat(Matrix4f mat){
		super.loadmatrix(projmatloc, mat);
	}
	
	public void loadrotation(Matrix4f rotx,Matrix4f roty,Matrix4f rotz){
		super.loadmatrix(rotxloc, rotx);
		super.loadmatrix(rotyloc, roty);
		super.loadmatrix(rotzloc, rotz);
	}
	
	public void loadveiwrot(Matrix4f matx,Matrix4f maty){
		super.loadmatrix(veiwrotxloc, matx);
		super.loadmatrix(veiwrotyloc, maty);
	}
	
	public void loadveiw(Vector3f pos){
		super.loadVector3f(veiwposloc, pos);
	}

}
