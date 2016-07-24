package models;

import java.sql.Date;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;

@Entity
@Table(name="PreviousVisits")
public class PreviousVisits extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="PatientID")
	public Patients patient;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="ServiceID")
	public Services service;
	
	@Column(name="VisitDate")
	@NotNull
	public Date visitDate;
}