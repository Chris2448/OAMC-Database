package models;

import javax.persistence.*;

import com.avaje.ebean.validation.NotNull;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name="EmployeeIds")
public class EmployeeIds extends Model {

	@Id
	@Column(name="EmployeeID")
	public int employeeId;
	
	@Column(name="Password")
	@Required
	@NotNull
	public String password;

	@ManyToOne
	@JoinColumn(name="StaffID")
	public Staff staff;

	@ManyToOne
	@JoinColumn(name="DoctorID")
	public Doctors doctor;
	
	public static Finder<Integer,EmployeeIds> find = new Finder<Integer,EmployeeIds>(
			Integer.class, EmployeeIds.class
	);
}