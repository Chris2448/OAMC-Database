package models;

import java.text.NumberFormat;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="Services")
public class Services extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@Column(name="Name")
	@NotNull
	public String name;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="UnitID")
	public Units unit;
	
	@Column(name="Fee")
	@Pattern(regex="^[0-9]*$")
	public Double fee;
	
	public static Finder<Integer,Services> find = new Finder<Integer,Services>(
			Integer.class, Services.class
	);
	
	public String getUnit() {
		
		return unit.name;
	}
	
	public String getFee() {
		
		if(fee != null)
		{
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			return formatter.format(fee);
		}
		else
		{
			return "Not Applicable";
		}
	}
	
	public static void create(Services service)
	{
		service.save();
	}
	
	public static void delete(int id)
	{
		find.ref(id).delete();
	}
}