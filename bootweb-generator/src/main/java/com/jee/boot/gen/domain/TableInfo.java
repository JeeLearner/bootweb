package com.jee.boot.gen.domain;

import com.jee.boot.common.core.domain.BaseEntity;
import com.jee.boot.common.utils.text.JeeStringUtils;

import java.util.List;

/**
 * 数据库表
 * 
 * @author jeeLearner
 */
public class TableInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 表名称 */
    private String tableName;
    /** 表描述 */
    private String tableComment;

    /** 表的主键列信息 */
    private ColumnInfo primaryKey;

    /** 表的列名(不包含主键) */
    private List<ColumnInfo> columns;

    /** 类名(第一个字母大写) */
    private String className;

    /** 类名(第一个字母小写) */
    private String classname;

    /** 类名(第一个字母大写、带前缀 */
    private String classNameWithPre;

    public ColumnInfo getColumnsLast() {
        ColumnInfo columnInfo = null;
        if (JeeStringUtils.isNotEmpty(columns)){
            columnInfo = columns.get(0);
        }
        return columnInfo;
    }


    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    public String getTableComment()
    {
        return tableComment;
    }

    public void setTableComment(String tableComment)
    {
        this.tableComment = tableComment;
    }

    public List<ColumnInfo> getColumns()
    {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns)
    {
        this.columns = columns;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getClassname()
    {
        return classname;
    }

    public void setClassname(String classname)
    {
        this.classname = classname;
    }

    public ColumnInfo getPrimaryKey()
    {
        return primaryKey;
    }

    public void setPrimaryKey(ColumnInfo primaryKey)
    {
        this.primaryKey = primaryKey;
    }

    public String getClassNameWithPre() {
        return classNameWithPre;
    }

    public void setClassNameWithPre(String classNameWithPre) {
        this.classNameWithPre = classNameWithPre;
    }
}
