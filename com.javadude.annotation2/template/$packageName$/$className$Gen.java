//CODE GENERATED BY JAVADUDE BEAN ANNOTATION PROCESSOR
// -- DO NOT EDIT  -  THIS CODE WILL BE REGENERATED! --
package $packageName$;
// The following are dummy definitions to get this template to compile								//#DUMMY
class $superclass$ {																				//#DUMMY
	interface PropertyNames {}																		//#DUMMY
	public java.util.Map<java.lang.String, java.lang.Object> createPropertyMap() { return null; }	//#DUMMY
	String paramString() {return null;}																//#DUMMY
}																									//#DUMMY
class $name$ {																						//#DUMMY
	public $returnType$ $name_2$() { return null;}													//#DUMMY
}																									//#DUMMY
class $instantiateType$ extends $name$ {}															//#DUMMY
class $className$ {}																				//#DUMMY
class $type$ {}																						//#DUMMY
class $keyType$ {}																					//#DUMMY
class $returnType$ {}																				//#DUMMY
class $PARENT_name$ {																				//#DUMMY
	public void $name$() {}																			//#DUMMY
}																									//#DUMMY
@java.lang.SuppressWarnings("all")
/**
 * Generated superclass for $packageName$.$className$.
 * This class was generated by the JavaDude Bean Annotation Processor.
 * You are free to distribute this generated code under any terms you
 * 	would like.
 **/
