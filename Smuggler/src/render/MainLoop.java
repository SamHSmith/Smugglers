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
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import models.RawModel;
import models.Texturedmodel;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GLContext;

import shaders.StaticShader;
import textures.ModelTexture;
import entety.Object;
import entety.PhiObject;

public class MainLoop {

	public static final float FOV = 70;
	public static final float NEARPLANE = 0.1f;
	public static final float FARPLANE = 1000;
	public static final float SENSITYVITY = 30;
	public static final float SPACE_JUMP = 0.03f;
	private GLFWErrorCallback errorCallback = Callbacks
			.errorCallbackPrint(System.err);
	long window;
	ModelLoader loader;
	StaticShader shader;
	Renderer ren;
	ArrayList<Object> Objects;
	float viewrotx=0;
	float viewroty=0;
	public Vector3f viewpos = new Vector3f();
	public Matrix4f viewrotationx = new Matrix4f();
	public Matrix4f viewrotationy = new Matrix4f();
	
	
	private boolean keyforward;
	private boolean keybackward;
	private boolean keyleft;
	private boolean keyright;
	private boolean keyupp;
	private boolean keydown;
	
	private int keyFORWARD=GLFW.GLFW_KEY_W;
	private int keyBACKWARD=GLFW.GLFW_KEY_S;
	private int keyLEFT=GLFW.GLFW_KEY_A;
	private int keyRIGHT=GLFW.GLFW_KEY_D;
	private int keyUPP=GLFW.GLFW_KEY_SPACE;
	private int keyDOWN=GLFW.GLFW_KEY_LEFT_SHIFT;
	
	
	
	

	public static int WIDTH = 1280/2;
	public static int HEIGHT = 720/2;

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

		window = glfwCreateWindow(WIDTH, HEIGHT, "Smuggler", NULL, NULL);

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
		ren = new Renderer(shader, this);
		

		float[] vertices = { -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f,
				0f, 0.5f, 0.5f, 0f };

		int[] indeces = { 0, 1, 3, 3, 1, 2 };

		float[] texturecords = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.LoadToVAO(vertices, texturecords, indeces);
		ModelTexture texture = new ModelTexture(loader.loadTexture("Colorfull"));
		Texturedmodel tmodel = new Texturedmodel(model, texture);
		Objects=new ArrayList<Object>();
		Object ent = new PhiObject(new Vector3f(0, 0, -1), new Vector3f(), 0, 0, 0, 1f, tmodel, true);
		Objects.add(ent);
		
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
					viewrotx= (float)xpos * 180 /(WIDTH);
					viewroty= (float)ypos * -180 / (HEIGHT);

					
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
				ren.render(Objects);
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

	private void tick(){
		viewrotationy.rotX((float) Math.toRadians(-viewroty));
		viewrotationx.rotY((float) Math.toRadians(viewrotx));
		
		
		keyaction();
	}
	
	public void ckeckkeys(int key, boolean setto){
		if(key == keyFORWARD){
			keyforward = setto;
		}
		if(key == keyBACKWARD){
			keybackward = setto;
		}
		
		if(key == keyLEFT){
			keyleft = setto;
		}
		if(key == keyRIGHT){
			keyright = setto;
		}
		if(key == keyUPP){
			keyupp = setto;
		}
		if(key == keyDOWN){
			keydown = setto;
		}
				
	}
	
	public void keyaction(){
		if(keyforward){
			viewpos.z-=(0.02f*Math.cos(Math.toRadians(viewrotx)));
			viewpos.x+=(0.02f*Math.sin(Math.toRadians(viewrotx)));
		}
		if(keybackward){
			viewpos.z+=(0.02f*Math.cos(Math.toRadians(viewrotx)));
			viewpos.x-=(0.02f*Math.sin(Math.toRadians(viewrotx)));
		}
		
		if(keyleft){
			viewpos.z-=(0.02f*Math.sin(Math.toRadians(viewrotx)));
			viewpos.x-=(0.02f*Math.cos(Math.toRadians(viewrotx)));
		}
		if(keyright){
			viewpos.z+=(0.02f*Math.sin(Math.toRadians(viewrotx)));
			viewpos.x+=(0.02f*Math.cos(Math.toRadians(viewrotx)));
		}
		if(keyupp){
			viewpos.y+=SPACE_JUMP;
		}
		if(keydown){
			viewpos.y-=SPACE_JUMP;
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
