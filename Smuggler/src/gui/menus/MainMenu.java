package gui.menus;

import gui.buildingblocks.MLabel;
import gui.buildingblocks.MPanel;
import input.Mouse;
import loading.MasterLoader;
import loading.Resource;
import loading.ResourceType;
import math3d.Vector3f;
import models.Texturedmodel;
import sound.Sound;
import sound.Source;
import controler.GameState;
import controler.UniverseHandler;

public class MainMenu extends MPanel {

	private Source src;
	private Sound s;
	private static final float volume=0.5f;

	public MainMenu(float width, float height, GameState showstate) {
		super(new Vector3f(), 0, 0, 0, width, height, showstate);

		Resource rawmodel = MasterLoader.getResource("SimpleGUI.obj");

		Resource texture = MasterLoader.getResource("MMBack.png");

		Resource readable = MasterLoader.getResource("Readable");

		Resource music = MasterLoader.getResource("Epic.wav");

		if (rawmodel != null & texture != null) {
			if (rawmodel.getRestype() == ResourceType.Model
					&& texture.getRestype() == ResourceType.Texture) {

				Texturedmodel model = new Texturedmodel(
						MasterLoader.models.get(rawmodel.getId()),
						MasterLoader.textures.get(texture.getId()));

				this.addGUI(new MPanel(new Vector3f(), rx, ry, rz, 1, 1,
						showstate, model));

			}
		}

		if (readable != null) {
			if (readable.getRestype() == ResourceType.Font)
				this.addGUI(new MLabel(new Vector3f(0, 0, 0), 0, 0, 0, 0.5f,
						0.5f, showstate, model, "Smugglers", MasterLoader.fonts
								.get(readable.getId()), new Vector3f(1, 1, 1),
						4));
			
			this.addGUI(new MLabel(new Vector3f(0.42f, 0.47f, 0), 0, 0, 0, 0.2f,
					0.1f, showstate, model, "Version Alpha 0.0.1", MasterLoader.fonts
							.get(readable.getId()), new Vector3f(1, 1, 1),
					1));

		}
		
		src = new Source(new Vector3f(), new Vector3f(), 1, false);

		if (music != null) {
			if (music.getRestype() == ResourceType.Sound) {
				s = MasterLoader.sounds.get(music.getId());
				
				src.Play(s, true);
			}
		}

	}

	@Override
	public void update(Mouse mouse, GameState state) {
		super.update(mouse, state);
		
		src.update(new Vector3f(), new Vector3f(),volume*UniverseHandler.masterVolume);
		
		if (state == this.getShowstate()) {

		}else{
			src.Stop(s);
		}
	}
}
