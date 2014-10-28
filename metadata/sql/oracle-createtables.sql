/*
Dette skript skal kun brukes på Oracle og kompletterer Ant-target "db-prepare":
ant db-prepare

Kommandoen "ant db-prepare" feiler etter opprettelsen av noen tabeller. Dette skript oppretter gjenstående tabeller og constraints. 
*/

create table attachment (
	id number(18,0) not null, 
	contenttype varchar(100), 
	courseid number(18,0), 
	filename varchar(100), 
	locationid number(18,0),
	"size" number(18,0), 
	storedname varchar(255), 
	primary key (id)
);

create table organization (
  id number(19,0) not null, 
  selectable number(1,0) not null, 
  name varchar2(50 char) not null, 
  "number" number(19,0) not null, 
  invoice_address varchar2(255 char), 
  invoice_city varchar2(255 char), 
  invoice_province varchar2(255 char), 
  invoice_country varchar2(255 char), 
  invoice_postal_code varchar2(255 char), 
  invoice_name varchar2(255 char), 
  parentid number(19,0), 
  type number(10,0) not null, 
  primary key (id)
);

create table registration (
  id number(19,0) not null,
  participants number(10,0),
  courseid number(19,0) not null,
  username varchar2(100 char),
  email varchar2(50 char) not null,
  employeenumber number(10,0),
  firstname varchar2(100 char) not null,
  invoiced number(1,0) not null,
  jobtitle varchar2(100 char),
  lastname varchar2(100 char) not null,
  mobilephone varchar2(30 char),
  locale varchar2(10 char) not null,
  organizationid number(19,0),
  phone varchar2(30 char),
  registered timestamp not null,
  serviceareaid number(19,0),
  usemailaddress varchar2(100 char),
  "comment" varchar2(255 char),
  attended number(1,0) not null,
  workplace varchar2(100 char),
  invoice_address varchar2(255 char),
  invoice_city varchar2(255 char),
  invoice_province varchar2(255 char),
  invoice_country varchar2(255 char),
  invoice_postal_code varchar2(255 char),
  invoice_name varchar2(255 char),
  closest_leader varchar2(255 char),
  status number(10,0) not null,
  birthdate timestamp,
  primary key (id)
);


alter table app_user add constraint FK459C572966AA1591 foreign key (organization2id) references organization;
alter table app_user add constraint FK459C5729E058A465 foreign key (organizationid) references organization;
alter table attachment add constraint FK8AF759236C3B7B35 foreign key (courseid) references course;
alter table attachment add constraint FK8AF7592399F3E2E9 foreign key (locationid) references location;
alter table course add constraint FKAF42E01B66AA1591 foreign key (organization2id) references organization;
alter table course add constraint FKAF42E01BE058A465 foreign key (organizationid) references organization;
alter table location add constraint FK714F9FB566AA1591 foreign key (organization2id) references organization;
alter table location add constraint FK714F9FB5E058A465 foreign key (organizationid) references organization;
alter table notification add constraint FK237A88EB5F05D1F1 foreign key (registrationid) references registration;
alter table organization add constraint FK4644ED335DA5BE1C foreign key (parentid) references organization;
alter table registration add constraint FKAF83E8B96C3B7B35 foreign key (courseid) references course;
alter table registration add constraint FKAF83E8B9E058A465 foreign key (organizationid) references organization;
alter table registration add constraint FKAF83E8B9928F9645 foreign key (username) references app_user;
alter table registration add constraint FKAF83E8B9179C401B foreign key (serviceareaid) references servicearea;
alter table servicearea add constraint FK8D1534C2E058A465 foreign key (organizationid) references organization;

