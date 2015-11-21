package shaders;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import entity.Light;
import entity.Warp;

public class EntityShader extends ShaderProgram {

	private static final String VERTEXFILE = "src/shaders/vertexshader.txt";
	private static final String FRAGFILE = "src/shaders/fragmentshader.txt";

	private static final int MAX_LIGHTS = 4;

	private int projmatloc;
	private int warpingRangeloc;
	private int warpingStrengthloc;
	private int transmatloc;
	private int viewmatloc;
	private int rotmatloc;
	private int warpingpointloc;
	private int lightposloc[];
	private int lightcolorloc[];
	private int attenuationloc[];
	private int viewposmatloc;
	private int shadprojmatloc;
	private int shadowmaploc;
	private int shadviewloc;

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
		transmatloc = super.GetUniFormL("transmat");
		viewposmatloc = super.GetUniFormL("viewposmat");
		viewmatloc = super.GetUniFormL("viewrotmat");
		rotmatloc = super.GetUniFormL("rotmat");
		projmatloc = super.GetUniFormL("projmat");
		warpingpointloc = super.GetUniFormL("warpingpoint");
		warpingRangeloc = super.GetUniFormL("warpingRange");
		warpingStrengthloc = super.GetUniFormL("warpingStrength");
		shadprojmatloc = super.GetUniFormL("shadprojmat");
		shadowmaploc = super.GetUniFormL("shadowmap");
		shadviewloc=super.GetUniFormL("shadview");

		lightcolorloc = new int[MAX_LIGHTS];
		lightposloc = new int[MAX_LIGHTS];
		attenuationloc = new int[MAX_LIGHTS];

		for (int i = 0; i < MAX_LIGHTS; i++) {
			lightposloc[i] = super.GetUniFormL("lightpos[" + i + "]");
			lightcolorloc[i] = super.GetUniFormL("lightcolor[" + i + "]");
			attenuationloc[i] = super.GetUniFormL("attenuation[" + i + "]");
		}
	}

	public void loadshadowmap(int i) {
		super.loadInt(shadowmaploc, i);
		
		start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, i);
		stop();
	}
	
	public void loadshadprojmat(Matrix4f mat) {
		super.loadmatrix(shadprojmatloc, mat);
	}

	public void loadprojmat(Matrix4f mat) {
		super.loadmatrix(projmatloc, mat);
	}

	public void loadrotmat(Matrix4f viewmat) {
		super.loadmatrix(rotmatloc, viewmat);
	}

	public void loadviewmat(Matrix4f viewmat) {
		super.loadmatrix(viewposmatloc, viewmat);
	}
	
	public void loadshadview(Matrix4f viewmat) {
		super.loadmatrix(shadviewloc, viewmat);
	}

	public void loadviewrotmat(Matrix4f viewmat) {
		super.loadmatrix(viewmatloc, viewmat);
	}

	public void loadTransmat(Matrix4f transmat) {
		super.loadmatrix(transmatloc, transmat);
	}

	public void loadWarp(Warp warp) {
		super.loadVector3f(warpingpointloc, warp.getPosition());
		super.loadFloat(warpingRangeloc, warp.getRange());
		super.loadFloat(warpingStrengthloc, warp.getStrength());
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

}
