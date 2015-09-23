/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Scott Stanchfield - initial API and implementation
 *******************************************************************************/
package com.javadude.annotation;


/**
 * <p>Defines a type (interface or class) for which you want to generate delegation methods.</p>
 * <p>Delegation methods for all public methods in the target type will be created.</p>
 *
 * <p>You specify the property attribute to indicate a target object to delegate calls to. If
 * this property has been defined via a &#064;Property annotation, its field will be used to access
 * the method. For example:</p>
 * <pre>&#064;Bean(delegates={&#064;Delegate(type="javax.swing.table.TableModel", property="model")},
 *        properties={&#064;Property(name="model", type="javax.swing.table.TableModel")})
 *  public class Foo extends FooGen implements TableModel {...}</pre>
 * <p>will generate code like the following:</p>
 * <pre>  public class FooGen {
 *      private javax.swing.table.TableModel model_;
 *      public void setModel(javax.swing.table.TableModel model) { model_ = model; }
 *      public javax.swing.table.TableModel getModel() { return model_; }
 *      public int getRowCount() { return model_.getRowCount(); }
 *      // and so on for other methods in javax.swing.table.TableModel
 *  }</pre>
 *
 * <p>If a property is not defined, you can use the instantiateAs() attribute to
 * define the instance to delegate to:</p>
 * <pre>&#064;Bean(delegates={&#064;Delegate(type="javax.swing.table.TableModel", property="model", instantiateAs=MyTableModel.class)})
 *  public class Foo extends FooGen implements TableModel {...}</pre>
 * <p>will generate code like the following:</p>
 * <pre>  public class FooGen {
 *      private javax.swing.table.TableModel model_ = new MyTableModel();
 *      public int getRowCount() { return model_.getRowCount(); }
 *      // and so on for other methods in javax.swing.table.TableModel
 *  }</pre>
 *
 * <p>If you have another means to access the item to delegate to (for example, it's
 * defined in a superclass or accessible via a Singleton), you can specify the accessor
 * attribute <i>instead of</i> the property attribute:</p>
 * <pre>&#064;Bean(delegates={&#064;Delegate(type="javax.swing.table.TableModel", accessor="Utils.getModel()")})
 *  public class Foo extends FooGen implements TableModel {...}</pre>
 * <p>will generate code like the following:</p>
 * <pre>  public class FooGen {
 *      public int getRowCount() { return Utils.getModel().getRowCount(); }
 *      // and so on for other methods in javax.swing.table.TableModel
 *  }</pre>
 *
 * <p>If you would like to have &#064;Override annotations added to generated methods,
 * specify addOverrides=true.</p>
 *
 * <p>You can specify as many delegations as you would like. Note that currently there
 * is no checking if the same method is defined in multiple delegations. For example, if
 * you defined</p>
 * <pre>  &#064;Bean(delegates={&#064;Delegate(property="a", type="Foo"),
 *                   &#064;Delegate(property="b", type="Fee")},
 *        ...)</pre>
 * <p>and Foo and Fee both defined method plah(), the generated code would contain
 * two plah() methods, causing a compile-time error. I'll try to fix this in later
 * releases.</p>
 */
public @interface Delegate {
    /**
     * An accessor expression that retrieves the delegation target.
     */
    String accessor();

    /**
     * The target interface type of the delegation.
     * Exactly one of type or typeString must be specified.
     */
    Class<?> type() default Void.class;

    /**
     * The target interface type of the delegation.
     * Exactly one of type or typeString must be specified.
     */
    String typeString() default "";
}
