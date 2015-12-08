package render.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import math3d.Matrix4f;
import math3d.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram {

	private int programID;
	private int vShaderID;
	private int fragshaderID;

	private static FloatBuffer matrixbuffer = BufferUtils.createFloatBuffer(16);

	public ShaderProgram(InputStream vfile, InputStream fragmentfile) {
		vShaderID = loadShader(vfile, GL20.GL_VERTEX_SHADER);
		fragshaderID = loadShader(fragmentfile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vShaderID);
		GL20.glAttachShader(programID, fragshaderID);
		bindAtributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniforms();
	}

	protected abstract void getAllUniforms();

	protected int GetUniFormL(String uniformname) {
		return GL20.glGetUniformLocation(programID, uniformname);

	}

	protected void loadFloat(int location, float valeu) {
		GL20.glUniform1f(location, valeu);
	}
	
	protected void loadInt(int location, int valeu) {
		GL20.glUniform1i(location, valeu);
	}

	protected void loadVector3f(int location, Vector3f vec) {
		GL20.glUniform3f(location, vec.x, vec.y, vec.z);
	}

	protected void loadmatrix(int location, Matrix4f viewmat){
		GL20.glUniformMatrix4(location, false, writeMatrixToBuffer(viewmat, matrixbuffer));
		}

	public static FloatBuffer writeMatrixToBuffer(Matrix4f matrix,
			FloatBuffer buffer) {
		matrix.get(buffer);
		return buffer;
	}

	public void start() {
		GL20.glUseProgram(programID);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void cleanup() {
		stop();
		GL20.glDetachShader(programID, vShaderID);
		GL20.glDetachShader(programID, fragshaderID);
		GL20.glDeleteShader(vShaderID);
		GL20.glDeleteShader(fragshaderID);
		GL20.glDeleteProgram(programID);
	}

	protected abstract void bindAtributes();

	protected void bindAtrib(int atribute, String variblename) {
		GL20.glBindAttribLocation(programID, atribute, variblename);
	}

	public static int loadShader(InputStream in, int type) {
		StringBuilder shaderscorce = new StringBuilder();

		try {
			BufferedReader reader = new BufferedReader (new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderscorce.append(line).append("\n");
			}
			reader.close();
		} catch (Exception e) {
			System.err.println("FAILED TO LOAD SHADER");
			System.err.println("THE GAME WILL STILL RUN BUT THE SHADER WON'T WORK");
		}
		int shaderid = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderid, shaderscorce);
		GL20.glCompileShader(shaderid);
		if (GL20.glGetShaderi(shaderid, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderid));
			System.err.println("Could not Compile shader");
			System.exit(shaderid);
		}
		return shaderid;
	}
}
