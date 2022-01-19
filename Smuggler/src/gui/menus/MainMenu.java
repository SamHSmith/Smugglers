package gui.menus;

import gui.buildingblocks.MButton;
import gui.buildingblocks.MLabel;
import gui.buildingblocks.MPanel;
import gui.events.ActivationListener;
import input.Mouse;
import loading.MasterLoader;
import loading.Resource;
import loading.ResourceType;
import math3d.Vector3f;
import models.Texturedmodel;
import sound.Sound;
import sound.Source;

import java.util.Random;

import controler.GameState;
import controler.UniverseHandler;

public class MainMenu extends MPanel implements ActivationListener {

	private Source src;
	private Sound s;
	private static float volume = 0.5f;

	public MainMenu(float width, float height, GameState showstate) {
		super(new Vector3f(), 0, 0, 0, width, height, showstate);

		Resource rawmodel = MasterLoader.getResource("SimpleGUI.obj");

		Resource texture = MasterLoader.getResource("MMBack.png");
		
		Resource texture2 = MasterLoader.getResource("SinglePlayerButton.png");

		Resource readable = MasterLoader.getResource("Readable");

		Resource music = MasterLoader.getResource("otherepic.wav");
		
		if(new Random().nextBoolean())
		{ music = MasterLoader.getResource("Epic.wav"); }

		if (rawmodel != null && texture != null && texture2 != null) {
			if (rawmodel.getRestype() == ResourceType.Model
					&& texture.getRestype() == ResourceType.Texture) {

				Texturedmodel model = new Texturedmodel(
						MasterLoader.models.get(rawmodel.getId()),
						MasterLoader.textures.get(texture.getId()));

				this.addGUI(new MPanel(new Vector3f(), rx, ry, rz, 1, 1,
						showstate, model));

			}
			if (rawmodel.getRestype() == ResourceType.Model
					&& texture2.getRestype() == ResourceType.Texture) {

				Texturedmodel model = new Texturedmodel(
						MasterLoader.models.get(rawmodel.getId()),
						MasterLoader.textures.get(texture2.getId()));

				this.addGUI(new MButton(new Vector3f(0.0f, -1.2f, 0.0f), 0, 0, 0, 0.2f, 0.2f,
						showstate, model, this, "",
						MasterLoader.fonts.get(readable.getId()), new Vector3f(1,1,1), height));

			}
		}

		if (readable != null) {
			if (readable.getRestype() == ResourceType.Font)
				this.addGUI(new MLabel(new Vector3f(0, 0, 0), 0, 0, 0, 0.5f,
						0.5f, showstate, model, "Smugglers", MasterLoader.fonts
								.get(readable.getId()), new Vector3f(1, 1, 1),
						4));

			this.addGUI(new MLabel(new Vector3f(0.42f, 0.47f, 0), 0, 0, 0,
					0.2f, 0.1f, showstate, model, "Version Alpha 0.0.1",
					MasterLoader.fonts.get(readable.getId()), new Vector3f(1,
							1, 1), 1));

		}

		src = new Source(new Vector3f(), new Vector3f(), 1, false);

		if (music != null) {
			if (music.getRestype() == ResourceType.Sound) {
				s = MasterLoader.sounds.get(music.getId());
			}
		}

	}
	
	private GameState lastState=GameState.None;

	@Override
	public void update(Mouse mouse, GameState state) {
		super.update(mouse, state);
		
		

		src.update(new Vector3f(), new Vector3f(), volume
				* UniverseHandler.masterVolume);

		if (state == this.getShowstate()) {
			if (lastState!=this.getShowstate()) {
				src.Play(s, true);
			}
			volume = 0.5f;
		} else {
			//src.Stop(s);
			volume = 0.1f;
		}
		lastState=state;
	}

	@Override
	public void Action() {
		// TODO Auto-generated method stub
		System.out.println("Hello there my friend you have pressed the button");
		UniverseHandler.singleton.setState(GameState.InGame);
	}
}
