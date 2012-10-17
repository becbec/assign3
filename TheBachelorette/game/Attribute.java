package game;
public class Attribute {
	private String attributeType;
	private int attributeValue;
	
	public Attribute(String attributeType, int attributeValue){
		this.attributeType = attributeType;
		this.attributeValue = attributeValue;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	public int getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(int attributeValue) {
		this.attributeValue = attributeValue;
	}
	
}
