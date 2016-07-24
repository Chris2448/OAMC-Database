package controllers;

import java.sql.Time;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import play.data.Form;
import play.data.format.Formatters;
import play.mvc.*;
import models.*;
import views.html.*;

public class Main extends Controller {

	public static Form<Services> form = form(Services.class);
	
	public static Staff employee;
	public static Doctors doctor;
	
	public static Result index(int employeeId)
	{
		Integer staffId;
		
		try {
			staffId = EmployeeIds.find.where().eq("EmployeeID", employeeId).findUnique().staff.employeeId;
		}
		catch(Exception e) {
			staffId = 0;
		}
		
		// See if it's a staff
		if(Staff.find.where().eq("ID", staffId).findRowCount() == 1)
		{
			employee = Staff.find.where().eq("ID", staffId).findUnique();
		
			// get the position of that staff
			Positions staffPos = Positions.find.where().eq("ID", employee.position.id).findUnique();
			
			// get the title of that staff
			Titles staffTitle = Titles.find.where().eq("ID", staffPos.title.id).findUnique();
			
			//Admin/Director
			if(staffTitle.name.equals(Constants.Admin) || staffTitle.name.equals(Constants.Director))
			{
				NumberFormat formatter = NumberFormat.getCurrencyInstance();
				String sal = formatter.format(employee.salary);
				
				String unit = employee.position.title.byHour == true ? "Hour" : "Year";
				
				return ok(mainIndex.render(sal, unit));
			}
			
			// Nurse
			else
			{
				Positions nursePosition = Positions.find.where().eq("ID", employee.position.id).findUnique();
				
				//Typical Nurse
				if(nursePosition.name.equals(Constants.Typical))
				{
					Staff nurse = Staff.find.where().eq("ID", staffId).findUnique();
					
					NumberFormat formatter = NumberFormat.getCurrencyInstance();
					String sal = formatter.format(nurse.salary);
					
					// Nurse
					// 29118
					// rYaxMasG
					List<Shifts> allShifts = Shifts.find.where().eq("StaffID", nurse.employeeId).findList();
					
					List<Shifts> shifts = new ArrayList<Shifts>();
					
					for(int i=0 ; i<allShifts.size() ; i++)
					{
						Boolean addIt = true;
						for(int j=0 ; j<shifts.size(); j++)
						{
							if(allShifts.get(i).day.id == shifts.get(j).day.id)
							{
								addIt = false;
								break;
							}
						}
						
						if (addIt)
						{
							shifts.add(allShifts.get(i));
						}
					}
					
					List<StaffServiceAssignments> ssa = StaffServiceAssignments.find.where().eq("StaffID", nurse.employeeId).findList();
					
					List<ServiceSchedules> ss = new ArrayList<ServiceSchedules>();
					for(int i=0 ; i<ssa.size() ; i++)
					{
						ss.add(ssa.get(i).serviceSchedule);
					}
					
					return ok(nurseIndex.render(shifts, ss, sal, ""));
				}
				// It's supervisor shift
				else
				{
					Staff nurse = Staff.find.where().eq("ID", staffId).findUnique();
					
					NumberFormat formatter = NumberFormat.getCurrencyInstance();
					String sal = formatter.format(nurse.salary);
					
					return ok(superNurseMain.render(sal, "Hour"));
				}
			}
		}
		// It's a doctor
		else
		{
			int doctorId = EmployeeIds.find.where().eq("EmployeeID", employeeId).findUnique().doctor.employeeId;
			
			// 3: Senior Doctor
			// 92064
			// gmodAMH
			if(Doctors.find.where().eq("ID", doctorId).eq("DoctorTypeID", 3).findRowCount() == 1)
			{
				doctor = Doctors.find.where().eq("ID", doctorId).findUnique();
				
				List<ServiceSchedules> ss = getSS();
				
				return ok(doctorIndex.render(ss));
			}
			// It's a Resident/Intern
			else
			{
				doctor = Doctors.find.where().eq("ID", doctorId).findUnique();
				
				NumberFormat formatter = NumberFormat.getCurrencyInstance();
				String sal = formatter.format(doctor.salary);
				
				// Intern
				// 97322
				// fnN4AhA2Xw
				List<Shifts> shifts = getShifts();
				List<ServiceSchedules> ss = getSS();
				
				return ok(riIndex.render(shifts, ss, sal, ""));
			}
		}
	}
	
