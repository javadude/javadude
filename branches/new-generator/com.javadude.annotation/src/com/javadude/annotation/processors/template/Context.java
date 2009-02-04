package com.javadude.annotation.processors.template;

import java.io.Writer;

import com.javadude.annotation.Bean;
import com.javadude.annotation.Property;
import com.javadude.annotation.processors.Symbols;

@Bean(
	properties={
		@Property(name="symbols", type=Symbols.class),
		@Property(name="line", type=int.class),
		@Property(name="writer", type=Writer.class),
		@Property(name="indent")
	}
)
public class Context extends ContextGen {
	// nothing else
}
