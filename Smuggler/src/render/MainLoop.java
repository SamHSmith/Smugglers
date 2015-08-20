package render;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import models.RawModel;
import models.Texturedmodel;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;
import entety.Object;
import entety.PhiObject;

public class MainLoop {

	private static final float FOV = 70;
	private static final float NEARPLANE = 0.1f;
	private static final float FARPLANE = 1000;
	protected static final float SENSITYVITY = 30;
	private static final float SPACE_JUMP = 0.01f	;
	private GLFWErrorCallback errorCallback = Callbacks
			.errorCallbackPrint(System.err);
	long window;
	ModelLoader loader;
	StaticShader shader;
	Renderer ren;
	Texturedmodel tmodel;
	Object ent;
	Camera Cam;
	private Matrix4f projmat;
	float viewrotx=0;
	float viewroty=0;
	public Vector3f viewpos = new Vector3f();
	public Matrix4f viewrotationx = new Matrix4f();
	public Matrix4f viewrotationy = new Matrix4f();
	public boolean keyw;
	public boolean keys;
	public boolean keya;
	public boolean keyd;
	public boolean keyspace;
	
	

	public static int width = 1280/2;
	public static int height = 720/2;

	public MainLoop() {
		createDisplay();
		init();
		loop();
		close();
	}

	private void createDisplay() {

		glfwSetErrorCallback(errorCallback);

		if (glfwInit() != GL_TRUE) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		window = glfwCreateWindow(width, height, "Smuggler", NULL, NULL);

		if (window == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwMakeContextCurrent(window);
		GLContext.createFromCurrent();
		glfwSwapInterval(1);

	}


	private void init() {
		
		loader = new ModelLoader();
		shader = new StaticShader();
		ren = new Renderer(shader);
		
		createProj();
		shader.start();
		shader.loadprojmat(projmat);
		shader.stop();

		float[] vertices = { -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f,
				0f, 0.5f, 0.5f, 0f };

		int[] indeces = { 0, 1, 3, 3, 1, 2 };

		float[] texturecords = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.LoadToVAO(vertices, texturecords, indeces);
		ModelTexture texture = new ModelTexture(loader.loadTexture("Colorfull"));
		tmodel = new Texturedmodel(model, texture);
		ent = new PhiObject(new Vector3f(0, 0, -1), new Vector3f(), 0, 0, 0, 1f, tmodel);
		
		GLFWKeyCallback kc = new GLFWKeyCallback() {
			
			@Override
			public void invoke(long arg0, int key, int arg2, int action, int arg4) {
				if(action == GLFW.GLFW_PRESS){
					ckeckkeys(key, true);
				}
				if(action == GLFW.GLFW_RELEASE){
					ckeckkeys(key, false);
				}
			}
		};
		GLFW.glfwSetKeyCallback(window, kc);
		
		GLFWCursorPosCallback cpc = new GLFWCursorPosCallback() {
			
			@Override
			public void invoke(long arg0, double xpos, double ypos) {
					viewrotx= (float)xpos * 180 /(width);
					viewroty= (float)ypos * -180 / (height);

					
					System.out.println("In invoke callback xpos "+xpos+" ypos "+ypos+ " viewrotx "+viewrotx+" viewroty "+viewroty+" Matrix: "+viewrotationx);
							
			}
		};
		
		GLFW.glfwSetCursorPosCallback(window, cpc);
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		
	}

	private void loop() {
		int fps = 0, updates = 0;
		long fpstimer = System.currentTimeMillis();
		double nsPerTick = 1000000000 / 60;

		double then = System.nanoTime();
		double unprocessed = 0;

		boolean shouldrender = false;

		while (glfwWindowShouldClose(window) != GL_TRUE) {

			double now = System.nanoTime();
			unprocessed += (now - then) / nsPerTick;
			then = now;

			while (unprocessed >= 1) {
				updates++;
				tick();
				unprocessed--;
				shouldrender = true;
			}

			if (shouldrender) {
				ren.renderEntity(ent);
				fps++;
				shouldrender = false;
			}

			glfwPollEvents();
			glfwSwapBuffers(window);

			// Fps Timer

			if (System.currentTimeMillis() - fpstimer > 1000) {
				System.out.println();
				System.out.println(updates + " :ticks fps: " + fps);
				System.out.println();
				updates = 0;
				fps = 0;
				fpstimer = System.currentTimeMillis();
			}

		}
	}

	private void createProj() {
		float aspect = width / height;
	    
	    projmat = new Matrix4f();
	    
	    Maths.setToProjection(projmat, NEARPLANE, FARPLANE, FOV, aspect);

	}
	

	private void tick(){
		shader.start();
		viewrotationy.rotX((float) Math.toRadians(-viewroty));
		viewrotationx.rotY((float) Math.toRadians(viewrotx));
		
		Matrix4f scale = new Matrix4f();
		Matrix4f rotx = new Matrix4f();
		Matrix4f roty = new Matrix4f();
		Matrix4f rotz = new Matrix4f();
		
		scale.setScale(ent.getScale());
		
		rotx.rotX(ent.getRx());
		roty.rotY(ent.getRy());
		rotz.rotZ(ent.getRz());
		
		
		shader.loadScaleandpos(scale, ent.getPosition());
		shader.loadrotation(rotx, roty, rotz);
		
		shader.loadveiw(viewpos);
		shader.loadveiwrot(viewrotationx, viewrotationy);
		shader.stop();
		
		keyaction();
		ent.rotate(0.02f, 0, 0);
	}
	
	public void ckeckkeys(int key, boolean setto){
		if(key == GLFW.GLFW_KEY_W){
			keyw = setto;
		}
		if(key == GLFW.GLFW_KEY_S){
			keys = setto;
		}
		
		if(key == GLFW.GLFW_KEY_A){
			keya = setto;
		}
		if(key == GLFW.GLFW_KEY_D){
			keyd = setto;
		}
		if(key == GLFW.GLFW_KEY_SPACE){
			keyspace = setto;
		}
				
	}
	
	public void keyaction(){
		if(keyw){
			viewpos.z-=(0.02f*Math.cos(Math.toRadians(viewrotx)));
			viewpos.x+=(0.02f*Math.sin(Math.toRadians(viewrotx)));
		}
		if(keys){
			viewpos.z+=(0.02f*Math.cos(Math.toRadians(viewrotx)));
			viewpos.x-=(0.02f*Math.sin(Math.toRadians(viewrotx)));
		}
		
		if(keya){
			viewpos.z-=(0.02f*Math.sin(Math.toRadians(viewrotx)));
			viewpos.x-=(0.02f*Math.cos(Math.toRadians(viewrotx)));
		}
		if(keyd){
			viewpos.z+=(0.02f*Math.sin(Math.toRadians(viewrotx)));
			viewpos.x+=(0.02f*Math.cos(Math.toRadians(viewrotx)));
		}
		if(keyspace){
			viewpos.y+=SPACE_JUMP;
		}
	}

	private void close() {
		loader.cleanup();
		shader.cleanup();
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	public static void main(String args[]) {
		new MainLoop();
	}

}
