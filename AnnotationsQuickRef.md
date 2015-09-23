JavaDude Tools->[Annotations](Annotations.md)
# Annotations Quick Reference #
## @Bean Annotation ##
```
import com.javadude.annotation.Access;
import com.javadude.annotation.Bean;
import com.javadude.annotation.Delegate;
import com.javadude.annotation.NullObject;
import com.javadude.annotation.Observer;
import com.javadude.annotation.Property;
import com.javadude.annotation.PropertyKind;
 
@Bean(
    cloneable=true, // default:false
        // implements Cloneable and defines clone() method
 
    createPropertyMap=true, // default:false
        // generates Map<String, Object> getPropertyMap()
    
    defineSimpleEqualsAndHashCode=true, // default:false
        // generates equals() and hashCode() methods
    equalsShouldCheckSuperEquals=true, // default:false
        // generated equals() calls superclass equals()
    
    overrideParamString=true,  // default:false
        // @Override added to generated paramString() method
    
    superclass=SomeType.class,
    superclassString="x.y.z.SomeType",
        // adds extends clause to generated superclass    
        // optionally specify one of these; default:[ignored]
    
    superConstructorArgs="Type x, Type y, Type z", // default:""
        // parameters for superclass constructor
    superConstructorSuperCall="x,y,z", // default: ""
        // super(...) call arguments for superclass constructor
    
    reader=Access.NONE, // or PACKAGE, PROTECTED, PUBLIC - default: PUBLIC
        // default access for property reader methods
        // NONE means that no reader methods will be generated
    writer=Access.NONE, // or PACKAGE, PROTECTED, PUBLIC - default: PUBLIC
        // default access for property writer methods
        // NONE means that no writer methods will be generated
    
    delegates= {
        // objects to delegate to
        @Delegate(
            accessor="getDelegate()", // default:[ignored]
                // explicit means to access the delegate instance
            
            addOverrides=true, // default:false
                // adds @Override to generated methods
            
            instantiateAs=ImplementationOfSomeInterface.class,
            instantiateAsString="x.y.z.ImplementationOfSomeInterface<Foo>",
                // explicitly creates an instance to delegate to
                // default:[ignored]
            
            property="delegate", // default:[ignored]
                // property that holds the delegate instance
            
            type=SomeInterface.class,
            typeString="x.y.z.SomeInterface")
                // type that defines the delegation methods to generate
                // must specify exactly one of these two attributes
    },
    
    nullObjectImplementations = {
        // "do nothing" methods to be implemented
        @NullObject(
            addOverrides=true, // default:false
                // adds @Override to generated methods
            
            type=SomeInterface.class,
            typeString="x.y.z.SomeInterface")
                // type that defines the null methods to generate
                // must specify exactly one of these two attributes
    },
    
    observers = {
        // observer pattern handling generation
        @Observer(
            addOverrides=true,  // default:false
                // adds @Override to generated methods
                
            type=SomeInterface.class,
            typeString="x.y.z.SomeInterface")
                // observer type to register, generate method calls
                // must specify exactly one of these two attributes
    },
    
    properties = {
        // JavaBean property generation
        @Property(
            name="name", // required
            // name of property
                
            type=SomeType.class,
            typeString="x.y.z.SomeType",

            bound=true,  // default:false
                // generates PropertyChangeListener observer support
                // fires propertyChange(...) events from setter methods
            
            keyType=SomeType.class,
            keyTypeString="x.y.z.SomeType<Foo>",
                // type of the keys in a MAP/UNMODIFIABLE_MAP property
                // exactly one of these required for MAP/UNMODIFIABLE_MAP properties
                // default:[ignored]
            
            kind=PropertyKind.SIMPLE, // or MAP, SIMPLE, SET, UNMODIFIABLE_LIST, UNMODIFIABLE_MAP, UNMODIFIABLE_SET
                // kind of property code to generate
                //   SIMPLE: single value property
                //   LIST: list of values property
                //   SET: set of values property
                //   MAP: map of key/value pairs property
                //   UNMODIFIABLE_LIST: same as LIST, but collection "get" returns unmodifiableList
                //   UNMODIFIABLE_MAP: same as MAP, but collection "get" returns unmodifiableMap
                //   UNMODIFIABLE_SET: same as SET, but collection "get" returns unmodifiableMap
                // default: SIMPLE
            
            omitFromToString=true, // default:false
                // do not include in the generated toString() method
            
            plural="names", // default:[ignored]
                // for non-SIMPLE kinds, an explicit name for the collection getter(s)
            
            reader=Access.NONE, // or PACKAGE, PROTECTED, PUBLIC
                // access modifier for generated reader methods
                //   use NONE to generate no readers
                // default:[@Bean.reader() value]
            writer=Access.NONE // or PACKAGE, PROTECTED, PUBLIC
                // access modifier for generated writer methods
                //   use NONE to generate no writers
                // default:[@Bean.writer() value]
 
            isStatic=true, // default:false
                // defines the generated fields and methods for this property as static
 
            isSynchronized=true, // default:false
                // defines the generated methods for this property as synchronized
 
            notNull=true, // default:false
                // adds != null check for simple properties, throwing IllegalArgumentException if violated
                // note this check always happens for list, set and map properties
        ),
    }
)    
 
public class Sample extends SampleGen {
    // for a class named XXXX, declaration must extend XXXXGen
 
    public void someMethod(int x, @Default("10") int y, @Default("Hello") String s) {
        // non-@Default parameters cannot appear after @Default parameters
        // You cannot use @Default on a static method
        // If you use @Default on a non @Bean, it will be ignored
        ...
    }
}
```

## @ExtractInterface Annotation ##
```
package com.javadude.annotation.example;
 
import com.javadude.annotation.Expose;
import com.javadude.annotation.ExtractInterface;
 
@ExtractInterface(
    exposeAllPublicMethods=true,  // default:false
        // if true, all public methods in the annotated class will
        //   be added to the generated interface
        
    name="InterfaceName",
        // the name of the interface to generate. Right now, it
        //   will be generated in the same package as the class
                  
    nonPublic=true,  // default:false
        // if true, the generated interface will not be public
                  
    superinterfaces={"x.y.z.Interface1","x.y.z.Interface2"}
        // a list of interfaces for the generated interface to extend
)
 
public class AnotherSample extends AnotherSampleGen {
    // for a class named XXXX, declaration must extend XXXXGen
 
    @Expose public void someMethod() {}
        // if exposeAllPublicMethods=false, add @Expose to indicate 
        //   which methods to add to the generated interface
}
```
JavaDude Tools->[Annotations](Annotations.md)