package com.ecommerce.sb_ecom.exceptions;



public class ResourceNotFoundException extends RuntimeException {
//    private static final long serialVersionUID = 1L;

    private  String resourceName;
    private  String fieldName;
    private  String fieldValue;
    private  Long fieldValueInt;

    public ResourceNotFoundException(){}

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldValueInt) {
        super(String.format("%s not found with %s: %d", resourceName, fieldName, fieldValueInt));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValueInt = fieldValueInt;
    }

    // Optional getters
    public String getResourceName() { return resourceName; }
    public String getFieldName() { return fieldName; }
    public String getFieldValue() { return fieldValue; }
}
