package input;

public class Mouse {
	boolean right;
	boolean left;
	boolean middle;
	public float x;
	public float y;
	
	
	public Mouse(boolean right, boolean left, boolean middle) {
		super();
		this.right = right;
		this.left = left;
		this.middle = middle;
	}
	public boolean isRight() {
		return right;
	}
	public void setRight(boolean right) {
		this.right = right;
	}
	public boolean isLeft() {
		return left;
	}
	public void setLeft(boolean left) {
		this.left = left;
	}
	public boolean isMiddle() {
		return middle;
	}
	public void setMiddle(boolean middle) {
		this.middle = middle;
	}
	
	

}
