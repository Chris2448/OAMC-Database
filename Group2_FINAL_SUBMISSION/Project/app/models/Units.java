package models;

import javax.persistence.*;

import com.avaje.ebean.validation.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="Units")
public class Units extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@Column(name="Name")
	@NotNull
	public String name;
	
	public static Finder<Integer,Units> find = new Finder<Integer,Units>(
			Integer.class, Units.class
	);
}