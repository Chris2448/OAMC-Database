package models;

import java.util.Date;
import java.sql.Time;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="ServiceSchedules")
public class ServiceSchedules extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="ServiceID")
	public Services service;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="RoomID")
	public ServiceRooms room;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="PatientID")
	public Patients patient;
	
	@Column(name="Date")
	@NotNull
	public Date date;
	
	@Column(name="Start")
	@NotNull
	public Time start;
	
	@Column(name="Finish")
	@NotNull
	public Time finish;
	
	public static Finder<Integer,ServiceSchedules> find = new Finder<Integer,ServiceSchedules>(
			Integer.class, ServiceSchedules.class
	);
	
	public String getFee()
	{
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(service.fee);
	}
	
	public String getServiceName()
	{
		return service.name;
	}
	
	public String getRoomNumber()
	{
		return String.valueOf(room.number);
	}
	
	public String getPatientName()
	{
		return patient.firstName + " " + patient.lastName;
	}
	
	public String getDate()
	{
		return new SimpleDateFormat("dd-MM-yyyy").format(date);
	}
	
	public String getStart()
	{
		return new SimpleDateFormat("HH:mm").format(start);
	}
	
	public String getEnd()
	{
		return new SimpleDateFormat("HH:mm").format(finish);
	}
	
	public static void create(ServiceSchedules serviceSchedule)
	{
		serviceSchedule.save();
	}
	
	public static void delete(int ssdId)
	{
		find.ref(ssdId).delete();
	}
}