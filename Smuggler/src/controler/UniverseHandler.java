package controler;

import fontMeshCreator.FontType;
import fontRendering.TextMaster;
import gui.ActivationListener;
import gui.GUI;
import gui.MButton;
import gui.MConsole;
import gui.MLabel;
import gui.MPanel;
import input.Key;
import input.MainLoop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import loading.ModelLoader;
import loading.ObjFileLoader;
import models.RawModel;
import models.Texturedmodel;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import physics.MainEngine;
import physics.PhysicsEngine;
import render.Renderer;
import sound.Sound;
import sound.Source;
import textures.ModelTexture;
import entity.BasicEntity;
import entity.Light;
import entity.PhiEntity;
import entity.Warp;

public class UniverseHandler {
	private MainLoop loop;
	public ArrayList<BasicEntity> entitys;
	public ArrayList<GUI> guis;
	public ArrayList<Light> lights;
	public Warp warp;
	public Renderer ren;
	public ModelLoader loader;
	private GameState state = GameState.MainMenu;
	private Source music;
	private Sound mainmenu;
	private PhysicsEngine phe;
	public static Texturedmodel[] models;

	public UniverseHandler() {
		loop = new MainLoop(this);
		loop.start();
	}

	public void init() {
		entitys = new ArrayList<BasicEntity>();
		guis = new ArrayList<GUI>();
		lights = new ArrayList<Light>();
		warp = new Warp(new Vector3f(), new Vector3f(), 0, 0, 0, 1, 0, 0);
		TextMaster.init(loader);
		models =new Texturedmodel[256];
		phe=new MainEngine(this);
		
		RawModel model = ObjFileLoader.loadObjModel("SimpleGui", loader, false);
		ModelTexture text = new ModelTexture(loader.loadTexture("MMBack"));
		Texturedmodel tmodel = new Texturedmodel(model, text);
		
		models[0]=tmodel;

		try {
			mainmenu = new Sound("res/Sound/Epic.wav");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		music = new Source(new Vector3f(), new Vector3f(), 1);

		guis.add(new MPanel(new Vector3f(0, 0, 0), 0, 0, 0, 1.2f, 1.2f,
				GameState.MainMenu, models[0]));

		text = new ModelTexture(loader.loadTexture("white"));
		tmodel = new Texturedmodel(model, text);
		
		models[1]=tmodel;

		guis.get(0).add(
				new MLabel(new Vector3f(0.3f,0.4f,0), 0, 0, 0, 0.5f, 0.2f, state, null,
						"You should try it", new FontType(loader
								.loadFontTexture("Sloppy"), new File(
								"res/Fonts/Sloppy/Font.fnt")), new Vector3f(0.5f,
								0.2f, 1), 2));
		
		MConsole c=new MConsole(new Vector3f(0.3f,-0.3f,0), 0, 0, 0, 0.3f, 0.2f, GameState.All, null, "", new FontType(loader
				.loadFontTexture("Readable"), new File(
				"res/Fonts/Readable/Font.fnt")), new Vector3f(1f,1f,1f), 2);
		
		guis.add(c);
		
		c.add(new Exicuter(new Vector3f(0.3f,-0.4f,0), 0, 0, 0, 0.4f, 0.2f, GameState.All, null, 2, new FontType(loader
				.loadFontTexture("Readable"),new File(
						"res/Fonts/Readable/Font.fnt")), new Vector3f(1,1,1), this));
		
		System.setOut(c.getP());

		guis.get(0).add(
				new MButton(new Vector3f(0, 0, 0f), 0, 0, 0, (float) 0.2f,
						0.1f, GameState.All, models[1], new ActivationListener() {

							@Override
							public void Action() {
								setState(GameState.InGame);
								MainLoop.mousedis=true;
							}
						}, "PlayDemo", new FontType(loader
								.loadFontTexture("Sloppy"), new File(
								"res/Fonts/Sloppy/Font.fnt")), new Vector3f(0,
								1, 1), 2.5f));

		text = new ModelTexture(loader.loadTexture("MMTitle"));
		tmodel = new Texturedmodel(model, text);
		
		models[2]=tmodel;

		guis.get(0).add(
				new MPanel(new Vector3f(0, 1.5f, 0), 0, 0, 0, 0.7f, 0.4f,
						GameState.All, models[2]));
		
		lights.add(new Light(new Vector3f(0,0,0), new Vector3f(-1,1,-1), 0, 0, 1, 0, new Vector3f(0.5f,1,1)));
		
		model = ObjFileLoader.loadObjModel("Buss", loader, false);
		text = new ModelTexture(loader.loadTexture("white"));
		tmodel = new Texturedmodel(model, text);
		
		models[101]=tmodel;
		
		model = ObjFileLoader.loadObjModel("Sphere", loader, false);
		text = new ModelTexture(loader.loadTexture("white"));
		tmodel = new Texturedmodel(model, text);
		
		models[100]=tmodel;
		
		entitys.add(new PhiEntity(new Vector3f(1.5f,0,5), new Vector3f(-0.001f,0,0), new Vector3f(0f,0,0), 0, 0, 0, 0.4f, models[100]));
		entitys.add(new PhiEntity(new Vector3f(-1,0.5f,5), new Vector3f(0.001f,0,0), new Vector3f(0f,0,0), 0, 0, 0, 0.4f, models[100]));
		
		model = ObjFileLoader.loadObjModel("Grass", loader, false);
		tmodel = new Texturedmodel(model, text);

		entitys.add(new PhiEntity(new Vector3f(0,-2,0), new Vector3f(), new Vector3f(), 0, 0, 0, 1f, tmodel));
		
		music.Play(mainmenu, true);
	}

	public void tick() {
		///////////////////////////////////////////////////
		phe.simulate();
		
		phe.collision();
		
		phe.collisionResponse();
		
		
		///////////////////////////////////////////////////

		
		guis.get(0).rotate(0, 0, 0.02f);

		if (MainLoop.getKey(GLFW.GLFW_KEY_ESCAPE) == Key.Press) {
			loop.close();
		}
		
		if (MainLoop.getKey(GLFW.GLFW_KEY_F12) == Key.Press) {
			loop.camerax=0;
			loop.cameray=0;
			loop.cameraz=0;
		}

		for (GUI gui : guis) {
			gui.update(loop.mouse, state);
		}

		if (((state == GameState.InGame || state == GameState.None))) {
			music.Stop(mainmenu);
		}
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public static void main(String[] args) {
		new UniverseHandler();
	}

}
