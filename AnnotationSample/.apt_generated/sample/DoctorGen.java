// CODE GENERATED BY JAVADUDE BEAN ANNOTATION PROCESSOR 
// -- DO NOT EDIT  -  THIS CODE WILL BE REGENERATED! --
package sample;

@javax.annotation.Generated(
    value = "com.javadude.annotation.processors.BeanAnnotationProcessor", 
    date = "Wed Sep 23 15:29:55 EDT 2015", 
    comments = "CODE GENERATED BY JAVADUDE BEAN ANNOTATION PROCESSOR; DO NOT EDIT! THIS CODE WILL BE REGENERATED!")
public abstract class DoctorGen  {
    public DoctorGen() {
        ;
    }

    private sample.IPerson basePerson_;
    public sample.IPerson getBasePerson() {
        return basePerson_;
    }
    public void setBasePerson(sample.IPerson value)  {
        basePerson_ = value;
    }


    public java.lang.String getAddress() {
        return basePerson_.getAddress();
    }
    public java.lang.String getName() {
        return basePerson_.getName();
    }
    public java.lang.String getPhone() {
        return basePerson_.getPhone();
    }
    public void setAddress(java.lang.String value) {
        basePerson_.setAddress(value);
    }
    public void setName(java.lang.String value) {
        basePerson_.setName(value);
    }
    public void setPhone(java.lang.String value) {
        basePerson_.setPhone(value);
    }
    @Override
    public java.lang.String toString() {
        return getClass().getName() + '[' + paramString() + ']';
    }
    protected java.lang.String paramString() {
        return 
               "basePerson=" + basePerson_;
    }
}
