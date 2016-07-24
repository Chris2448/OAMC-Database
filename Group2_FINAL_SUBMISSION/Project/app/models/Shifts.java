package models;

import java.sql.Time;
import java.text.SimpleDateFormat;

import javax.persistence.*;

import com.avaje.ebean.annotation.Formula;
import com.avaje.ebean.validation.*;

import controllers.Constants;
import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="Shifts")
public class Shifts extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@Column(name="Begin")
	@NotNull
	public Time begin;
	
	@Column(name="End")
	@NotNull
	public Time end;
	
	@Column(name="Length")
	@NotNull
	public double length;
	
	// 1-7 sun-sat
	@NotNull
	@OneToOne
	@JoinColumn(name="Day")
	public Days day;
	
	@ManyToOne
	@JoinColumn(name="StaffID")
	public Staff staff;
	
	@ManyToOne
	@JoinColumn(name="DoctorID")
	public Doctors doctor;
	
	public static Finder<Integer,Shifts> find = new Finder<Integer,Shifts>(
			Integer.class, Shifts.class
	);
	
    public String getEmployeeId() {
    	
    	if(staff != null)
    	{
    		return String.valueOf(EmployeeIds.find.where().eq("StaffID", staff.employeeId).findUnique().employeeId);
    	}
    	else
    	{
    		return String.valueOf(EmployeeIds.find.where().eq("DoctorID", doctor.employeeId).findUnique().employeeId);
    	}
    }
    
    public String getName() {
    	
    	if(staff != null)
    	{
    		return staff.firstName + " " + staff.lastName;
    	}
    	else
    	{
    		return doctor.firstName + " " + doctor.lastName;
    	}
    }
    
    public String detDays() {
    	return day.name;
    }
    
    public String getTime() {
    	return new SimpleDateFormat("HH:mm").format(begin) + " to " + new SimpleDateFormat("HH:mm").format(end);
    }
    
    public String getTitle() {
    	
    	if(staff != null)
    	{
    		String title = staff.position.title.name;
    		String pos = staff.position.name;
    		
    		if (pos.equals(Constants.Typical))
    		{
    			return title;
    		}
    		else
    		{
    			return title + " (" + pos + ")";
    		}
    	}
    	else
    	{
    		return "Doctor (" + doctor.doctorType.name + ")";
    	}
    }
}