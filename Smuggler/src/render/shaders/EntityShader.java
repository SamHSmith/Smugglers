package render.shaders;

import java.util.ArrayList;

import math3d.Matrix4f;
import math3d.Vector3f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import entity.Light;
import entity.Warp;

public class EntityShader extends ShaderProgram {

	private static final String VERTEXFILE = "vertexshader.txt";
	private static final String FRAGFILE = "fragmentshader.txt";

	private static final int MAX_LIGHTS = 4;

	private int projmatloc;
	private int lightposloc[];
	private int lightcolorloc[];
	private int attenuationloc[];
	private int transformloc;
	private int viewmatloc;

	public EntityShader() {
		super(EntityShader.class.getResourceAsStream(VERTEXFILE), EntityShader.class.getResourceAsStream(FRAGFILE));
	}

	@Override
	protected void bindAtributes() {
		super.bindAtrib(0, "position");
		super.bindAtrib(1, "texturecoords");
		super.bindAtrib(2, "normal");
	}

	@Override
	protected void getAllUniforms() {
		projmatloc = super.GetUniFormL("projmat");
		
		transformloc=super.GetUniFormL("transform");
		viewmatloc=super.GetUniFormL("viewmat");

		lightcolorloc = new int[MAX_LIGHTS];
		lightposloc = new int[MAX_LIGHTS];
		attenuationloc = new int[MAX_LIGHTS];

		for (int i = 0; i < MAX_LIGHTS; i++) {
			lightposloc[i] = super.GetUniFormL("lightpos[" + i + "]");
			lightcolorloc[i] = super.GetUniFormL("lightcolor[" + i + "]");
			attenuationloc[i] = super.GetUniFormL("attenuation[" + i + "]");
		}
	}

	public void loadprojmat(Matrix4f mat) {
		super.loadmatrix(projmatloc, mat);
	}
	
	public void loadtransform(Matrix4f mat) {
		super.loadmatrix(transformloc, mat);
	}
	public void loadviewmat(Matrix4f mat) {
		super.loadmatrix(viewmatloc, mat);
	}
	

	public void loadlights(ArrayList<Light> lights) {

		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector3f(lightposloc[i], lights.get(i).getPosition());
				super.loadVector3f(lightcolorloc[i], lights.get(i).getColor());
				super.loadVector3f(attenuationloc[i], lights.get(i)
						.GetAttenuation());
			} else {
				super.loadVector3f(lightposloc[i], new Vector3f(0, 0, 0));
				super.loadVector3f(lightcolorloc[i], new Vector3f(0, 0, 0));
				super.loadVector3f(attenuationloc[i], new Vector3f(1, 0, 0));
			}
		}
	}

	@Override
	protected void printErrorMessage(String message) {
		System.err.println("Entity Shader Failed: ");
		System.err.println(message);
	}

}
