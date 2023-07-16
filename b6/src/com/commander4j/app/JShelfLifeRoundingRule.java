package com.commander4j.app;

/**
 * @author David Garratt
 * 
 * Project Name : Commander4j
 * 
 * Filename     : JShelfLifeRoundingRule.java
 * 
 * Package Name : com.commander4j.app
 * 
 * License      : GNU General Public License
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * http://www.commander4j.com/website/license.html.
 * 
 */

import java.util.Vector;

import com.commander4j.util.JUtility;

public class JShelfLifeRoundingRule
{
	private String dbErrorMessage;
	private String dbShelflifeRuleDescription;
	private String dbShelflifeRule;

	public String getDescription() {
		String result = "";
		if (dbShelflifeRuleDescription != null)
			result = dbShelflifeRuleDescription;
		return result;
	}

	public String getErrorMessage() {
		return dbErrorMessage;
	}

	public void setRule(String rule) {
		dbShelflifeRule = rule;
	}

	public void setDescription(String description) {
		dbShelflifeRuleDescription = description;
	}

	public void getShelfLifeRuleProperties(String rule) {
		if (JUtility.isNullORBlank(rule) == false)
		{
			if (rule.equals("-"))
			{
				setRule(rule);
				setDescription("1st of Month");
			}
			if (rule.equals("="))
			{
				setRule(rule);
				setDescription("Actual Date");
			}
			if (rule.equals("+"))
			{
				setRule(rule);
				setDescription("End of Month");
			}
		}
		else
		{
			setRule("");
			setDescription("");
		}
	}

	public Vector<JShelfLifeRoundingRule> getShelfLifeRoundingRules() {
		Vector<JShelfLifeRoundingRule> ruleList = new Vector<JShelfLifeRoundingRule>();

		JShelfLifeRoundingRule slu = new JShelfLifeRoundingRule();
		slu.setRule("-");
		slu.setDescription("1st of Month");
		ruleList.add(slu);

		slu = new JShelfLifeRoundingRule();
		slu.setRule("=");
		slu.setDescription("Actual Date");
		ruleList.add(slu);

		slu = new JShelfLifeRoundingRule();
		slu.setRule("+");
		slu.setDescription("End of Month");
		ruleList.add(slu);

		return ruleList;
	}

	public String getRule() {
		String result = "";
		if (dbShelflifeRule != null)
			result = dbShelflifeRule;
		return result;
	}

	public String toString() {
		String result = "";
		if (getRule().equals(""))
			result = "";
		else
			result = JUtility.padString(getRule(), true, 1, " ") + " " + getDescription();

		return result;
	}

}
