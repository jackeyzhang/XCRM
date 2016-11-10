package com.xcrm.common;


public class AttributeJsonWrap {
  
  public Object values;
  
  public Object attributes;

  public AttributeJsonWrap(Object _values, Object _attributes) {
    this.values= _values;
    this.attributes = _attributes;
  }

  
  public Object getValues() {
    return values;
  }

  
  public void setValues( Object values ) {
    this.values = values;
  }

  
  public Object getAttributes() {
    return attributes;
  }

  
  public void setAttributes( Object attributes ) {
    this.attributes = attributes;
  }
  
  

}
