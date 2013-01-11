package no.unified.soak.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.sql.DataSource;

import no.unified.soak.Constants;
import no.unified.soak.model.Address;
import no.unified.soak.model.Category;
import no.unified.soak.model.Configuration;
import no.unified.soak.model.Course;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Organization.Type;
import no.unified.soak.model.Person;
import no.unified.soak.model.Registration;
import no.unified.soak.model.ServiceArea;
import no.unified.soak.model.User;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.DatabaseUpdateManager;
import no.unified.soak.service.LocationManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.PersonManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.RoleManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.DefaultQuotedNamingStrategy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.validator.EmailValidator;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * User: gv Date: 05.jun.2008 Time: 10:26:23
 */
public class DatabaseUpdateManagerImpl extends BaseManager implements DatabaseUpdateManager {
	private JdbcTemplate jt = new JdbcTemplate();
	private CourseManager courseManager = null;
	private RegistrationManager registrationManager = null;
	private UserManager userManager = null;
	private ServiceAreaManager serviceAreaManager = null;
	private OrganizationManager organizationManager = null;
	private CategoryManager categoryManager = null;
	private ConfigurationManager configurationManager = null;
	private RoleManager roleManager = null;
	private PersonManager personManager = null;
	private LocationManager locationManager = null;

	// hack for setting messagesource and locale to ApplicationResourcesUtil
	// once
	public void setLocale(Locale locale) {
		// ApplicationResourcesUtil.setLocale(locale);
	}

	public void setMessageSource(MessageSource messageSource) {
		ApplicationResourcesUtil.setMessageSource(messageSource);
	}

	// --- end hack

	public void setDataSource(DataSource dataSource) {
		jt.setDataSource(dataSource);
	}

