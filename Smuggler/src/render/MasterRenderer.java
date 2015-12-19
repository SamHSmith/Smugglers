package render;

import fontRendering.TextMaster;
import gui.GUI;
import input.MainLoop;

import java.util.ArrayList;

import loading.ModelLoader;
import models.RawModel;

import org.lwjgl.opengl.GL11;

import controler.UniverseHandler;
import entity.BasicEntity;
import entity.Light;
import entity.Partical;
import entity.Warp;

public class MasterRenderer extends Renderer {

	private static final float[] PARTICALMODEL = { -0.5f, 0.5f, -0.5f, -0.5f,
			0.5f, 0.5f, 0.5f, -0.5f };
	private RawModel quad;

	public MasterRenderer(MainLoop loop, UniverseHandler unihand,
			ModelLoader loader) {
		super(loop, unihand);

		quad = new RawModel(loader.LoadToVAO(PARTICALMODEL, 2), 4);

	}

	@Override
	public void render(ArrayList<BasicEntity> ents, ArrayList<GUI> gUIs,
			ArrayList<Light> lights, Camera cam) {

		prepare();

		clearBuffers();

		createProj(MainLoop.WIDTH, MainLoop.HEIGHT);

		for (int i = 0; i < ents.size(); i++) {
			BasicEntity current = ents.get(i);

			if (current instanceof Partical) {

				loadPartical(cam, (Partical) current);

				renderPartical((Partical) current, pshader, quad);

			} else {

				if (current.getModel() != null) {

					loadEntity(current, cam, lights);

					renderEntity(current, shader);

				}
			}
		}

		GL11.glEnable(GL11.GL_BLEND);

		GL11.glDisable(GL11.GL_DEPTH_TEST);

		for (GUI current : gUIs) {
			renderGUI(current);
		}

		TextMaster.render();
	}

	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void cleanup() {
		super.cleanup();
	}

}
