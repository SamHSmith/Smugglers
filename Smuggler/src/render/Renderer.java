package render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import fontRendering.TextMaster;
import gui.GUI;
import input.MainLoop;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import models.RawModel;
import models.Texturedmodel;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;

import controler.GameState;
import controler.UniverseHandler;
import shaders.EntityShader;
import shaders.GUIshader;
import toolbox.Maths;
import entity.BasicEntity;
import entity.Light;
import entity.Warp;

public class Renderer {

	private EntityShader shader;
	private MainLoop loop;
	private Matrix4f projmat;
	private GUIshader guishader;
	private UniverseHandler unihand;

	public Renderer(MainLoop loop, UniverseHandler unihand) {
		super();
		this.shader = new EntityShader();
		this.guishader = new GUIshader();
		this.loop = loop;
		this.unihand = unihand;
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glCullFace(GL11.GL_BACK);
		createProj();

		shader.start();
		shader.loadprojmat(projmat);
		shader.stop();
	}

	public void render(ArrayList<BasicEntity> Objects, ArrayList<GUI> gUIs,
			ArrayList<Light> lights, Warp warp) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
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
					(float) Math.toRadians(loop.cameray),
					(float) Math.toRadians(loop.camerax), 0));
			shader.loadlights(lights);

			shader.loadWarp(warp);

			shader.stop();

			renderEntity(current);
		}

		GL11.glDisable(GL11.GL_DEPTH_TEST);

		for (GUI current : gUIs) {
			renderGUI(current);
		}

		TextMaster.render();
	}

	private void renderGUI(GUI gui) {
		gui.prepare();
		if (gui.getShowstate() == unihand.getState()
				|| gui.getShowstate() == GameState.All) {

			RawModel rawmodel = gui.getModel().getModel();
			Texturedmodel model = gui.getModel();
			guishader.start();

			guishader.loadmatices(Maths.createtransmat(gui.getPosition(),
					gui.getWidth(), gui.getHeight()), Maths.createrotmat(
					gui.getRx(), (float) gui.getRy(), (float) gui.getRz()));

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

			for (GUI currgui : gui.getSubGUIs()) {
				renderGUI(currgui);
			}
			gui.endrendering();
		}
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

	public void createProj() {
		float aspect = loop.WIDTH / loop.HEIGHT;

		projmat = new Matrix4f();

		Maths.setToProjection(projmat, MainLoop.NEARPLANE, MainLoop.FARPLANE,
				MainLoop.FOV, aspect);

		shader.start();
		shader.loadprojmat(projmat);
		shader.stop();

		GL11.glViewport(0, 0, loop.WIDTH, loop.HEIGHT);

	}

	public EntityShader getShader() {
		return shader;
	}

	public void setShader(EntityShader shader) {
		this.shader = shader;
	}

	public GUIshader getGuishader() {
		return guishader;
	}

	public void setGuishader(GUIshader guishader) {
		this.guishader = guishader;
	}

}
