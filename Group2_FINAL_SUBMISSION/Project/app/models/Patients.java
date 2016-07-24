package models;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.data.validation.Constraints.*;
import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="Patients")
public class Patients extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="UnitID")
	public Units unit;
	
	@Column(name="BirthDate")
	@NotNull
	public Date birthDate;
	
	@Column(name="Address")
	@NotNull
	public String address;
	
	@Column(name="PhoneNumber")
	@NotNull
	public String phoneNumber;
	
	@Column(name="ValidMedicareCard")
	@NotNull
	public Boolean validMedicareCard;
	
	@Column(name="ValidHospitalCard")
	@NotNull
	public Boolean validHospitalCard;
	
	@Column(name="LastName")
	@NotNull
	public String lastName;
	
	@Column(name="FirstName")
	@NotNull
	public String firstName;
	
	public static Finder<Integer,Patients> find = new Finder<Integer,Patients>(
			Integer.class, Patients.class
	);
	
	public String getName()
	{
		return firstName + " " + lastName;
	}
	
	public String getBirthday()
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(birthDate);
	}
	
	public String getmCard()
	{
		if(validMedicareCard)
		{
			return "Yes";
		}
		else
		{
			return "No";
		}
	}
	
	public String gethCard()
	{
		if(validHospitalCard)
		{
			return "Yes";
		}
		else
		{
			return "No";
		}
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String getValidMedicare()
	{
		if(validMedicareCard)
		{
			return "checked";
		}
		else
		{
			return "";
		}
	}
	
	public String getInvalidMedicare()
	{
		if(!validMedicareCard)
		{
			return "checked";
		}
		else
		{
			return "";
		}
	}
	
	public String getValidHospital()
	{
		if(validHospitalCard)
		{
			return "checked";
		}
		else
		{
			return "";
		}
	}
	
	public String getInvalidHospital()
	{
		if(!validHospitalCard)
		{
			return "checked";
		}
		else
		{
			return "";
		}
	}
	
	public static void update(Patients patient)
	{
		patient.update();
	}
}