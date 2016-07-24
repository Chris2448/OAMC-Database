package models;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;

import com.avaje.ebean.validation.NotNull;

import controllers.Constants;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="Doctors")
public class Doctors extends Model {

	@Id
	@Column(name="ID")
	public int employeeId;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="DoctorTypeID")
	public DoctorTypes doctorType;

	@NotNull
	@ManyToOne
	@JoinColumn(name="UnitID")
	public Units unit;
	
	@ManyToOne
	@JoinColumn(name="ServiceScheduleID")
	public ServiceSchedules serviceSchedule;
	
	// 1 - Intern 2,3,4 - Resident 4< Senior
	@Column(name="Experience")
	@NotNull
	public int experience;
	
	@Column(name="LastName")
	@NotNull
	public String lastName;

	@Column(name="FirstName")
	@NotNull
	public String firstName;
	
	@Column(name="Salary")
	public Double salary;
	
	@OneToOne
	@JoinColumn(name="SeniorSupervisorID")
	public Doctors seniorSupervisorId;
	
	@Column(name="SurgeryAssistance")
	public Integer surgeryAssistance;
	
	@Column(name="HireDate")
	@NotNull
	public Date hireDate;
	
	public static Finder<Integer,Doctors> find = new Finder<Integer,Doctors>(
			Integer.class, Doctors.class
	);
	
	public String getTitle() {
		
		return "Doctor (" + doctorType.name + ")";
	}
	
	public String getJuniorTitle() {
		
		return doctorType.name;
	}
	
	public String getJuniorSenior() {
		
		return seniorSupervisorId.firstName + " " + seniorSupervisorId.lastName;
	}
	
	public String getName() {
		return firstName + " " + lastName;
	}
	
	public String getUnit() {
		
		return unit.name;
	}
	
	public String getHireDate() {
		
		return new SimpleDateFormat("dd-MM-yyyy").format(hireDate);
	}
	
	public String getSalary() {
		
		if (salary == null)
		{
			return "Per visit";
		}
		else
		{
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			return formatter.format(salary) + "/year";
		}
	}
	
	public String getEmployeeId() {
		
		return String.valueOf(EmployeeIds.find.where().eq("DoctorID", employeeId).findUnique().employeeId);
	}
	
	public int sameSenior(int seniorId) {
		
		return seniorSupervisorId.employeeId == seniorId ? 1 : 0;
	}
	
	public static void update(Doctors doctor)
	{
		doctor.update();
	}
}