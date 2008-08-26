package sample;

import com.javadude.annotation.Bean;
import com.javadude.annotation.Delegate;
import com.javadude.annotation.Property;

@Bean(
	properties = {
		@Property(name = "basePerson", type = IPerson.class)
	},
	delegates = {
		@Delegate(type = IPerson.class, property = "basePerson")
	}
)
public class Doctor extends DoctorGen implements IPerson {
	public Doctor(IPerson person) {
		setBasePerson(person);
	}
	@Override public String getName() {
		return "Dr. " + super.getName();
	}
}
