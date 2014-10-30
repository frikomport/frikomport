package no.unified.soak.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.DefaultNamingStrategy;

public class DefaultQuotedNamingStrategy extends DefaultNamingStrategy {
    static boolean usesOracle = false;

    public DefaultQuotedNamingStrategy() {
        super();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("database.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            if (StringUtils.contains(properties.getProperty("hibernate.connection.url"), "oracle")) {
                String oracleUsername = properties.getProperty("hibernate.connection.username");
                if (StringUtils.isNotBlank(oracleUsername)) {
                    usesOracle = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String columnName(String columnName) {
        StringBuffer columnNameNew = addQuotes(new StringBuffer(super.columnName(columnName)));
        return columnNameNew.toString().toUpperCase();
    }

    @Override
    public String propertyToColumnName(String propertyName) {
        String propertyToColumnName = addQuotes(new StringBuffer(super.propertyToColumnName(propertyName))).toString();
        return propertyToColumnName.toUpperCase();
    }

    /**
     * Adds backticks before and after the name.
     * 
     * @param input
     *            the input to quote
     * @return the quoted input
     */
    public static StringBuffer addQuotes(StringBuffer input) {
        return input.insert(0, '`').append('`');
    }

	/**
	 * Is the application using Oracle database? 
	 * @return true if Oracle database is used. false is mysql database is used.
	 */
    public static boolean usesOracle() {
        return usesOracle;
    }
}
