package sample;

public interface Processor {
	public enum Size {SMALL, MEDIUM, LARGE};
	void process(String item, Size size, String color, int quantity);
}
