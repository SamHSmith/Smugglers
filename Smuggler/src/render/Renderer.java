package render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.util.ArrayList;

import models.RawModel;
import models.Texturedmodel;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import shaders.EntityShader;
import shaders.GUIshader;
import toolbox.Maths;
import entity.BasicEntity;
import entity.GUI;
import entity.Light;

public class Renderer {

	private EntityShader shader;
	private MainLoop loop;
	private Matrix4f projmat;
	private GUIshader guishader;

	public Renderer(EntityShader shader, GUIshader guishader, MainLoop loop) {
		super();
		this.shader = shader;
		this.guishader = guishader;
		this.loop = loop;
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProj();

		shader.start();
		shader.loadprojmat(projmat);
		shader.stop();
	}

	public void render(ArrayList<BasicEntity> Objects, ArrayList<GUI> gUIs,
			ArrayList<Light> lights) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClearDepth(1);

		for (int i = 0; i < Objects.size(); i++) {
			BasicEntity current = Objects.get(i);

			shader.start();

			shader.loadTransmat(Maths.createtransmat(current.getPosition(),
					current.getScale()));

			shader.loadviewmat(Maths.createnegativetransmat(loop.viewpos));
			shader.loadrotmat(Maths.createrotmat(current.getRx(),
					current.getRy(), current.getRz()));
			shader.loadviewrotmat(Maths.createrotmat(
					(float) Math.toRadians(loop.viewrotx),
					(float) Math.toRadians(loop.viewroty), 0));
			shader.loadlights(lights);

			shader.stop();

			renderEntity(current);
		}

		for (GUI current : gUIs) {
			guishader.start();
			guishader.loadmatices(
					Maths.createtransmat(current.getPosition(),
							current.getScale()),
					Maths.createrotmat(current.getRx() + (float)Math.toRadians(loop.viewrotx),
							(float) (current.getRy() + Math.toRadians(loop.viewroty)), (float) (current.getRz()
									+ Math.toRadians(loop.viewrotz))));
			guishader.stop();

			renderGUI(current);
		}
	}

	private void renderGUI(GUI gui) {

		RawModel rawmodel = gui.getModel().getModel();
		Texturedmodel model = gui.getModel();
		guishader.start();

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
		guishader.stop();

	}

	private void renderEntity(BasicEntity ent) {

		RawModel rawmodel = ent.getModel().getModel();
		Texturedmodel model = ent.getModel();
		shader.start();
		GL30.glBindVertexArray(rawmodel.getVaoid());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTextID());
		GL11.glDrawElements(GL_TRIANGLES, rawmodel.getVertexCount(),
				GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		shader.stop();

	}

	private void createProj() {
		float aspect = MainLoop.WIDTH / MainLoop.HEIGHT;

		projmat = new Matrix4f();

		Maths.setToProjection(projmat, MainLoop.NEARPLANE, MainLoop.FARPLANE,
				MainLoop.FOV, aspect);

	}

}
