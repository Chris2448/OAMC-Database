# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table Days (
  ID                        integer auto_increment not null,
  Name                      varchar(255) not null,
  constraint pk_Days primary key (ID))
;

create table DoctorTypes (
  ID                        integer auto_increment not null,
  Name                      varchar(255) not null,
  PaymentFrequency          varchar(255) not null,
  ByYear                    tinyint(1) default 0 not null,
  BaseSalary                double,
  Raise                     double,
  constraint pk_DoctorTypes primary key (ID))
;

create table Doctors (
  ID                        integer auto_increment not null,
  Password                  varchar(255) not null,
  DoctorTypeID              integer not null,
  UnitID                    integer not null,
  ShiftID                   integer not null,
  ServiceScheduleID         integer,
  Experience                integer not null,
  LastName                  varchar(255) not null,
  FirstName                 varchar(255) not null,
  Salary                    double,
  SeniorSupervisorID        integer,
  HireDate                  datetime not null,
  constraint pk_Doctors primary key (ID))
;

create table Patients (
  ID                        integer auto_increment not null,
  UnitID                    integer not null,
  BirthDate                 date not null,
  Address                   varchar(255) not null,
  PhoneNumber               varchar(255) not null,
  ValidMedicareCard         tinyint(1) default 0 not null,
  ValidHospitalCard         tinyint(1) default 0 not null,
  LastName                  varchar(255) not null,
  FirstName                 varchar(255) not null,
  constraint pk_Patients primary key (ID))
;

create table Positions (
  ID                        integer auto_increment not null,
  TitleID                   integer not null,
  Name                      varchar(255) not null,
  BaseSalary                double not null,
  Raise                     double,
  RaiseFrequencyInYear      integer,
  OvertimeSalary            double,
  constraint pk_Positions primary key (ID))
;

create table PreviousVisits (
  ID                        integer auto_increment not null,
  PatientID                 integer not null,
  ServiceID                 integer not null,
  VisitDate                 date not null,
  constraint pk_PreviousVisits primary key (ID))
;

create table ServiceRooms (
  ID                        integer auto_increment not null,
  Number                    integer not null,
  IsAvailable               tinyint(1) default 0 not null,
  constraint pk_ServiceRooms primary key (ID))
;

create table ServiceSchedules (
  ID                        integer auto_increment not null,
  ServiceID                 integer not null,
  RoomID                    integer not null,
  PatientID                 integer not null,
  Date                      date not null,
  Start                     time not null,
  Finish                    time not null,
  constraint pk_ServiceSchedules primary key (ID))
;

create table Services (
  ID                        integer auto_increment not null,
  Name                      varchar(255) not null,
  UnitID                    integer not null,
  constraint pk_Services primary key (ID))
;

create table Shifts (
  ID                        integer auto_increment not null,
  Begin                     time not null,
  End                       time not null,
  Length                    double not null,
  Day                       integer not null,
  StaffID                   integer,
  DoctorID                  integer,
  constraint pk_Shifts primary key (ID))
;

create table Staff (
  ID                        integer auto_increment not null,
  Password                  varchar(255) not null,
  PositionID                integer not null,
  UnitID                    integer not null,
  HireDate                  datetime not null,
  LastName                  varchar(255) not null,
  FirstName                 varchar(255) not null,
  Salary                    double not null,
  OverTimeInHour            integer,
  constraint pk_Staff primary key (ID))
;

create table StaffServiceAssignments (
  StaffID                   integer not null,
  ServiceScheduleID         integer not null)
;

create table StorageUnits (
  ID                        integer auto_increment not null,
  UnitID                    integer not null,
  Number                    integer not null,
  constraint pk_StorageUnits primary key (ID))
;

create table Supplies (
  ID                        integer auto_increment not null,
  SupplyTypeID              integer not null,
  SupplyRoomID              integer,
  StorageUnitID             integer,
  UnitID                    integer not null,
  Name                      varchar(255) not null,
  QuantityInPercentage      integer not null,
  constraint pk_Supplies primary key (ID))
