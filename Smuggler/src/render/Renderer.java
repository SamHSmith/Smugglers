package render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import fontRendering.TextMaster;
import gui.GUI;
import input.MainLoop;

import java.util.ArrayList;

import math3d.Matrix4f;
import models.RawModel;
import models.Texturedmodel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import render.shaders.EntityShader;
import render.shaders.GUIshader;
import render.shaders.ShaderProgram;
import toolbox.Maths;
import controler.GameState;
import controler.UniverseHandler;
import entity.BasicEntity;
import entity.Light;
import entity.Warp;

public class Renderer {

	private EntityShader shader;
	private int fbo;
	private MainLoop loop;
	private Matrix4f projmat;
	private GUIshader guishader;
	private UniverseHandler unihand;
	public static final int shadow_Map_Width_And_Height = 512;

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
		createProj(MainLoop.WIDTH, MainLoop.HEIGHT);

		fbo = GL30.glGenFramebuffers();

		shader.start();
		shader.loadprojmat(projmat);
		shader.stop();
	}

	public void render(ArrayList<BasicEntity> ents, ArrayList<GUI> gUIs,
			ArrayList<Light> lights, Warp warp) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClearDepth(1);

		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo);

		int fb = 0;

		fb = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, fb);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER,
				GL30.GL_DEPTH_COMPONENT32F, shadow_Map_Width_And_Height,
				shadow_Map_Width_And_Height);
		GL30.glFramebufferRenderbuffer(GL30.GL_DRAW_FRAMEBUFFER,
				GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, fb);

		int inGame;

		inGame = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, inGame);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
				shadow_Map_Width_And_Height, shadow_Map_Width_And_Height, 0,
				GL11.GL_RGBA, GL11.GL_FLOAT, 0);

		GL32.glFramebufferTexture(GL30.GL_DRAW_FRAMEBUFFER,
				GL30.GL_DEPTH_ATTACHMENT, inGame, 0);

		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);

		createProj(MainLoop.WIDTH, MainLoop.HEIGHT);

		for (int i = 0; i < ents.size(); i++) {
			BasicEntity current = ents.get(i);
			if (current.getModel() != null) {

				shader.start();

				shader.loadTransmat(Maths.createtransmat(current.getPosition(),
						current.getScale()));

				shader.loadviewmat(Maths.createnegativetransmat(loop.cam.getPosition()));
				shader.loadrotmat(Maths.createrotmat(current.getRx(),
						current.getRy(), current.getRz()));
				shader.loadviewrotmat(Maths.createrotmat(
						(float) Math.toRadians(loop.cam.getRx()),
						(float) Math.toRadians(loop.cam.getRy()),
						(float) Math.toRadians(loop.cam.getRz())));
				shader.loadlights(lights);

				shader.loadWarp(warp);

				shader.stop();

				renderEntity(current, shader);
			}
		}

		GL11.glEnable(GL11.GL_BLEND);

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

			if (gui.getModel() != null) {

				RawModel rawmodel = gui.getModel().getModel();
				Texturedmodel model = gui.getModel();
				guishader.start();

				guishader.loadmatices(Maths.createtransmat(gui.getPosition(),
						gui.getWidth(), gui.getHeight()), Maths.createrotmat(
						gui.getRx(), gui.getRy(), gui.getRz()));

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

			for (GUI currgui : gui.getSubGUIs()) {
				renderGUI(currgui);
			}
			gui.endrendering();
		}
	}

	private void renderEntity(BasicEntity ent, ShaderProgram shader) {
		
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

	public void createProj(int width, int height) {
		float aspect = width / height;

		projmat = new Matrix4f();

		Maths.setToProjection(projmat, MainLoop.NEARPLANE, MainLoop.FARPLANE,
				MainLoop.FOV, aspect);

		shader.start();
		shader.loadprojmat(projmat);
		shader.stop();

		GL11.glViewport(0, 0, width, height);

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
