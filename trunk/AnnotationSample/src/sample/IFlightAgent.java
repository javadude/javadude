package sample;

import java.util.List;

public interface IFlightAgent {
	List<IFlight> getFlight();
	void reserve(IFlight flight);
}
