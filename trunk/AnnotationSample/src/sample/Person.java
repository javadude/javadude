package sample;

import com.javadude.annotation.Bean;
import com.javadude.annotation.Property;

@Bean(properties = {
		@Property(name = "name"),
		@Property(name = "address"),
		@Property(name = "phone")
	}
)
public class Person extends PersonGen implements IPerson {
	/**
	 * does something
	 * @param x the parameter
	 * @throws IllegalArgumentException if the paramerer is bad
	 */
	public void foo(int x) throws IllegalArgumentException {
		// do something
	}
}
