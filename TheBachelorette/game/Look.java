package game;
public class Look {
	private String lookType;
	private String lookValue;
	
	public Look(String lookType, String lookValue){
		this.lookType = lookType;
		this.lookValue = lookValue;
	}
	
	public Look(){
	}

	public String getLookType() {
		return lookType;
	}

	public void setLookType(String lookType) {
		this.lookType = lookType;
	}

	public String getLookValue() {
		return lookValue;
	}

	public void setLookValue(String lookValue) {
		this.lookValue = lookValue;
	}
	
	
}
