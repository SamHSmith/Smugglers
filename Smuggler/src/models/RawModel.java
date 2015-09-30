package models;

public class RawModel {
	
	private int vaoid;
	private int vertexCount;
	float[] collcoords;

	public RawModel(int vaoid,int vertexCount,float[] collcoords) {
		this.vaoid=vaoid;
		this.vertexCount=vertexCount;
		this.collcoords=collcoords;
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
