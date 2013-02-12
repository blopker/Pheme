package models.datatypes;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Id;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import controllers.EventBus;

public class Count extends Model implements DataType{

	private static final long serialVersionUID = 1974692107443461977L;
		
	@Id
	@Constraints.Min(10)
	public String id = UUID.randomUUID().toString();

	@Constraints.Required
	public String sourceName;

	@Constraints.Required
	public String counterName;

	@Constraints.Required
	public long addToCount;

	@Formats.DateTime(pattern = "dd/MM/yyyy")
	public Date created = new Date();

	public static Finder<String, Count> find = new Finder<String, Count>(String.class,
			Count.class);

	public static DataType create(String sourceName, String counterName, long addToCount) {
//		System.out.println("count: " + addToCount);
		Count count = Count.find.where().eq("sourceName", sourceName).eq("counterName", counterName).findUnique();
		if (count == null) {
			count = new Count();
			count.counterName = counterName.toLowerCase();
			count.sourceName = sourceName.toLowerCase();
			count.addToCount = 0;
		}
		count.addToCount += addToCount;
		count.save();
		EventBus.post(count);
		return count;
	}
	
	@Override
	public DataTypes getDataType() {
		return DataTypes.COUNT;
	}

}
