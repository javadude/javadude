package sample;

import java.util.List;

public interface IHotelAgent {
	List<IHotel> getHotels();
	void reserve(IHotel hotel);
}
