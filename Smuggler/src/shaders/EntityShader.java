package shaders;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import entity.Light;


public class EntityShader extends ShaderProgram {
	
	private static final String VERTEXFILE ="src/shaders/vertexshader.txt";
	private static final String FRAGFILE="src/shaders/fragmentshader.txt";
	
	private static final int MAX_LIGHTS=4;
	
	private int projmatloc;
	private int warpingRangeloc;
	private int transmatloc;
	private int viewmatloc;
	private int rotmatloc;
	private int warpingpointloc;
	private int lightposloc[];
	private int lightcolorloc[];
	private int attenuationloc[];
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
		warpingpointloc=super.GetUniFormL("warpingpoint");
		warpingRangeloc=super.GetUniFormL("warpingRange");
		
		lightcolorloc=new int[MAX_LIGHTS];
		lightposloc=new int[MAX_LIGHTS];
		attenuationloc=new int[MAX_LIGHTS];
		
		for(int i=0;i<MAX_LIGHTS;i++){
			lightposloc[i] = super.GetUniFormL("lightpos["+i+"]");
			lightcolorloc[i] = super.GetUniFormL("lightcolor["+i+"]");
			attenuationloc[i] = super.GetUniFormL("attenuation["+i+"]");
		}
	}
	
	public void loadprojmat(Matrix4f mat){
		super.loadmatrix(projmatloc, mat);
	}
	
	public void loadwarpingRange(float warpingRange){
		super.loadFloat(warpingRangeloc, warpingRange);
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
	public void loadWarp(Vector3f offset){
		super.loadVector3f(warpingpointloc, offset);
	}
	public void loadlights(ArrayList<Light> lights){
		
		for(int i=0;i<MAX_LIGHTS;i++){
			if(i<lights.size()){
				super.loadVector3f(lightposloc[i], lights.get(i).getPosition());
				super.loadVector3f(lightcolorloc[i], lights.get(i).getColor());
				super.loadVector3f(attenuationloc[i], lights.get(i).GetAttenuation());
			}else{
				super.loadVector3f(lightposloc[i], new Vector3f(0,0,0));
				super.loadVector3f(lightcolorloc[i], new Vector3f(0,0,0));
				super.loadVector3f(attenuationloc[i], new Vector3f(1,0,0));
			}
		}
	}

}
