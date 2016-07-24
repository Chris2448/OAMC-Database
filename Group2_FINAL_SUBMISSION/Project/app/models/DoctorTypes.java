package models;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;

@Entity
@Table(name="DoctorTypes")
public class DoctorTypes extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@Column(name="Name")
	@NotNull
	public String name;
	
	//weekly
	@Column(name="PaymentFrequency")
	@NotNull
	public String paymentFrequency;
	
	@Column(name="ByYear")
	@NotNull
	public Boolean byYear;
	
	@Column(name="BaseSalary")
	public Double baseSalary;
	
	@Column(name="Raise")
	public Double raise;
}