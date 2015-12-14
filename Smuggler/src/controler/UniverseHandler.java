package controler;

import fontRendering.TextMaster;
import gui.GUI;
import input.Key;
import input.MainLoop;

import java.io.File;
import java.util.ArrayList;

import loading.MasterLoader;
import loading.ModelLoader;
import math3d.Vector3f;
import models.Texturedmodel;

import org.lwjgl.glfw.GLFW;

import physics.MainEngine;
import physics.PhysicsEngine;
import render.Renderer;
import entity.BasicEntity;
import entity.Light;
import entity.Warp;

public class UniverseHandler {
	private MainLoop loop;
	public static ArrayList<BasicEntity> entitys;
	public static ArrayList<BasicEntity> removedentitys;
	public ArrayList<GUI> guis;
	public ArrayList<Light> lights;
	public Warp warp;
	public Renderer ren;
	public ModelLoader loader;
	private GameState state = GameState.MainMenu;
	private PhysicsEngine phe;
	public static Texturedmodel[] models;

	public UniverseHandler() {
		loop = new MainLoop(this);
		loop.start();
	}

	public void init() {
		entitys = new ArrayList<BasicEntity>();
		removedentitys = new ArrayList<BasicEntity>();
		guis = new ArrayList<GUI>();
		lights = new ArrayList<Light>();
		warp = new Warp(new Vector3f(), new Vector3f(), 0, 0, 0, 1, 0, 0f);
		phe = new MainEngine(this);
	}

	public void tick() {
		// /////////////////////////////////////////////////
		phe.simulate();

		phe.collision();

		// /////////////////////////////////////////////////

		if (MainLoop.getKey(GLFW.GLFW_KEY_ESCAPE) == Key.Press) {
			loop.close();
		}

		for (GUI gui : guis) {
			gui.update(loop.mouse, state);
		}
	}

	public GameState getState() {
		return state;
	}

	public static void addEntity(BasicEntity e) {
		entitys.add(e);
	}

	public static void removeEntity(BasicEntity e) {
		removedentitys.add(e);
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public static void main(String[] args) {
		if(args.length>0)
		MasterLoader.resFolder=new File(args[0]);
		
		new UniverseHandler();
	}

}
