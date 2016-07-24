package controllers;

import java.util.List;

import play.data.*;
import play.mvc.*;
import models.*;
import views.html.*;

public class Application extends Controller {

	public static Form<EmployeeIds> form = form(EmployeeIds.class);
	
	public static Result index() {
		return redirect(routes.Application.loginIndex());
	}
	
	public static Result loginIndex() {
		return ok(index.render(form, ""));
	}
	
	public static Result login() {
		Form<EmployeeIds> filledForm = form.bindFromRequest();
		
		if(filledForm.hasErrors())
		{
			return badRequest(index.render(form, "Please enter your Employee ID and Password"));
		}
		else
		{
			//Admin or Director
			//ID: 46675
			//Password: UhdKesPE
			int count = EmployeeIds.find
						.where().eq("EmployeeID", filledForm.get().employeeId)
						.eq("Password", filledForm.get().password)
						.findRowCount();
			
			if(count == 1)
			{
				return redirect(routes.Main.index(filledForm.get().employeeId));
			}
			else
			{
				return badRequest(index.render(form, "Wrong Employee ID and/or Password"));
			}
		}
	}
}