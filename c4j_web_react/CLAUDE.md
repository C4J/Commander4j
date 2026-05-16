# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this project is (and isn't)

Despite the directory name `c4j_web_react`, this is **not** a React project. It is a Jakarta EE web application — server-rendered JSPs backed by a single Java servlet, packaged as a WAR and deployed to **Tomcat 11** on **Java 21**. There is no JavaScript build step and no SPA. The `react` in the name appears to be a project codename, not a technology choice.

It is the mobile/RF-terminal web front-end for **Commander4j** (a warehouse / production / despatch / waste management system). The heavy lifting (DB access, business logic, barcode parsing, printing, i18n) lives in `commander4j.jar`, which is built from the sibling project at `../c4j_commander4j/`.

## Build & run

The project uses **Ant** for compilation and packaging; **Maven is only used to fetch third-party JARs**, not to build.

```sh
# Fetch/refresh third-party dependency JARs into the local Maven repo.
# (Note: getdependencies.sh expects a `java21` shell function/alias to switch JDK.)
sh getdependencies.sh

# Compile sources -> build/classes
ant build

# Package WAR -> target/c4j_web_react.war
ant war

# Clean compiled output
ant clean
```

`build.xml` hard-codes the Tomcat lib path as `../../../../tools/apache-tomcat-11.0.7 (Java 21)/lib` (relative to this project) — it is needed for `jakarta.servlet` APIs at compile time. If Tomcat lives elsewhere on a fresh checkout, override `tomcat.lib.dir`:

```sh
ant -Dtomcat.lib.dir=/path/to/tomcat/lib war
```

The runtime classpath also pulls JARs from `WebContent/WEB-INF/lib/`. The most important one is `commander4j.jar` — this is **not** built here. It comes from the sibling project `../c4j_commander4j/` and must be copied into `WebContent/WEB-INF/lib/` whenever it changes. There is no automated wiring between the two projects.

There are no unit tests in this module.

## Architecture

### Front-controller dispatch

The entire app runs through one servlet:

- `src/com/commander4j/servlet/Process.java` — mapped to `/Process` in `WebContent/WEB-INF/web.xml`. Also registered as an `HttpSessionListener` / `HttpSessionAttributeListener`.
- Every JSP submits its form to `action="Process"` with two hidden fields: `formName` (the JSP filename) and `button` (which button was clicked).
- `doPost` reads `formName` and routes to a `formName.equals("xyz.jsp")` branch that calls a handler method (`despatchSelect`, `palletConfirm`, `wasteLog`, …). Each handler inspects `button`, mutates session state, and ends with `response.sendRedirect("nextPage.jsp")`.
- `doGet` is essentially only used to detect new sessions and redirect to `sessionTimeout.jsp`. **Never add `doGet` logic** — all real flows go through POST.

To add a new screen: create the JSP, post `formName=<filename>.jsp` + `button=<action>`, and add a matching `if (formName.equals(...))` branch in `Process.doPost` that delegates to a new handler method following the existing pattern.

### Session & state model

There are **two parallel state stores**, and most code writes to both:

1. `HttpSession` attributes — read directly by JSPs.
2. `Common.sd` (a `SessionData` store from `commander4j.jar`) keyed by `session.getId()` — read by the Java handlers.

`Process.saveData(session, key, value, allowBlank)` is the canonical helper that writes to both at once. Reading the Java side uses `Common.sd.getData(sessionID, key)`. **Always use `saveData` for writes** so the two views stay in sync.

On session destroy (`sessionDestroyed` / `releaseSessionResources`), all per-session DB connections are released via `Common.hostList.disconnectSessionAllHosts(sessionID)` and `Common.userList.removeUser(sessionID)`. Don't allocate connections that escape this lifecycle.

### Multi-tenant database routing

The user picks a "host" (a database) on `hosts.jsp` before logging in. Hosts are configured in `WebContent/xml/hosts/hosts.xml` (mysql / SQL Server / Oracle examples are checked in; passwords are AES-encrypted). The chosen host is stored in session as `selectedHost`.

All DB-access classes (`JDBDespatch`, `JDBPallet`, `JDBUser`, `JDBWasteLog`, …) take `(selectedHost, sessionID)` in their constructor and route through `Common.hostList.getHost(selectedHost)`. SQL is **per-driver** and loaded from XML at host-connect time:

- `WebContent/xml/sql/sql.<jdbcDriver>.xml` — driver-specific SQL statements
- `WebContent/xml/view/view.<jdbcDriver>.xml` — driver-specific view definitions
- `WebContent/xml/schema/<jdbcDriver>/` — schema files

If you add or change SQL, update **all three** driver XMLs (mysql / sqlserver / oracle) — they are not generated.

### Where the code actually lives

This module's `src/` contains exactly one Java file (`Process.java`, ~2500 lines) plus `package-info.java`. Everything else — `com.commander4j.db.*`, `com.commander4j.sys.Common`, `com.commander4j.html.JMenuRF*`, `com.commander4j.bar.JEANBarcode`, `com.commander4j.bean.JLanguage`, `com.commander4j.util.*` — lives in `commander4j.jar`, sourced from `../c4j_commander4j/src/`. If you need to inspect or modify those classes, work in that sibling project and rebuild the jar.

### JSP conventions

- Most JSPs declare `<jsp:useBean id="Lang" class="com.commander4j.bean.JLanguage" scope="page">` and use `Lang.getText("key")` for i18n labels.
- JSPs read pre-rendered HTML fragments from session attributes (e.g. `menuList`, `despatchList`, `hostList`) — these are built server-side by `JMenuRF*` classes in `Process` handlers and dropped into the page with `<%= ... %>`.
- Every page sets aggressive no-cache headers (mirror this on any new page).
- Styling is plain CSS at `WebContent/style/commander.css`.

## Configuration files

- `WebContent/xml/hosts/hosts.xml` — DB connection definitions (one `<Site>` per database).
- `WebContent/xml/config/config.xml`, `email.xml` — global app & email config.
- `WebContent/xml/log/log4j2.xml` — Log4j2 config; logs to console + `./logs/commander4j.log` (rolling).
- `WebContent/xml/barcode/` — barcode label / EAN-128 templates.

## Things to be careful about

- The `commander4j.jar` in `WebContent/WEB-INF/lib/` is **not** in version control of this module's source — treat it as an external artifact rebuilt from `../c4j_commander4j/`.
- `Process.java` uses `synchronized` heavily on instance-level handler methods. The servlet is a singleton, so this serializes all requests through those methods. Don't introduce blocking I/O without thinking about contention.
- The Eclipse `.classpath` / `.project` files are present and Eclipse-friendly (WTP / Tomcat 11 server runtime); the project also opens cleanly in Eclipse if that is preferred over the command-line Ant build.
