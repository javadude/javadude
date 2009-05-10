package sample;

import com.javadude.annotation.Bean;
import com.javadude.annotation.Delegate;

@Bean(delegates = {
    @Delegate(type = IHotelAgent.class,
              property = "hotelAgent",
              instantiateAs = HotelAgentImpl.class),
    @Delegate(type = ICarAgent.class,
              property = "carAgent",
              instantiateAs = CarAgentImpl.class),
    @Delegate(type = IFlightAgent.class,
              property = "flightAgent",
              instantiateAs = FlightAgentImpl.class)
	}
)
public class TravelAgent extends TravelAgentGen {
	// everything is done in @Bean above
}