	public static Result nurseSchedule()
	{
		List<Shifts> allShifts = Shifts.find.where().gt("StaffID", 0).findList();
		
		List<Shifts> nurseShifts = new ArrayList<Shifts>();
		for(int i=0 ; i<allShifts.size(); i++)
		{
			if(allShifts.get(i).staff.position.id == 1 &&
			   allShifts.get(i).staff.position.title.id == 1 &&
			   allShifts.get(i).staff.unit.id == employee.unit.id)
			{
				nurseShifts.add(allShifts.get(i));
			}
		}
		
		return ok(superNurseIndex.render(nurseShifts));
	}
	
	public static Result patientSection()
	{
		List<ServiceSchedules> allServiceSchedules = ServiceSchedules.find.findList();
		List<ServiceRooms> allRooms = ServiceRooms.find.orderBy("Number").findList();
		List<Patients> allPatients = Patients.find.orderBy("FirstName").findList();

		return ok(patientInfoIndex.render(allServiceSchedules, "", allRooms, allPatients));
	}
	
	private static List<Patients> findModifiableP()
	{
		List<StaffServiceAssignments> ssa = StaffServiceAssignments.find
				.where().eq("DoctorID", doctor.employeeId).findList();

		List<ServiceSchedules> ss = ServiceSchedules.find.findList();
		
		List<Patients> modifiablePatients = new ArrayList<Patients>();
		for(int i=0 ; i<ss.size(); i++)
		{
			for(int j=0 ; j<ssa.size(); j++)
			{
				if(ss.get(i).id == ssa.get(j).serviceSchedule.id)
				{
					modifiablePatients.add(ss.get(i).patient);
					break;
				}
			}
		}
		
		return modifiablePatients;
	}
	
	private static List<Patients> findRemainigP(List<Patients> modifiablePatients)
	{
		List<Patients> allPatients = Patients.find.findList();
		List<Patients> remainingPatients = new ArrayList<Patients>();
		for(int i=0 ; i<allPatients.size() ; i++)
		{
			Boolean addPatient = true;
			
			for(int j=0 ; j<modifiablePatients.size() ; j++)
			{
				if(allPatients.get(i).id == modifiablePatients.get(j).id)
				{
					addPatient = false;
					break;
				}
			}
			
			if(addPatient)
			{
				remainingPatients.add(allPatients.get(i));
			}
		}
		
		return remainingPatients;
	}
	
	public static Result allPatients()
	{
		List<Patients> modifiablePatients = findModifiableP();
		List<Patients> remainingPatients = findRemainigP(modifiablePatients);
		
		Patients emptyPatient = new Patients();
		emptyPatient.id = -1;
		
		return ok(patientIndex.render(modifiablePatients, remainingPatients, emptyPatient, ""));
	}
	
	public static Result modifyPatient(int patientId)
	{
		Patients patient = Patients.find.where().eq("ID", patientId).findUnique();
		
		List<Patients> modifiablePatients = findModifiableP();
		List<Patients> remainingPatients = findRemainigP(modifiablePatients);
		
		return ok(patientIndex.render(modifiablePatients, remainingPatients, patient, ""));
	}
	
	public static Result submitPatientChanges(int patientId)
	{
		Form<Patients> filledForm = form(Patients.class).bindFromRequest();
		
		Patients currentPatientInfo = Patients.find.where().eq("ID", patientId).findUnique();
		
		Patients patient = new Patients();
		patient.id = Integer.valueOf(filledForm.data().get("id"));
		patient.firstName = filledForm.data().get("firstName");
		patient.lastName = filledForm.data().get("lastName");
		patient.validMedicareCard = Integer.valueOf(filledForm.data().get("validMedicareCard")) == 1 ? true : false;
		patient.validHospitalCard = Integer.valueOf(filledForm.data().get("validHospitalCard")) == 1 ? true : false;
		patient.phoneNumber = filledForm.data().get("phoneNumber");
		patient.address = filledForm.data().get("address");
		try {
		patient.birthDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(filledForm.data().get("birthDate")).getTime());
		}
		catch(Exception e) {
			return badRequest();
		}
		patient.unit = currentPatientInfo.unit;
		
