package models;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import controllers.Constants;

import javax.persistence.*;

import com.avaje.ebean.validation.NotNull;

import play.db.ebean.Model;

@Entity
@Table(name="Staff")
public class Staff extends Model {

	@Id
	@Column(name="ID")
	public int employeeId;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="PositionID")
	public Positions position;

	@NotNull
	@ManyToOne
	@JoinColumn(name="UnitID")
	public Units unit;
	
	@Column(name="HireDate")
	@NotNull
	public Date hireDate;
	
	@Column(name="LastName")
	@NotNull
	public String lastName;

	@Column(name="FirstName")
	@NotNull
	public String firstName;
	
	@Column(name="Salary")
	@NotNull
	public double salary;
	
	@Column(name="OverTimeInHour")
	public Integer overTimeInHour;
	
	public static Finder<Integer,Staff> find = new Finder<Integer,Staff>(
			Integer.class, Staff.class
	);
	
	public String getTitle() {
		
		String title = position.title.name;
		String pos = position.name;
		
		if (pos.equals(Constants.Typical))
		{
			return title;
		}
		else
		{
			return title + " (" + pos + ")";
		}
	}
	
	public String getUnit() {
		
		return unit.name;
	}
	
	public String getHireDate() {
		
		return new SimpleDateFormat("dd-MM-yyyy").format(hireDate);
	}
	
	public String getSalary() {
		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String sal = formatter.format(salary);
		
		if (position.title.byHour)
		{
			return sal + "/hr";
		}
		else
		{
			return sal + "/year";
		}
	}
	
	public String getEmployeeId() {
		
		return String.valueOf(EmployeeIds.find.where().eq("StaffID", employeeId).findUnique().employeeId);
	}
}