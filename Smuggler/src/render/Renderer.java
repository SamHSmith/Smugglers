package render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.util.ArrayList;

import javax.vecmath.Matrix4f;

import models.RawModel;
import models.Texturedmodel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import shaders.StaticShader;
import toolbox.Maths;
import entety.Object;

public class Renderer {
	
	private StaticShader shader;
	private MainLoop loop;
	private Matrix4f projmat;

	public Renderer(StaticShader shader,MainLoop loop) {
		super();
		this.shader = shader;
		this.loop=loop;
		
		createProj();
		
		shader.start();
		shader.loadprojmat(projmat);
		shader.stop();
	}
	
	public void render(ArrayList<Object> Objects){
		for(Object current:Objects){
			
			shader.start();
			
			Matrix4f scale = new Matrix4f();
			Matrix4f rotx = new Matrix4f();
			Matrix4f roty = new Matrix4f();
			Matrix4f rotz = new Matrix4f();
			
			scale.setScale(current.getScale());
			
			rotx.rotX(current.getRx());
			roty.rotY(current.getRy());
			rotz.rotZ(current.getRz());
			
			
			shader.loadScaleandpos(scale, current.getPosition());
			shader.loadrotation(rotx, roty, rotz);
			
			shader.loadveiw(loop.viewpos);
			shader.loadveiwrot(loop.viewrotationx, loop.viewrotationy);
			shader.stop();
			
			
			renderEntity(current);
		}
	}

	private void renderEntity(Object ent) {

		GL11.glClear(GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0, 0.5f, 0.5f, 1);

		RawModel rawmodel = ent.getModel().getModel();
		Texturedmodel model = ent.getModel();

		shader.start();
		GL30.glBindVertexArray(rawmodel.getVaoid());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTextID());
		GL11.glDrawElements(GL_TRIANGLES, rawmodel.getVertexCount(),
				GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		shader.stop();

	}
	
	private void createProj() {
		float aspect = MainLoop.WIDTH / MainLoop.HEIGHT;
	    
	    projmat = new Matrix4f();
	    
	    Maths.setToProjection(projmat, MainLoop.NEARPLANE, MainLoop.FARPLANE, MainLoop.FOV, aspect);

	}

}
