package com.commander4j.db;

public class JDBUpdateRequest
{

	public boolean schema_updateRequired = false;
	public int schema_CURVersion = -1;
	public int schema_NEWVersion = -1;

	public boolean program_updateRequired = false;
	public Double program_NEWVersion = new Double("-1");
	public Double program_CURVersion = new Double("-1");

}
