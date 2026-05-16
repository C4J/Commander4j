# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and run

This is an Eclipse-style Java EE / Jakarta web project (Java 21, Servlet 5.0/Jakarta) built with Ant and deployed as a WAR to Tomcat 11.

- `ant build` — compiles `src/main/java` to `build/classes`. Classpath includes `src/main/webapp/WEB-INF/lib/*.jar` plus Tomcat 11 libs from `../../../../tools/apache-tomcat-11.0.7 (Java 21)/lib` (path is hard-coded in `build.xml`; adjust the `tomcat.lib.dir` property if Tomcat lives elsewhere).
- `ant war` — produces `target/c4j_web_Issue.war`.
- `ant clean` — removes `build/classes`.
- No test framework is configured. There are no unit tests in this project.
- The project is also importable into Eclipse (`.project`, `.classpath`, `.settings/`); the WTP context root is `c4j_web_Issue`.

## Architecture

This WAR is one of several siblings under `Source/b7/Alternate/` in the wider Commander4j ecosystem. It is a **thin servlet façade**: most of the domain logic, database connection management, host configuration, and shared utilities live in `commander4j.jar` (built from the sibling `c4j_commander4j` project and copied into `src/main/webapp/WEB-INF/lib/`). When code references `com.commander4j.sys.Common`, `com.commander4j.db.JDBUser`, `com.commander4j.db.JDBPallet`, `com.commander4j.db.JDBProcessOrder`, `com.commander4j.util.JUtility`, etc., those classes come from that shared jar — not from `src/main/java`. The classes under `src/main/java/com/commander4j/db/` named `JQM*DB` are this WAR's own thin DB query helpers; the `JDB*` classes are from the shared jar.

### Request flow

1. **Static pages** in `src/main/webapp/html/` (`userLogon.html`, `menu.html`, pallet/process-order screens) load page-specific scripts from `src/main/webapp/javascript/` (`userScript.js`, `palletScript.js`, etc.). The frontend is plain HTML/JS — no framework — and calls back to the servlets via `fetch()` against `getContextPath() + "/<Resource>"`.
2. **`CanonicalUrlFilter`** (`src/main/java/com/commander4j/filter/CanonicalUrlFilter.java`, mapped to `/*`) intercepts every request, looks up the lowercased path in `WEB-INF/canon-paths.properties`, and 301-redirects to the canonical mixed-case URL when the case doesn't match. Add new HTML pages to that properties file so case-insensitive URLs continue to work.
3. **Controllers** (`com.commander4j.controller.JQM*Controller`) are `HttpServlet`s registered in `WEB-INF/web.xml` under URL patterns like `/Users`, `/ProcessOrders`, `/Pallets`, `/PalletHistory`, `/Returnable`, `/Resources`, `/ViewBOM`, `/Host`. They:
   - Use `Gson` to (de)serialize JSON request bodies into entities under `com.commander4j.entity` (`JQM*Entity`).
   - Use `JURL` (`com.commander4j.util.JURL`) for query-string parsing.
   - Dispatch on a string `action` parameter or field (e.g. `getOrders`, `getOrder`, `query`, `info`, `logon`) — there is no annotation-based routing; each controller is one big switch.
   - Delegate to the WAR-local `JQM*DB` classes, which run prepared statements through `Common.hostList.getHost(hostID).getConnection(sessionID).prepareStatement(...)` with SQL keyed by name from `getSqlstatements().getSQL("...")`.
4. **SQL is externalized**, not embedded. Statement bodies live in `src/main/webapp/xml/sql/sql.<jdbcDriver>.xml` (one file per driver: MySQL, SQL Server, Oracle), and views in `src/main/webapp/xml/view/view.<jdbcDriver>.xml`. The driver-appropriate file is selected at session creation time based on the configured host's `jdbcDriver`. When adding a new query, **add it to all three driver XMLs** under the same key, then call it from the DB class with `getSqlstatements().getSQL("YourKey")`.

### Lifecycle and state

- `AppServletContextListener.contextInitialized` runs at WAR startup: it reconfigures Log4j2 from `/xml/log/log4j2.xml`, populates `Common.paths`, then loads the host list. The hosts XML is **not** read directly from the WAR — it is copied on first run from `/xml/hosts/hosts.xml` to `${catalina.home}/c4j_config/<contextPath>/hosts.xml` and read from there on subsequent runs. Edit the external copy in `c4j_config` for environment-specific changes; the in-WAR copy is the seed/default.
- One JVM-wide selected host (`Common.selectedHostID`) is chosen by the unique ID `"service"` from `hosts.xml`. All sessions use this host.
- `AppServletSessionListener.sessionCreated` opens a per-`HttpSession` JDBC connection via `Common.hostList.getHost(...).connect(sessionId, ...)` and loads the driver-specific SQL/view XML. `sessionDestroyed` disconnects. **All DB classes are constructed with `(hostID, sessionId)` and look up the connection from `Common`** — never open your own connection.
- Session timeout is 60 minutes (`web.xml`).

### Adding a new endpoint

1. Add a `JQM<Name>Entity` POJO under `com.commander4j.entity` with Gson-friendly getters/setters.
2. Add a `JQM<Name>DB` under `com.commander4j.db` taking `(hostID, sessionID)`; route SQL via `Common.hostList.getHost(getHostID()).getSqlstatements().getSQL("Key")`.
3. Add the SQL under the matching key in all three `sql.<driver>.xml` files (and view XMLs if applicable).
4. Add a `JQM<Name>Controller extends HttpServlet`; implement `doGet`/`doPut` and dispatch on an `action` param.
5. Register the servlet and `<servlet-mapping>` (both `/Name` and `/Name/*`) in `WEB-INF/web.xml`.
6. If you add HTML pages, register them in `WEB-INF/canon-paths.properties` so the canonical-URL filter handles case variants.

## Conventions and gotchas

- Java package is `com.commander4j.*` — same root namespace as the shared jar. Be careful when adding classes that you don't shadow a class from `commander4j.jar`.
- `web.xml` opens with `<?com.commander4j.xml ... ?>` and uses `https://jakarta.ee/com.commander4j.xml/...` namespaces — these look broken but appear to be intentional renames in this project; don't "fix" them without confirming, the WAR builds and deploys as-is.
- Controllers use `org.apache.catalina.connector.Response` for status constants (`SC_ACCEPTED`, `SC_NOT_ACCEPTABLE`) — that's a Tomcat-internal class. It only resolves at compile/run time because the Tomcat libs are on the classpath via `build.xml`.
- `JURL.getParameters` does its own `&`/`=` split and only `%20` decoding; it is not a full URL decoder. Don't pass `+`-encoded spaces or other percent-escapes through it.
- Logs go to wherever `xml/log/log4j2.xml` directs them. The `logs/` and `src/main/webapp/logs/` directories in the project are usually empty placeholders.
- The pre-built `dist` file (~50 MB) and `target/c4j_web_Issue.war` are checked-in build artifacts — regenerate them with `ant war` rather than editing.
