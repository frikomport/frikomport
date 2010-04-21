package no.unified.soak.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.DefaultNamingStrategy;

public class DefaultQuotedNamingStrategy extends DefaultNamingStrategy {
    static String tablePrefix = "";

    public DefaultQuotedNamingStrategy() {
        super();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("database.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            if (StringUtils.contains(properties.getProperty("hibernate.connection.url"), "oracle")) {
                String oracleUsername = properties.getProperty("hibernate.connection.username");
                if (StringUtils.isNotBlank(oracleUsername)) {
                    tablePrefix = oracleUsername + ".";
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity,
//            String associatedEntityTable, String propertyName) {
//        StringBuffer newTableName = addPrefix(new StringBuffer(super.collectionTableName(ownerEntity, ownerEntityTable,
//                associatedEntity, associatedEntityTable, propertyName)));
//        return newTableName.toString();
//    }
//
//    @Override
//    public String classToTableName(String className) {
//        StringBuffer newTableName = addPrefix(new StringBuffer(super.classToTableName(className)));
//        return newTableName.toString();
//    }
//
//    @Override
//    public String tableName(String tableName) {
//        StringBuffer newTableName = addPrefix(new StringBuffer(super.tableName(tableName)));
//        return newTableName.toString();
//    }

    @Override
    public String columnName(String columnName) {
        StringBuffer columnNameNew = addQuotes(new StringBuffer(super.columnName(columnName)));
        return columnNameNew.toString();
    }

    @Override
    public String propertyToColumnName(String propertyName) {
        String propertyToColumnName = addQuotes(new StringBuffer(super.propertyToColumnName(propertyName))).toString();
        return propertyToColumnName;
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
     * Adds prefix before the name.
     * 
     * @param input
     *            the input to quote
     * @return the quoted input
     */
    private static StringBuffer addPrefix(StringBuffer input) {
        return input.insert(0, tablePrefix);
    }

    public static String getTablePrefix() {
        return tablePrefix;
    }
}
