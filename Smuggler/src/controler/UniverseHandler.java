package controler;

import gui.GUI;
import gui.menus.MainMenu;
import input.Key;
import input.MainLoop;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import loading.MasterLoader;
import loading.ModelLoader;
import loading.Resource;
import loading.ResourceType;
import math3d.Vector3f;
import models.ParticalTexture;
import models.Texturedmodel;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import physics.MainEngine;
import physics.PhysicsEngine;
import render.MasterRenderer;
import entity.BasicEntity;
import entity.Light;
import entity.ParticalEmiter;
import entity.PhiEntity;

public class UniverseHandler {
	private MainLoop loop;
	public ArrayList<BasicEntity> entitys;
	public ArrayList<BasicEntity> removedentitys;
	public ArrayList<GUI> guis;
	public ArrayList<Light> lights;
	public MasterRenderer ren;
	public ModelLoader loader;
	private GameState state = GameState.InGame;
	private PhysicsEngine phe;
	public static Texturedmodel[] models;
	public static float masterVolume = 1;

	public UniverseHandler() {
		loop = new MainLoop(this);
		loop.start();
	}

	public void init() {
		entitys = new ArrayList<BasicEntity>();
		removedentitys = new ArrayList<BasicEntity>();
		guis = new ArrayList<GUI>();
		lights = new ArrayList<Light>();
		phe = new MainEngine(this);

		guis.add(new MainMenu(1, 1, GameState.MainMenu));

		Light l = new Light(new Vector3f(0, 0.001f, -0.002f), new Vector3f(),
				new Vector3f(0, 1, 0.5f));

		lights.add(l);

		entitys.add(l);

		l = new Light(new Vector3f(0, -0.1f, 0.1f), new Vector3f(0, 110, 0),
				new Vector3f(1, 1, 1f));

		lights.add(l);

		entitys.add(l);

		Resource m = MasterLoader.getResource("wildBobrillaHead.obj");

		Resource t = MasterLoader.getResource("white.png");

		if (m != null && m.getRestype() == ResourceType.Model && t != null
				&& t.getRestype() == ResourceType.Texture) {

			entitys.add(new PhiEntity(new Vector3f(0, -3, -7), new Vector3f(
					0.005f, 0.005f, 0), new Vector3f(0.3f, 0.05f, 0.05f), 0, 0,
					0, 0.2f, 1, new Texturedmodel(MasterLoader.models.get(m
							.getId()), MasterLoader.textures.get(t.getId()))));
		}

	}

	public void tick() {
		// /////////////////////////////////////////////////
		phe.simulate();

		phe.collision();

		// /////////////////////////////////////////////////

		for (Iterator<BasicEntity> iterator = tempents.iterator(); iterator
				.hasNext();) {
			BasicEntity ent = (BasicEntity) iterator.next();

			entitys.add(ent);

			iterator.remove();

		}

		for (Iterator<BasicEntity> iterator = tempremoveents.iterator(); iterator
				.hasNext();) {
			BasicEntity ent = (BasicEntity) iterator.next();

			removedentitys.add(ent);

			iterator.remove();

		}

		for (Iterator<BasicEntity> ents = entitys.iterator(); ents.hasNext();) {
			BasicEntity basicEntity = (BasicEntity) ents.next();

			for (Iterator<BasicEntity> removed = removedentitys.iterator(); removed
					.hasNext();) {

				BasicEntity basicEntity2 = (BasicEntity) removed.next();

				if (basicEntity == basicEntity2) {
					ents.remove();
					removed.remove();
				}
			}
		}

		for (Iterator<BasicEntity> iterator = entitys.iterator(); iterator
				.hasNext();) {
			BasicEntity ent = (BasicEntity) iterator.next();

			ent.tick();

		}

		if (MainLoop.getKey(GLFW.GLFW_KEY_ESCAPE) == Key.Press) {
			loop.close();
		}

		if (state == GameState.InGame) {
			MainLoop.mousedis = true;
		}

		if (state == GameState.MainMenu) {
			MainLoop.mousedis = false;
		}

		for (GUI gui : guis) {
			gui.update(loop.mouse, state);
		}
	}

	public GameState getState() {
		return state;
	}

	private static ArrayList<BasicEntity> tempents = new ArrayList<BasicEntity>();
	private static ArrayList<BasicEntity> tempremoveents = new ArrayList<BasicEntity>();

	public static void addEntity(BasicEntity e) {
		tempents.add(e);
	}

	public static void removeEntity(BasicEntity e) {
		tempremoveents.add(e);
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public static void main(String[] args) {
		if (args.length > 0)
			MasterLoader.resFolder = new File(args[0]);

		new UniverseHandler();
	}

}
