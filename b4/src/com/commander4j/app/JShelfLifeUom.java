package com.commander4j.app;

import java.util.Vector;

import com.commander4j.util.JUtility;

public class JShelfLifeUom
{
	private String dbErrorMessage;
	private String dbShelflifeDescription;
	private String dbShelflifeUom;

	public String getDescription() {
		String result = "";
		if (dbShelflifeDescription != null)
			result = dbShelflifeDescription;
		return result;
	}

	public String getErrorMessage() {
		return dbErrorMessage;
	}

	public void setUom(String uom) {
		dbShelflifeUom = uom;
	}

	public void setDescription(String description) {
		dbShelflifeDescription = description;
	}

	public JShelfLifeUom()
	{

	}

	public JShelfLifeUom(String uom, String description)
	{
		setUom(uom);
		setDescription(description);
	}

	public void getShelfLifeUomProperties(String uom) {

		if (JUtility.isNullORBlank(uom) == false)
		{
			if (uom.equals("H"))
			{
				setUom(uom);
				setDescription("Hours");
			}
			if (uom.equals("D"))
			{
				setUom(uom);
				setDescription("Days");
			}
			if (uom.equals("W"))
			{
				setUom(uom);
				setDescription("Weeks");
			}
			if (uom.equals("M"))
			{
				setUom(uom);
				setDescription("Months");
			}
			if (uom.equals("Y"))
			{
				setUom(uom);
				setDescription("Years");
			}
		}
		else
		{
			setUom("");
			setDescription("");
		}

	}

	public Vector<JShelfLifeUom> getShelfLifeUOMs() {
		Vector<JShelfLifeUom> uomList = new Vector<JShelfLifeUom>();

		JShelfLifeUom slu = new JShelfLifeUom();
		slu.setUom("H");
		slu.setDescription("Hours");
		uomList.add(slu);

		slu = new JShelfLifeUom();
		slu.setUom("D");
		slu.setDescription("Days");
		uomList.add(slu);

		slu = new JShelfLifeUom();
		slu.setUom("W");
		slu.setDescription("Weeks");
		uomList.add(slu);

		slu = new JShelfLifeUom();
		slu.setUom("M");
		slu.setDescription("Months");
		uomList.add(slu);

		slu = new JShelfLifeUom();
		slu.setUom("Y");
		slu.setDescription("Years");
		uomList.add(slu);

		return uomList;
	}

	public String getUom() {
		String result = "";
		if (dbShelflifeUom != null)
			result = dbShelflifeUom;
		return result;
	}

	public String toString() {
		String result = "";
		if (getUom().equals(""))
			result = "";
		else
			result = JUtility.padString(getUom(), true, 1, " ") + " - " + getDescription();

		return result;
	}

}
