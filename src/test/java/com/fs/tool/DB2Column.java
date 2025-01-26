package com.fs.tool;

public class DB2Column {
	private boolean keyField = false;
	private String columnName = null;
	private int columnType = 0;
	private int columnSize = 0;
	private String label = null;
	private int precision = 0;
	private int scale = 0;
	private int width = 0;
	private String defaultValue = null;
	private boolean isAutoIncrement = false;
	
	public DB2Column() {
		super();
	}
	public DB2Column(String columnName) {
		setColumnName(columnName);
		setLabel(columnName);
	}
	public DB2Column(String columnName,int columnType) {
		setColumnName(columnName);
		setColumnType(columnType);
		setLabel(columnName);
	}
	public String getColumnName() {
		return columnName;
	}
	public int getColumnSize() {
		return columnSize;
	}
	public int getColumnType() {
		return columnType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public String getLabel() {
		return label;
	}
	public int getPrecision() {
		return precision;
	}
	public int getScale() {
		return scale;
	}
	public int getWidth() { 
		return width;
	}
	public boolean isAutoIncrement() {
		return isAutoIncrement;
	}
	public boolean isKeyField() {
		return keyField;
	}
	public void setAutoIncrement(boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public void setColumnSize(int size) {
		this.columnSize = size;
	}
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public void setKeyField(boolean keyField) {
		this.keyField = keyField;
	}
	public void setLabel(String label) {
		this.label = label;
	}	
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public void setWidth(int width) {
		this.width = width;
	}	
	public String toString() {
		return columnName;
	}
}
