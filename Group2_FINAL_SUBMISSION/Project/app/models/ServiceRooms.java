package models;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="ServiceRooms")
public class ServiceRooms extends Model {

	@Id
	@Column(name="ID")
	public int roomId;
	
	@Column(name="Number")
	@NotNull
	public int number;
	
	public static Finder<Integer,ServiceRooms> find = new Finder<Integer,ServiceRooms>(
			Integer.class, ServiceRooms.class
	);
}