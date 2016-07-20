package com.commander4j.app;

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
