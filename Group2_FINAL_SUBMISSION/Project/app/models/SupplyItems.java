package models;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="SupplyItems")
public class SupplyItems extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@NotNull
	@Column(name="Name")
	public String name;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="SupplyID")
	public Supplies supply;
	
	@NotNull
	@Column(name="Quantity")
	public int quantity;
	
	@Column(name="Capacity")
	@NotNull
	public int capacity;

	public static Finder<Integer,SupplyItems> find = new Finder<Integer,SupplyItems>(
			Integer.class, SupplyItems.class
	);
	
	public String getSubCat()
	{
		return supply.name;
	}
	
	public String getCat()
	{
		return supply.supplyType.name;
	}
	
	public static void create(SupplyItems item)
	{
		item.save();
	}
}