;

create table SupplyItems (
  Name                      varchar(255) not null,
  SupplyID                  integer not null,
  constraint pk_SupplyItems primary key (Name))
;

create table SupplyRooms (
  ID                        integer auto_increment not null,
  Floor                     integer not null,
  CapacityInPercentage      integer not null,
  constraint pk_SupplyRooms primary key (ID))
;

create table SupplyTypes (
  ID                        integer auto_increment not null,
  Name                      varchar(255) not null,
  constraint pk_SupplyTypes primary key (ID))
;

create table Titles (
  ID                        integer auto_increment not null,
  Name                      varchar(255) not null,
  PaymentFrequency          varchar(255) not null,
  ByHour                    tinyint(1) default 0 not null,
  WeekLimitInHour           double not null,
  constraint pk_Titles primary key (ID))
;

create table Units (
  ID                        integer auto_increment not null,
  Name                      varchar(255) not null,
  constraint pk_Units primary key (ID))
;

create table Vendors (
  ID                        integer auto_increment not null,
  SupplyTypeID              integer not null,
  Name                      varchar(255) not null,
  Address                   varchar(255) not null,
  PhoneNumber               varchar(255) not null,
  constraint pk_Vendors primary key (ID))
;

alter table Doctors add constraint fk_Doctors_doctorType_1 foreign key (DoctorTypeID) references DoctorTypes (ID) on delete restrict on update restrict;
create index ix_Doctors_doctorType_1 on Doctors (DoctorTypeID);
alter table Doctors add constraint fk_Doctors_unit_2 foreign key (UnitID) references Units (ID) on delete restrict on update restrict;
create index ix_Doctors_unit_2 on Doctors (UnitID);
alter table Doctors add constraint fk_Doctors_shift_3 foreign key (ShiftID) references Shifts (ID) on delete restrict on update restrict;
create index ix_Doctors_shift_3 on Doctors (ShiftID);
alter table Doctors add constraint fk_Doctors_serviceSchedule_4 foreign key (ServiceScheduleID) references ServiceSchedules (ID) on delete restrict on update restrict;
create index ix_Doctors_serviceSchedule_4 on Doctors (ServiceScheduleID);
alter table Doctors add constraint fk_Doctors_seniorSupervisorId_5 foreign key (SeniorSupervisorID) references Doctors (ID) on delete restrict on update restrict;
create index ix_Doctors_seniorSupervisorId_5 on Doctors (SeniorSupervisorID);
alter table Patients add constraint fk_Patients_unit_6 foreign key (UnitID) references Units (ID) on delete restrict on update restrict;
create index ix_Patients_unit_6 on Patients (UnitID);
alter table Positions add constraint fk_Positions_title_7 foreign key (TitleID) references Titles (ID) on delete restrict on update restrict;
create index ix_Positions_title_7 on Positions (TitleID);
alter table PreviousVisits add constraint fk_PreviousVisits_patient_8 foreign key (PatientID) references Patients (ID) on delete restrict on update restrict;
create index ix_PreviousVisits_patient_8 on PreviousVisits (PatientID);
alter table PreviousVisits add constraint fk_PreviousVisits_service_9 foreign key (ServiceID) references Services (ID) on delete restrict on update restrict;
create index ix_PreviousVisits_service_9 on PreviousVisits (ServiceID);
alter table ServiceSchedules add constraint fk_ServiceSchedules_service_10 foreign key (ServiceID) references Services (ID) on delete restrict on update restrict;
create index ix_ServiceSchedules_service_10 on ServiceSchedules (ServiceID);
alter table ServiceSchedules add constraint fk_ServiceSchedules_room_11 foreign key (RoomID) references ServiceRooms (ID) on delete restrict on update restrict;
create index ix_ServiceSchedules_room_11 on ServiceSchedules (RoomID);
alter table ServiceSchedules add constraint fk_ServiceSchedules_patient_12 foreign key (PatientID) references Patients (ID) on delete restrict on update restrict;
create index ix_ServiceSchedules_patient_12 on ServiceSchedules (PatientID);
alter table Services add constraint fk_Services_unit_13 foreign key (UnitID) references Units (ID) on delete restrict on update restrict;
create index ix_Services_unit_13 on Services (UnitID);
alter table Shifts add constraint fk_Shifts_day_14 foreign key (Day) references Days (ID) on delete restrict on update restrict;
create index ix_Shifts_day_14 on Shifts (Day);
alter table Shifts add constraint fk_Shifts_staff_15 foreign key (StaffID) references Staff (ID) on delete restrict on update restrict;
create index ix_Shifts_staff_15 on Shifts (StaffID);
alter table Shifts add constraint fk_Shifts_doctor_16 foreign key (DoctorID) references Doctors (ID) on delete restrict on update restrict;
create index ix_Shifts_doctor_16 on Shifts (DoctorID);
alter table Staff add constraint fk_Staff_position_17 foreign key (PositionID) references Positions (ID) on delete restrict on update restrict;
create index ix_Staff_position_17 on Staff (PositionID);
alter table Staff add constraint fk_Staff_unit_18 foreign key (UnitID) references Units (ID) on delete restrict on update restrict;
create index ix_Staff_unit_18 on Staff (UnitID);
alter table StaffServiceAssignments add constraint fk_StaffServiceAssignments_st_19 foreign key (StaffID) references Staff (ID) on delete restrict on update restrict;
create index ix_StaffServiceAssignments_st_19 on StaffServiceAssignments (StaffID);
alter table StaffServiceAssignments add constraint fk_StaffServiceAssignments_se_20 foreign key (ServiceScheduleID) references ServiceSchedules (ID) on delete restrict on update restrict;
create index ix_StaffServiceAssignments_se_20 on StaffServiceAssignments (ServiceScheduleID);
alter table StorageUnits add constraint fk_StorageUnits_unit_21 foreign key (UnitID) references Units (ID) on delete restrict on update restrict;
create index ix_StorageUnits_unit_21 on StorageUnits (UnitID);
alter table Supplies add constraint fk_Supplies_supplyType_22 foreign key (SupplyTypeID) references SupplyTypes (ID) on delete restrict on update restrict;
create index ix_Supplies_supplyType_22 on Supplies (SupplyTypeID);
alter table Supplies add constraint fk_Supplies_supplyRoom_23 foreign key (SupplyRoomID) references SupplyRooms (ID) on delete restrict on update restrict;
create index ix_Supplies_supplyRoom_23 on Supplies (SupplyRoomID);
alter table Supplies add constraint fk_Supplies_storageUnit_24 foreign key (StorageUnitID) references StorageUnits (ID) on delete restrict on update restrict;
create index ix_Supplies_storageUnit_24 on Supplies (StorageUnitID);
alter table Supplies add constraint fk_Supplies_unit_25 foreign key (UnitID) references Units (ID) on delete restrict on update restrict;
create index ix_Supplies_unit_25 on Supplies (UnitID);
alter table SupplyItems add constraint fk_SupplyItems_supply_26 foreign key (SupplyID) references Supplies (ID) on delete restrict on update restrict;
create index ix_SupplyItems_supply_26 on SupplyItems (SupplyID);
alter table Vendors add constraint fk_Vendors_supplyType_27 foreign key (SupplyTypeID) references SupplyTypes (ID) on delete restrict on update restrict;
create index ix_Vendors_supplyType_27 on Vendors (SupplyTypeID);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table Days;

drop table DoctorTypes;

drop table Doctors;

drop table Patients;

drop table Positions;

drop table PreviousVisits;

drop table ServiceRooms;

drop table ServiceSchedules;

drop table Services;

drop table Shifts;

drop table Staff;

drop table StaffServiceAssignments;

drop table StorageUnits;

drop table Supplies;

drop table SupplyItems;

drop table SupplyRooms;

drop table SupplyTypes;

drop table Titles;

drop table Units;

drop table Vendors;

SET FOREIGN_KEY_CHECKS=1;

