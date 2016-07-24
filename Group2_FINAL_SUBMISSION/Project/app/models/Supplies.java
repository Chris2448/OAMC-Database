package models;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="Supplies")
public class Supplies extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="SupplyTypeID")
	public SupplyTypes supplyType;
	
	@Column(name="IsRoom")
	@NotNull
	public Boolean isRoom;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="UnitID")
	public Units unit;
	
	@Column(name="Name")
	@NotNull
	public String name;

	public static Finder<Integer,Supplies> find = new Finder<Integer,Supplies>(
			Integer.class, Supplies.class
	);
}