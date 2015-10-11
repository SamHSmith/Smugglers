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

import java.io.IOException;
import java.util.ArrayList;

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
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALContext;
import org.lwjgl.opengl.GLContext;

import com.sun.media.sound.AlawCodec;

import shaders.EntityShader;
import shaders.GUIshader;
import sound.Listener;
import sound.Sound;
import sound.Source;
import textures.ModelTexture;
import toolbox.Maths;
import entity.BasicEntity;
import entity.GUI;
import entity.Light;
import entity.PhiEntity;

public class MainLoop {

	public static final float FOV = 70;
	public static final float NEARPLANE = 0.1f;
	public static final float FARPLANE = 1000;
	public static final float SENSITYVITY = 30;
	public static final float SPACE_JUMP = 0.05f;
	private GLFWErrorCallback errorCallback = Callbacks
			.errorCallbackPrint(System.err);
	GLFWKeyCallback kc;
	ALContext al;
	long window;
	ModelLoader loader;
	EntityShader shader;
	GUIshader guishader;
	Renderer ren;
	ArrayList<BasicEntity> entitys;
	ArrayList<GUI> guis;
	public float viewrotx = 0;
	public float viewroty = 0;
	ArrayList<Light> lights;
	public Vector3f viewpos = new Vector3f();
	public boolean keyw;
	public boolean keys;
	public boolean keya;
	public boolean keyd;
	public boolean keyspace;
	public float viewrotz;
	public static boolean mousedis = true;
	public static int mousedistimer = 0;
	Sound sound;
	Source forest;
	Listener listen;

