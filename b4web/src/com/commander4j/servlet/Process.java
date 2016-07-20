package com.commander4j.servlet;

import com.commander4j.bar.JEANBarcode;
import com.commander4j.db.JDBControl;
import com.commander4j.db.JDBDespatch;
import com.commander4j.db.JDBLanguage;
import com.commander4j.db.JDBLocation;
import com.commander4j.db.JDBPallet;
import com.commander4j.db.JDBReportRequest;
import com.commander4j.db.JDBUser;

import com.commander4j.html.*;

import com.commander4j.sys.Common;
import com.commander4j.sys.JHost;

import com.commander4j.util.*;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

public class Process extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet, HttpSessionListener, HttpSessionAttributeListener {
	private static final long serialVersionUID = 1;
	private String xmlfilename;
	private String logfilename;
	private Logger logger = Logger.getLogger(Process.class);
	private JEANBarcode bcode;
	private boolean bcodeLoaded = false;

	public Process()
	{
		super();
	}

	public void attributeAdded(HttpSessionBindingEvent event)
	{
		// System.out.println("attributeAdded for session = " +
		// event.getSession().getId() + " name =" + event.getName() + " value="
		// + event.getValue());
	}

	public void attributeRemoved(HttpSessionBindingEvent event)
	{
		// System.out.println("attributeRemoved for session = " +
		// event.getSession().getId() + " name =" + event.getName() + " value="
		// + event.getValue());
	}

	public void attributeReplaced(HttpSessionBindingEvent event)
	{
		// System.out.println("attributeReplaced for session = " +
		// event.getSession().getId() + " name =" + event.getName() + " value="
		// + event.getValue());
	}

	private synchronized boolean despatchCheckUser(String despNo, HttpSession session)
	{
		boolean result = true;
		String errormessage = "";
		String sessionID = session.getId();

		session.setAttribute("_ErrorMessage", errormessage);

		if (despNo.length() > 0)
		{
			JDBDespatch desp = new JDBDespatch(Common.sd.getData(sessionID, "selectedHost"), sessionID);

			if (desp.getDespatchProperties(despNo))
			{
				String currentUser = Common.sd.getData(sessionID, "username").toUpperCase();
				String assignedUser = desp.getUserID();

				if (assignedUser.equals(currentUser) == false)
				{
					result = false;
					errormessage = "Despatch is assigned to " + assignedUser;
					session.setAttribute("_ErrorMessage", errormessage);
				} else
				{
					session.setAttribute("_ErrorMessage", "");
				}

			}

		}

		return result;
	}

