package sample;

import java.util.List;

public interface ICarAgent {
	List<ICar> getCars();
	void reserve(ICar car);
}
