package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="StaffServiceAssignments")
public class StaffServiceAssignments extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="StaffID")
	public Staff staff;
	
	@ManyToOne
	@JoinColumn(name="DoctorID")
	public Doctors doctor;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="ServiceScheduleID")
	public ServiceSchedules serviceSchedule;
	
	public static Finder<Integer,StaffServiceAssignments> find = new Finder<Integer,StaffServiceAssignments>(
			Integer.class, StaffServiceAssignments.class
	);
	
	public String getServiceName()
	{
		return serviceSchedule.service.name;
	}
	
	public String getUnit()
	{
		return serviceSchedule.service.unit.name;
	}
	
	public String getDoctorNames()
	{
		return doctor.firstName + " " + doctor.lastName;
	}
	
	public String getDate()
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(serviceSchedule.date);
	}
}