	public void setCourseManager(CourseManager courseManager) {
		this.courseManager = courseManager;
	}

	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
		this.serviceAreaManager = serviceAreaManager;
	}

	public void setOrganizationManager(OrganizationManager organizationManager) {
		this.organizationManager = organizationManager;
	}

	public void setCategoryManager(CategoryManager categoryManager) {
		this.categoryManager = categoryManager;
	}

	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public void updateDatabase() {
		dropConfigurationTableOnUpgradeToVersion18();
		
		alterUserAndRoleAndCourseBySQL();
		
		alterUserBySQL(); // i forb. med overgang til brukersykronisering på basis av APP_USER istedet for henting av alle brukere fra eZ / svvtrunkmerge
		
		updateParticipantsForRegistrationsWithoutBySQL();
		
		changeRolesBySQL();

		insertDefaultValues();

		updateBySQLStatements();

		// ServiceArea updates
		updateServiceAreas();
		// User updates
		updateUsers();
		// Organization updates
		updateOrganizations();
		// Course updates
		updateCourses();
		// Registration updates
		updateRegistrations();

		// Location updates
		updateLocations();

		// Schema updates postactions
		updateDatabaseSchemaAfter();

		// update configuration content
		updateConfigurations();
		
		//delete deprecated configurations
		deleteConfigurations();
	}

	private void dropConfigurationTableOnUpgradeToVersion18(){
		ColumnInfo id = getColumnInfo("configuration", "id");
		if(id != null){
			String type = id.getType();
			int size = id.getSize();
			if("bigint".equalsIgnoreCase(type) && size == 20){
				// pre v1.8 kolonne "id" - ny tabell opprettes av hibernate
				try {
					jt.execute("drop table configuration");
					log.info("\"Configuration\"-tabell droppet pga. oppgradering!");

					String create = "CREATE TABLE configuration (\r\n"
						+ "id int(10) unsigned AUTO_INCREMENT NOT NULL,\r\n"
						+ "name varchar(100) NOT NULL,\r\n"
						+ "value     varchar(100) NULL,\r\n"
						+ "active    tinyint(1) NULL DEFAULT '0',\r\n"
						+ "PRIMARY KEY(id))";

					if(DefaultQuotedNamingStrategy.usesOracle()){
						log.warn("Oppretting av \"Configuration\"-tabell må foretaes manuelt for Oracle!!");
						log.info("Følgende sql må tilpasses: " + create);
					}
					else {
						jt.execute(create);
						log.info("\"Configuration\"-tabell opprettet på nytt format!");
					}
					
				}catch (Exception e) {
					log.error("Feil oppstod ved 'drop/create table configuration'", e);
				}
			}
		}
	}

	private void deleteTable(String tablename) {
		String sql = "delete from " + tablename;
		jt.execute(sql);
	}

	private boolean isEmptyTable(String table) {
		String sql = "select count(*) from " + table;
		int numRows = jt.queryForInt(sql);
		return (numRows == 0);
	}

	private void closeConnectionIfNeeded(Connection connection, String MethodnameOrCodeIdentifier) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			log.error("Error closing database connection in DatabaseUpdateManagerImpl." + MethodnameOrCodeIdentifier, e);
		}
	}

	/**
	 * For upgrade from 1.7.X to SVV
	 */
	private void changeRolesBySQL() {
		try {
			String s1 = "select count(*) from role where name = 'instructor'";
			String s2 = "select count(*) from role where name = 'eventresponsible'";
			String s3 = "insert into role values ('eventresponsible', 'Kursansvarlig'";
			String s4 = "update user_role set role_name = 'eventresponsible' where role_name = 'instructor'";
			String s5 = "delete from role where name = 'instructor'";
			if (DefaultQuotedNamingStrategy.usesOracle()) {
				s1 = "select count(*) from role where \"name\" = 'instructor'";
				s2 = "select count(*) from role where \"name\" = 'eventresponsible'";
				// s3 - trenger ikke endres
				s4 = "update user_role set \"role_name\" = 'eventresponsible' where \"role_name\" = 'instructor'";
				s5 = "delete from role where \"name\" = 'instructor'";
			}
			if(jt.queryForInt(s1) == 1 && jt.queryForInt(s2) == 0){
				// 'instructor' finnes  -- 'eventresponsible' finnes ikke..
				jt.execute(s3);
				log.info("Opprettet ny rolle 'eventresponsible' i ROLE");
				int k = jt.update(s4);
				log.info("Flyttet " + k + " brukere med rolle 'instructor' over til 'eventresponsible'");
				jt.execute(s5);
				log.info("Slettet utfaset rolle 'instructor' fra ROLE");
			}
		} catch (Exception e) {
			log.error("Feil ved dataendring i \"ROLE\"-tabell", e);
		}
	}

	private void updateLocations() {
		ColumnInfo mapurl = getColumnInfo("location", "mapurl");
		if (mapurl != null && mapurl.getSize() < 350) {
			String sql = "alter table location modify mapurl varchar(350)";
			if (DefaultQuotedNamingStrategy.usesOracle()) {
				sql = "alter table location modify \"mapurl\" varchar2(350)";
			}
			try {
				int nRowsAffected = jt.update(sql);
				log.info("Oppdatert " + nRowsAffected + " rader i LOCATION pga endring av mapurl-kolonne.");
			} catch (Exception e) {
				log.error("Feil under endring av LOCATION.mapurl fra varchar2(200) til varchar2(350)", e);
			}
		}

		ColumnInfo detailurl = getColumnInfo("location", "detailurl");
		if (detailurl != null && detailurl.getSize() < 350) {
			String sql = "alter table location modify detailurl varchar(350)";
			if (DefaultQuotedNamingStrategy.usesOracle()) {
				sql = "alter table location modify \"detailurl\" varchar2(350)";
			}
			try {
				int nRowsAffected = jt.update(sql);
				log.info("Oppdatert " + nRowsAffected + " rader i LOCATION pga endring av detailurl-kolonne.");
			} catch (Exception e) {
				log.error("Feil under endring av LOCATION.detailurl fra varchar2(200) til varchar2(350)", e);
			}
		}
	}

	/**
	 * For upgrade from 1.7.x to svvtrunkmerge (1.8 ?)
	 */
	private void alterUserBySQL() {

		if(!ApplicationResourcesUtil.isSVV()){
			String sql = "update app_user set hashuser = false where username not like '%@%'";
			if (DefaultQuotedNamingStrategy.usesOracle()) {
				sql = "update app_user set \"hashuser\" = false where \"username\" not like '%@%'";
			}
			try {
				jt.execute(sql);
				log.info("Satt COURSE.hashuser = false for brukere hvor 'username' ikke er en epostadresse!");
			} catch (Exception e) {
				log.error("SQL feilet: " + sql, e);
			}

			sql = "update app_user set hashuser = true where username like '%@%'";
			if (DefaultQuotedNamingStrategy.usesOracle()) {
				sql = "update app_user set \"hashuser\" = false where \"username\" like '%@%'";
			}
			try {
				jt.execute(sql);
				log.info("Satt COURSE.hashuser = true for brukere hvor 'username' er en epostadresse!");
			} catch (Exception e) {
				log.error("SQL feilet: " + sql, e);
			}
		}
	}
	
	
	/**
	 * For upgrade from 1.7.x to svvtrunkmerge (1.8 ?)
	 */
	private void updateParticipantsForRegistrationsWithoutBySQL(){
		String sql = "update registration set participants = 1 where participants is null";
		if (DefaultQuotedNamingStrategy.usesOracle()) {
			sql = "update registration set \"participants\" = 1 where \"participants\" is null";
		}
		try {
			int count = jt.update(sql);
			if(count > 0)
			log.info("Oppdatert " + count + " rader : " + sql);
		} catch (Exception e) {
			log.error("SQL feilet: " + sql, e);
		}
	}
	
	
	/**
	 * For upgrade from 1.7.X to SVV
	 */
	private void alterUserAndRoleAndCourseBySQL() {

		// removes ID from APP_USER -- column has no purpose..
		ColumnInfo id = getColumnInfo("app_user", "id");
		if (id != null) {
			log.info("Column APP_USER.ID exists.");
			String sql = "update app_user set hashuser = true where id=0";
			if (DefaultQuotedNamingStrategy.usesOracle()) {
				sql = "update app_user set \"hashuser\" = 1 where \"id\"=0";
			}
			try {
				jt.execute(sql);
				log.info("Column APP_USER.HASHUSER updated from ID=0!");
			} catch (Exception e) {
				log.error("Alter table APP_USER.HASHUSER failed", e);
			}

			sql = "alter table app_user drop column id";
			if (DefaultQuotedNamingStrategy.usesOracle()) {
				sql = "alter table app_user drop column \"id\"";
			}
			try {
				jt.execute(sql);
				log.info("Column APP_USER.ID removed!");
			} catch (Exception e) {
				log.error("Alter table APP_USER failed", e);
			}
		}

		// removes VERSION from ROLE -- column has no purpose..
		ColumnInfo version = getColumnInfo("role", "version");
		if (version != null) {
			log.info("Column ROLE.VERSION exists.");
			String sql = "alter table role drop column version";
			if (DefaultQuotedNamingStrategy.usesOracle()) {
				sql = "alter table role drop column \"version\"";
			}
			try {
				jt.execute(sql);
				log.info("Column ROLE.VERSION removed!");
			} catch (Exception e) {
				log.error("Alter table ROLE failed", e);
			}
		}

		ColumnInfo serviceareaid = getColumnInfo("course", "serviceareaid");
		if (serviceareaid != null && !serviceareaid.isNullable()) {
			String sql = "alter table course modify serviceareaid bigint(19) null";
			if (DefaultQuotedNamingStrategy.usesOracle()) {
				sql = "alter table course modify \"serviceareaid\" number(19,0) null";
			}
			log.debug(sql);
			try {
				jt.execute(sql);
				log.info("Column COURSE.SERVICEAREAID changed to nullable!");
			} catch (Exception e) {
				log.error("Alter table COURSE failed", e);
			}
		}

	}

	private void insertDefaultValues() {
		try {
			String[][] sqlSelectAndInsertRoleArray = {
					// Role insert
					{ "select count(*) from role where name='anonymous'",
							"INSERT INTO role (name, description) VALUES('anonymous', 'Anonymous')" },
					{ "select count(*) from role where name='admin'",
							"INSERT INTO role (name, description) VALUES('admin', 'Administrator')" },
					{ "select count(*) from role where name='employee'",
							"INSERT INTO role (name, description) VALUES('employee', 'Ansatt')" },
					{ "select count(*) from role where name='eventresponsible'",
							"INSERT INTO role (name, description) VALUES('eventresponsible', 'Kursansvarlig')" },
					{ "select count(*) from role where name='editor'",
							"INSERT INTO role (name, description) VALUES('editor', 'Opplaringsansvarlig')" } };
			insertIntoTableBySQLStatements("role", sqlSelectAndInsertRoleArray);
		} catch (Exception e) {
			log.warn("Feil ved insert av roller", e);
		}

		if (ApplicationResourcesUtil.isSVV()) {
			try {
				String[][] sqlSelectAndInsertRoleSVVArray = {
				// Role insert
				{ "select count(*) from role where name='reader'",
						"INSERT INTO role (name, description) VALUES('reader', 'Reader')" } };
				insertIntoTableBySQLStatements("role", sqlSelectAndInsertRoleSVVArray);
			} catch (Exception e) {
				log.warn("Feil ved insert av rolle", e);
			}

		}

		// CATEGORIES
		try {
			String kurs = "select count(*) from category where name = 'Kurs'";
			String hendelse = "update category set name = '" + Category.Name.HENDELSE.getDBValue() + "' where name = 'Kurs'";
			if(DefaultQuotedNamingStrategy.usesOracle()){
				kurs = "select count(*) where \"name\" = 'Kurs'";
				hendelse = "update category set \"name\" = '" + Category.Name.HENDELSE.getDBValue() + "' where \"name\" = 'Kurs'";
			}
			if(jt.queryForInt(kurs) == 1){
				jt.update(hendelse);
				log.info("\"Category\" endret fra 'Kurs' til '" + Category.Name.HENDELSE.getDBValue() + "'");
			}
			else {
				try {
					categoryManager.getCategory(Category.Name.HENDELSE.getDBValue());
				} catch (ObjectRetrievalFailureException e) {
					Category cat = new Category();
					cat.setName(Category.Name.HENDELSE.getDBValue());
					cat.setSelectable(true);
					categoryManager.saveCategory(cat);
					log.info("\"Category\" lagt til i DB: " + cat);
				}
			}
		}
		catch (Exception e) {
			log.error("Problem ved opprettelse/endring av \"Category\"", e);
		}
		

		// PERSONS
		if (ApplicationResourcesUtil.isSVV()) {
			try {
				List persons = personManager.getPersons(null, false);
				if (persons.isEmpty()) {
					Person mengdetrening = new Person();
					mengdetrening.setName("Mengdetrening");
					mengdetrening.setEmail("mengdetrening@vegvesen.no");
					mengdetrening.setSelectable(true);
					personManager.savePerson(mengdetrening);
					log.info("Lagt inn dummy-person i DB: " + mengdetrening);
				}
			} catch (Exception e) {
				log.error("Feil ved innlegging av dummy-person \"Mengdetrening\"", e);
			}
		}

		// CONFIGURATIONS
		Vector<Configuration> configurationsToInsert = new Vector<Configuration>();
		// common configurations
		configurationsToInsert.add(new Configuration("access.course.singleprice", false, null));
		configurationsToInsert.add(new Configuration("access.course.showAttendantDetails", true, null));

		configurationsToInsert.add(new Configuration("access.registration.delete", false, null));
		configurationsToInsert.add(new Configuration("access.registration.emailrepeat", false, null));
		configurationsToInsert.add(new Configuration("access.registration.showCancelled", true, null));
		configurationsToInsert.add(new Configuration("access.registration.showServiceArea", false, null));

		configurationsToInsert.add(new Configuration("access.location.usePostalCode", true, null));

		configurationsToInsert.add(new Configuration("sms.confirmedRegistrationChangedCourse", false, null));

		configurationsToInsert.add(new Configuration("mail.course.sendSummary", true, null));
		configurationsToInsert.add(new Configuration("access.course.showDescription", true, null));

		configurationsToInsert.add(new Configuration("mail.registration.notifyResponsible", false, null));


		// profile
		configurationsToInsert.add(new Configuration("access.profile.showAddress", true, null));

		if (ApplicationResourcesUtil.isSVV()) {
			// configurations specific for FKPSVV enviroment.
			configurationsToInsert.add(new Configuration("show.menu", false, null));

			// registrationForm
			configurationsToInsert.add(new Configuration("access.registration.showEmployeeFields", false, null));
			configurationsToInsert.add(new Configuration("access.registration.showServiceArea", false, null));
			configurationsToInsert.add(new Configuration("access.registration.showJobTitle", false, null));
			configurationsToInsert.add(new Configuration("access.registration.showWorkplace", false, null));
			configurationsToInsert.add(new Configuration("access.registration.useBirthdate", true, null));
			configurationsToInsert.add(new Configuration("access.registration.mobilePhone.digitsOnly.minLength8", true, null));
			configurationsToInsert.add(new Configuration("access.registration.useWaitlists", false, null));
			configurationsToInsert.add(new Configuration("access.registration.showComment", false, null));
			configurationsToInsert.add(new Configuration("access.registration.useParticipants", true, null));


			// course
			configurationsToInsert.add(new Configuration("access.course.usePayment", false, null));
			configurationsToInsert.add(new Configuration("access.course.showDuration", false, null));
			configurationsToInsert.add(new Configuration("access.course.showRole", false, null));
			configurationsToInsert.add(new Configuration("access.course.showType", false, null));
			configurationsToInsert.add(new Configuration("access.course.showRestricted", false, null));
			configurationsToInsert.add(new Configuration("access.course.showCourseName", false, null));
			configurationsToInsert.add(new Configuration("access.course.useAttendants", true, null));
			configurationsToInsert.add(new Configuration("access.course.useRegisterBy", false, null));
			configurationsToInsert.add(new Configuration("access.course.useOrganization2", true, null));
			configurationsToInsert.add(new Configuration("access.course.showDescriptionToPublic", false, null));
			configurationsToInsert.add(new Configuration("access.course.showCourseUntilFinished", false, null));
			configurationsToInsert.add(new Configuration("access.course.showAdditionalInfo", false, null));

			// location
			configurationsToInsert.add(new Configuration("access.location.useOrganization2", true, null));

			// Organization
			configurationsToInsert.add(new Configuration("access.organization.useType", true, null)); // NB! useType for organization + organization2 må konfigureres likt!!
			configurationsToInsert.add(new Configuration("access.organization2.useType", true, null)); // NB! useType for organization + organization2 må konfigureres likt!!
			
			// User
			configurationsToInsert.add(new Configuration("access.user.useBirthdate", false, null));
			configurationsToInsert.add(new Configuration("access.user.useWebsite", false, null));
			configurationsToInsert.add(new Configuration("access.user.useCountry", false, null));

			// profile
			configurationsToInsert.add(new Configuration("access.profile.showInvoiceaddress", false, null));

		} else {
			// configurations specific for non-FKPSVV enviroment.
			configurationsToInsert.add(new Configuration("show.menu", true, null));

			// registrationForm
			configurationsToInsert.add(new Configuration("access.registration.showEmployeeFields", true, null));
			configurationsToInsert.add(new Configuration("access.registration.showJobTitle", true, null));
			configurationsToInsert.add(new Configuration("access.registration.showWorkplace", true, null));
			configurationsToInsert.add(new Configuration("access.registration.useBirthdate", false, null));
			configurationsToInsert.add(new Configuration("access.registration.mobilePhone.digitsOnly.minLength8", false, null));
			configurationsToInsert.add(new Configuration("access.registration.useWaitlists", true, null));
			configurationsToInsert.add(new Configuration("access.registration.showComment", true, null));
			configurationsToInsert.add(new Configuration("access.registration.useParticipants", false, null));
			configurationsToInsert.add(new Configuration("access.registration.userdefaults", false, null));
			configurationsToInsert.add(new Configuration("access.registration.anonymous", true, null));

			// course
			configurationsToInsert.add(new Configuration("access.course.filterlocation", false, null));
			configurationsToInsert.add(new Configuration("access.course.showCourseName", true, null));
			configurationsToInsert.add(new Configuration("access.course.showDuration", true, null));
			configurationsToInsert.add(new Configuration("access.course.showDescriptionToPublic", true, null));
			configurationsToInsert.add(new Configuration("access.course.showCourseUntilFinished", true, null));
			configurationsToInsert.add(new Configuration("access.course.showRole", true, null));
			configurationsToInsert.add(new Configuration("access.course.showType", true, null));
			configurationsToInsert.add(new Configuration("access.course.showRestricted", true, null));
			configurationsToInsert.add(new Configuration("access.course.showAdditionalInfo", true, null));
			configurationsToInsert.add(new Configuration("access.course.rePublishCancelled", false, null));
			
			configurationsToInsert.add(new Configuration("access.course.usePayment", true, null));
			configurationsToInsert.add(new Configuration("access.course.useServiceArea", true, null));
			configurationsToInsert.add(new Configuration("access.course.useAttendants", false, null));
			configurationsToInsert.add(new Configuration("access.course.useRegisterBy", true, null));
			configurationsToInsert.add(new Configuration("access.course.useOrganization2", false, null));

			// location
			configurationsToInsert.add(new Configuration("access.location.useOrganization2", false, null));

			// User
			configurationsToInsert.add(new Configuration("access.user.useBirthdate", true, null));
			configurationsToInsert.add(new Configuration("access.user.useWebsite", true, null));
			configurationsToInsert.add(new Configuration("access.user.useCountry", true, null));

			// profile
			configurationsToInsert.add(new Configuration("access.profile.showInvoiceaddress", true, null));

			// listevisning
			configurationsToInsert.add(new Configuration("list.itemCount", false, "25"));
		}

		List<Configuration> configurationsInDB = configurationManager.getConfigurations();
		if (configurationsInDB.isEmpty()) {
			// insert all configurations for env
			for (int i = 0; i < configurationsToInsert.size(); i++) {
				Configuration c = configurationsToInsert.get(i);
				configurationManager.saveConfiguration(c);
				log.info("\"Configuration\" lagt til i DB: " + c);
			}
		} else {
			// insert missing configurations
			for (int i = 0; i < configurationsToInsert.size(); i++) {
				Configuration configuration = configurationsToInsert.get(i);
				boolean insert = true;
				Iterator confList = configurationsInDB.iterator();
				while (confList.hasNext()) {
					Configuration alreadyInDB = (Configuration) confList.next();
					if (alreadyInDB.getName().equalsIgnoreCase(configuration.getName())){
							insert = false;
						}
				}
				if (insert) {
					configurationManager.saveConfiguration(configuration);
					log.info("\"Configuration\" lagt til i DB: " + configuration);
				}
			}
		}

		// viser alle konfigurasjoner ved oppstart av applikasjon
		List<Configuration> configurationsActive = configurationManager.getConfigurations();
		Iterator<Configuration> it = configurationsActive.iterator();
		while(it.hasNext()){
			Configuration c = it.next();
			log.info(c.getName() + ": " + c.getActive());
		}
		// ------------------------------------------------------
		
		if (ApplicationResourcesUtil.isSVV()) {

			List<Organization> organizationsInDB = organizationManager.getAll();

			Vector<Organization> organizationsToInsert = new Vector<Organization>();

			List regioner = organizationManager.getOrganizationsByType(Type.REGION);
			List omrader = organizationManager.getOrganizationsByType(Type.AREA);
			Organization north;
			Organization mid;
			Organization west;
			Organization south;
			Organization east;
			Integer regionTypeDBValue = Type.REGION.getTypeDBValue();
			Integer areaTypeDBValue = Type.AREA.getTypeDBValue();
			Integer countyTypeDBValue = Type.COUNTY.getTypeDBValue();

			if (regioner.isEmpty()) {
				// regioner
				north = new Organization("Region Nord", 1, regionTypeDBValue, true, null);
				mid = new Organization("Region Midt", 2, regionTypeDBValue, true, null);
				west = new Organization("Region Vest", 3, regionTypeDBValue, true, null);
				south = new Organization("Region Sør", 4, regionTypeDBValue, true, null);
				east = new Organization("Region Øst", 5, regionTypeDBValue, true, null);
				organizationManager.saveOrganization(north);
				organizationManager.saveOrganization(mid);
				organizationManager.saveOrganization(west);
				organizationManager.saveOrganization(south);
				organizationManager.saveOrganization(east);
			} else {
				north = Organization.getFirstOrgByNumber(regioner, 1);
				mid = Organization.getFirstOrgByNumber(regioner, 2);
				west = Organization.getFirstOrgByNumber(regioner, 3);
				south = Organization.getFirstOrgByNumber(regioner, 4);
				east = Organization.getFirstOrgByNumber(regioner, 5);
			}

			if (omrader.isEmpty()) {
				// områder
				organizationsToInsert.add(new Organization("Område Helgeland", 0, areaTypeDBValue, true, north));
				organizationsToInsert.add(new Organization("Område Salten", 0, areaTypeDBValue, true, north));
				organizationsToInsert.add(new Organization("Område Midtre Hålogaland", 0, areaTypeDBValue, true, north));
				organizationsToInsert.add(new Organization("Område Midtre Troms", 0, areaTypeDBValue, true, north));
				organizationsToInsert.add(new Organization("Område Nord-Troms og Vest-Finnmark", 0, areaTypeDBValue, true, north));
				organizationsToInsert.add(new Organization("Område Øst-Finnmark", 0, areaTypeDBValue, true, north));

				organizationsToInsert.add(new Organization("Område Møre og Romsdal", 0, areaTypeDBValue, true, mid));
				organizationsToInsert.add(new Organization("Område Sør-Trøndelag", 0, areaTypeDBValue, true, mid));
				organizationsToInsert.add(new Organization("Område Nord-Trøndelag", 0, areaTypeDBValue, true, mid));

				organizationsToInsert.add(new Organization("Område Sør-Rogaland", 0, areaTypeDBValue, true, west));
				organizationsToInsert.add(new Organization("Område Haugaland og Sunnhordaland", 0, areaTypeDBValue, true, west));
				organizationsToInsert.add(new Organization("Område Bergen og Nordhordaland", 0, areaTypeDBValue, true, west));
				organizationsToInsert.add(new Organization("Område Indre Hordaland og Sogn og Fjordane", 0, areaTypeDBValue, true,
						west));

				organizationsToInsert.add(new Organization("Område Agder", 0, areaTypeDBValue, true, south));
				organizationsToInsert.add(new Organization("Område Nedre-Telemark og Vestfold", 0, areaTypeDBValue, true, south));
				organizationsToInsert.add(new Organization("Område Øvre-Telemark og Buskerud", 0, areaTypeDBValue, true, south));

				organizationsToInsert.add(new Organization("Område Follo og Østfold", 0, areaTypeDBValue, true, east));
				organizationsToInsert.add(new Organization("Område Asker, Bærum og Oslo", 0, areaTypeDBValue, true, east));
				organizationsToInsert.add(new Organization("Område Glåmdal og Romerike", 0, areaTypeDBValue, true, east));
				organizationsToInsert.add(new Organization("Område Hedemarken-Østerdalen", 0, areaTypeDBValue, true, east));
				organizationsToInsert.add(new Organization("Område Oppland", 0, areaTypeDBValue, true, east));

				// fylker
				organizationsToInsert.add(new Organization("Østfold", 1, countyTypeDBValue, true, east));
				organizationsToInsert.add(new Organization("Akershus", 2, countyTypeDBValue, true, east));
				organizationsToInsert.add(new Organization("Oslo", 3, countyTypeDBValue, true, east));
				organizationsToInsert.add(new Organization("Hedmark", 4, countyTypeDBValue, true, east));
				organizationsToInsert.add(new Organization("Oppland", 5, countyTypeDBValue, true, east));
				organizationsToInsert.add(new Organization("Buskerud", 6, countyTypeDBValue, true, south));
				organizationsToInsert.add(new Organization("Vestfold", 7, countyTypeDBValue, true, south));
				organizationsToInsert.add(new Organization("Telemark", 8, countyTypeDBValue, true, south));
				organizationsToInsert.add(new Organization("Aust-Agder", 9, countyTypeDBValue, true, south));
				organizationsToInsert.add(new Organization("Vest-Agder", 10, countyTypeDBValue, true, south));
				organizationsToInsert.add(new Organization("Rogaland", 11, countyTypeDBValue, true, west));
				organizationsToInsert.add(new Organization("Hordaland", 12, countyTypeDBValue, true, west));
				organizationsToInsert.add(new Organization("Sogn og Fjordane", 14, countyTypeDBValue, true, west));
				organizationsToInsert.add(new Organization("Møre og Romsdal", 15, countyTypeDBValue, true, mid));
				organizationsToInsert.add(new Organization("Sør-Trøndelag", 16, countyTypeDBValue, true, mid));
				organizationsToInsert.add(new Organization("Nord-Trøndelag", 17, countyTypeDBValue, true, mid));
				organizationsToInsert.add(new Organization("Nordland", 18, countyTypeDBValue, true, north));
				organizationsToInsert.add(new Organization("Troms", 19, countyTypeDBValue, true, north));
				organizationsToInsert.add(new Organization("Finnmark", 20, countyTypeDBValue, true, north));
			}

			if (organizationsInDB.isEmpty() || omrader.isEmpty()) {
				// insert organizations for env
				for (int i = 0; i < organizationsToInsert.size(); i++) {
					Organization o = organizationsToInsert.get(i);
					organizationManager.saveOrganization(o);
					log.info("\"Organization\" lagt til i DB: " + o);
				}
			} else {
				// insert missing organizations
				for (int i = 0; i < organizationsToInsert.size(); i++) {
					Organization organization = organizationsToInsert.get(i);
					boolean insert = true;
					Iterator orgList = organizationsInDB.iterator();
					while (orgList.hasNext()) {
						Organization alreadyInDB = (Organization) orgList.next();

						Organization parentInDB = alreadyInDB.getParent();
						Organization parent = organization.getParent();

						if (alreadyInDB.getName().equalsIgnoreCase(organization.getName())) {
							insert = false;

							if (parent != null && parentInDB == null) { // update
								// parent
								// for
								// fylker
								alreadyInDB.setParent(parent);
								organizationManager.saveOrganization(alreadyInDB);
								log.info("\"Organization\" oppdatert i DB: " + organization);
							}
						}

					}
					if (insert) {
						organizationManager.saveOrganization(organization);
						log.info("\"Organization\" lagt til i DB: " + organization);
					}
				}
			}
		}
	}

	/**
	 * For every row, run the first (select) statement (column index 0) and see
	 * if it returns a 0 integer. If it does, then run the insert statement in
	 * the next sql statement (column index 1). If the first element (column
	 * index 0) is null, then run only second statement without any checking.
	 * 
	 * @param table
	 * @param sqlStatements
	 */
	private void insertIntoTableBySQLStatements(String table, String[][] sqlStatements) {
		int count = 0;
		for (int i = 0; i < sqlStatements.length; i++) {
			String[] aSelectAndInsert = sqlStatements[i];

			int existCount = 0;
			if (aSelectAndInsert[0] != null) {
				existCount = jt.queryForInt(adjustToOracle(aSelectAndInsert[0]));
			}
			if (existCount == 0 && aSelectAndInsert[1] != null) {
				count += jt.update(adjustToOracle(aSelectAndInsert[1]));
			}
		}
		if (count > 0 && log.isInfoEnabled()) {
			log.info("Number of " + table + " rows inserted (or updated) in database: " + count);
		}
	}

	private String adjustToOracle(String sql) {
		StringBuffer sqlSB = new StringBuffer(sql.toLowerCase());
		boolean usesOracle = DefaultQuotedNamingStrategy.usesOracle();
		if (usesOracle) {
			// Table name adjustment for Oracle
			// int fromPos = sqlSB.indexOf(" from ");
			// int tableStartPos = (fromPos > -1 ? fromPos :
			// sqlSB.indexOf("insert into ")) + 6;
			// if (tableStartPos == 5) {
			// return sql;
			// }
			// sqlSB.insert(tableStartPos, tablePrefix);

			// Column name adjustment for Oracle
			int startPos = sqlSB.indexOf(" where ");
			if (startPos > -1) {
				sqlSB.insert(startPos + 7, '"');
				int endPos = sqlSB.indexOf(" ", startPos + 8);
				if (endPos == -1) {
					endPos = sqlSB.indexOf("=", startPos + 8);
				}
				sqlSB.insert(endPos, '"');
			}

			startPos = sqlSB.indexOf(" (") + 2;
			if (startPos > -1 + 2) {
				int endPos = sqlSB.indexOf(")");
				String[] fieldArray = sqlSB.substring(startPos, endPos).replaceAll(" ", "").split(",", 0);
				String fieldQuoted = "\"" + StringUtils.join(fieldArray, "\",\"") + "\"";
				sqlSB.delete(startPos, endPos);
				sqlSB.insert(startPos, fieldQuoted);
			}

			// Boolean value adjustment for Oracle. true->1, false->0.
			startPos = indexOfIgnorecase(sqlSB.toString(), new String[] { " true)", " true,", " true ,", ",true)", ",true )",
					",true,", "(true," });
			if (startPos > -1) {
				sqlSB.replace(startPos + 1, startPos + 5, "1");
			}
			startPos = indexOfIgnorecase(sqlSB.toString(), new String[] { " false)", " false,", ",false)", ",false ", ",false,",
					"(false,", "(false " });
			if (startPos > -1) {
				sqlSB.replace(startPos + 1, startPos + 5, "0");
			}

			return sqlSB.toString();
		} else {
			return sql;
		}
	}

	private static int indexOfIgnorecase(String sbOrig, String[] strings) {
		String sb = sbOrig.toLowerCase();
		for (int i = 0; i < strings.length; i++) {
			String string = strings[i];
			int index = sb.indexOf(string.toLowerCase());
			if (index > -1) {
				return index;
			}

		}

		return -1;
	}

	private void updateBySQLStatements() {
		updateRegistrationbySQLStatements();
		updateOrganizationbySQLStatements();
	}

	/**
	 * For upgrade from 1.7.X to SVV and to SVVTrunkMerge (1.8?)
	 */
	private void updateRegistrationbySQLStatements() {

		ColumnInfo reserved = getColumnInfo("registration", "reserved");
		if (reserved == null) {
			// No column 'reserved' to convert data from. No initial writing to
			// 'status' column is done in the database.
			return;
		}

		String sql = "update registration set status = reserved + 1 where status is null";
		int nRowsAffected = jt.update(sql);
		if (nRowsAffected > 0) {
			log.info(nRowsAffected + " rows affected by convertion from reserved to status field in database.");
		} else {
			log.info(nRowsAffected + " rows affected by convertion from reserved to status field in database. The field \"reserved\" in table \"registration\" should be dropped.");
		}
	}

	/**
	 * For upgrade from 1.7.X to SVV and to SVVTrunkMerge (1.8?)
	 */
	private void updateOrganizationbySQLStatements(){
		String s1 = "select count(*) from organization where type is null";
		String s2 = "update organization set type = 1 where type is null";

		try {
			if(DefaultQuotedNamingStrategy.usesOracle()){
				s1 = "select count(*) from organization where \"type\" is null";
				s2 = "update organization set \"type\" = 1 where \"type\" is null";
			}
			if(jt.queryForInt(s1) != 0){
				int nRowsAffected = jt.update(s2);
				if (nRowsAffected > 0) {
					log.info(nRowsAffected + " organisasjoner oppdatert med type = 1 (tidligere null)");
				}
			}
		}catch(Exception e){
			log.error("Feil i oppdatering av \"organization.type\"", e);
		}
	}

	
	/**
	 * Retrieves a description of the table.
	 * NB!! -- Do not support mySQL -- but at least Oracle DB
	 * NB!! Needs rewrite like getColumnInfo(...) below
	 * @param table
	 * @return
	 */
	private TableInfo getTableInfo(String table) {
		ResultSet rsTables = null;
		DatabaseMetaData meta = null;
		Connection connection = null;
		try {
			connection = jt.getDataSource().getConnection();
			meta = connection.getMetaData();
			rsTables = meta.getTables(connection.getCatalog(), null, table.toUpperCase(), new String[] { "TABLE" });
			while (rsTables.next()) {
				String tableName = rsTables.getString("TABLE_NAME");

				if (table.equalsIgnoreCase(tableName)) {
					String type = rsTables.getString("TABLE_TYPE");
					String category = rsTables.getString("TABLE_CAT");
					String schema = rsTables.getString("TABLE_SCHEM");
					return new TableInfo(tableName, type, category, schema);
				}
			}
		} catch (SQLException e) {
			log.warn("Error fetching metadata of table " + table + ". \nMetadata object=" + meta + "\nrsTables=" + rsTables, e);
		}
		closeConnectionIfNeeded(connection, "getTableInfo");
		return null;
	}

	private ColumnInfo getColumnInfo(String table, String column) {
		ResultSet rsColumns = null;
		DatabaseMetaData meta = null;
		Connection connection = null;
		try {
			connection = jt.getDataSource().getConnection();

			if(DefaultQuotedNamingStrategy.usesOracle()){
				meta = connection.getMetaData();
				rsColumns = meta.getColumns(null, null, table.toUpperCase(), column);
				while (rsColumns.next()) {
					String columnName = rsColumns.getString("COLUMN_NAME");
					if (column.equalsIgnoreCase(columnName)) {
						String type = rsColumns.getString("TYPE_NAME");
						int size = rsColumns.getInt("COLUMN_SIZE");
						int nullable = rsColumns.getInt("NULLABLE");
						return new ColumnInfo(columnName, type, size, (nullable == DatabaseMetaData.columnNullable ? true : false), table);
					}
				}
			}
			else {
				// mysql returnerer ikke metadata direkte, men som resultat av "select"
				java.sql.Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery("select * from " + table + " limit 1");
				ResultSetMetaData rsMetaData = rs.getMetaData();
				int numColumns = rsMetaData.getColumnCount();
				for(int c=1; c<=numColumns; c++){
					String columnName = rsMetaData.getColumnName(c);
					if(column.equalsIgnoreCase(columnName)){
						String type = rsMetaData.getColumnTypeName(c);
						int size = rsMetaData.getColumnDisplaySize(c);
						int nullable = rsMetaData.isNullable(c);
						return new ColumnInfo(columnName, type, size, (nullable == DatabaseMetaData.columnNullable ? true : false),	table);
					}
				}
			}
		} catch (SQLException e) {
			log.warn("Error fetching metadata of column " + column + " in table " + table + ". \nMetadata object=" + meta
					+ "\nrsColumns" + rsColumns, e);
		}
		closeConnectionIfNeeded(connection, "getColumnInfo()");
		return null;
	}

	private void updateDatabaseSchemaAfter() {
	}

	/**
	 * Updates invoiceaddress for organization
	 * 
	 * @since 1.5
	 */
	private void updateOrganizations() {
		List<Organization> organizations = organizationManager.getAllIncludingDisabled();
		if (organizations != null && !organizations.isEmpty()) {
			Iterator<Organization> it = organizations.iterator();
			while (it.hasNext()) {
				Organization organization = it.next();
				// update invoiceaddress
				if (organization.getInvoiceAddress() == null) {
					Address invoice = new Address();
					invoice.setPostalCode("0");
					organization.setInvoiceAddress(invoice);
					organizationManager.saveOrganization(organization);
				}
			}
		}
	}

	/**
	 * Updates invoiceaddress for user
	 * 
	 * @since 1.5
	 */
	private void updateUsers() {
		List<User> users = userManager.getUsers(new User(), false);
		if (users != null && !users.isEmpty()) {
			Iterator<User> it = users.iterator();
			while (it.hasNext()) {
				boolean save = false;
				User user = it.next();
				// Checks username
				if ("".equals(user.getUsername())) {
					userManager.removeUser("");
				}
				// Updates invoice address
				if (user.getInvoiceAddress() == null) {
					Address invoice = new Address();
					invoice.setPostalCode("0");
					user.setInvoiceAddress(invoice);
					save = true;
				}
				// updates serviceareas
				if (user.getOrganization() != null && user.getServiceArea() != null) {
					if (!user.getOrganizationid().equals(user.getServiceArea().getOrganizationid())) {
						ServiceArea search = new ServiceArea();
						search.setOrganizationid(user.getOrganizationid());
						List<ServiceArea> serviceAreas = serviceAreaManager.searchServiceAreas(search);
						Iterator<ServiceArea> sit = serviceAreas.iterator();
						while (sit.hasNext()) {
							ServiceArea serviceArea = sit.next();
							if (user.getServiceArea().getName().equals(serviceArea.getName())) {
								user.setServiceArea(serviceArea);
								user.setServiceAreaid(serviceArea.getId());
								save = true;
								break;
							}
						}
					}
				}
				if (save) {
					userManager.updateUser(user);
				}
			}
		}
	}

	/**
	 * Checks the registrations table and creates users based on email addresses
	 * 
	 * @since 1.4
	 */
	private void updateRegistrations() {
		List<Registration> registrations = registrationManager.getRegistrations(new Registration());
		if (registrations != null && !registrations.isEmpty()) {
			Iterator<Registration> it = registrations.iterator();
			while (it.hasNext()) {
				boolean save = false;
				Registration registration = it.next();
				// Checks the registrations table and creates users based on
				// email addresses
				if (registration.getUsername() == null || registration.getUsername().trim().length() == 0) {
					if (EmailValidator.getInstance().isValid(registration.getEmail())) {
						User user = userManager.findUserByEmail(registration.getEmail());
						if (user == null) {
							user = userManager.addUser(registration.getEmail(), registration.getFirstName(), registration
									.getLastName(), registration.getEmail(), new Integer(0), null, new Integer(0), null, null);
						}
						// Connect user with registration
						registration.setUser(user);
						registration.setUsername(user.getUsername());
						save = true;
					} else {
						log.warn("Følgende påmelding har ugyldig epostadresse: " + registration.toString());
					}
				}
				// updates the invoice address
				if (registration.getInvoiceAddress() == null) {
					Address invoice = new Address();
					invoice.setPostalCode("0");
					registration.setInvoiceAddress(invoice);
					save = true;
				}
				// updates serviceareas
				if (registration.getOrganization() != null && registration.getServiceArea() != null) {
					if (!registration.getOrganizationid().equals(registration.getServiceArea().getOrganizationid())) {
						ServiceArea search = new ServiceArea();
						search.setOrganizationid(registration.getOrganizationid());
						List<ServiceArea> serviceAreas = serviceAreaManager.searchServiceAreas(search);
						Iterator<ServiceArea> sit = serviceAreas.iterator();
						while (sit.hasNext()) {
							ServiceArea serviceArea = sit.next();
							if (registration.getServiceArea().getName().equals(serviceArea.getName())) {
								registration.setServiceArea(serviceArea);
								registration.setServiceAreaid(serviceArea.getId());
								save = true;
								break;
							}
						}
					}
				}
				if (save) {
					registrationManager.saveRegistration(registration);
				}
			}
		}
	}

	/**
	 * @since 1.4
	 */
	private void updateCourses() {
		List<Course> courses = courseManager.getAllCourses();
		if (courses != null && !courses.isEmpty()) {
			Iterator<Course> it = courses.iterator();
			while (it.hasNext()) {
				boolean save = false;
				Course course = it.next();
				if (course.getRole().equals("Anonymous")) {
					course.setRole(Constants.ANONYMOUS_ROLE);
					save = true;
				} else if (course.getRole().equals("Ansatt")) {
					course.setRole(Constants.EMPLOYEE_ROLE);
					save = true;
				} else if (course.getRole().equals("Kommuneansatt")) {
					course.setRole(Constants.EMPLOYEE_ROLE);
					save = true;
				} else if (course.getRole().equals("Kursansvarlig")) {
					course.setRole(Constants.EVENTRESPONSIBLE_ROLE);
					save = true;
				} else if (course.getRole().equals("Opplaringsansvarlig")) {
					course.setRole(Constants.EDITOR_ROLE);
					save = true;
				} else if (course.getRole().equals("Opplï¿½ringsansvarlig")) {
					course.setRole(Constants.EDITOR_ROLE);
					save = true;
				} else if (course.getRole().equals("Admin")) {
					course.setRole(Constants.ADMIN_ROLE);
					save = true;
				}
				// responsible username
				if (course.getResponsible() != null && course.getResponsibleUsername() == null) {
					course.setResponsibleUsername(course.getResponsible().getUsername());
					save = true;
				}
				// restrictions
				if (course.getRestricted() == null) {
					course.setRestricted(false);
					save = true;
				}

				if (course.getCategory() == null || course.getCategoryid() == 0) {
					Category category = categoryManager.getCategory(Category.Name.HENDELSE.getDBValue());
					course.setCategory(category);
					course.setCategoryid(category.getId());
					save = true;
				}

				// chargeoverdue
				if (course.getChargeoverdue() == null) {
					course.setChargeoverdue(false);
					save = true;
				}
				if (save) {
					courseManager.saveCourse(course);
				}
			}
		}
	}

	/**
	 * @since 1.5
	 */
	private void updateServiceAreas() {
		List<ServiceArea> serviceAreas = serviceAreaManager.getAllIncludingDisabled();
		List<Organization> organizations = organizationManager.getAllIncludingDisabled();
		if (serviceAreas != null && !serviceAreas.isEmpty()) {
			Iterator<ServiceArea> it = serviceAreas.iterator();
			while (it.hasNext()) {
				ServiceArea serviceArea = it.next();
				if (serviceArea.getOrganizationid() == -1) {
					for (int i = 0; i < organizations.size(); i++) {
						Organization organization = organizations.get(i);
						if (i == 0) { // oppdater første og lag kopier
							// etterpÃ¥.
							serviceArea.setOrganization(organization);
							serviceArea.setOrganizationid(organization.getId());
							serviceAreaManager.saveServiceArea(serviceArea);
						} else {
							ServiceArea newServiceArea = new ServiceArea();
							newServiceArea.setName(serviceArea.getName());
							newServiceArea.setSelectable(serviceArea.getSelectable());
							newServiceArea.setOrganization(organization);
							newServiceArea.setOrganizationid(organization.getId());
							serviceAreaManager.saveServiceArea(newServiceArea);
						}
					}
				}
			}
		}
	}

	/**
	 * @since 1.7
	 */
	private void updateConfigurations() {
		/**
		 * DB has changed from key, value to key, active, value. Most values in
		 * DB are boolean defined as strings in value field, this method moves
		 * values to correct new field
		 */
		List<Configuration> configurations = configurationManager.getConfigurations();
		if (configurations != null) {
			Iterator<Configuration> iterator = configurations.iterator();
			while (iterator.hasNext()) {
				Configuration configuration = iterator.next();
				String name = configuration.getName();
				String value = configuration.getValue();
				boolean updated = false;

				if (value != null && value.equals("true")) {
					configuration.setActive(new Boolean(true));
					configuration.setValue(null);
					updated = true;
				}
				if (value != null && value.equals("false")) {
					configuration.setActive(new Boolean(false));
					configuration.setValue(null);
					updated = true;
				}

				// invers properties from hide* to show*
				if (name.equals("access.registration.hideEmployeeFields")) {
					configuration.setName("access.registration.showEmployeeFields");
					if (configuration.getActive())
						configuration.setActive(new Boolean(false));
					else
						configuration.setActive(new Boolean(true));
					updated = true;
				}

				if (name.equals("access.registration.hideServiceArea")) {
					configuration.setName("access.registration.showServiceArea");
					if (configuration.getActive())
						configuration.setActive(new Boolean(false));
					else
						configuration.setActive(new Boolean(true));
					updated = true;
				}

				if (name.equals("access.registration.hideComment")) {
					configuration.setName("access.registration.showComment");
					if (configuration.getActive())
						configuration.setActive(new Boolean(false));
					else
						configuration.setActive(new Boolean(true));
					updated = true;
				}
				// -- end invers

				if (updated)
					configurationManager.saveConfiguration(configuration);
			}
		}
	}

	/**
	 * Metode for å slette utfasede konfigurasjoner
	 * @since 1.8
	 */
	private void deleteConfigurations(){
		List<String> delete = new ArrayList<String>();
		// add configurations to delete here..!

		if (!ApplicationResourcesUtil.isSVV()) {
			delete.add("access.organization.useType");
			delete.add("access.organization2.useType");
		}
		
		List<Configuration> configurations = configurationManager.getConfigurations();
		if (configurations != null) {
			Iterator<Configuration> iterator = configurations.iterator();
			while (iterator.hasNext()) {
				Configuration configuration = iterator.next();
				String name = configuration.getName();
				if(delete.contains(name)){
					configurationManager.deleteConfiguration(configuration);
					log.info("Configuration '" + name + "' deleted!");
				}
			}
		}
	}
	
	public class ColumnInfo {
		String name;
		String type;
		int size;
		boolean nullable;
		String table;

		public ColumnInfo(String name, String type, int size, boolean nullable, String table) {
			this.name = name;
			this.type = type;
			this.size = size;
			this.nullable = nullable;
			this.table = table;
		}

		public String getType() {
			return type;
		}

		public int getSize() {
			return size;
		}

		public boolean isNullable() {
			return nullable;
		}

		public String toString() {
			return new ToStringBuilder(this).append("name", name).append("type", type).append("size", size).append("nullable",
					nullable).append("table", table).toString();
		}
	}

	public class TableInfo {
		String name;
		String type;
		String catalog;
		String schema;

		public TableInfo(String name, String type, String cat, String schema) {
			this.name = name;
			this.type = type;
			this.catalog = cat;
			this.schema = schema;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		/**
		 * @return the catalog
		 */
		public String getCatalog() {
			return catalog;
		}

		/**
		 * @return the schema
		 */
		public String getSchema() {
			return schema;
		}

		public String toString() {
			return new ToStringBuilder(this).append("name", name).append("type", type).append("catalog", catalog).append("schema",
					schema).toString();
		}
	}

	public void executeTask() {
		log.info("running databaseUpdateManager");
		updateDatabase();
	}
}
