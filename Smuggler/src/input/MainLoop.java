package input;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;

import loading.ModelLoader;
import loading.ObjFileLoader;
import models.RawModel;
import models.Texturedmodel;
import net.Client;
import net.Server;

import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWgammaramp;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALContext;
import org.lwjgl.opengl.GLContext;

import controler.UniverseHandler;
import render.Renderer;
import shaders.EntityShader;
import shaders.GUIshader;
import sound.Listener;
import sound.Sound;
import sound.Source;
import textures.ModelTexture;
import toolbox.Maths;
import entity.BasicEntity;
import entity.Light;
import entity.PhiEntity;
import entity.Warp;
import fontRendering.TextMaster;
import gui.GUI;

public class MainLoop {

	public static final float FOV = 70;
	public static final float NEARPLANE = 0.01f;
	public static final float FARPLANE = 1000;
	GLFWCursorPosCallback cpc;
	private GLFWErrorCallback errorCallback = Callbacks
			.errorCallbackPrint(System.err);
	GLFWKeyCallback kc;
	GLFWWindowSizeCallback wsc;
	GLFWMouseButtonCallback mbc;
	GLFWCharCallback cc;
	ALContext al;
	long window;
	ModelLoader loader;
	EntityShader shader;
	GUIshader guishader;
	Renderer ren;
	Warp warp;
	ArrayList<BasicEntity> entitys;
	ArrayList<GUI> guis;
	public float cameray = 0;
	public float camerax = 0;
	ArrayList<Light> lights;
	public Vector3f viewpos = new Vector3f(0,0,0);
	public static ArrayList<Key> keys;
	public float cameraz;
	public static boolean mousedis = false;
	public static int mousedistimer = 0;
	Listener listen;
	private UniverseHandler unihand;
	public Mouse mouse;
	private static Typestream ts;

	/**
	 * This class is the main class that uses all the other classes and maneges
	 * of the inputs suchas window mouse or keybored
	 */

	public static int WIDTH = 1280;
	public static int HEIGHT = 720;

	public MainLoop(UniverseHandler unihand) {
		this.unihand = unihand;
	}

	public void start() {
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
		// Make the context current
		al.makeCurrent();
		ALCapabilities capabilities = al.getCapabilities();

		AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);

		if (!capabilities.OpenAL10)
			throw new RuntimeException("OpenAL Context Creation failed");

		glfwMakeContextCurrent(window);
		GLContext.createFromCurrent();
		glfwSwapInterval(1);

	}

	private void init() {
		guishader = new GUIshader();
		ren = new Renderer(this, unihand);
		keys = new ArrayList<Key>();
		listen = new Listener();
		loader = new ModelLoader();
		unihand.loader = loader;
		mouse = new Mouse(false, false, false);

		for (int i = 0; i < 1000; i++) {
			keys.add(Key.False);
		}

		kc = new GLFWKeyCallback() {
			@Override
			public void invoke(long arg0, int key, int arg2, int action,
					int arg4) {
				if (action == GLFW.GLFW_PRESS) {
					if (key == GLFW.GLFW_KEY_BACKSPACE) {
						if (ts != null) {
							ts.backspace();
						}
					}
					if (key == GLFW.GLFW_KEY_ENTER) {
						if (ts != null) {
							ts.enter();
						}
					}
					
					ckeckkeys(key, true);
				}
				if (action == GLFW.GLFW_RELEASE) {
					ckeckkeys(key, false);
				}
			}
		};

		cpc = new GLFWCursorPosCallback() {

			@Override
			public void invoke(long arg0, double xpos, double ypos) {
				camerax = (float) xpos * 180 / (WIDTH);
				cameray = -((float) ypos * -180 / (HEIGHT));

				mouse.x = (float) ((xpos * 2 / (WIDTH)) - 1);
				mouse.y = (float) -((ypos * 2 / (HEIGHT)) - 1);

			}
		};

		wsc = new GLFWWindowSizeCallback() {

			@Override
			public void invoke(long window, int nwidth, int nheight) {
				WIDTH = nwidth;
				HEIGHT = nheight;
				ren.createProj(nwidth, nheight);
				glfwMakeContextCurrent(window);
				GLContext.createFromCurrent();
				glfwSwapInterval(1);
			}
		};

		mbc = new GLFWMouseButtonCallback() {

			@Override
			public void invoke(long window, int button, int action, int mods) {
				if (action == GLFW.GLFW_PRESS) {
					click(button, true);
				} else {
					click(button, false);
				}
			}
		};

		cc = new GLFWCharCallback() {

			@Override
			public void invoke(long arg0, int charecter) {
				String s = Character.toString((char) charecter);
				if (ts != null) {
					ts.type(s);
				}
			}
		};
		
		GLFW.glfwSetMouseButtonCallback(window, mbc);
		GLFW.glfwSetWindowSizeCallback(window, wsc);
		GLFW.glfwSetKeyCallback(window, kc);
		GLFW.glfwSetCursorPosCallback(window, cpc);
		GLFW.glfwSetCharCallback(window, cc);
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR,
				GLFW.GLFW_CURSOR_DISABLED);
		unihand.init();

		listen = new Listener();
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
				ren.render(unihand.entitys, unihand.guis, unihand.lights,
						unihand.warp);
				fps++;
				shouldrender = false;
			}

			glfwPollEvents();
			glfwSwapBuffers(window);

			// Fps Timer

			if (System.currentTimeMillis() - fpstimer > 10000) {
				System.out.println();
				System.out.println(updates/10 + " :ticks fps: " + fps/10);
				System.out.println();
				updates = 0;
				fps = 0;
				fpstimer = System.currentTimeMillis();

			}

		}
	}

	private void tick() {
		listen.UpdateListenerValuse(viewpos, new Vector3f(), cameray, camerax,
				cameraz);

		if (mousedis) {
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR,
					GLFW.GLFW_CURSOR_DISABLED);
		} else {
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR,
					GLFW.GLFW_CURSOR_NORMAL);
		}
		
		unihand.tick();
		updateKeys();
	}

	public void click(int button, boolean setto) {
		if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			mouse.setRight(setto);
		}
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			mouse.setLeft(setto);
		}
		if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
			mouse.setMiddle(setto);
		}

	}

	public void updateKeys() {
		for (int i = 0; i < keys.size(); i++) {
			if (keys.get(i) == Key.Press) {
				keys.set(i, Key.True);
			}
			if (keys.get(i) == Key.Realesed) {
				keys.set(i, Key.False);
			}
		}
	}

	public static Key getKey(int id) {
		return keys.get(id);
	}

	public void ckeckkeys(int key, boolean setto) {
		if (setto) {
			keys.set(key, Key.Press);
		} else {
			keys.set(key, Key.Realesed);
		}
	}

	public void close() {
		loader.cleanup();
		TextMaster.cleanUp();
		ren.getShader().cleanup();
		ren.getGuishader().cleanup();
		al.destroy();
		al.getDevice().destroy();
		glfwDestroyWindow(window);
		glfwTerminate();
		System.exit(0);
	}

	public static void setTypeStream(Typestream nts) {
		ts = nts;
	}

	public Renderer getRen() {
		return ren;
	}

	public void setRen(Renderer ren) {
		this.ren = ren;
	}
}
