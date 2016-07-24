package models;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="Positions")
public class Positions extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="TitleID")
	public Titles title;
	
	@Column(name="Name")
	@NotNull
	public String name;
	
	@Column(name="BaseSalary")
	@NotNull
	public double baseSalary;

	@Column(name="Raise")
	public Double raise;
	
	@Column(name="RaiseFrequencyInYear")
	public int raiseFrequencyInYear;
	
	@Column(name="OvertimeSalary")
	public Double overtimeSalary;
	
	public static Finder<Integer,Positions> find = new Finder<Integer,Positions>(
			Integer.class, Positions.class
	);
}