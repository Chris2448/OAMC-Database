package models;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;

@Entity
@Table(name="Vendors")
public class Vendors extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="SupplyTypeID")
	public SupplyTypes supplyType;
	
	@Column(name="Name")
	@NotNull
	public String name;
	
	@Column(name="Address")
	@NotNull
	public String address;
	
	@Column(name="PhoneNumber")
	@NotNull
	public String phoneNumber;
}