package models;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="Titles")
public class Titles extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@Column(name="Name")
	@NotNull
	public String name;
	
	//bi-weekly
	@Column(name="PaymentFrequency")
	@NotNull
	public String paymentFrequency;
	
	@Column(name="ByHour")
	@NotNull
	public Boolean byHour;
	
	@Column(name="WeekLimitInHour")
	@NotNull
	public double weekLimitInHour;
	
	public static Finder<Integer,Titles> find = new Finder<Integer,Titles>(
			Integer.class, Titles.class
	);
}