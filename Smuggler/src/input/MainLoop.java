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

import java.util.ArrayList;
import java.util.Iterator;

import loading.MasterLoader;
import loading.ModelLoader;
import math3d.Vector3f;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALContext;
import org.lwjgl.opengl.GLContext;

import render.Camera;
import render.MasterRenderer;
import render.Renderer;
import render.shaders.EntityShader;
import render.shaders.GUIshader;
import sound.Listener;
import controler.UniverseHandler;
import entity.BasicEntity;
import entity.Light;
import entity.Warp;
import exceptions.DirectoryNotSetException;
import fontRendering.TextMaster;
import gui.GUI;

public class MainLoop {

	public static final float FOV = 70;
	public static final float NEARPLANE = 0.01f;
	public static final float FARPLANE = 1000;
	GLFWCursorPosCallback cpc;
	private GLFWErrorCallback errorCallback;
	GLFWKeyCallback kc;
	GLFWWindowSizeCallback wsc;
	GLFWMouseButtonCallback mbc;
	GLFWCharCallback cc;
	ALContext al;
	long window;
	ModelLoader loader;
	EntityShader shader;
	GUIshader guishader;
	MasterRenderer ren;
	Warp warp;
	ArrayList<BasicEntity> entitys;
	ArrayList<GUI> guis;
	ArrayList<Light> lights;
	public Camera cam;
	public static ArrayList<Key> keys;
	public static boolean mousedis = false;
	Listener listen;
	private UniverseHandler unihand;
	public Mouse mouse;
	private static Typestream ts;
	public static final int keyamount = 500;

	/**
	 * This class is the main class that uses all the other classes and manegers
	 * of the inputs such as window, mouse or keyboard
	 */

	public static int WIDTH = 1280;
	public static int HEIGHT = 720;

	public MainLoop(UniverseHandler unihand) {
		this.unihand = unihand;
	}

	public void start() {
		try {
			createDisplay();
			init();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		loop();
		close();
	}

	private void createDisplay() {

		errorCallback = new GLFWErrorCallback() {

			@Override
			public void invoke(int error, long descripten) {
				System.err
						.println("Error "
								+ error
								+ " happend. I don't now what it means but I do have the message of it.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.err.println("But there is a problem.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.err.println("You see the message is just a number.");
				System.err.println("I bet it means somthing but I don't no.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.err.println("Here is the Message");
				System.err.println(descripten);
			}
		};

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

static double last_xpos = 0.0, last_ypos = 0.0;

	private void init() {
		guishader = new GUIshader();
		keys = new ArrayList<Key>();
		listen = new Listener();
		loader = new ModelLoader();
		unihand.loader = loader;
		ren = new MasterRenderer(this, unihand, loader);
		mouse = new Mouse(false, false, false);
		cam = new Camera(new Vector3f(), 0, 0, 0, false);
		TextMaster.init(loader);

		for (int i = 0; i < keyamount; i++) {
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

					if (key < keyamount && key > 0) {
						ckeckkeys(key, true);
					}
				}
				if (action == GLFW.GLFW_RELEASE) {
					if (key < keyamount && key > 0) {
						ckeckkeys(key, false);
					}
				}
			}
		};

		cpc = new GLFWCursorPosCallback() {

			@Override
			public void invoke(long arg0, double xpos, double ypos) {
				if (!cam.isCin()) {					
					
					cam.setRy(cam.getRy() + ((float) (xpos - last_xpos) * 180 / (WIDTH)) / 2.0f);
					cam.setRx(cam.getRx() - ((float) (ypos - last_ypos) * -180 / (HEIGHT))/2.0f);
				}
				last_xpos = xpos;
				last_ypos = ypos;

				mouse.x = (float) ((xpos * 2 / (WIDTH)) - 1);
				mouse.y = (float) -((ypos * 2 / (HEIGHT)) - 1);

			}
		};

		wsc = new GLFWWindowSizeCallback() {

			@Override
			public void invoke(long window, int nwidth, int nheight) {
				WIDTH = nwidth;
				HEIGHT = nheight;

				if (WIDTH == 0) {
					WIDTH = 1;
				}

				if (HEIGHT == 0) {
					HEIGHT = 1;
				}

				ren.createProj(WIDTH, HEIGHT);
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

		try {
			MasterLoader.init(loader);
		} catch (DirectoryNotSetException e1) {
			System.err.println(e1.getMessage());
			e1.printStackTrace();
			close();
		}

		try {
			unihand.init();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			close();
		}

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
				try {
					tick();
				} catch (Exception e) {
					e.printStackTrace(System.out);
				}
				unprocessed--;
				shouldrender = true;
			}
			
			if(cam.getRx() > 20.0f)
			{ cam.setRx(20.f); }
			else if(cam.getRx() < -20.0f)
			{ cam.setRx(-20.f); }

			if (shouldrender) {
				ren.render(unihand.entitys, unihand.guis,
						unihand.lights, cam);
				fps++;
				shouldrender = false;
			}

			glfwPollEvents();
			glfwSwapBuffers(window);

			// Fps Timer

			if (System.currentTimeMillis() - fpstimer > 10000) {
				System.out.println();
				System.out.println(updates / 10 + " :ticks fps: " + fps / 10);
				System.out.println();
				updates = 0;
				fps = 0;
				fpstimer = System.currentTimeMillis();

			}

		}
	}

	private void tick() {
		listen.UpdateListenerValuse(new Vector3f(), cam);

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
		MasterLoader.cleanup();
		ren.cleanup();
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

	public void setRen(MasterRenderer ren) {
		this.ren = ren;
	}
}
