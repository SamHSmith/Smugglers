package shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import entity.Light;


public class EntityShader extends ShaderProgram {
	
	private static final String VERTEXFILE ="src/shaders/vertexshader.txt";
	private static final String FRAGFILE="src/shaders/fragmentshader.txt";
	
	private int projmatloc;
	private int transmatloc;
	private int viewmatloc;
	private int rotmatloc;
	private int offsetloc;
	private int lightposloc;
	private int lightcolorloc;
	private int viewposmatloc;
	public EntityShader() {
		super(VERTEXFILE, FRAGFILE);
	}

	@Override
	protected void bindAtributes() {
		super.bindAtrib(0, "position");
		super.bindAtrib(1, "texturecoords");
		super.bindAtrib(2, "normal");
	}

	@Override
	protected void getAllUniforms() {
		transmatloc=super.GetUniFormL("transmat");
		viewposmatloc=super.GetUniFormL("viewposmat");
		viewmatloc=super.GetUniFormL("viewrotmat");
		rotmatloc=super.GetUniFormL("rotmat");
		projmatloc=super.GetUniFormL("projmat");
		offsetloc=super.GetUniFormL("offset");
		lightcolorloc=super.GetUniFormL("lightcolor");
		lightposloc=super.GetUniFormL("lightpos");
	}
	
	public void loadprojmat(Matrix4f mat){
		super.loadmatrix(projmatloc, mat);
	}
	
	public void loadrotmat(Matrix4f viewmat){
		super.loadmatrix(rotmatloc, viewmat);
	}
	public void loadviewmat(Matrix4f viewmat){
		super.loadmatrix(viewposmatloc, viewmat);
	}
	
	public void loadviewrotmat(Matrix4f viewmat){
		super.loadmatrix(viewmatloc, viewmat);
	}
	
	public void loadTransmat(Matrix4f transmat){
		super.loadmatrix(transmatloc, transmat);
	}
	public void loadOffset(Vector3f offset){
		super.loadVector3f(offsetloc, offset);
	}
	public void loadlight(Light light){
		super.loadVector3f(lightposloc, light.getPosition());
		super.loadVector3f(lightcolorloc, light.getColor());
	}

}
