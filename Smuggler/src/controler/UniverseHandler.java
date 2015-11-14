package controler;

import fontMeshCreator.FontType;
import fontRendering.TextMaster;
import gui.ActivationListener;
import gui.GUI;
import gui.MButton;
import gui.MPanel;
import input.Key;
import input.KeyValues;
import input.MainLoop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import loading.ModelLoader;
import loading.ObjFileLoader;
import models.RawModel;
import models.Texturedmodel;

import org.joml.Vector3f;

import render.Renderer;
import sound.Sound;
import sound.Source;
import textures.ModelTexture;
import entity.BasicEntity;
import entity.Light;
import entity.Warp;

public class UniverseHandler {
	private MainLoop loop;
	public ArrayList<BasicEntity> entitys;
	public ArrayList<GUI> guis;
	public ArrayList<Light> lights;
	public Warp warp;
	public Renderer ren;
	public ModelLoader loader;
	private GameState state=GameState.MainMenu;
	private Source music;
	private Sound mainmenu;
	
	public UniverseHandler(){
		loop=new MainLoop(this);
		loop.start();
	}
	
	public void init(){
		entitys=new ArrayList<BasicEntity>();
		guis=new ArrayList<GUI>();
		lights=new ArrayList<Light>();
		warp=new Warp(new Vector3f(), new Vector3f(), 0, 0, 0, 1, 0, 0);
		TextMaster.init(loader);
		
		try {
			mainmenu=new Sound("res/Sound/Epic.wav");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		music= new Source(new Vector3f(), new Vector3f(), 1);
		
		RawModel model=ObjFileLoader.loadObjModel("SimpleGui", loader, false);
		ModelTexture text =new ModelTexture(loader.loadTexture("MMBack"));
		Texturedmodel tmodel=new Texturedmodel(model, text);
		
		
		guis.add(new MPanel(new Vector3f(0,0,-2), 0, 0, 0, 1.2f, 1.2f, GameState.MainMenu, tmodel));
		
		guis.add(new MPanel(new Vector3f(0,0,0), 0, 0, 0, 1.2f, 1.2f, GameState.SPMenu, tmodel));
		
		text =new ModelTexture(loader.loadTexture("white"));
		tmodel=new Texturedmodel(model, text);
		
		guis.get(0).add(new MButton(new Vector3f(0,0,0f), 0, 0, 0, (float) 0.2f, 0.1f, GameState.All, tmodel, new ActivationListener() {
			
			@Override
			public void Action() {
				setState(GameState.SPMenu);
			}
		}, "PlayDemo", new FontType(loader.loadFontTexture("Sloppy"), new File("res/Fonts/Sloppy/Font.fnt")), new Vector3f(0,1,1), 2.5f));
		
		text =new ModelTexture(loader.loadTexture("MMTitle"));
		tmodel=new Texturedmodel(model, text);
		
		guis.get(0).add(new MPanel(new Vector3f(0,1.5f,0), 0, 0, 0, 0.7f, 0.4f, GameState.All, tmodel));
		
		music.Play(mainmenu, true);
	}
	
	public void updatepositions() {
		for (BasicEntity ent : entitys) {
			ent.getPosition().add(ent.getVelocity());
			ent.rotate(ent.getRotVelocity().x, ent.getRotVelocity().y,
					ent.getRotVelocity().z);
		}
	}
	public void tick(){
		this.updatepositions();
		
		guis.get(1).rotate(0, 0, 0.02f);
		guis.get(0).rotate(0, 0, 0.02f);
		
		if(loop.getKey(KeyValues.keyEscape)==Key.Press){
			loop.close();
		}
		
		for(GUI gui:guis){
			gui.update(loop.mouse);
		}
		
		if(((state==GameState.InGame||state==GameState.None))){
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
