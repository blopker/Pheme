package adapters.rmi;

import models.Component;
import models.Components;
import models.datatypes.Count;
import models.datatypes.DataTypes;
import models.datatypes.Log;
import adapters.rmi.api.DTOs.CountDTO;
import adapters.rmi.api.DTOs.DTO;
import adapters.rmi.api.DTOs.LogDTO;

public class DTOMapper {
	public static void map(DTO dto){
		try{
			DataTypes type = DataTypes.is(dto.getDataType());
			Component component = Component.get(dto.getSenderName(), Components.COMPUTER);
			switch (type) {
			case LOG:
				LogDTO log = (LogDTO) dto;
				Log.create(log.getSenderName(), log.getType(), log.getMessage());
				break;
			case COUNT:
				CountDTO count = (CountDTO) dto;
				Count.create(component, count.getName(), count.getCountToAdd());
				break;
			default:
				break;
			}
		} catch (IllegalArgumentException e){
			System.out.println(DTOMapper.class.getSimpleName() + ": Data type from RMI not recognized, " + dto.getDataType());
		}
	}
}
