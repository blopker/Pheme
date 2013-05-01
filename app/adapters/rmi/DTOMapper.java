package adapters.rmi;

import models.Component;
import models.Components;
import models.datatypes.Count;
import models.datatypes.DataTypes;
import models.datatypes.Gauge;
import models.datatypes.Log;
import pheme.api.ComponentType;
import pheme.api.dtos.CountDTO;
import pheme.api.dtos.DTO;
import pheme.api.dtos.GaugeDTO;
import pheme.api.dtos.LogDTO;

public class DTOMapper {
	public static void create(DTO dto){
		try{
			DataTypes type = DataTypes.is(dto.getDataType());
			switch (type) {
			case LOG:
				create((LogDTO) dto);
				break;
			case COUNT:
				create((CountDTO) dto);
				break;
			case GAUGE:
				create((GaugeDTO) dto);
				break;
			default:
				break;
			}
		} catch (IllegalArgumentException e){
			System.out.println(DTOMapper.class.getSimpleName() + ": Data type from RMI not recognized, " + dto.getDataType());
		}
	}
	
	public static void create(CountDTO count) {
		Count.create(creatComponent(count), count.getName(), count.getCountToAdd());
	}
	
	public static void create(LogDTO log) {
		Log.create(creatComponent(log), log.getType(), log.getMessage());
	}
	
	public static void create(GaugeDTO gauge) {
		Gauge.create(creatComponent(gauge), gauge.getName(), gauge.getGaugeValue());
	}
	
	private static Component creatComponent(DTO dto) {
		return Component.findOrCreate(dto.getComponentName(), mapComponentType(dto.getComponentType()));
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