/*$classModifiers$*/abstract class $className$Gen/*$genericDecls$*/
		extends $superclass$/*$genericDecls$*/			//? superclass
		implements Cloneable							//? cloneable
{
	//#FOREACH superclassConstructors
	/*$modifiers$*//*$genericDecls$*/$className$Gen(/*$argDecls$*/) /*$throwsClause$*/{
		super(/*$args$*/);
	}
	//#END
	//#IF cloneable
	/**	<p>Create a shallow copy of the <code>$className$</code>.</p>
	 *	@return the copy
	 **/
	public $className$ clone() {
		try {
			return ($className$) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("This cannot happen; this class implements Cloneable");
		}
	}
	//#END
	//#IF definePropertyChangeSupport
	private java.beans.PropertyChangeSupport propertyChangeSupport_ = new java.beans.PropertyChangeSupport(this);
	protected java.beans.PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport_;
	}
	/**
	 * <p>Add a property change listener to the <code>$className$</code>. This listener
	 * 	will be notified whenever the values of the bound properties
	 * 	in this class change.</p>
	 *
	 * @param listener The listener to register.
	 **/
	public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
		getPropertyChangeSupport().addPropertyChangeListener(listener);
	}
	/**
	 * <p>Add a property change listener to the <code>$className$</code>. This listener
	 * 	will be notified whenever the value of the specified property
	 * 	in this class changes.</p>
	 *
	 * @param propertyName The name of the property to watch. Note that
	 * 			the	listener will only be notified if the property is bound.
	 * @param listener The listener to register.
	 **/
	public void addPropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
		getPropertyChangeSupport().addPropertyChangeListener(propertyName, listener);
	}
	/**
	 * <p>Remove a property change listener from this <code>$className$</code>.</p>
	 * @param listener The listener to remove.
	 **/
	public void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
		getPropertyChangeSupport().removePropertyChangeListener(listener);
	}
	/**
	 * <p>Remove a property change listener from this <code>$className$</code>.</p>
	 * @param listener The listener to remove.
	 **/
	public void removePropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
		getPropertyChangeSupport().removePropertyChangeListener(propertyName, listener);
	}
	//#END
	//#IF definePropertyNameConstants
	/**
	 *	<p>An interface that holds the names of the properties defined
	 * 	in class <code>$className$</code>. This is useful when registering for property
	 * 	change events or checking which property has changed in
	 * 	a property change notification. This helps reduce typos
	 * 	when specifying the property names.</p>
	 *	<p>For example:</p>
	 * 	<code>$LOWER_className$.addPropertyChangeListener($className$.PropertyNames.$firstPropertyName$, ...);</code>
	 **/
	public interface PropertyNames
	extends $superclass$.PropertyNames						//? propertyNameConstantsInherited
	{
		//#FOREACH properties
		/** <p>The name of the $name$ property. Use when registering for property change events to avoid typos.</p> **/
		static final String $name$ = "$name$";
		//#END
	}
	//#END
	//#FOREACH properties
	private /*$extraFieldKeywords$*/$type$ $name$_;																							//? simple
	private /*$extraFieldKeywords$*/final java.util.List<$type$> $pluralName_list$_ = new java.util.ArrayList<$type$>();					//? list
	private /*$extraFieldKeywords$*/final java.util.Set<$type$> $pluralName_set$_ = new java.util.HashSet<$type$>();						//? set
	private /*$extraFieldKeywords$*/final java.util.Map<$keyType$, $type$> $pluralName_map$_ = new java.util.HashMap<$keyType$, $type$>();	//? map
	//#IF simple
	/**																											//? readable
	 *	<p>Get the value of property $name$.</p>	 															//? readable
	 *	<p>Property: $name$</br>																				//? readable
	 *  Type: $type$</br>																						//? readable
	 *  Bound (fires property change): $bound$</p>																//? readable
	 *  @return the value of property $name$.																	//? readable
	 **/																										//? readable
	/*$readerAccess$$extraMethodKeywords$*/$type$ get$UPPER_name$() { return $name$_; }							//? readable & !boolean
	/*$readerAccess$$extraMethodKeywords$*/$type$ is$UPPER_name$() { return $name$_; }							//? readable & boolean
	/**																											//? writeable
	 *	<p>Set the value of property $name$.</p>																//? writeable
	 *	<p>Property: $name$</br>																				//? writeable
	 *	Type: $type$</br>																						//? writeable
	 *  Bound (fires property change): $bound$</p>																//? writeable
	 *  <p>If calling this method changes the value of $name$ (to a value different than its current value)		//? writeable & bound
	 *  or null) a property change event will be fired to any registered property change listeners	</p>		//? writeable & bound
	 *  @param value the new value for property $name$.															//? writeable
	 *  	This value cannot be null; if so, an IllegalArgumentException will be thrown						//? writeable & notNull
	 *	@throws IllegalArgumentException if the value is null													//? writeable & notNull
	 **/																										//? writeable
	/*$writerAccess$$extraMethodKeywords$*/void set$UPPER_name$($type$ value) {									//? writeable
		if (value == null) {																					//? writeable & notNull
			throw new IllegalArgumentException("$name$ cannot be null");										//? writeable & notNull
		}																										//? writeable & notNull
		$type$ oldValue = $name$_;																				//? writeable & bound
		$name$_ = value;																						//? writeable
		getPropertyChangeSupport().firePropertyChange("$name$", oldValue, value);								//? writeable & bound
	}																											//? writeable
	//#END if simple
	//#IF list
	/**																											//? readable
	 *	<p>Get one of the values of list property $name$.</p>													//? readable
	 *	<p>Property: $name$</br>																				//? readable
	 *  Type: List&lt;$type$&gt;</br>																			//? readable
	 *  Bound (fires property change): $bound$</p>																//? readable
	 *  @param the index of the property value to retrieve 														//? readable
	 *  @return the specified value of property $name$.															//? readable
	 **/																										//? readable
	/*$readerAccess$$extraMethodKeywords$*/$type$ get$UPPER_name$(int i) { return $pluralName_list$_.get(i); }	//? readable & !boolean
	/*$readerAccess$$extraMethodKeywords$*/$type$ is$UPPER_name$(int i) { return $pluralName_list$_.get(i); }	//? readable & boolean
	/**																											//? readable
	 *	<p>Get all of the values of list property $name$.</p>													//? readable
	 *	<p>Property: $name$</br>																				//? readable
	 *  Type: List&lt;$type$&gt;</br>																			//? readable
	 *  Bound (fires property change): $bound$</p>																//? readable
	 *  @return an unmodifiable List containing all values of property $name$.									//? readable
	 **/																										//? readable
	/*$readerAccess$$extraMethodKeywords$*/java.util.List<$type$> get$UPPER_pluralName$() {						//? readable
		return java.util.Collections.unmodifiableList($pluralName_list$_);										//? readable
	}																											//? readable
	/**																											//? writeable
	 *	<p>Remove a value from list property $name$.</p>														//? writeable
	 *	<p>Property: $name$</br>																				//? writeable
	 *  Type: List&lt;$type$&gt;</br>																			//? writeable
	 *  Bound (fires property change): $bound$</p>																//? writeable
	 *  <p>A property change event will be fired to any registered property change listeners</p>				//? writeable & bound
	 *  @param i the index of the value to remove.																//? writeable
	 *  @return the removed value.																				//? writeable
	 **/																										//? writeable
	/*$writerAccess$$extraMethodKeywords$*/$type$ remove$UPPER_name$(int i) {									//? writeable
		$type$ result = $pluralName_list$_.remove(i);															//? writeable
		getPropertyChangeSupport().firePropertyChange("$name$", null, $pluralName_list$_);						//? writeable & bound
		return result;																							//? writeable
	}																											//? writeable
	/**																											//? writeable
	 *	<p>Add a value to list property $name$.</p>																//? writeable
	 *	<p>Property: $name$</br>																				//? writeable
	 *  Type: List&lt;$type$&gt;</br>																			//? writeable
	 *  Bound (fires property change): $bound$</p>																//? writeable
	 *  <p>A property change event will be fired to any registered property change listeners</p>				//? writeable & bound
	 *  @param i the position into which the item will be added.												//? writeable
	 *  @param value the value to insert.																		//? writeable
	 **/																										//? writeable
	/*$writerAccess$$extraMethodKeywords$*/void add$UPPER_name$(int i, $type$ value) {							//? writeable
		if (value == null) {																					//? writeable
			throw new IllegalArgumentException("Cannot add null to $name$");									//? writeable
		}																										//? writeable
		$pluralName_list$_.add(i, value);																		//? writeable
		getPropertyChangeSupport().firePropertyChange("$name$", null, $pluralName_list$_);						//? writeable & bound
	}																											//? writeable
	//#END if list
	//#IF set
	/**																											//? readable
	 *	<p>Get all values of set property $name$.</p>															//? readable
	 *	<p>Property: $name$</br>																				//? readable
	 *  Type: Set&lt;$type$&gt;</br>																			//? readable
	 *  Bound (fires property change): $bound$</p>																//? readable
	 *  @return an unmodifiable set of the property values.														//? readable
	 **/																										//? readable
	/*$readerAccess$$extraMethodKeywords$*/java.util.Set<$type$> get$UPPER_pluralName_set$() {					//? readable
		return java.util.Collections.unmodifiableSet($pluralName_set$_);										//? readable
	}																											//? readable
	//#END if set
	//#IF list | set
	/**																											//? readable
	 *	<p>Returns whether the list property $name$ contains the specified value.</p>							//? readable & list
	 *	<p>Returns whether the set property $name$ contains the specified value.</p>							//? readable & set
	 *	<p>Property: $name$</br>																				//? readable
	 *  Type: List&lt;$type$&gt;</br>																			//? readable & list
	 *  Type: Set&lt;$type$&gt;</br>																			//? readable & set
	 *  Bound (fires property change): $bound$</p>																//? readable
	 *  @param value the value to test for containment															//? readable
	 *  @return true if $name$ contains the specified value														//? readable
	 **/																										//? readable
	/*$readerAccess$$extraMethodKeywords$*/boolean $pluralName$Contains($type$ value) {							//? readable
		return $pluralName_list$_.contains(value);																//? readable
	}																											//? readable
	/**																											//? writeable
	 *	<p>Add a value to list property $name$.</p>																//? writeable & list
	 *	<p>Add a value to set property $name$.</p>																//? writeable & set
	 *	<p>Property: $name$</br>																				//? writeable
	 *  Type: List&lt;$type$&gt;</br>																			//? writeable & list
	 *  Type: Set&lt;$type$&gt;</br>																			//? writeable & set
	 *  Bound (fires property change): $bound$</p>																//? writeable
	 *  <p>A property change event will be fired to any registered property change listeners</p>				//? writeable & bound
	 *  @param value the value to insert.																		//? writeable
	 **/																										//? writeable
	/*$writerAccess$$extraMethodKeywords$*/void add$UPPER_name$($type$ value) {									//? writeable
		if (value == null) {																					//? writeable
			throw new IllegalArgumentException("Cannot add null to $name$");									//? writeable
		}																										//? writeable
		$pluralName_list$_.add(value);																			//? writeable
		getPropertyChangeSupport().firePropertyChange("$name$", null, $pluralName_list$_);						//? writeable & bound
	}																											//? writeable
	/**																											//? writeable
	 *	<p>Remove a value from list property $name$.</p>														//? writeable & list
	 *	<p>Remove a value from set property $name$.</p>															//? writeable & set
	 *	<p>Property: $name$</br>																				//? writeable
	 *  Type: List&lt;$type$&gt;</br>																			//? writeable & list
	 *  Type: Set&lt;$type$&gt;</br>																			//? writeable & set
	 *  Bound (fires property change): $bound$</p>																//? writeable
	 *  <p>A property change event will be fired to any registered property change listeners</p>				//? writeable & bound
	 *  @param value the value to remove.																		//? writeable
	 **/																										//? writeable
	/*$writerAccess$$extraMethodKeywords$*/boolean remove$UPPER_name$($type$ value) {							//? writeable
		if (value == null) {																					//? writeable
			throw new IllegalArgumentException("Cannot remove null from $name$");								//? writeable
		}																										//? writeable
		boolean result = $pluralName_list$_.remove(value);														//? writeable
		getPropertyChangeSupport().firePropertyChange("$name$", null, $pluralName_list$_);						//? writeable & bound
		return result;																							//? writeable
	}																											//? writeable
	/**																											//? writeable
	 *	<p>Clear all values from list property $name$.</p>														//? writeable & list
	 *	<p>Clear all values from set property $name$.</p>														//? writeable & set
	 *	<p>Property: $name$</br>																				//? writeable
	 *  Type: List&lt;$type$&gt;</br>																			//? writeable & list
	 *  Type: Set&lt;$type$&gt;</br>																			//? writeable & set
	 *  Bound (fires property change): $bound$</p>																//? writeable
	 *  <p>A property change event will be fired to any registered property change listeners</p>				//? writeable & bound
	 **/																										//? writeable
	/*$writerAccess$$extraMethodKeywords$*/void clear$UPPER_pluralName$() {										//? writeable
		$pluralName_list$_.clear();																				//? writeable
		getPropertyChangeSupport().firePropertyChange("$name$", null, $pluralName_list$_);						//? writeable & bound
	}																											//? writeable
	//#END if list | set
	//#IF map
	/**																											//? readable
	 *	<p>Gets a keyed value from map property $name$.</p>														//? readable
	 *	<p>Property: $name$</br>																				//? readable
	 *  Type: Map&lt;$keyType$,$type$&gt;</br>																	//? readable
	 *  Bound (fires property change): $bound$</p>																//? readable
	 *  @param the key of the property value to retrieve 														//? readable
	 *  @return the specified value of property $name$.															//? readable
	 **/																										//? readable
	/*$readerAccess$$extraMethodKeywords$*/$type$ get$UPPER_name$($keyType$ key) {								//? readable
		return $pluralName_map$_.get(key);																		//? readable
	}																											//? readable
	/**																											//? readable
	 *	<p>Get all the values of map property $name$.</p>														//? readable
	 *	<p>Property: $name$</br>																				//? readable
	 *  Type: Map&lt;$keyType$,$type$&gt;</br>																	//? readable
	 *  Bound (fires property change): $bound$</p>																//? readable
	 *  @return an unmodifiable map containing all values of property $name$.									//? readable
	 **/																										//? readable
	/*$readerAccess$$extraMethodKeywords$*/java.util.Map<$keyType$, $type$> get$UPPER_pluralName_map$() {		//? readable
		return java.util.Collections.unmodifiableMap($pluralName_map$_);										//? readable
	}																											//? readable
	/**																											//? readable
	 *	<p>Returns whether the map property $name$ contains the specified key.</p>								//? readable
	 *	<p>Property: $name$</br>																				//? readable
	 *  Type: Map&lt;$keyType$,$type$&gt;</br>																	//? readable
	 *  Bound (fires property change): $bound$</p>																//? readable
	 *  @param key the key to test.																				//? readable
	 *  @return true if $name$ contains the specified key.														//? readable
	 **/																										//? readable
	/*$readerAccess$$extraMethodKeywords$*/boolean $pluralName$ContainsKey($keyType$ key) {						//? readable
		return $pluralName_map$_.containsKey(key);																//? readable
	}																											//? readable
	/**																											//? readable
	 *	<p>Returns whether the map property $name$ contains the specified value.</p>							//? readable
	 *	<p>Property: $name$</br>																				//? readable
	 *  Type: Map&lt;$keyType$,$type$&gt;</br>																	//? readable
	 *  Bound (fires property change): $bound$</p>																//? readable
	 *  @param value the value to test.																			//? readable
	 *  @return true if $name$ contains the specified value.													//? readable
	 **/																										//? readable
	/*$readerAccess$$extraMethodKeywords$*/boolean $pluralName$ContainsValue($type$ value) {					//? readable
		return $pluralName_map$_.containsValue(value);															//? readable
	}																											//? readable
	/**																											//? writeable
	 *	<p>Put a key/value pair into map property $name$.</p>													//? writeable
	 *	<p>Property: $name$</br>																				//? writeable
	 *  Type: Map&lt;$keyType$,$type$&gt;</br>																	//? writeable
	 *  Bound (fires property change): $bound$</p>																//? writeable
	 *  <p>A property change event will be fired to any registered property change listeners</p>				//? writeable & bound
	 *  @param key the key to insert.																			//? writeable
	 *  @param value the value to insert.																		//? writeable
	 **/																										//? writeable
	/*$writerAccess$$extraMethodKeywords$*/void put$UPPER_name$($keyType$ key, $type$ value) {					//? writeable
		if (key == null) {																						//? writeable
			throw new IllegalArgumentException("Cannot put null key in $name$");								//? writeable
		}																										//? writeable
		if (value == null) {																					//? writeable
			throw new IllegalArgumentException("Cannot put null value in $name$");								//? writeable
		}																										//? writeable
		$pluralName_map$_.put(key, value);																		//? writeable
		getPropertyChangeSupport().firePropertyChange("$name$", null, $pluralName_map$_);						//? writeable & bound
	}																											//? writeable
	/**																											//? writeable
	 *	<p>Remove a key/value pair from map property $name$.</p>												//? writeable
	 *	<p>Property: $name$</br>																				//? writeable
	 *  Type: Map&lt;$keyType$,$type$&gt;</br>																	//? writeable
	 *  Bound (fires property change): $bound$</p>																//? writeable
	 *  <p>A property change event will be fired to any registered property change listeners</p>				//? writeable & bound
	 *  @param key the key to remove.																			//? writeable
	 *  @return the value that was removed.																		//? writeable
	 **/																										//? writeable
	/*$writerAccess$$extraMethodKeywords$*/$type$ remove$UPPER_name$($keyType$ key) {							//? writeable
		if (key == null) {																						//? writeable
			throw new IllegalArgumentException("Cannot remove null key from $name$");							//? writeable
		}																										//? writeable
		$type$ result = $pluralName_map$_.remove(key);															//? writeable
		getPropertyChangeSupport().firePropertyChange("$name$", null, $pluralName_map$_);						//? writeable & bound
		return result;																							//? writeable
	}																											//? writeable
	/**																											//? writeable
	 *	<p>Clear all keys/values from map property $name$.</p>													//? writeable
	 *	<p>Property: $name$</br>																				//? writeable
	 *  Type: Map&lt;$keyType$,$type$&gt;</br>																	//? writeable
	 *  Bound (fires property change): $bound$</p>																//? writeable
	 *  <p>A property change event will be fired to any registered property change listeners</p>				//? writeable & bound
	 **/																										//? writeable
	/*$writerAccess$$extraMethodKeywords$*/void clear$UPPER_pluralName_map$() {									//? writeable
		$pluralName_map$_.clear();																				//? writeable
		getPropertyChangeSupport().firePropertyChange("$name$", null, $pluralName_map$_);						//? writeable & bound
	}																											//? writeable
	//#END if map
	//#END foreach properties
	//#FOREACH delegates
	//#FOREACH methods
	/** Delegate for $PARENT_name$.$name$($argDecls$).
	 * 	@see $PARENT_name$.$name$($argDecls$)
	 **/
	/*$modifiers$*/abstract $returnType$ $name_1$(/*$argDecls$*/)/*$throwsClause$*/;							//? abstract
	/*$modifiers$*/$returnType$ $name_2$(/*$argDecls$*/)/*$throwsClause$*/ {									//? !abstract
		$accessor$.$name_2$(/*$args$*/);																		//? !abstract & !returns
		return $accessor$.$name_2$(/*$args$*/); 																//? !abstract & returns
	}																											//? !abstract
	//#END foreach methods
	//#END foreach delegates
	private $returnType$ $nullBody$;																			//#DUMMY
	private $name$ $accessor$;																					//#DUMMY
	//#FOREACH nullObjects
	//#FOREACH methods
	/** Null implementation for $PARENT_name$.$name$($argDecls$).
	 * 	@see $PARENT_name$.$name$($argDecls$)
	 **/
	public $returnType$ $name_3$(/*$argDecls$*/)/*$throwsClause$*/ {
		/*$nullBody$*/;																							//? !returns
		return $nullBody$;																						//? returns
	}
	//#END foreach methods
	//#END foreach observers
	//#FOREACH observers
	private java.util.List<$name$> $NOPACKAGE_LOWER_name$s_ = new java.util.ArrayList<$name$>();
	/** Register a $NOPACKAGE_name$ to listener for notifications from this class.
	 * 	@see $name$
	 **/
	public void add$NOPACKAGE_name$($name$ listener) {
		synchronized($NOPACKAGE_LOWER_name$s_) {
			$NOPACKAGE_LOWER_name$s_.add(listener);
		}
	}
	/** Unregister a $NOPACKAGE_name$ to listener to stop notifications from this class.
	 * 	@see $name$
	 **/
	public void remove$NOPACKAGE_name$($name$ listener) {
		synchronized($NOPACKAGE_LOWER_name$s_) {
			$NOPACKAGE_LOWER_name$s_.remove(listener);
		}
	}
	private java.util.List<$PARENT_name$> $PARENT_NOPACKAGE_LOWER_name$s_; //#DUMMY
	//	TODO - what if the methods are declared to throw exceptions (like PropertyVetoException)?
	//#FOREACH methods
	/** Fire a $name$ notification to a registered $PARENT_name$.
	 * 	@see $PARENT_name$
	 * 	@see $PARENT_name$.$name$($argDecls$)
	 **/
	protected void fire$UPPER_name$(/*$argDecls$*/) {
		java.util.List<$PARENT_name$> targets = null;
		synchronized($PARENT_NOPACKAGE_LOWER_name$s_) {
			targets = new java.util.ArrayList<$PARENT_name$>($PARENT_NOPACKAGE_LOWER_name$s_);
		}
		for ($PARENT_name$ listener : targets) {
			listener.$name$(/*$args$*/);
		}
	}
	//#END foreach methods
	//#END foreach observers
	//#IF defineEqualsAndHashCode
	public boolean equals(java.lang.Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != getClass() || !super.equals(obj)) {								//? equalsAndHashCodeCallSuper
			return false;																						//? equalsAndHashCodeCallSuper
		}																										//? equalsAndHashCodeCallSuper
		if (obj == null || obj.getClass() != getClass()) {														//? !equalsAndHashCodeCallSuper
			return false;																						//? !equalsAndHashCodeCallSuper
		}																										//? !equalsAndHashCodeCallSuper
		$className$Gen other = ($className$Gen) obj;
		//#FOREACH properties
		if ($name$_ != other.$name$_) {																			//? primitive
			return false;																						//? primitive
		}																										//? primitive
		if ($name$_ == null) {																					//? !primitive & simple
			if (other.$name$_ != null) {																		//? !primitive & simple
				return false;																					//? !primitive & simple
			}																									//? !primitive & simple
		} else if (!$name$_.equals(other.$name$_)) {															//? !primitive & simple
			return false;																						//? !primitive & simple
		}																										//? !primitive & simple
		if ($pluralName_set$_ == null) {																		//? !primitive & !simple
			if (other.$pluralName_set$_ != null) {																//? !primitive & !simple
				return false;																					//? !primitive & !simple
			}																									//? !primitive & !simple
		} else if (!$pluralName_set$_.equals(other.$pluralName_set$_)) {										//? !primitive & !simple
			return false;																						//? !primitive & !simple
		}																										//? !primitive & !simple
		//#END foreach properties
		return true;
	}
	private boolean $name_boolean$_;																			//#DUMMY
	private int $name_int$_;																					//#DUMMY
	private long $name_long$_;																					//#DUMMY
	private float $name_float$_;																				//#DUMMY
	private double $name_double$_;																				//#DUMMY
	public int hashCode() {
		int result;
		result = 1;																								//? !equalsAndHashCodeCallSuper
		result = super.hashCode();																				//? equalsAndHashCodeCallSuper
		long temp;																								//? atLeastOneDouble
		//#FOREACH properties
		result = 31 * result + ($name_boolean$_ ? 1231 : 1237);													//? boolean
		result = 31 * result + $name_int$_;																		//? byte | char | short | int
		result = 31 * result + (int) ($name_long$_ ^ ($name_long$_ >>> 32));									//? long
		result = 31 * result + Float.floatToIntBits($name_float$_);												//? float
		temp = Double.doubleToLongBits($name_double$_);															//? double
		result = 31 * result + (int) (temp ^ (temp >>> 32));													//? double
		result = 31 * result + ($name$_ == null ? 0 : $name$_.hashCode());										//? !primitive & simple
		result = 31 * result + ($pluralName_list$_ == null ? 0 : $pluralName_list$_.hashCode());				//? !primitive & !simple
		//#END properties
		return result;
	}
	//#END defineSimpleEqualsAndHashCode
	public java.lang.String toString() {
		return getClass().getName() + '[' + paramString() + ']';
	}
	/**
	 *	<p>A hook method used to provide the attributes listed in the toString() method.</p>
	 *	<p>This method can be overridden by subclasses to append attributes defined
	 *		in the subclasses.</p>
	 *	<p>A subclass override might look like</p>
	 *	<pre>public String paramString() {
	 *	&nbsp;&nbsp;&nbsp;&nbsp;return super.paramString() + ",foo=" + foo + ",fee=" + fee&semi;
	 *  }</pre>
	 * @return the attribute string, comma-separated.
	 **/
	/*$paramStringModifiers$*/ java.lang.String paramString() {
		String result;
		result = super.paramString();																			//? paramStringInherited
		result = "";																							//? !paramStringInherited
		//#FOREACH properties
		result += ',';																							//? !FIRST
		result += "$name$=" + $name$_;																			//? simple & !omitFromToString
		result += "$pluralName$=" + $pluralName_list$_;															//? !simple & !omitFromToString
		result += "$name$=<" + $name$_.getClass().getName() + '>';												//? simple & omitFromToString
		result += "$pluralName$=<" + $pluralName_list$_.getClass().getName() + '>';								//? !simple & omitFromToString
		//#END foreach properties
		return result;
	}
	//#IF defineCreatePropertyMap
	/**
	 *	<p>Creates a Map containing the names/values of all properties defined in this
	 *		object.</p>
	 *	@return Map containing name/value of each property. Note that this map is
	 *				a new, writeable map.
	 **/
	public java.util.Map<java.lang.String, java.lang.Object> createPropertyMap() {
		java.util.Map<java.lang.String, java.lang.Object> map =
			new java.util.HashMap<java.lang.String, java.lang.Object>();										//? !createPropertyMapInherited
			super.createPropertyMap();																			//? createPropertyMapInherited
		//#FOREACH properties
		map.put("$name$", $name$_);																				//? readable & simple
		map.put("$pluralName$", $pluralName_list$_);															//? readable & !simple
		//#END foreach properties
		return map;
	}
	//#END defineCreatePropertyMap
}
