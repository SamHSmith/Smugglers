package models;

public class RawModel {
	
	private int vaoid;
	private int vertexCount;

	public RawModel(int vaoid,int vertexCount) {
		this.vaoid=vaoid;
		this.vertexCount=vertexCount;
	}

	public int getVaoid() {
		return vaoid;
	}

	public void setVaoid(int vaoid) {
		this.vaoid = vaoid;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

}