	protected synchronized void despatchConfirm(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();

		if (button.equals("Yes"))
		{
			String test = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "despatchNo"));
			if (test.equals(""))
			{
				response.sendRedirect("despatchSelect.jsp");
			} else
			{
				if (despatchDataConfirm(Common.sd.getData(sessionID, "despatchNo"), request, session))
				{
					saveData(session, "printSTNonConfirm", JUtility.replaceNullStringwithBlank(request.getParameter("printSTNonConfirm")), true);

					String printSTNonConfirm = Common.sd.getData(sessionID, "printSTNonConfirm");
					logger.debug("printSTNonConfirm=" + printSTNonConfirm);

					if (printSTNonConfirm.equals("on") == true)
					{
						despatchPrint(session);
					}
					saveData(session, "despatchNo", "", true);
					despatchMenuDisplay(request, response);
				} else
				{
					response.sendRedirect("despatchHeader.jsp");
				}
			}
		} else
		{
			response.sendRedirect("despatchHeader.jsp");
		}
	}

	private synchronized boolean despatchDataConfirm(String despNo, HttpServletRequest request, HttpSession session)
	{

		boolean result = true;

		String sessionID = session.getId();

		JDBDespatch desp = new JDBDespatch(Common.sd.getData(sessionID, "selectedHost"), sessionID);

		desp.getDespatchProperties(despNo);

		if (desp.confirm() == false)
		{
			result = false;
			session.setAttribute("_ErrorMessage", desp.getErrorMessage());
			logger.debug("Cannot confirm Despatch " + despNo + " - " + desp.getErrorMessage());
		} else
		{
			logger.debug("Despatch " + despNo + " Confirmed");
			// saveData(session, "despatchNo","", true);
		}

		return result;
	}

	private synchronized String despatchDataCreateNew(HttpSession session)
	{

		String result = "";
		String sessionID = session.getId();
		saveData(session, "despatchNo", "", true);

		JDBDespatch desp = new JDBDespatch(Common.sd.getData(sessionID, "selectedHost"), sessionID);
		String number = desp.generateNewDespatchNo();

		if (number.equals("") == false)
		{

			desp.setDespatchNo(number);

			if (desp.create())
			{
				desp.updateUserID(number, Common.sd.getData(sessionID, "username").toUpperCase());

				result = desp.getDespatchNo();
				logger.debug("Despatch " + number + " created.");
				session.setAttribute("_ErrorMessage", "");
				saveData(session, "despatchNo", number, true);
				saveData(session, "despatchFromLocation", "", true);
				saveData(session, "despatchToLocation", "", true);
				saveData(session, "despatchTrailer", "", true);
				saveData(session, "despatchHaulier", "", true);
				saveData(session, "despatchLoadNo", "", true);
				saveData(session,"despatchJourneyRef","",true);
			} else
			{
				result = "";
				session.setAttribute("_ErrorMessage", desp.getErrorMessage());
			}

			saveData(session, "despatchFromLocationCombo", locationDBComboBox("despatchFromLocation", "", session), true);
			saveData(session, "despatchToLocationCombo", locationDBComboBox("despatchToLocation", "", session), true);
		} else
		{
			session.setAttribute("_ErrorMessage", desp.getErrorMessage());
			result = "";
		}

		return result;
	}

	private synchronized boolean despatchDataRetrieveFromDB(String despNo, HttpSession session)
	{

		boolean result = false;
		String sessionID = session.getId();
		saveData(session, "despatchNo", despNo, true);

		if (despNo.length() > 0)
		{
			JDBDespatch desp = new JDBDespatch(Common.sd.getData(sessionID, "selectedHost"), sessionID);

			if (desp.getDespatchProperties(Common.sd.getData(sessionID, "despatchNo")))
			{
				logger.debug("Despatch data for " + Common.sd.getData(sessionID, "despatchNo") + " retrieved.");
				saveData(session, "despatchFromLocation", desp.getLocationIDFrom(), true);
				saveData(session, "despatchToLocation", desp.getLocationIDTo(), true);
				saveData(session, "despatchTrailer", desp.getTrailer(), true);
				saveData(session, "despatchHaulier", desp.getHaulier(), true);
				saveData(session, "despatchLoadNo", desp.getLoadNo(), true);
				saveData(session, "despatchJourneyRef", desp.getJourneyRef(), true);
				saveData(session, "despatchPalletCount", String.valueOf(desp.getDespatchPalletCount()), true);
				saveData(session, "despatchFromLocationCombo", locationDBComboBox("despatchFromLocation", desp.getLocationIDFrom(), session), true);
				saveData(session, "despatchToLocationCombo", locationDBComboBox("despatchToLocation", desp.getLocationIDTo(), session), true);
				result = true;
			} else
			{
				saveData(session, "despatchFromLocation", "", true);
				saveData(session, "despatchToLocation", "", true);
				saveData(session, "despatchTrailer", "", true);
				saveData(session, "despatchHaulier", "", true);
				saveData(session, "despatchLoadNo", "", true);
				saveData(session, "despatchJourneyRef", "", true);
				saveData(session, "despatchPalletCount", "0", true);
				saveData(session, "despatchFromLocationCombo", locationDBComboBox("despatchFromLocation", "", session), true);
				saveData(session, "despatchToLocationCombo", locationDBComboBox("despatchToLocation", "", session), true);
				session.setAttribute("_ErrorMessage", desp.getErrorMessage());
				result = false;
			}
		}

		return result;
	}

	private synchronized boolean despatchDataSavetoDB(String despNo, HttpServletRequest request, HttpSession session)
	{

		boolean result = true;

		String sessionID = session.getId();

		JDBDespatch desp = new JDBDespatch(Common.sd.getData(sessionID, "selectedHost"), sessionID);

		saveData(session, "despatchFromLocation", request.getParameter("despatchFromLocation").toUpperCase(), true);
		saveData(session, "despatchToLocation", request.getParameter("despatchToLocation").toUpperCase(), true);
		saveData(session, "despatchTrailer", request.getParameter("despatchTrailer").toUpperCase(), true);
		saveData(session, "despatchHaulier", request.getParameter("despatchHaulier").toUpperCase(), true);
		saveData(session, "despatchLoadNo", request.getParameter("despatchLoadNo").toUpperCase(), true);
		saveData(session, "despatchJourneyRef", request.getParameter("despatchJourneyRef"), true);
		logger.debug("Updating Despatch No " + despNo);

		if (desp.getDespatchProperties(despNo))
		{
			saveData(session, "despatchPalletCount", String.valueOf(desp.getDespatchPalletCount()), true);
			desp.setLocationIDTo(Common.sd.getData(sessionID, "despatchToLocation"));
			desp.setLocationIDFrom(Common.sd.getData(sessionID, "despatchFromLocation"));
			desp.setTrailer(Common.sd.getData(sessionID, "despatchTrailer"));
			desp.setHaulier(Common.sd.getData(sessionID, "despatchHaulier"));
			desp.setLoadNo(Common.sd.getData(sessionID, "despatchLoadNo"));
			desp.setJourneyRef(Common.sd.getData(sessionID, "despatchJourneyRef"));
			desp.setDespatchDate(JUtility.getSQLDateTime());

			if (desp.update() == false)
			{
				result = false;
				session.setAttribute("_ErrorMessage", desp.getErrorMessage());
			} else
			{
				logger.debug("Despatch " + despNo + " updated.");
			}
		} else
		{
			result = false;
			session.setAttribute("_ErrorMessage", desp.getErrorMessage());
		}

		return result;
	}

	private synchronized void despatchHeader(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();

		saveData(session, "lastAddRemoveMode", "", true);
		saveData(session, "lastSSCCdespatched", "", true);

		if (despatchDataSavetoDB(Common.sd.getData(sessionID, "despatchNo"), request, session))
		{
			if (button.equals("Add Pallets"))
			{
				if (request.getParameter("despatchFromLocation").toUpperCase().equals("") == false)
				{
					if (request.getParameter("despatchToLocation").toUpperCase().equals("") == false)
					{
						session.setAttribute("_ErrorMessage", "");
						response.sendRedirect("despatchPallet.jsp");
					} else
					{
						session.setAttribute("_ErrorMessage", "Select TO Location");
						response.sendRedirect("despatchHeader.jsp");
					}
				} else
				{
					session.setAttribute("_ErrorMessage", "Select FROM Location");
					response.sendRedirect("despatchHeader.jsp");
				}
			}

			if (button.equals("Confirm Despatch"))
			{
				response.sendRedirect("despatchConfirm.jsp");
			}

			if (button.equals("Print STN"))
			{
				despatchPrint(session);
				response.sendRedirect("despatchHeader.jsp");
			}

			if (button.equals("Exit"))
			{
				session.setAttribute("_ErrorMessage", "");
				despatchMenuDisplay(request, response);
			}
		} else
		{
			//session.setAttribute("_ErrorMessage", "");
			response.sendRedirect("despatchHeader.jsp");
		}
	}

	private synchronized void despatchMenuDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		String sessionID = session.getId();
		session.setAttribute("sscc", "");

		String temp = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "currentDespatchListPage"));
		if (temp.equals(""))
		{
			temp = "0";
		}
		int currentDespatchListPage = Integer.valueOf(temp);

		JMenuRFDespatchList dl = new JMenuRFDespatchList(Common.sd.getData(sessionID, "selectedHost"), sessionID);
		String html = dl.buildDespatchList("Unconfirmed", Common.sd.getData(sessionID, "despatchNo"), currentDespatchListPage);

		currentDespatchListPage = dl.getReturnedPage();
		saveData(session, "currentDespatchListPage", String.valueOf(currentDespatchListPage), true);

		int maxPages = dl.getMaxPages();
		saveData(session, "maxDespatchPages", String.valueOf(maxPages), true);

		int despatchCount = dl.getDespatchCount();
		logger.debug("despatchCount=" + String.valueOf(despatchCount));

		int checkedIndex = dl.getCheckedIndex();

		session.removeAttribute("despatchList");
		session.setAttribute("despatchList", html);

		String despatchIndexFocus = "";
		if (despatchCount > 0)
		{
			if (checkedIndex < 0)
			{
				checkedIndex = 0;
			}
			String subscript = "";
			if (despatchCount != 1)
			{
				subscript = "[" + String.valueOf(checkedIndex) + "]";
			}
			logger.debug("subscript=" + subscript);
			despatchIndexFocus = "<script language=\"javascript\" type=\"text/javascript\">" + " function focusIt() {document.despatchSelect.elements[\"despatchNo\"]" + subscript + ".focus(); } " + "</script>";
		} else
		{
			despatchIndexFocus = "<script language=\"javascript\" type=\"text/javascript\">" + " function focusIt() {document.despatchSelect.buttonAmend.focus(); } " + "</script>";
		}
		session.setAttribute("despatchIndexFocus", despatchIndexFocus);

		response.sendRedirect("despatchSelect.jsp");
	}

	protected synchronized void despatchPallet(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();

		String sscc = JUtility.replaceNullStringwithBlank(request.getParameter("sscc"));
		saveData(session, "sscc", sscc, true);

		String addRemoveMode = request.getParameter("addRemoveMode");
		saveData(session, "addRemoveMode", addRemoveMode, true);

		String lastSSCC = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "lastSSCCdespatched"));
		String lastAddRemoveMode = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "lastAddRemoveMode"));

		if (button.equals(""))
		{
			button = "Submit";
		}

		if (button.equals("Submit"))
		{

			String dupCountString = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "dupCountString"));

			if (dupCountString.equals("") == true)
			{
				dupCountString = "0";
			}

			int dupCountint = Integer.valueOf(dupCountString);

			if (sscc.equals("") == false)
			{

				if ((sscc.equals(lastSSCC) == false) | ((sscc.equals(lastSSCC) == true) & (lastAddRemoveMode.equals(addRemoveMode) == false)))
				{
					dupCountint++;
					lastSSCC = sscc;

					saveData(session, "lastAddRemoveMode", addRemoveMode, true);
					saveData(session, "lastSSCCdespatched", sscc, true);

					if (bcode.parseBarcodeData(sscc) == true)
					{
						logger.debug("DEBUG 3 parsed ok");
						sscc = bcode.getStringforAppID("00");

						if (bcode.isValidSSCCformat(sscc) == true)
						{

							JDBDespatch desp = new JDBDespatch(Common.sd.getData(sessionID, "selectedHost"), sessionID);

							String despNo = Common.sd.getData(sessionID, "despatchNo");

							if (desp.getDespatchProperties(Common.sd.getData(sessionID, "despatchNo")) == true)
							{
								lastAddRemoveMode = addRemoveMode;
								if (addRemoveMode.equals("add") == true)
								{
									if (desp.assignSSCC(sscc) == true)
									{
										logger.debug(sscc + " added to despatch " + despNo);
										session.setAttribute("sscc", "");
										session.setAttribute("_ErrorMessage", "");
										saveData(session, "despatchPalletCount", String.valueOf(desp.getDespatchPalletCount()), true);
									} else
									{
										session.setAttribute("_ErrorMessage", desp.getErrorMessage());
									}
								}
								if (addRemoveMode.equals("remove") == true)
								{
									if (desp.unassignSSCC(sscc) == true)
									{
										logger.debug(sscc + " sscc removed from despatch " + despNo);
										session.setAttribute("sscc", "");
										session.setAttribute("_ErrorMessage", "");
										saveData(session, "despatchPalletCount", String.valueOf(desp.getDespatchPalletCount()), true);
									} else
									{
										session.setAttribute("_ErrorMessage", desp.getErrorMessage());
									}
								}
							} else
							{
								session.setAttribute("_ErrorMessage", "Despatch not found.");
							}
						} else
						{
							session.setAttribute("_ErrorMessage", "Invalid SSCC format.");
						}
					} else
					{
						session.setAttribute("_ErrorMessage", "Invalid barcode.");
					}
				} else
				{
					dupCountint++;

					if (dupCountint == 2)
					{
						dupCountint = 0;
						saveData(session, "lastSSCCdespatched", "", true);
						session.setAttribute("_ErrorMessage", "");
					}
				}
			} else
			{
				session.setAttribute("_ErrorMessage", "");
			}

			session.setAttribute("sscc", "");
			response.sendRedirect("despatchPallet.jsp");
		} else
		{
			session.setAttribute("sscc", "");
			session.setAttribute("_ErrorMessage", "");

			String despNo = Common.sd.getData(sessionID, "despatchNo");

			if (despatchDataRetrieveFromDB(despNo, session))
			{
				response.sendRedirect("despatchHeader.jsp");
			} else
			{
				response.sendRedirect("despatchSelect.jsp");
			}
		}
	}

	private synchronized void despatchPrint(HttpSession session)
	{
		String sessionID = session.getId();
		JDBReportRequest rr = new JDBReportRequest(Common.sd.getData(sessionID, "selectedHost"), sessionID);
		String printQueue = Common.sd.getData(sessionID, "defaultPrinter");
		rr.defineReport("RPT_DESPATCH_SERVICE", "ParameterOnly", ":", "", printQueue, 1);
		rr.addParameter("p_despatch_no", "String", Common.sd.getData(sessionID, "despatchNo"));
		rr.create();
	}

	private synchronized void despatchSelect(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();

		if (button.equals("Create"))
		{
			String despNo = despatchDataCreateNew(session);

			if (despNo.length() > 0)
			{
				if (despatchDataRetrieveFromDB(despNo, session))
				{
					response.sendRedirect("despatchHeader.jsp");
				}
			} else
			{
				despatchMenuDisplay(request, response);
			}
		}

		if (button.equals("PreviousPage"))
		{
			String temp = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "currentDespatchListPage"));
			if (temp.equals(""))
			{
				temp = "0";
			}
			int currentDespatchListPage = Integer.valueOf(temp);

			currentDespatchListPage--;

			if (currentDespatchListPage <= 0)
			{
				currentDespatchListPage = 1;
			}

			saveData(session, "currentDespatchListPage", String.valueOf(currentDespatchListPage), true);
			saveData(session, "despatchNo", "", true);
			session.setAttribute("_ErrorMessage", "");
			despatchMenuDisplay(request, response);

		}

		if (button.equals("NextPage"))
		{
			String temp = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "currentDespatchListPage"));
			if (temp.equals(""))
			{
				temp = "0";
			}
			int currentDespatchListPage = Integer.valueOf(temp);

			currentDespatchListPage++;

			if (currentDespatchListPage <= 0)
			{
				currentDespatchListPage = 1;
			}

			saveData(session, "currentDespatchListPage", String.valueOf(currentDespatchListPage), true);
			saveData(session, "despatchNo", "", true);
			session.setAttribute("_ErrorMessage", "");
			despatchMenuDisplay(request, response);

		}

		if (button.equals("Amend"))
		{

			String selectedDespatchNo = request.getParameter("despatchNo");
			String delims = "[ ]+";
			try
			{
				String[] tokens = selectedDespatchNo.split(delims);
				selectedDespatchNo = tokens[0];

			} catch (Exception ex)
			{
				logger.debug("Exception parsing despatch No");
				selectedDespatchNo = "";
			}

			saveData(session, "despatchNo", selectedDespatchNo, true);

			String despNo = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "despatchNo"));
			logger.debug("Selected despatch no = [" + despNo + "]");

			if (despNo.equals("") == false)
			{
				if (despatchDataRetrieveFromDB(despNo, session))
				{
					if (despatchCheckUser(despNo, session) == true)
					{
						logger.debug("Load despatchHeader for [" + despNo + "]");
						response.sendRedirect("despatchHeader.jsp");
					} else
					{
						logger.debug("Despatch assigned to a different user");
						despatchMenuDisplay(request, response);
					}
				} else
				{
					logger.debug("Despatch Not Found !");
					despatchMenuDisplay(request, response);
				}
			} else
			{
				logger.debug("Despatch NOT SELECTED");
				despatchMenuDisplay(request, response);
			}
		}

		if (button.equals("Exit"))
		{
			session.setAttribute("sscc", "");
			session.setAttribute("despatchNo", "");
			session.setAttribute("_ErrorMessage", "");
			displayMenu(request, response);

		}
	}

	public void destroy()
	{
		Common.hostList.disconnectAll();
		Common.userList.clear();
		JHost.deRegisterDrivers();
		logger.removeAllAppenders();
		logger = null;

		// int active = Thread.activeCount();
		// Thread all[] = new Thread[active];
		// Thread.enumerate(all);
		// for (int i = 0; i < active; i++) {
		// System.out.println(i + ": " + all[i]);
		// logger.debug(i + ": " + all[i]);
		// }
	}

	protected synchronized void displayHosts(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();

		if (button.equals("Submit"))
		{
			saveData(session, "selectedHost", request.getParameter("selectedHost"), false);

			if (Common.sd.getData(sessionID, "selectedHost").isEmpty() == false)
			{
				releaseSessionResources(session);

				if ((Common.hostList.getHost(Common.sd.getData(sessionID, "selectedHost")).getSiteURL().equals("http://") == false)
						& (Common.hostList.getHost(Common.sd.getData(sessionID, "selectedHost")).getDatabaseParameters().getjdbcDriver().equals("http") == true))

				{
					// If url specified and not jdbc driver specified then
					// redirect to the url.
					response.sendRedirect(Common.hostList.getHost(Common.sd.getData(sessionID, "selectedHost")).getSiteURL());
				} else
				{

					boolean connectedOK = false;

					if (Common.hostList.getHost(Common.sd.getData(sessionID, "selectedHost")).isConnected(sessionID) == false)
					{
						saveData(session, "siteDescription", Common.hostList.getHost(Common.sd.getData(sessionID, "selectedHost")).getSiteDescription(), false);

						String sqlPath = "/xml/sql/sql." + Common.hostList.getHost(Common.sd.getData(sessionID, "selectedHost")).getDatabaseParameters().getjdbcDriver() + ".xml";
						sqlPath = getServletContext().getRealPath(sqlPath);

						String viewPath = "/xml/view/view." + Common.hostList.getHost(Common.sd.getData(sessionID, "selectedHost")).getDatabaseParameters().getjdbcDriver() + ".xml";
						viewPath = getServletContext().getRealPath(viewPath);

						if (Common.hostList.getHost(Common.sd.getData(sessionID, "selectedHost")).connect(sessionID, Common.sd.getData(sessionID, "selectedHost"), sqlPath, viewPath))
						{
							connectedOK = true;
							// Common.init();
							if (bcodeLoaded == false)
							{
								bcodeLoaded = true;
								bcode = new JEANBarcode(Common.sd.getData(sessionID, "selectedHost"), sessionID);
							}
							session.setAttribute("_ErrorMessage", "");
						}
						else
						{
							session.setAttribute("_ErrorMessage", "Unable to connect to database (host)");
						}
					} else
					{
						connectedOK = true;
					}

					if (connectedOK)
					{
						response.sendRedirect("login.jsp");
					} else
					{
						response.sendRedirect("hosts.jsp");
					}
				}

			} else
			{
				displayHostSelect(request, response);
			}
		} else
		{
			response.sendRedirect("index.jsp");
		}
	}

	protected synchronized void displayHostSelect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();

		String selectedHost = JUtility.replaceNullObjectwithBlank(Common.sd.getData(sessionID, "selectedHost"));

		String html = Common.hostList.getHTMLmenu(selectedHost);
		int checkedIndex = Common.hostList.getCheckedIndex();

		session.removeAttribute("hostList");
		session.setAttribute("hostList", html);

		String hostIndexFocus = "";
		// if (checkedIndex >= 0)
		if (checkedIndex > 0)
		{
			hostIndexFocus = "<script language=\"javascript\" type=\"text/javascript\">" + " function focusIt() {document.hosts.elements[\"selectedHost\"][" + String.valueOf(checkedIndex) + "].focus(); } " + "</script>";
		} else
		{
			hostIndexFocus = "<script language=\"javascript\" type=\"text/javascript\">" + " function focusIt() { document.hosts.buttonSubmit.focus(); } " + "</script>";
		}
		session.setAttribute("hostIndexFocus", hostIndexFocus);

		session.setAttribute("_ErrorMessage", "");
		response.sendRedirect("hosts.jsp");
	}

	protected synchronized void displayMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();
		String hostID = Common.sd.getData(sessionID, "selectedHost");

		String selectedMenuOption = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "selectedMenuOption"));

		JMenuRFMenu rfm = new JMenuRFMenu(hostID, sessionID);
		String html = rfm.buildMenu(selectedMenuOption);
		int checkedIndex = rfm.getCheckedIndex();

		String menuIndexFocus = "";
		if (checkedIndex >= 0)
		{
			menuIndexFocus = "<script language=\"javascript\" type=\"text/javascript\">" + " function focusIt() {document.menus.elements[\"selectedMenuOption\"][" + String.valueOf(checkedIndex) + "].focus(); } " + "</script>";
		} else
		{
			menuIndexFocus = "<script language=\"javascript\" type=\"text/javascript\">" + " function focusIt() { document.menus.buttonSubmit.focus(); } " + "</script>";
		}
		session.setAttribute("menuIndexFocus", menuIndexFocus);

		session.removeAttribute("menuList");
		session.setAttribute("menuList", html);
		session.setAttribute("_ErrorMessage", "");

		response.sendRedirect("menu.jsp");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		if (session.isNew())
		{
			response.sendRedirect("sessionTimeout.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// synchronized (this)
		{

			HttpSession session = request.getSession();
			String sessionID = session.getId();
			String formName = "";
			String button = "";

			if (session.isNew())
			{
				formName = "New Session";
				response.sendRedirect("sessionTimeout.jsp");
			} else
			{
				button = JUtility.replaceNullObjectwithBlank((String) request.getParameter("button"));
				formName = JUtility.replaceNullObjectwithBlank((String) request.getParameter("formName"));

				String testHost = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "selectedHost"));
				if ((testHost.equals("") == true) & (formName.equals("hosts.jsp") == false) & (formName.equals("sessionTimeout.jsp") == false) & (formName.equals("index.jsp") == false))
				{
					formName = "New Session";
					response.sendRedirect("sessionTimeout.jsp");
					// formName = "index.jsp";
					// button = "Start";
				}
			}

			logger.debug("formName - " + formName + " " + " button - " + button);

			// ************************************
			// **************** INDEX *************
			// ************************************
			if (formName.equals("index.jsp"))
			{
				if (button.equals("Start"))
				{
					saveData(session, "selectedHost", "", true);
					displayHostSelect(request, response);
				}
			}

			// ******************************************
			// **************** SESSION TIMEOUT *********
			// ******************************************
			if (formName.equals("sessionTimeout.jsp"))
			{
				if (button.equals("Restart"))
				{
					saveData(session, "selectedHost", "", true);
					displayHostSelect(request, response);
				}
			}

			// ******************************************
			// **************** HOST SELECT *************
			// ******************************************
			if (formName.equals("hosts.jsp"))
			{
				displayHosts(request, response, button);
			}

			// ******************************************
			// ***************** LOGIN ******************
			// ******************************************
			if (formName.equals("login.jsp"))
			{
				logon(request, response, button);
			}

			// ******************************************
			// ***************** CHANGE PASSWORD ********
			// ******************************************
			if (formName.equals("changePassword.jsp"))
			{
				changeUserPassword(request, response, button);
			}

			// ******************************************
			// ************** MAIN MENU******************
			// ******************************************
			if (formName.equals("menu.jsp"))
			{
				menu(request, response, button);
			}

			// ******************************************
			// ************ DESPATCH SELECT *************
			// ******************************************
			if (formName.equals("despatchSelect.jsp"))
			{
				despatchSelect(request, response, button);
			}

			// ******************************************
			// ************** DESPATCH ******************
			// ******************************************
			if (formName.equals("despatchHeader.jsp"))
			{
				despatchHeader(request, response, button);
			}

			// ******************************************
			// ************ DESPATCH CONFIRM ************
			// ******************************************
			if (formName.equals("despatchConfirm.jsp"))
			{
				despatchConfirm(request, response, button);
			}

			// ******************************************
			// ************DESPATCH PALLET***************
			// ******************************************
			if (formName.equals("despatchPallet.jsp"))
			{
				despatchPallet(request, response, button);
			}

			// ******************************************
			// ********* PRODUCTION CONFIRMATION ********
			// ******************************************
			if (formName.equals("productionConfirm.jsp"))
			{
				palletConfirm(request, response, button);
			}

			// ******************************************
			// ************ PALLET DELETE ***************
			// ******************************************
			if (formName.equals("palletDelete.jsp"))
			{
				palletDelete(request, response, button);
			}

			// ******************************************
			// ************ PALLET INFO *****************
			// ******************************************
			if (formName.equals("palletInfo.jsp"))
			{
				palletInformation(request, response, button);
			}

			// ******************************************
			// ************ PALLET INFO DISPLAY *********
			// ******************************************
			if (formName.equals("palletInfoDisplay.jsp"))
			{
				palletInformationDisplay(request, response, button);
			}

			// ******************************************
			// ************ PRINTER SELECT **************
			// ******************************************
			if (formName.equals("printerSelect.jsp"))
			{
				printerSelect(request, response, button);
			}
		}
	}

	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		JPlaySound.disable();
		logger.debug("Process.init");
		Common.applicationMode = "Servlet";

		Common.paths.clear();
		Common.paths.put("sql.com.ibm.db2.jcc.DB2Driver.xml", getServletContext().getRealPath("/xml/sql/sql.com.ibm.db2.jcc.DB2Driver.xml"));
		Common.paths.put("sql.com.mysql.jdbc.Driver.xml", getServletContext().getRealPath("/xml/sql/sql.com.mysql.jdbc.Driver.xml"));
		Common.paths.put("sql.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml", getServletContext().getRealPath("/xml/sql/sql.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml"));
		Common.paths.put("sql.oracle.jdbc.driver.OracleDriver.xml", getServletContext().getRealPath("/xml/sql/sql.oracle.jdbc.driver.OracleDriver.xml"));

		Common.paths.put("view.com.ibm.db2.jcc.DB2Driver.xml", getServletContext().getRealPath("/xml/view/view.com.ibm.db2.jcc.DB2Driver.xml"));
		Common.paths.put("view.com.mysql.jdbc.Driver.xml", getServletContext().getRealPath("/xml/view/view.com.mysql.jdbc.Driver.xml"));
		Common.paths.put("view.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml", getServletContext().getRealPath("/xml/view/view.com.microsoft.sqlserver.jdbc.SQLServerDriver.xml"));
		Common.paths.put("view.oracle.jdbc.driver.OracleDriver.xml", getServletContext().getRealPath("/xml/view/view.oracle.jdbc.driver.OracleDriver.xml"));

		xmlfilename = getServletContext().getRealPath("/xml/hosts/hosts.xml");
		logfilename = getServletContext().getRealPath("/xml/log/log4j.xml");
		JUtility.initLogging(logfilename);

		Common.hostList.loadHosts(xmlfilename);
		JPrint.init();
	}

	private void loadServletMessages(HttpSession session)
	{
		logger.debug("Process - loadServletMessages");

		String sessionID = session.getId();
		JDBLanguage lang = new JDBLanguage(Common.sd.getData(sessionID, "selectedHost"), sessionID);
		lang.preLoad("%");
	}

	private synchronized String locationDBComboBox(String htmlName, String defaultLocation, HttpSession session)
	{

		String result = "";
		String sessionID = session.getId();

		JDBLocation locn = new JDBLocation(Common.sd.getData(sessionID, "selectedHost"), sessionID);

		result = locn.getHTMLPullDownCombo(htmlName, defaultLocation);

		return result;
	}

	protected synchronized void logon(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();

		if (button.equals("Submit"))
		{
			saveData(session, "username", request.getParameter("username"), true);
			saveData(session, "password", request.getParameter("password"), true);

			if (logonValidate(session) == true)
			{
				displayMenu(request, response);
				saveData(session, "_ErrorMessage", "", true);
			} else
			{

				String next = Common.sd.getData(sessionID, "screenAfterLogon");
				if (next.equals("login.jsp"))
				{
					saveData(session, "username", "", true);
					saveData(session, "password", "", true);
				}
				response.sendRedirect(next);
			}
		} else
		{
			saveData(session, "password", "", true);
			saveData(session, "username", "", true);
			Common.hostList.disconnectSessionAllHosts(sessionID);
			displayHostSelect(request, response);
		}
	}

	private synchronized boolean changeUserPassword(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{
		boolean result = false;

		HttpSession session = request.getSession();
		String sessionID = session.getId();

		if (button.equals(""))
		{
			button = "Submit";
		}

		if (button.equals("Submit"))
		{

			String currentUser = Common.sd.getData(sessionID, "username").toUpperCase();
			String currentPass = request.getParameter("password");

			saveData(session, "newPassword1", request.getParameter("newPassword1"), true);
			saveData(session, "newPassword2", request.getParameter("newPassword2"), true);
			
			JDBUser usr = new JDBUser(Common.sd.getData(sessionID, "selectedHost"), sessionID);
			usr.setUserId(currentUser);
			usr.setLoginPassword(currentPass);
			usr.setPasswordNew(Common.sd.getData(sessionID, "newPassword1"));
			usr.setPasswordVerify(Common.sd.getData(sessionID, "newPassword2"));
			result = usr.changePassword();

			if (result == true)
			{
				saveData(session, "_ErrorMessage", "Password changed", true);
				saveData(session, "password", Common.sd.getData(sessionID, "newPassword1"), true);
				saveData(session, "newPassword1", "", true);
				saveData(session, "newPassword2", "", true);
				displayMenu(request, response);
			} else
			{
				saveData(session, "_ErrorMessage", usr.getErrorMessage(), true);
				response.sendRedirect("changePassword.jsp");
			}
			session.setAttribute("sscc", "");

		} else
		{
			//saveData(session, "selectedMenuOption", "", true);
			String next = Common.sd.getData(sessionID, "changePasswordCancelScreen");
			if (next.equals("displayMenu"))
			{
				displayMenu(request, response);
			} else
			{
				saveData(session, "username", "", true);
				saveData(session, "password", "", true);
				response.sendRedirect(next);
			}
		}

		return result;
	}

	private synchronized boolean logonValidate(HttpSession session)
	{
		boolean result = false;
		String sessionID = session.getId();

		if (Common.hostList.getHost(Common.sd.getData(sessionID, "selectedHost")).isConnected(sessionID) == true)
		{
			logger.debug("Process - Connect Succeeded");
			saveData(session, "screenAfterLogon", "displayMenu", true);
			
			JDBControl control = new JDBControl(Common.sd.getData(sessionID, "selectedHost"), sessionID);

			control.setSystemKey("PASSWORD EXPIRY");
			if (control.getProperties() == true)
			{
				Common.user_password_expiry_days = Integer.parseInt(control.getKeyValue());
			}

			control.setSystemKey("PASSWORD ATTEMPTS");
			if (control.getProperties() == true)
			{
				Common.user_max_password_attempts = Integer.parseInt(control.getKeyValue());
			}
			
			
			JDBUser usr = new JDBUser(Common.sd.getData(sessionID, "selectedHost"), sessionID);
			usr.setUserId(Common.sd.getData(sessionID, "username").toUpperCase());
			usr.setLoginPassword(Common.sd.getData(sessionID, "password"));

			if (usr.login())
			{
				Common.userList.addUser(sessionID, usr);
				logger.debug("User " + usr + " logged on.");
				saveData(session, "language", usr.getLanguage(), true);
				saveData(session, "_ErrorMessage", "", true);
				saveData(session, "username", usr.getUserId(), true);
				loadServletMessages(session);
				Common.sd.setData(sessionID, "defaultPrinter", JPrint.getPreferredPrinterQueueName(), true);
				logger.debug("defaultPrintQueueName - " + JPrint.getPreferredPrinterQueueName());

				if ((usr.isPasswordExpired() == true) || (usr.isPasswordChangeRequired()))
				{
					logger.debug("User " + usr.getUserId() + " password expired.");
					saveData(session, "_ErrorMessage", "Password expired.", true);
					saveData(session, "changePasswordCancelScreen", "login.jsp", true);
					saveData(session, "screenAfterLogon", "changePassword.jsp", true);
					result = false;
				} else
				{
					result = true;
				}

			} else
			{
				logger.debug("User " + usr.getUserId() + " logon error : " + usr.getErrorMessage());
				saveData(session, "_ErrorMessage", usr.getErrorMessage(), true);
				saveData(session, "username", "", true);
				saveData(session, "screenAfterLogon", "login.jsp", true);
			}

		} else
		{
			logger.debug("Logon - Host Connect Failure");
		}

		return result;
	}

	private synchronized void menu(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();

		if (button.equals("Submit"))
		{
			saveData(session, "selectedMenuOption", request.getParameter("selectedMenuOption"), true);

			String selectedMenuOption = Common.sd.getData(sessionID, "selectedMenuOption");

			logger.debug("Selected Menu Option = " + request.getParameter("selectedMenuOption"));

			if (selectedMenuOption.length() == 0)
			{
				displayMenu(request, response);
			}

			if (selectedMenuOption.equals("FRM_PAL_PROD_CONFIRM"))
			{
				saveData(session, "confirmCount", "0", false);
				response.sendRedirect("productionConfirm.jsp");
			}

			if (selectedMenuOption.equals("FRM_PAL_DELETE"))
			{
				saveData(session, "deleteCount", "0", false);
				response.sendRedirect("palletDelete.jsp");
			}

			if (selectedMenuOption.equals("FRM_ADMIN_DESPATCH"))
			{
				saveData(session, "currentDespatchListPage", "1", true);
				despatchMenuDisplay(request, response);
			}

			if (selectedMenuOption.equals("FRM_PAL_INFO"))
			{
				response.sendRedirect("palletInfo.jsp");
			}

			if (selectedMenuOption.equals("FRM_CM_PRINTERS"))
			{
				JMenuRFPrinterList pl = new JMenuRFPrinterList();
				String defaultPrinter = Common.sd.getData(sessionID, "defaultPrinter");
				logger.debug("Default Printer = " + defaultPrinter);
				String html = pl.buildPrinterList(defaultPrinter);
				session.setAttribute("printerList", html);
				response.sendRedirect("printerSelect.jsp");
			}

			if (selectedMenuOption.equals("FRM_USER_PASS_CHANGE"))
			{
				saveData(session, "changePasswordCancelScreen", "displayMenu", true);
				response.sendRedirect("changePassword.jsp");
			}

		} else
		{
			saveData(session, "username", "", true);
			saveData(session, "password", "", true);
			saveData(session, "selectedMenuOption", "", true);
			response.sendRedirect("login.jsp");
		}
	}

	private synchronized void palletConfirm(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();

		if (button.equals(""))
		{
			button = "Submit";
		}

		if (button.equals("Submit"))
		{
			String sscc = JUtility.replaceNullStringwithBlank(request.getParameter("sscc"));
			saveData(session, "sscc", sscc, true);

			String dupCountString = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "dupCountString"));

			if (dupCountString.equals("") == true)
			{
				dupCountString = "0";
			}

			int dupCountint = Integer.valueOf(dupCountString);

			if (sscc.equals("") == false)
			{

				String lastSSCC = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "lastSSCCconfirmed"));
				logger.debug("Last SSCC confirmed = " + lastSSCC);

				if (sscc.equals(lastSSCC) == false)
				{
					dupCountint++;
					lastSSCC = sscc;
					saveData(session, "lastSSCCconfirmed", sscc, true);

					if (bcode.parseBarcodeData(sscc) == true)
					{
						sscc = bcode.getStringforAppID("00");

						if (bcode.isValidSSCCformat(sscc) == true)
						{

							JDBPallet pallet = new JDBPallet(Common.sd.getData(sessionID, "selectedHost"), sessionID);

							if (pallet.getPalletProperties(sscc))
							{
								pallet.setDateOfManufacture(JUtility.getSQLDateTime());

								if (pallet.confirm())
								{

									int confirmCount = Integer.valueOf(Common.sd.getData(sessionID, "confirmCount"));
									confirmCount++;
									session.setAttribute("_ErrorMessage", "SSCC " + sscc + " confirmed.");
									saveData(session, "confirmCount", String.valueOf(confirmCount), false);
									logger.debug(sscc + " confirmed. (" + String.valueOf(confirmCount) + ")");
								} else
								{
									session.setAttribute("_ErrorMessage", pallet.getErrorMessage());
								}
							} else
							{
								session.setAttribute("_ErrorMessage", "SSCC not found.");
							}
						} else
						{
							session.setAttribute("_ErrorMessage", bcode.getErrorMessage());
						}
					} else
					{
						session.setAttribute("_ErrorMessage", bcode.getErrorMessage());
					}
				} else
				{
					dupCountint++;

					if (dupCountint == 2)
					{
						dupCountint = 0;
						saveData(session, "lastSSCCconfirmed", "", true);
						session.setAttribute("_ErrorMessage", "");
					}
				}
			} else
			{
				session.setAttribute("_ErrorMessage", "");
			}

			session.setAttribute("sscc", "");
			response.sendRedirect("productionConfirm.jsp");
		} else
		{
			session.setAttribute("sscc", "");
			session.setAttribute("_ErrorMessage", "");
			displayMenu(request, response);
		}

	}

	private synchronized void palletDelete(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();

		if (button.equals(""))
		{
			button = "Submit";
		}

		if (button.equals("Submit"))
		{
			String sscc = JUtility.replaceNullStringwithBlank(request.getParameter("sscc"));
			saveData(session, "sscc", sscc, true);

			String dupCountString = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "dupCountString"));

			if (dupCountString.equals("") == true)
			{
				dupCountString = "0";
			}

			int dupCountint = Integer.valueOf(dupCountString);

			if (sscc.equals("") == false)
			{

				String lastSSCC = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "lastSSCCdeleted"));
				logger.debug("Last SSCC confirmed = " + lastSSCC);

				if (sscc.equals(lastSSCC) == false)
				{
					dupCountint++;
					lastSSCC = sscc;

					saveData(session, "lastSSCCdeleted", sscc, true);

					if (bcode.parseBarcodeData(sscc) == true)
					{
						sscc = bcode.getStringforAppID("00");

						if (bcode.isValidSSCCformat(sscc) == true)
						{

							JDBPallet pallet = new JDBPallet(Common.sd.getData(sessionID, "selectedHost"), sessionID);

							if (pallet.delete(sscc))
							{
								int deleteCount = Integer.valueOf(Common.sd.getData(sessionID, "deleteCount"));
								deleteCount++;
								session.setAttribute("_ErrorMessage", "SSCC " + sscc + " deleted.");
								saveData(session, "deleteCount", String.valueOf(deleteCount), false);
								logger.debug(sscc + " deleted. (" + String.valueOf(deleteCount) + ")");
							} else
							{
								session.setAttribute("_ErrorMessage", pallet.getErrorMessage());
							}

						} else
						{
							session.setAttribute("_ErrorMessage", bcode.getErrorMessage());
						}
					} else
					{
						session.setAttribute("_ErrorMessage", bcode.getErrorMessage());
					}
				} else
				{
					dupCountint++;

					if (dupCountint == 2)
					{
						dupCountint = 0;
						saveData(session, "lastSSCCdeleted", "", true);
						session.setAttribute("_ErrorMessage", "");
					}
				}
			} else
			{
				session.setAttribute("_ErrorMessage", "");
			}

			session.setAttribute("sscc", "");
			response.sendRedirect("palletDelete.jsp");
		} else
		{
			session.setAttribute("sscc", "");
			session.setAttribute("_ErrorMessage", "");
			displayMenu(request, response);
		}

	}

	private synchronized void palletInformation(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String sessionID = session.getId();
		if (button.equals(""))
		{
			button = "Submit";
		}

		if (button.equals("Submit"))
		{
			Boolean error = true;
			String sscc = JUtility.replaceNullStringwithBlank(request.getParameter("sscc"));
			saveData(session, "sscc", sscc, true);

			String dupCountString = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "dupCountString"));

			if (dupCountString.equals("") == true)
			{
				dupCountString = "0";
			}

			int dupCountint = Integer.valueOf(dupCountString);

			if (sscc.equals("") == false)
			{

				String lastSSCC = JUtility.replaceNullStringwithBlank(Common.sd.getData(sessionID, "lastSSCCinfo"));

				if (sscc.equals(lastSSCC) == false)
				{
					dupCountint++;
					lastSSCC = sscc;

					saveData(session, "lastSSCCinfo", sscc, true);

					if (bcode.parseBarcodeData(sscc) == true)
					{
						sscc = bcode.getStringforAppID("00");

						if (bcode.isValidSSCCformat(sscc) == true)
						{

							JDBPallet pallet = new JDBPallet(Common.sd.getData(sessionID, "selectedHost"), sessionID);
							pallet.setDateOfManufacture(JUtility.getSQLDateTime());

							if (pallet.getPalletProperties(sscc))
							{

								session.setAttribute("_ErrorMessage", "");
								if (pallet.isConfirmed())
								{
									session.setAttribute("material", pallet.getMaterial());
									session.setAttribute("location", pallet.getLocationID());
									session.setAttribute("despatchNo", pallet.getDespatchNo());
									session.setAttribute("batch", pallet.getBatchNumber());
									session.setAttribute("processOrder", pallet.getProcessOrder());
									session.setAttribute("palletStatus", pallet.getStatus());
									session.setAttribute("batchStatus", pallet.getMaterialBatchStatus());
									session.setAttribute("quantity", String.valueOf(pallet.getQuantity()));
									session.setAttribute("uom", pallet.getUom());
									session.setAttribute("dom", pallet.getDateOfManufacture().toString().substring(0, 16));
									session.setAttribute("expiry", pallet.getMaterialBatchExpiryDate().toString().substring(0, 16));
									error = false;
									saveData(session, "lastSSCCinfo", "", true);
									response.sendRedirect("palletInfoDisplay.jsp");
								} else
								{
									session.setAttribute("_ErrorMessage", "SSCC not confirmed.");
								}
							} else
							{
								session.setAttribute("_ErrorMessage", "SSCC not found.");
							}
						} else
						{
							session.setAttribute("_ErrorMessage", "Invalid SSCC format.");
						}
					} else
					{
						session.setAttribute("_ErrorMessage", "Invalid barcode.");
					}
				} else
				{
					dupCountint++;

					if (dupCountint == 2)
					{
						dupCountint = 0;
						saveData(session, "lastSSCCinfo", "", true);
						session.setAttribute("_ErrorMessage", "");
					}
				}
			} else
			{
				session.setAttribute("_ErrorMessage", "");
			}

			if (error)
			{
				session.setAttribute("sscc", "");
				response.sendRedirect("palletInfo.jsp");
			}
		} else
		{
			session.setAttribute("sscc", "");
			session.setAttribute("_ErrorMessage", "");

			displayMenu(request, response);
		}

	}

	private synchronized void palletInformationDisplay(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();

		if (button.equals("Exit"))
		{
			session.setAttribute("sscc", "");
			session.setAttribute("_ErrorMessage", "");
			response.sendRedirect("palletInfo.jsp");
		}
	}

	private synchronized void printerSelect(HttpServletRequest request, HttpServletResponse response, String button) throws ServletException, IOException
	{

		HttpSession session = request.getSession();

		if (button.equals("Select"))
		{
			saveData(session, "defaultPrinter", request.getParameter("selectedPrintQueue"), true);
			session.setAttribute("defaultPrinter", request.getParameter("selectedPrintQueue"));
			logger.debug("User selected printer :" + request.getParameter("selectedPrintQueue"));
		}
		displayMenu(request, response);
	}

	private void releaseSessionResources(HttpSession session)
	{
		String sessionID = session.getId();
		logger.debug("sessionDestroyed - " + sessionID);
		Common.hostList.disconnectSessionAllHosts(sessionID);
		Common.userList.removeUser(sessionID);
	}

	private void saveData(HttpSession session, String key, String data, boolean allowBlank)
	{
		String value = JUtility.replaceNullObjectwithBlank(data);
		String sessionID = session.getId();

		if (Common.sd.setData(sessionID, key, value, allowBlank))
		{
			session.setAttribute(key, value);
		}
	}

	public void sessionCreated(HttpSessionEvent arg0)
	{
		HttpSession session = arg0.getSession();
		String sessionID = session.getId();
		Common.sd.setData(sessionID, "silentExceptions", "Yes", true);

		logger.debug("sessionCreated - " + sessionID);

	}

	public void sessionDestroyed(HttpSessionEvent arg0)
	{
		HttpSession session = arg0.getSession();
		releaseSessionResources(session);
	}
}
