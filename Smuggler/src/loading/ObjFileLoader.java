package loading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import models.RawModel;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class ObjFileLoader {
	public static float[] vertecies;
	public static float[] texturecoords;
	public static float[] normals;

	public static int[] indecies;

	public static void loadvaribles(String filename, ModelLoader loader, boolean col) {
		FileReader fr = null;
		FileReader collfile = null;
		try {
			fr = new FileReader(new File("res/Model/" + filename + "/"
					+ "model" + ".obj"));
			if(col)
			collfile = new FileReader(new File("res/Model/" + filename + "/"
					+ "coll" + ".cof"));

		} catch (FileNotFoundException e) {
			System.err.println("Error while loading " + filename);
			System.err.println("remmember this does not mean that nothing was loadable");
		}
		try{
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		ArrayList<Vector2f> texturecoords = new ArrayList<Vector2f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Integer> indeces = new ArrayList<Integer>();
		float[] verticesarray = null;
		float[] normalarray = null;
		float[] texturearray = null;
		int[] indesiecarray = null;
		try {

			while (true) {

				line = br.readLine();
				String[] current = line.split(" ");
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(
							Float.parseFloat(current[1]),
							Float.parseFloat(current[2]),
							Float.parseFloat(current[3]));
					vertices.add(vertex);
				} else if (line.startsWith("vt ")) {
					Vector2f textcoord = new Vector2f(
							Float.parseFloat(current[1]),
							Float.parseFloat(current[2]));
					texturecoords.add(textcoord);
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(
							Float.parseFloat(current[1]),
							Float.parseFloat(current[2]),
							Float.parseFloat(current[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					texturearray = new float[vertices.size() * 2];
					normalarray = new float[vertices.size() * 3];
					break;
				}
			}

			while (line != null) {
				if (!line.startsWith("f ")) {
					line = br.readLine();
					continue;
				}

				String[] current = line.split(" ");
				String[] vertex1 = current[1].split("/");
				String[] vertex2 = current[2].split("/");
				String[] vertex3 = current[3].split("/");

				prosessvertex(vertex1, indeces, texturecoords, normals,
						texturearray, normalarray);
				prosessvertex(vertex2, indeces, texturecoords, normals,
						texturearray, normalarray);
				prosessvertex(vertex3, indeces, texturecoords, normals,
						texturearray, normalarray);
				line = br.readLine();
			}
			br.close();

		} catch (IOException e) {
			System.err.println("Unknown file format");
			e.printStackTrace();
		}

		verticesarray = new float[vertices.size() * 3];
		indesiecarray = new int[indeces.size()];

		int vertexpointer = 0;
		for (Vector3f vertex : vertices) {
			verticesarray[vertexpointer++] = vertex.x;
			verticesarray[vertexpointer++] = vertex.y;
			verticesarray[vertexpointer++] = vertex.z;
		}

		for (int i = 0; i < indeces.size(); i++) {
			indesiecarray[i] = indeces.get(i);
		}

		if (collfile != null) {

			br = new BufferedReader(collfile);
			ArrayList<Float> coll = new ArrayList<Float>();
			try {
				line=br.readLine();
				String[] part1 = line.split(" ");
				line=br.readLine();
				String[] part2 = line.split(" ");
				line=br.readLine();
				String[] part3 = line.split(" ");
				while (true) {

					coll.add(Float.parseFloat(part1[0]));
					coll.add(Float.parseFloat(part1[1]));
					coll.add(Float.parseFloat(part1[2]));
					
					coll.add(Float.parseFloat(part2[0]));
					coll.add(Float.parseFloat(part2[1]));
					coll.add(Float.parseFloat(part2[2]));
					
					coll.add(Float.parseFloat(part3[0]));
					coll.add(Float.parseFloat(part3[1]));
					coll.add(Float.parseFloat(part3[2]));
					
					line=br.readLine();
					
					part1=part2;
					part2=part3;
					part3=line.split(" ");
					
				}

			} catch (IOException e) {
				System.out.println("End of coll data");
			} catch (NullPointerException e1){}

		
			
		}

		ObjFileLoader.vertecies = verticesarray;
		ObjFileLoader.texturecoords = texturearray;
		ObjFileLoader.normals = normalarray;
		ObjFileLoader.indecies = indesiecarray;
		}catch(NullPointerException e){
			//TODO do catchh
		}
	}

	public static RawModel loadObjModel(String filename, ModelLoader loader,boolean col) {
		loadvaribles(filename, loader,col);

		return loader.LoadToVAO(vertecies, texturecoords, normals, indecies);
	}

	public static void prosessvertex(String[] vertexData,
			ArrayList<Integer> indeces, ArrayList<Vector2f> textures,
			ArrayList<Vector3f> normals, float[] texturearray,
			float[] normalsarray) {
		int currentvertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indeces.add(currentvertexPointer);
		Vector2f currenttex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		texturearray[currentvertexPointer * 2] = currenttex.x;
		texturearray[currentvertexPointer * 2 + 1] = 1 - currenttex.y;

		Vector3f currentnorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsarray[currentvertexPointer * 3] = currentnorm.x;
		normalsarray[currentvertexPointer * 3 + 1] = currentnorm.y;
		normalsarray[currentvertexPointer * 3 + 2] = currentnorm.z;

	}

}
