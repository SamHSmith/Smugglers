package toolbox;

public class StringDefiner {

	public static CommandType DefineCommand(String[] command){
		if(command[0]=="move"){
			return CommandType.move;
		}
		return null;
	}
	
	public static String[] StringToArray(String s){
		return s.split(" ");
	}

}
