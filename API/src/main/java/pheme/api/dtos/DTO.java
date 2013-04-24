package pheme.api.dtos;

import java.io.Serializable;

import pheme.api.ComponentType;


public abstract class DTO implements Serializable{
	private static final long serialVersionUID = 2227151883169667020L;
	final String componentName;
	final ComponentType componentType;
	final String dataType;
	
	public DTO(String componentName, ComponentType componentType, String dataType) {
		this.componentName = componentName;
		this.componentType = componentType;
		this.dataType = dataType;
	}
	
	public String getComponentName(){
		return this.componentName;
	}
	public ComponentType getComponentType(){
		return this.componentType;
	}
	public String getDataType(){
		return this.dataType;
	}
}