		Patients.update(patient);
		
		List<Patients> modifiablePatients = findModifiableP();
		List<Patients> remainingPatients = findRemainigP(modifiablePatients);
		
		Patients emptyPatient = new Patients();
		emptyPatient.id = -1;
		
		return ok(patientIndex.render(modifiablePatients, remainingPatients, emptyPatient, ""));
	}
	
	public static Result seeJuniorDr()
	{
		// 3: Senior
		List<Doctors> juniors = Doctors.find.where().ne("DoctorTypeID", 3).findList();
		
		return ok(juniorDrIndex.render(juniors, "", doctor.employeeId));
	}
	
	public static Result modifyJunior(int juniorId)
	{
		Doctors junior = Doctors.find.where().eq("ID", juniorId).findUnique();
		
		junior.seniorSupervisorId = doctor;
		
		Doctors.update(junior);
		
		List<Doctors> juniors = Doctors.find.where().ne("DoctorTypeID", 3).findList();
		String msg = junior.firstName + " " + junior.lastName + " has been Assigned to " + doctor.firstName + " " + doctor.lastName;
		
		return ok(juniorDrIndex.render(juniors, msg, doctor.employeeId));
	}
	
	private static List<Shifts> getShifts()
	{
		List<Shifts> allShifts = Shifts.find.where().eq("DoctorID", doctor.employeeId).findList();
		
		List<Shifts> shifts = new ArrayList<Shifts>();
		
		for(int i=0 ; i<allShifts.size() ; i++)
		{
			Boolean addIt = true;
			for(int j=0 ; j<shifts.size(); j++)
			{
				if(allShifts.get(i).day.id == shifts.get(j).day.id)
				{
					addIt = false;
					break;
				}
			}
			
			if (addIt)
			{
				shifts.add(allShifts.get(i));
			}
		}
		
		return shifts;
	}
	
	private static List<ServiceSchedules> getSS()
	{
		List<StaffServiceAssignments> ssa = StaffServiceAssignments.find.where().eq("DoctorID", doctor.employeeId).findList();
		
		List<ServiceSchedules> ss = new ArrayList<ServiceSchedules>();
		for(int i=0 ; i<ssa.size() ; i++)
		{
			ss.add(ssa.get(i).serviceSchedule);
		}
		
		return ss;
	}
	
	public static Result doctorSchedule()
	{
		List<Shifts> shifts = getShifts();
		List<ServiceSchedules> ss = getSS();
		
		return ok(doctorScheduleIndex.render(shifts, new ArrayList<StaffServiceAssignments>(), ss, ""));
	}
	
	public static Result cancelAppointment(int ssId)
	{
		ServiceSchedules.delete(ssId);
		
		List<Shifts> shifts = getShifts();
		List<ServiceSchedules> ss = getSS();
		
		return ok(doctorScheduleIndex.render(shifts, new ArrayList<StaffServiceAssignments>(), ss, ""));
	}
	
	public static Result doctorReports()
	{
		// 1: interns
		List<Doctors> interns = Doctors.find.where().eq("DoctorTypeID", 1).orderBy("SurgeryAssistance DESC").findList();
		
		return ok(doctorReportIndex.render(interns, ""));
	}
	
	public static Result allStaff()
	{
		List<Staff> allStaff = Staff.find.findList();
		
		List<Doctors> allDoctors = Doctors.find.findList();
		
		return ok(staffIndex.render(allStaff, allDoctors, Collections.<Shifts>emptyList()));
	}
	
	public static Result staffSchedules()
	{
		List<Shifts> allShifts = Shifts.find.findList();
		
		return ok(staffIndex.render(Collections.<Staff>emptyList(), Collections.<Doctors>emptyList(), allShifts));
	}
	
	public static Result allServices()
	{
		List<Services> allServices = Services.find.where().eq("UnitID", employee.unit.id).findList();
		List<Units> allUnits = Units.find.findList();
		
		return ok(servicesIndex.render(allServices, "", allUnits));
	}
	
	public static Result addService()
	{	
		Form<Services> filledForm = form(Services.class).bindFromRequest();
		Form<Units> filledFormU = form(Units.class).bindFromRequest();
		
		if(filledForm.get().name.isEmpty())
		{
			return badRequest(servicesIndex.render(Services.find.findList(), 
												   "Please enter the name of the service", 
												   Units.find.findList()));
		}
		
		int unitId = filledFormU.get().id;
		Units unit = Units.find.where().eq("ID", unitId).findUnique();
		
		Services service = filledForm.get();
		service.unit = unit;
		service.id = 0;
		service.fee = !filledForm.data().get("fee").isEmpty() ? Double.valueOf(filledForm.data().get("fee")) : null;
		
		Services.create(service);
		
		return redirect(routes.Main.allServices());
	}
	
	public static Result deleteService(int id)
	{
		Services.delete(id);
		return redirect(routes.Main.allServices());
	}
	
	public static Result serviceSchedules()
	{
		List<ServiceSchedules> allServiceSchedules = ServiceSchedules.find.findList();
		List<ServiceRooms> allRooms = ServiceRooms.find.orderBy("Number").findList();
		List<Patients> allPatients = Patients.find.orderBy("FirstName").findList();

		return ok(serviceSchedulesIndex.render(allServiceSchedules, "", allRooms, allPatients));
	}
	
	public static Result addSurgerySchedule()
	{
		Form<ServiceSchedules> filledFormS = form(ServiceSchedules.class).bindFromRequest();
		Form<ServiceRooms> filledFormR = form(ServiceRooms.class).bindFromRequest();
		Form<Patients> filledFormP = form(Patients.class).bindFromRequest();
		

		int roomId = filledFormR.get().roomId;
		ServiceRooms room = ServiceRooms.find.where().eq("ID", roomId).findUnique();
		
		int patientId = filledFormP.get().id;
		Patients patient = Patients.find.where().eq("ID", patientId).findUnique();
		
		ServiceSchedules serviceSchedule = new ServiceSchedules();
		serviceSchedule.service = Services.find.where().eq("Name","Surgery").findUnique();
		serviceSchedule.room = room;
		serviceSchedule.patient = patient;
		try {
			serviceSchedule.date = new java.sql.Date(new SimpleDateFormat("yyyy/MM/dd").parse(filledFormS.data().get("date")).getTime());
			serviceSchedule.start = new java.sql.Time(new SimpleDateFormat("kk:mm").parse(filledFormS.data().get("start")).getTime());
			serviceSchedule.finish = new java.sql.Time(new SimpleDateFormat("kk:mm").parse(filledFormS.data().get("finish")).getTime());
		}
		catch(Exception e) {
			return badRequest();
		}
		
		List<ServiceSchedules> chosenDateSchedule = ServiceSchedules.find.where().eq("Date", serviceSchedule.date).findList();
		
		Boolean canSchedule = true;
		
		for(int i=0 ; i<chosenDateSchedule.size() ; i++)
		{
			if(chosenDateSchedule.get(i).room.roomId != serviceSchedule.room.roomId)
			{
				continue;
			}
			
			Time mySt = serviceSchedule.start;
			Time St = chosenDateSchedule.get(i).start;
			
			Time myFi = serviceSchedule.finish;
			Time Fi = chosenDateSchedule.get(i).finish;
			
			if(mySt.after(St) && mySt.before(Fi) ||
			   myFi.before(Fi) && myFi.after(St) ||
			   mySt.before(St) && myFi.after(Fi) ||
			   mySt.equals(St) && myFi.equals(Fi))
			{
				canSchedule = false;
				break;
			}
		}
		
		if(canSchedule)
		{			
			ServiceSchedules.create(serviceSchedule);
			
			List<ServiceSchedules> allServiceSchedules = ServiceSchedules.find.findList();
			List<ServiceRooms> allRooms = ServiceRooms.find.orderBy("Number").findList();
			List<Patients> allPatients = Patients.find.orderBy("FirstName").findList();
			
			return ok(serviceSchedulesIndex.render(allServiceSchedules, "", allRooms, allPatients));
		}
		else
		{
			List<ServiceSchedules> allServiceSchedules = ServiceSchedules.find.findList();
			List<ServiceRooms> allRooms = ServiceRooms.find.orderBy("Number").findList();
			List<Patients> allPatients = Patients.find.orderBy("FirstName").findList();
			
			return badRequest(serviceSchedulesIndex.render(allServiceSchedules, "The chosen room is reserved at this time.", allRooms, allPatients));
		}
	}

	public static Result supplies()
	{
		List<Object> allSupplies = Supplies.find.where().eq("UnitID", employee.unit.id).findIds();
		List<SupplyItems> allItems = SupplyItems.find.where().in("SupplyID", allSupplies).findList();
		
		return ok(suppliesIndex.render(allItems, ""));
	}
	
	public static Result orderItem()
	{
		List<Object> allSupplies = Supplies.find.where().eq("UnitID", employee.unit.id).findIds();
		List<SupplyItems> allItems = SupplyItems.find.where().in("SupplyID", allSupplies).findList();
		
		Form<SupplyItems> filledForm = form(SupplyItems.class).bindFromRequest();
		
		SupplyItems item = SupplyItems.find.where().eq("ID", filledForm.data().get("id")).findUnique();
		
		int newQuantity = filledForm.get().quantity;
		int oldQuantity = item.quantity;
		
		if(newQuantity + oldQuantity > item.capacity)
		{
			return badRequest(suppliesIndex.render(allItems, "Capacity Exceeded!"));
		}
		
		double percent = (double)oldQuantity/item.capacity;
		if(percent > 0.1)
		{
			return badRequest(suppliesIndex.render(allItems, "We have more than 10% stock"));
		}
		
		item.quantity += newQuantity;
		SupplyItems.create(item);
		
		allSupplies = Supplies.find.where().eq("UnitID", employee.unit.id).findIds();
		allItems = SupplyItems.find.where().in("SupplyID", allSupplies).findList();
		
		return ok(suppliesIndex.render(allItems, ""));
	}
	
	private static List<String> getMonths()
	{
		List<String> months = new ArrayList<String>();
		months.add("January");months.add("February");months.add("March");months.add("April");
		months.add("May");months.add("June");months.add("July");months.add("August");months.add("September");
		months.add("October");months.add("November");months.add("December");
		
		return months;
	}
	
	private static List<Integer> getYears()
	{
		int maxY=0,minY=0,maxM,minM;
		
		Date maxYearDate = ServiceSchedules.find.orderBy("Date DESC").findList().get(0).date;
		Date minYearDate = ServiceSchedules.find.orderBy("Date ASC").findList().get(0).date;
		
		DateFormat dfsql = new SimpleDateFormat("dd/MM/yyyy");
		String maxYear = dfsql.format(maxYearDate);
		String minYear = dfsql.format(minYearDate);
		
        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final Calendar c = Calendar.getInstance();
        
        try {
            c.setTime(df.parse(maxYear));
            maxY = c.get(Calendar.YEAR);
            
            c.setTime(df.parse(minYear));
            minY = c.get(Calendar.YEAR);
        }
        catch(Exception e)
        {}
        
        List<Integer> years = new ArrayList<Integer>();
        
        for(int i=minY; i<=maxY; i++)
        {
        	years.add(i);
        }
        
        return years;
	}
	
	public static Result reports()
	{
		List<String> months = getMonths();
		List<Integer> years = getYears();
		
		return ok(reportsIndex.render(months, years, "", new ArrayList<StaffServiceAssignments>()));
	}
	
	public static Result getReport()
	{
		List<String> months = getMonths();
		List<Integer> years = getYears();
		
		Map<String, Integer> monthToNumber = fillMap();
		
		Form<Object> filledForm = form(Object.class).bindFromRequest();

		List<Object> filteredSchedules = ServiceSchedules.find.where()
												  .eq("YEAR(Date)", filledForm.data().get("year"))
												  .eq("MONTH(Date)",monthToNumber.get(filledForm.data().get("month")))
												  .findIds();
		
		List<StaffServiceAssignments> ssa = StaffServiceAssignments.find
											.where().gt("DoctorID", 0)
											.in("ServiceScheduleID", filteredSchedules)
											.findList();
		
		
		return ok(reportsIndex.render(months, years, "", ssa));
	}
	
	private static Map<String, Integer> fillMap()
	{
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		map.put("January", 1);
		map.put("February", 2);
		map.put("March", 3);
		map.put("April", 4);
		map.put("May", 5);
		map.put("June", 6);
		map.put("July", 7);
		map.put("August", 8);
		map.put("September", 9);
		map.put("October", 10);
		map.put("November", 11);
		map.put("December", 12);
		
		return map;
	}
	
}