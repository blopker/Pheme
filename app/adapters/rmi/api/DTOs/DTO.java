package adapters.rmi.api.DTOs;

import java.io.Serializable;

public interface DTO extends Serializable{

	public String getSenderName();
	public String getSenderType();
	public String getDataType();
}
