package render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.util.ArrayList;

import entity.BasicEntity;
import entity.Light;
import entity.Partical;
import entity.Warp;
import gui.GUI;
import input.MainLoop;
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
import render.shaders.ParticalShader;
import render.shaders.ShaderProgram;
import toolbox.Maths;
import controler.GameState;
import controler.UniverseHandler;

public abstract class Renderer {

	protected EntityShader shader;
	protected ParticalShader pshader;
	private int fbo;
	private Matrix4f projmat;
	protected GUIshader guishader;
	private UniverseHandler unihand;
	public static final int shadow_Map_Width_And_Height = 512;

	protected Renderer(MainLoop loop, UniverseHandler unihand) {
		super();
		this.shader = new EntityShader();
		this.pshader = new ParticalShader();
		this.guishader = new GUIshader();
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

	public abstract void render(ArrayList<BasicEntity> ents,
			ArrayList<GUI> gUIs, ArrayList<Light> lights, Camera cam);

	protected int createRenderBuffer() {
		int rb = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rb);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER,
				GL30.GL_DEPTH_COMPONENT32F, shadow_Map_Width_And_Height,
				shadow_Map_Width_And_Height);
		return rb;
	}

	protected void clearBuffers() {
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClearDepth(1);
	}

	protected int bindNewDrawingTexture() {
		int texture;

		texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
				shadow_Map_Width_And_Height, shadow_Map_Width_And_Height, 0,
				GL11.GL_RGBA, GL11.GL_FLOAT, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return texture;
	}

	protected void bindBuffers(int texture, int renderBuffer, int fbo) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo);
		GL32.glFramebufferTexture(GL30.GL_DRAW_FRAMEBUFFER,
				GL30.GL_COLOR_ATTACHMENT0, texture, 0);
		GL30.glFramebufferRenderbuffer(GL30.GL_DRAW_FRAMEBUFFER,
				GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RENDERBUFFER, renderBuffer);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	protected void unbindBuffers() {
		GL32.glFramebufferTexture(GL30.GL_DRAW_FRAMEBUFFER,
				GL30.GL_COLOR_ATTACHMENT0, 0, 0);
		GL30.glFramebufferRenderbuffer(GL30.GL_DRAW_FRAMEBUFFER,
				GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RENDERBUFFER, 0);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
	}

	protected void deleteTextureAndRenderBuffer(int texture, int renderbuffer) {
		GL30.glDeleteRenderbuffers(renderbuffer);
		GL11.glDeleteTextures(texture);
	}

	protected void loadEntity(BasicEntity ent, Camera cam,
			ArrayList<Light> lights) {
		shader.start();

		shader.loadprojmat(projmat);

		shader.loadtransform(Maths.createtransmat(ent.getPosition(),
				ent.getScale(), (float) Math.toRadians(ent.getRx()),
				(float) Math.toRadians(ent.getRy()),
				(float) Math.toRadians(ent.getRz())));

		shader.loadviewmat(Maths.createtransmat(cam.getPosition(), 1,
				(float) Math.toRadians(-cam.getRx()),
				(float) Math.toRadians(-cam.getRy()),
				(float) Math.toRadians(cam.getRz())).invert());

		shader.loadlights(lights);

		shader.stop();
	}

	protected void loadPartical(Camera cam, Partical p) {
		pshader.start();

		Matrix4f m = new Matrix4f();

		m.translate(p.getPosition());

		Matrix4f vm = new Matrix4f();
		
		vm.rotateX((float) Math.toRadians(cam.getRx()));
		vm.rotateY((float) Math.toRadians(cam.getRy()));
		vm.rotateZ((float) Math.toRadians(cam.getRz()));

		m.m00 = vm.m00;
		m.m01 = vm.m10;
		m.m02 = vm.m20;
		m.m10 = vm.m01;
		m.m11 = vm.m11;
		m.m12 = vm.m21;
		m.m20 = vm.m02;
		m.m21 = vm.m12;
		m.m22 = vm.m22;

		Matrix4f mvm = new Matrix4f();
		
		Matrix4f rm = new Matrix4f();

		vm.mul(m, mvm);

		rm.rotateZ((float) Math.toRadians(p.getRz()));

		rm.scale(p.getScale());

		pshader.loadmodelviewmat(mvm);
		pshader.loadrotmat(rm);
		
		pshader.stop();
	}

	protected void renderGUI(GUI gui) {
		gui.prepare();
		if (gui.getShowstate() == unihand.getState()
				|| gui.getShowstate() == GameState.All) {

			if (gui.getModel() != null) {

				RawModel rawmodel = gui.getModel().getModel();
				Texturedmodel model = gui.getModel();
				guishader.start();

				guishader.loadmatices(Maths.createtransmat(gui.getPosition(),
						gui.getWidth(), gui.getHeight(), gui.getRx(),
						gui.getRy(), gui.getRz()));

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

	protected void renderPartical(Partical p, ParticalShader shader,
			RawModel model) {
		
		GL11.glEnable(GL11.GL_BLEND);

		int texture = p.getTexture().getId();
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, p.getTexture().getBlendfunc());
		
		shader.start();
		
		shader.loadAtlasData(p.getTexture().getNumberofrows(), p.getBlend(), p.getTextoffset1(), p.getTextoffset2());
		
		GL30.glBindVertexArray(model.getVaoid());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, model.getVertexCount());

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

		shader.stop();
		
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected void renderEntity(BasicEntity ent, ShaderProgram shader) {

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

		pshader.start();
		pshader.loadprojmat(projmat);
		pshader.stop();

		GL11.glViewport(0, 0, width, height);

	}

	public EntityShader getShader() {
		return shader;
	}

	protected void cleanup() {
		shader.cleanup();
		guishader.cleanup();
		GL30.glDeleteFramebuffers(fbo);
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
