package adapters.rmi;

import models.Component;
import models.Components;
import models.datatypes.Count;
import models.datatypes.DataTypes;
import models.datatypes.Log;
import pheme.api.ComponentType;
import pheme.api.dtos.CountDTO;
import pheme.api.dtos.DTO;
import pheme.api.dtos.LogDTO;

public class DTOMapper {
	public static void create(DTO dto){
		try{
			DataTypes type = DataTypes.is(dto.getDataType());
			Component component = Component.findOrCreate(dto.getComponentName(), mapComponentType(dto.getComponentType()));
			switch (type) {
			case LOG:
				LogDTO log = (LogDTO) dto;
				Log.create(component, log.getType(), log.getMessage());
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
	
	private static Components mapComponentType(ComponentType type) {
		switch (type) {
		case JOB:
			return Components.JOB;
		case COMPUTER:
			return Components.COMPUTER;
		default:
			return Components.COMPUTER;
		}
	}
}
