package com.info.baymax.dsp.data.consumer.beans.source;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by joey on 2017/5/26.
 */
@Setter
@Getter
@ToString(doNotUseGetters = true, exclude = {"password"})
public class JdbcDataSource extends DataSource {

    private String dbType;// mysql, sqlserver, oracle

    private String driver;

    private String url;

    private String username;

    private String password;

    private String catalog;

    private String schema;

    private String table;

    private String tableExt;

    private String selectSQL;

    private boolean dateToTimestamp;

    private long fetchSize;

    private long queryTimeout;

    private String version = "";

    private String nullString = "";

    private String nullNonString = "";

    public JdbcDataSource() {
        super("JDBC");
    }

    @Override
    public String getObject() {
        return this.table;
    }

    private String encodeURL(String url) {
        try {
            return URLEncoder.encode(getUrl(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    @Override
    public String getReaderName() {
        if ("9i".equals(version)) {
            return "oracle9i";
        } else {
            return "jdbc";
        }
    }

    /*
     * @Override public void format(Task task, List<String> cmdList) { SyncDataTask syncDataTask = (SyncDataTask) task;
     * cmdList.add("-Rurl=" + getUrl()); cmdList.add("-Rdriver=" + getDriver()); cmdList.add("-Rusername=" +
     * getUsername()); cmdList.add("-Rpassword=" + getPassword()); cmdList.add("-Rtable=" +
     * (StringUtils.isNotBlank(getTableExt()) ? DynamicReplacer.replace(getTableExt()) : getTable()));
     *
     * cmdList.add("-Rnull.string=" + getNullString()); cmdList.add("-Rnull.non.string=" + getNullNonString());
     *
     * if (StringUtils.isNotBlank(getCatalog())) { cmdList.add("-Rcatalog=" + getCatalog()); } if
     * (StringUtils.isNotBlank(getSchema())) { cmdList.add("-Rschema=" + getSchema()); } StringBuilder where = new
     * StringBuilder(); String sql = getSelectSQL(); if (StringUtils.isNotBlank(sql)) { where.append(sql); } String
     * cursorCol = syncDataTask.getCursorCol(); String cursorType = syncDataTask.getCursorType(); String cursorVal =
     * syncDataTask.getCursorVal(); if (StringUtils.isNotBlank(cursorCol)) { cmdList.add("-Rcursor.column=" +
     * cursorCol); } if (StringUtils.isNotBlank(cursorType)) { cmdList.add("-Rcursor.type=" + cursorType); } if
     * (StringUtils.isNotBlank(cursorVal)) { cmdList.add("-Rcursor.value=" + cursorVal); } if (where.length() > 0) {
     * cmdList.add("-Rwhere=" + where.toString()); } if (fetchSize > 0) { cmdList.add("-Rmax.size.per.fetch=" +
     * fetchSize); } if (queryTimeout > 0) { cmdList.add("-Rquery.timeout=" + queryTimeout); } List<String> sourceFields
     * = syncDataTask.getSourceFields(); List<String> fields = new ArrayList<>(); for (int i = 0; i <
     * sourceFields.size(); i++) { String field = sourceFields.get(i); if (StringUtils.isBlank(field)) { field =
     * "'' AS field" + i; } fields.add(field); } if (fields.size() > 0) { if
     * (getDriver().toLowerCase().contains("mysql")) { StringBuffer buf = new StringBuffer(); //
     * mysql针对数字作为字段名时必须用``引起来在select时 for (String field : fields) { if (isStartWithNumber(field)) { buf.append("`" +
     * field + "`" + ",");// mysql对于数字做字段名必须加``,这里判断只要是数字开头的都加 } else { buf.append(field + ","); } }
     * buf.setLength(buf.length() - 1); cmdList.add("-Rcolumns=" + buf.toString()); } else if
     * (getDriver().toLowerCase().contains("oracle")) { StringBuffer buf = new StringBuffer(); //
     * oracle针对数字开头的字段必须用""双引号在select时 for (String field : fields) { if (isStartWithNumber(field)) { buf.append("\"" +
     * field + "\"" + ",");// oracle如果字段名为数字开头,必须加双引号 } else { buf.append(field + ","); } } buf.setLength(buf.length() -
     * 1); cmdList.add("-Rcolumns=" + buf.toString()); } else { cmdList.add("-Rcolumns=" + StringUtils.join(fields,
     * ",")); } } }
     */

    // 判断字符串是不是以数字开头
    public static boolean isStartWithNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str.charAt(0) + "");
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}
