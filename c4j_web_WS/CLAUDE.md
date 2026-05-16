# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

`c4j_web_WS2` is a Jakarta EE 5 servlet web app (target: Apache Tomcat 11, Java 21) that exposes a REST-style CRUD API over the Commander4j Quality Management (QM) panel-scoring tables. It also serves a static HTML/JS operator UI under `src/main/webapp/panel/`. Output artifact is `target/c4j_web_WS.war`.

## Build / package / deploy

This is an Eclipse + Ant project (no Maven/Gradle, no `pom.xml`). All commands run from the project root.

- Compile only: `ant build`
- Clean: `ant clean`
- Build WAR: `ant war` → `target/c4j_web_WS.war`
- Deploy: drop the WAR into Tomcat's `webapps/`. The bundled Tomcat lives at `../../../../tools/apache-tomcat-11.0.7 (Java 21)` (path is hardcoded in `build.xml` as `tomcat.lib.dir` for the compile classpath).

There is **no test suite** in this repo — no `src/test`, no JUnit on the classpath. Don't claim tests "pass"; verify changes by deploying the WAR and exercising the endpoints (see `index.html` for the route map and per-resource HTML pages like `Trays.html` for JSON examples).

To run a single endpoint manually after deploy: `curl 'http://localhost:8080/c4j_web_WS/Trays?panelID=1'` etc. Servlet URL patterns are defined in `src/main/webapp/WEB-INF/web.xml`.

## Architecture

### Servlet → DAO → Entity triplets

Each REST resource follows the same three-class pattern in `com.commander4j.c4jWS`:

- `J*Controller extends HttpServlet` — implements `doGet`/`doPost`/`doPut`/`doDelete`. Parses URL params via `JURL`, deserializes JSON body via Gson (`new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")`), delegates to the DAO, serializes the entity back as `application/json`.
- `J*DB` — JDBC DAO. Holds `hostID` + `sessionID`, looks up the per-session `Connection` via `Common.hostList.getHost(hostID).getConnection(sessionID)`, and executes `PreparedStatement`s whose SQL text comes from `getSqlstatements().getSQL("<id>")`.
- `J*Entity` — POJO (annotated `@jakarta.persistence.Entity` but **not** actually persisted by JPA — it's a Gson DTO; the annotation is decorative). Field names are the JSON shape.

Resources: `Trays`, `TraySamples`, `TrayResults`, `Panels`, `Users` (QM users), `Controls` (SYS_CONTROL key/value), `SelectLists`.

### Per-session JDBC connections

`AppServletSessionListener.sessionCreated` opens one JDBC `Connection` per HTTP session and stores it under the session ID; `sessionDestroyed` closes it. Every controller's first line is `request.getSession()` to force this listener to fire before any DB call. DAOs always pass `request.getSession().getId()` into their constructor so they can fetch the right connection. Session timeout is 60 minutes (`web.xml`).

### `Common` global state

`com.commander4j.c4jWS.Common` is a class of `public static` fields used as app-wide singletons:
- `hostList: JHostList` — all configured DB hosts loaded from `hosts.xml`
- `selectedHostID: String` — the active host (set at context init to the host whose `<UniqueID>` is the literal string `service`)
- `paths: Hashtable` — context-relative paths to the SQL/view XML files, keyed by `sql.<driverClass>.xml` / `view.<driverClass>.xml`
- `encryptionKey: String` — AES key used to decrypt `<jdbcPassword>` entries in `hosts.xml`. Hardcoded.

### Configuration loading (important quirk)

`AppServletContextListener.contextInitialized` does **not** read `hosts.xml` from inside the WAR at runtime. On first startup it copies the WAR's `xml/hosts/hosts.xml` and `hosts.dtd` to a location **outside** `webapps/`:

```
${catalina.home}/c4j_config/<contextPath>/hosts.xml
```

…and from then on reads from that external location. Editing `src/main/webapp/xml/hosts/hosts.xml` only affects fresh installs; to change DB targets on a deployed Tomcat, edit `$CATALINA_HOME/c4j_config/c4j_web_WS/hosts.xml` (or whatever the deployed context name is).

**`hosts.xml` is only read at context init.** Edits do not take effect until Tomcat is stopped and started — a hot redeploy of the WAR is not required (and would not help on its own, since the external config copy already exists). The "active" site is whichever `<Site>` has `<UniqueID>service</UniqueID>`.

`log4j2.xml` is loaded from inside the WAR at `xml/log/log4j2.xml` and writes to `./logs/commander4j.log` (relative to Tomcat's working dir).

### Externalised SQL

SQL is **not** in Java source. It lives in `src/main/webapp/xml/sql/sql.<jdbcDriverClass>.xml`, with one file per supported driver (`com.mysql.cj.jdbc.Driver`, `com.microsoft.sqlserver.jdbc.SQLServerDriver`, `oracle.jdbc.driver.OracleDriver`). Statements are looked up by string ID like `JDBQMTrays.isValidTrayID`, `JDBControl.update`, etc. `{schema}` is a substitution token resolved at runtime. There's a parallel `xml/view/view.<driver>.xml` for view DDL.

When adding/changing a query, update **all three** driver XML files if the resource is meant to be portable, otherwise add only to the drivers you target. The DAO calls fail at runtime if the ID is missing — there's no compile-time check.

Path lookup for these XMLs goes through `Common.paths`, which is populated in `AppServletContextListener` against the legacy MySQL driver name `com.mysql.jdbc.Driver` — note the `hosts.xml` actually uses `com.mysql.cj.jdbc.Driver`. If you touch driver wiring, check whether this mismatch matters for your code path.

### Frontend

`src/main/webapp/` contains:
- Top-level `*.html` (e.g. `Trays.html`, `Panels.html`) — hand-written HTML reference docs for each REST resource, linked from `index.html`. Not part of the operator UI.
- `panel/` — the actual operator UI (vanilla HTML + per-page `script*.js`, no build step, no framework). Entry point: `panel/menu.html`. Pages call the REST endpoints under the same context using `getContextPath()`.

There is no JS bundler, no `package.json`. `jsconfig.json` exists only to give the IDE JS IntelliSense.

## Conventions to preserve

- Keep the controller→DAO→entity pattern when adding a new resource; register the servlet in `web.xml` (this project does **not** use Jersey/JAX-RS annotations even though Jersey is on the classpath — servlet mappings are explicit).
- Keep SQL in the driver-specific XML files, not inline in Java.
- Don't introduce JPA persistence — `@Entity` here is misleading; entities are Gson DTOs only.
- The first line of every servlet method is `request.getSession();` to force connection setup. Don't remove it.
