package render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import models.RawModel;
import models.Texturedmodel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entety.Object;
import shaders.StaticShader;

public class Renderer {
	
	private StaticShader entshader;

	public Renderer(StaticShader entshader) {
		super();
		this.entshader = entshader;
	}

	public void renderEntity(Object ent) {

		GL11.glClear(GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0, 0.5f, 0.5f, 1);

		RawModel rawmodel = ent.getModel().getModel();
		Texturedmodel model = ent.getModel();

		entshader.start();
		GL30.glBindVertexArray(rawmodel.getVaoid());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTextID());
		GL11.glDrawElements(GL_TRIANGLES, rawmodel.getVertexCount(),
				GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		entshader.stop();

	}

}