	/**
	 * This class is the main class that uses all the other classes and maneges
	 * of the inputs suchas window mouse or keybored
	 */

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
		al = ALContext.create();
		al.makeCurrent();
		glfwMakeContextCurrent(window);
		GLContext.createFromCurrent();
		glfwSwapInterval(1);

	}

	private void init() {

		loader = new ModelLoader();
		shader = new EntityShader();
		guishader = new GUIshader();
		ren = new Renderer(shader, guishader, this);
		lights = new ArrayList<Light>();
		entitys = new ArrayList<BasicEntity>();
		guis = new ArrayList<GUI>();

		RawModel model = ObjFileLoader.loadObjModel("Buss", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		Texturedmodel tmodel = new Texturedmodel(model, texture);

		entitys.add(new PhiEntity(new Vector3f(0, 0, 0), new Vector3f(0, 0,
				0.005f), new Vector3f(0.01f, 0.001f, 0), 0, 0, 0, 0.5f, tmodel));

		lights.add(new Light(new Vector3f(), new Vector3f(0, 0, 0), 0, 0, 0,
				0.1f, new Vector3f(0, 1, 1), tmodel));
		entitys.add(lights.get(0));

		lights.add(new Light(new Vector3f(-0.02f, -0.01f, 0.02f), new Vector3f(
				0, 0, 0), 0, 0, 0, 0.1f, new Vector3f(1, 1, 0), tmodel));
		entitys.add(lights.get(1));

		lights.add(new Light(new Vector3f(0.02f, 0.01f, 0.02f), new Vector3f(0,
				0, 0), 0, 0, 0, 0.1f, new Vector3f(1, 0, 0), tmodel,
				new Vector3f(1, 0.002f, 0.002f)));
		entitys.add(lights.get(2));

		model = ObjFileLoader.loadObjModel("Buss", loader);
		texture = new ModelTexture(loader.loadTexture("EagleTexture"));
		tmodel = new Texturedmodel(model, texture);

		guis.add(new GUI(new Vector3f(9f, -9f, 0), 0, 0, 0, 0.07f, tmodel, true));

		kc = new GLFWKeyCallback() {
			@Override
			public void invoke(long arg0, int key, int arg2, int action,
					int arg4) {
				if (action == GLFW.GLFW_PRESS) {
					ckeckkeys(key, true);
				}
				if (action == GLFW.GLFW_RELEASE) {
					ckeckkeys(key, false);
				}
			}
		};
		GLFW.glfwSetKeyCallback(window, kc);

		GLFWCursorPosCallback cpc = new GLFWCursorPosCallback() {

			@Override
			public void invoke(long arg0, double xpos, double ypos) {
				viewroty = (float) xpos * 180 / (WIDTH);
				viewrotx = -((float) ypos * -180 / (HEIGHT));

			}
		};

		GLFW.glfwSetCursorPosCallback(window, cpc);
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR,
				GLFW.GLFW_CURSOR_DISABLED);

		try {
			sound = new Sound("Forest.wav");
			forest = new Source(new Vector3f(), new Vector3f(), sound);
			listen = new Listener();
			listen.SetListenerValuse(viewpos, new Vector3f(), viewrotx,
					viewroty, viewrotz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

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
				ren.render(entitys, guis, lights);
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

	private void tick() {
		updatepositions();
		keyaction();
		listen.SetListenerValuse(viewpos, new Vector3f(), viewrotx, viewroty,
				viewrotz);

		if (mousedis) {
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR,
					GLFW.GLFW_CURSOR_DISABLED);
		} else {
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR,
					GLFW.GLFW_CURSOR_NORMAL);
		}
	}

	public void ckeckkeys(int key, boolean setto) {
		if (key == GLFW.GLFW_KEY_W) {
			keyw = setto;
		}
		if (key == GLFW.GLFW_KEY_S) {
			keys = setto;
		}

		if (key == GLFW.GLFW_KEY_A) {
			keya = setto;
		}
		if (key == GLFW.GLFW_KEY_D) {
			keyd = setto;
		}
		if (key == GLFW.GLFW_KEY_SPACE) {
			keyspace = setto;
		}
		if (key == GLFW.GLFW_KEY_ESCAPE) {
			close();
			System.exit(0);
		}
		if (key == GLFW.GLFW_KEY_M) {
			if (mousedis && mousedistimer >= 30) {
				mousedis = false;
				mousedistimer = 0;
			} else if (mousedistimer >= 30) {
				mousedis = true;
				mousedistimer = 0;
			}
		}

	}

	public void keyaction() {
		Vector3f dist;
		if (keyw) {
			dist = new Vector3f(0, 0, -0.2f);
			Vector3f vec = Maths.angleMove(Maths.flippedcreaterotmat(
					(float) Math.toRadians(-viewrotx),
					(float) Math.toRadians(-viewroty),
					(float) Math.toRadians(viewrotz)), dist);
			viewpos.x += vec.x;
			viewpos.y += vec.y;
			viewpos.z += vec.z;
		}
		if (keys) {
			dist = new Vector3f(0, 0, 0.02f);
			Vector3f vec = Maths.angleMove(Maths.flippedcreaterotmat(
					(float) Math.toRadians(-viewrotx),
					(float) Math.toRadians(-viewroty),
					(float) Math.toRadians(viewrotz)), dist);
			viewpos.x += vec.x;
			viewpos.y += vec.y;
			viewpos.z += vec.z;
		}

		if (keya) {
			dist = new Vector3f(-0.02f, 0, 0);
			Vector3f vec = Maths.angleMove(Maths.flippedcreaterotmat(
					(float) Math.toRadians(-viewrotx),
					(float) Math.toRadians(-viewroty),
					(float) Math.toRadians(viewrotz)), dist);
			viewpos.x += vec.x;
			viewpos.y += vec.y;
			viewpos.z += vec.z;
		}
		if (keyd) {
			dist = new Vector3f(0.02f, 0, 0);
			Vector3f vec = Maths.angleMove(Maths.flippedcreaterotmat(
					(float) Math.toRadians(-viewrotx),
					(float) Math.toRadians(-viewroty),
					(float) Math.toRadians(viewrotz)), dist);
			viewpos.x += vec.x;
			viewpos.y += vec.y;
			viewpos.z += vec.z;
		}
		if (keyspace) {
			dist = new Vector3f(0, SPACE_JUMP, 0);
			Vector3f vec = Maths.angleMove(Maths.flippedcreaterotmat(
					(float) Math.toRadians(-viewrotx),
					(float) Math.toRadians(-viewroty),
					(float) Math.toRadians(viewrotz)), dist);
			viewpos.x += vec.x;
			viewpos.y += vec.y;
			viewpos.z += vec.z;
		}
	}

	private void updatepositions() {
		for (BasicEntity ent : entitys) {
			ent.getPosition().add(ent.getVelocity());
			ent.rotate(ent.getRotVelocity().x, ent.getRotVelocity().y,
					ent.getRotVelocity().z);
		}
	}

	private void close() {
		loader.cleanup();
		shader.cleanup();
		guishader.cleanup();
		AL.destroy(al);
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	public static void main(String args[]) {
		new MainLoop();
	}

}
