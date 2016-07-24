package models;

import javax.persistence.*;
import com.avaje.ebean.validation.*;

import play.db.ebean.*;

@Entity
@Table(name="Days")
public class Days extends Model {

	@Id
	@Column(name="ID")
	public int id;
	
	@Column(name="Name")
	@NotNull
	public String name;
}