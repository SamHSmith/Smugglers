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




import java.util.Random;

import loading.ModelLoader;
import loading.ObjFileLoader;
import models.RawModel;
import models.Texturedmodel;

import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GLContext;

import shaders.StaticShader;
import textures.ModelTexture;
import entity.BasicEntity;
import entity.Light;
import entity.PhiEntity;

public class MainLoop {

	public static final float FOV = 70;
	public static final float NEARPLANE = 0.1f;
	public static final float FARPLANE = 1000;
	public static final float SENSITYVITY = 30;
	public static final float SPACE_JUMP = 0.01f;
	private GLFWErrorCallback errorCallback = Callbacks
			.errorCallbackPrint(System.err);
	long window;
	ModelLoader loader;
	StaticShader shader;
	Renderer ren;
	ArrayList<BasicEntity> entitys;
	public float viewrotx=0;
	public float viewroty=0;
	Light light;
	public Vector3f viewpos = new Vector3f();
	public boolean keyw;
	public boolean keys;
	public boolean keya;
	public boolean keyd;
	public boolean keyspace;
	
	

	public static int WIDTH = 1280;
	public static int HEIGHT = 720;

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

		RawModel model = ObjFileLoader.loadObjModel("Sculp", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		Texturedmodel tmodel = new Texturedmodel(model, texture);
		entitys=new ArrayList<BasicEntity>();
		
		light=new Light(new Vector3f(), new Vector3f(0,0,-5f), false, 0, 0, 0, 1, new Vector3f(1,1,1));
		entitys.add(new PhiEntity(new Vector3f(0,0,-5), new Vector3f(), 0f, 0f, 0f, 1f, tmodel, false));
		
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
				ren.render(entitys, light);
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
		light.move(0, 0.02f, 0);
		entitys.get(0).rotate(0.02f, 0.02f, 0.02f);
		keyaction();
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
		if(key == GLFW.GLFW_KEY_ESCAPE){
			close();
			System.exit(0);
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
