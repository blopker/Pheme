package models.datatypes;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import controllers.EventBus;

@Entity
public class Count extends Model implements DataType{

	private static final long serialVersionUID = 1974692107443461977L;
		
	@Id
	@Constraints.Min(10)
	public String id = UUID.randomUUID().toString();

	@Constraints.Required
	public String sourceName;

	@Constraints.Required
	public String countName;

	@Constraints.Required
	public long count;

	@Formats.DateTime(pattern = "dd/MM/yyyy")
	public Date created = new Date();

	public static Finder<String, Count> find = new Finder<String, Count>(String.class,
			Count.class);

	public static DataType create(String sourceName, String countName, long addToCount) {
		
		Count count = Count.find.where().eq("sourceName", sourceName).eq("countName", countName).findUnique();
		if (count == null) {
			count = new Count();
			count.countName = countName;
			count.sourceName = sourceName;
			count.count = 0;
		}
		
		count.count += addToCount;
//		System.out.println("count: " + count.count + " " + count.sourceName);
//		System.out.println("counts: " + Count.find.all().size());
		EventBus.post(count);
		count.save();
		return count;
	}
	
	@Override
	public DataTypes getDataType() {
		return DataTypes.COUNT;
	}

}
