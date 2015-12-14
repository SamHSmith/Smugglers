package loading;

public class Resource {
	private int id;
	private ResourceType restype;
	private String name;
	
	/**
	 * @author Sam
	 * This class represents a resource
	 * @serialField id the id
	 * @serialField restype the type of resource (Font,Texture,TextFile,Model etc) 
	 * @serialField name the name
	 */

	/**
	 * @param id the id
	 * @param restype the type of resource (Font,Texture,TextFile,Model etc) 
	 * @param name the name
	 */
	public Resource(int id, ResourceType restype, String name) {
		super();
		this.id = id;
		this.restype = restype;
		this.name = name;
	}
	
	@Override
	public String toString(){
		return "Name: "+name+" ID: "+id+" ResourceType: "+restype;
	}

	public int getId() {
		return id;
	}

	public ResourceType getRestype() {
		return restype;
	}

	public String getName() {
		return name;
	}

}
