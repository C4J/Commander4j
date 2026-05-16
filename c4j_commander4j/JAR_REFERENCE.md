# Jar reference — Commander4j core (Scratchpad)

**Generated:** 2026-05-03 from current `lib/` (115 jars) and `mvn dependency:tree`.

A sanity-check reference for what each jar does, why Commander4j needs it, and
where it comes from in the dep tree. Use it when triaging classpath issues, when
deciding whether something is safe to remove, or when a library version needs
bumping and you want to know what'll break.

**Direct vs transitive:** "Direct" = declared in `c4j_commander4j/pom.xml`.
"Transitive" = pulled in by another dep; the **Pulled by** column lists the parent.
"Out of band" = managed manually outside Maven (only `c4j_cups4j` falls in this category).

---

## 1. Core JVM language

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `groovy-4.0.31.jar` | 4.0.31 | The Apache Groovy language runtime — dynamic JVM language used by JasperReports for expression evaluation in `language="groovy"` reports. | Reports' Groovy expressions evaluate against this. Also usable as a scripting language by Commander4j itself. | Direct |
| `groovy-sandbox-1.26-jaspersoft-2.jar` | 1.26-jaspersoft-2 | Sandbox/whitelist mechanism for Groovy — restricts what Groovy code can call. JasperReports fork of the Kohsuke library. | Lets JasperReports run untrusted Groovy from `.jrxml` files without exposing arbitrary JVM APIs. | `jasperreports-groovy:7.0.6` |

---

## 2. Logging

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `slf4j-api-2.0.17.jar` | 2.0.17 | The SLF4J facade — neutral API used by libraries that don't want to commit to a specific logging implementation. Itself does no logging; needs a binding. | Logging API used by many transitives (JasperReports, hibernate, jaxb-runtime, c4j_cups4j, etc.). Without this they'd fail to load. | Direct |
| `log4j-api-2.25.3.jar` | 2.25.3 | Apache Log4j 2 — the API portion. Code logs against this. | Direct dep — Commander4j (and your reports/Spring layer) log via Log4j 2. | Direct |
| `log4j-core-2.25.3.jar` | 2.25.3 | Apache Log4j 2 — the implementation. The actual log routing/appenders. | Required at runtime to make `log4j-api` calls actually go anywhere. | Direct |
| `commons-logging-1.3.6.jar` | 1.3.6 | Older facade pre-dating SLF4J — Apache HttpClient 4.x and Spring still call into it. | Required by `httpclient-4.x` (used by `jasperreports-data-adapters-http`) and by Spring core. | Direct |
| `jboss-logging-3.5.0.Final.jar` | 3.5.0.Final | JBoss's own logging facade. Hibernate calls into this; it can route to SLF4J. | Hibernate's logging would NPE without it. | `hibernate-core` (runtime) |

> **Note:** four logging frameworks coexist on the classpath (SLF4J, Log4j 2, commons-logging, JBoss Logging). Each library picks whichever it was written against; they don't clash but you may need bridges if you want unified output.

---

## 3. HTTP clients

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `httpclient5-5.4.3.jar` | 5.4.3 | Apache HttpClient **5** — modern HTTP client. Async-capable, HTTP/2 support, jakarta-namespace. | Used by `c4j_cups4j` to send IPP printing requests over HTTP. Now declared directly so removing the cups4j Maven dep doesn't lose it. | Direct |
| `httpcore5-5.3.4.jar` | 5.3.4 | Low-level HTTP transport for HttpClient 5 — connection management, message parsing. | Required by `httpclient5`. | `httpclient5` |
| `httpcore5-h2-5.3.4.jar` | 5.3.4 | HTTP/2 transport extension for HttpClient 5. | Required by `httpclient5` even if you only use HTTP/1.1 — protocol negotiation. | `httpclient5` |
| `httpclient-4.5.14.jar` | 4.5.14 | Apache HttpClient **4** — the older generation, classpath-coexists with v5 (different package names, no conflict). | Required by `jasperreports-data-adapters-http` for the HTTP data adapter feature. | `jasperreports-data-adapters-http:7.0.6` |
| `httpcore-4.4.16.jar` | 4.4.16 | Low-level HTTP transport for HttpClient 4. | Required by `httpclient-4.x`. | `jasperreports-data-adapters-http:7.0.6` |

---

## 4. XML / JAXB stack (jakarta side, single coherent set)

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `jakarta.xml.bind-api-4.0.5.jar` | 4.0.5 | Jakarta XML Binding **API** (jakarta.xml.bind). Annotations + interfaces only, no impl. The successor to javax.xml.bind. | XML ↔ Java POJO marshalling for c4j_cups4j's IPP attribute config files, plus any other JAXB usage in Commander4j. | Direct |
| `jaxb-runtime-4.0.7.jar` | 4.0.7 | EclipseLink/GlassFish JAXB runtime — the implementation behind the API. | Required at runtime; without it `JAXBContext.newInstance(...)` would fail to find a provider. | Direct |
| `jaxb-core-4.0.7.jar` | 4.0.7 | Shared core utilities for jaxb-runtime. | Internal dep of jaxb-runtime. | `jaxb-runtime` |
| `txw2-4.0.7.jar` | 4.0.7 | Typed XML Writer 2 — internal helper for jaxb-runtime's marshalling. | Internal dep of jaxb-runtime. | `jaxb-runtime` → `jaxb-core` |
| `istack-commons-runtime-4.1.2.jar` | 4.1.2 | Internal-stack utility classes shared across Eclipse XML projects. | Internal dep of jaxb-core. | `jaxb-runtime` → `jaxb-core` |
| `jakarta.activation-api-2.1.4.jar` | 2.1.4 | Jakarta Activation API — MIME type handling abstraction (DataSource, DataHandler). JAXB and JavaMail both use it. | Required by jakarta.xml.bind-api and angus-mail. | `jakarta.xml.bind-api` |
| `angus-activation-2.0.3.jar` | 2.0.3 | Eclipse Angus implementation of Jakarta Activation API. | The runtime impl behind jakarta.activation-api. | Direct (runtime) |

> **Don't trust appearances:** if you ever see `jakarta.xml.bind-api:2.3.x` show up here, it still contains `javax.xml.bind.*` packages despite the jakarta groupId. Only versions ≥ 3.0.0 are truly the jakarta namespace.

---

## 5. XML parsing & extras (non-JAXB)

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `aalto-xml-1.3.4.jar` | 1.3.4 | Ultra-fast non-blocking XML pull parser (StAX2 implementation). | Direct dep — used where Commander4j needs streaming XML parsing. | Direct |
| `stax2-api-4.2.2.jar` | 4.2.2 | StAX2 API — extensions on top of `javax.xml.stream` (StAX). | Required by aalto-xml and woodstox-core. | `aalto-xml` |
| `woodstox-core-7.0.0.jar` | 7.0.0 | High-performance StAX XML parser/writer (alternative to JDK's built-in). | Used by `jackson-dataformat-xml` for XML serialization. | `jackson-dataformat-xml` |
| `jaxen-2.0.0.jar` | 2.0.0 | XPath engine — multi-DOM XPath implementation. | Direct dep — used for XPath-based XML traversal. Also pulled by `jasperreports-jaxen`. | Direct |
| `xml-apis-ext-1.3.04.jar` | 1.3.04 | Older "external" XML APIs (SVG/SMIL DOM). | Required by Batik (SVG library) for SVG DOM types. | `batik-anim` |

---

## 6. JSON & data formats

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `jackson-core-2.18.2.jar` | 2.18.2 | Jackson — low-level streaming JSON parser/generator. | Required by everything Jackson — JasperReports uses it for JSON data adapters and serialization. | `jasperreports:7.0.6` |
| `jackson-annotations-2.18.2.jar` | 2.18.2 | Jackson — annotations like `@JsonProperty`. | Required by jackson-databind. | `jasperreports:7.0.6` |
| `jackson-databind-2.18.2.jar` | 2.18.2 | Jackson — POJO ↔ JSON mapping. The high-level API. | JasperReports JSON data adapter. | `jasperreports:7.0.6` |
| `jackson-dataformat-xml-2.18.2.jar` | 2.18.2 | Jackson XML support — POJO ↔ XML using same patterns as JSON. | JasperReports' newer XML serialization paths. | `jasperreports:7.0.6` |

---

## 7. Apache Commons

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `commons-lang3-3.20.0.jar` | 3.20.0 | Foundational utility classes — `StringUtils`, `ObjectUtils`, `ArrayUtils`, etc. The 3.x package is `org.apache.commons.lang3.*` (the 2.x `org.apache.commons.lang.*` package is **not** on this classpath — see `JAR_CLEANUP.md` §3 for the report-fix story). | Direct app use + transitive of dozens of libraries. | Direct |
| `commons-text-1.15.0.jar` | 1.15.0 | String manipulation extras — text differencing, similarity, escape utilities. Successor to lang3's deprecated escape APIs. | Direct dep. | Direct |
| `commons-io-2.21.0.jar` | 2.21.0 | I/O utilities — `IOUtils`, `FileUtils`, file filters, byte-array helpers. | Direct dep + transitive of c4j_cups4j and others. | Direct |
| `commons-codec-1.21.0.jar` | 1.21.0 | Encoding/decoding — Base64, hex, MD5/SHA digesters, URL codec. | Direct dep + used by HttpClient, c4j_cups4j (Base64 IPP auth), etc. | Direct |
| `commons-collections-3.2.2.jar` | 3.2.2 | Older collection utilities (predates Java 5 generics). Still required by some legacy libraries. | Direct dep — kept for legacy compatibility with libs that haven't moved to commons-collections4. | Direct |
| `commons-collections4-4.5.0.jar` | 4.5.0 | Modern, generic-aware version of commons-collections. Bag, Bidi map, etc. | Required by opencsv and used directly. | `opencsv` |
| `commons-beanutils-1.11.0.jar` | 1.11.0 | JavaBeans property reflection — `BeanUtils.copyProperties`, `PropertyUtils.getProperty`. | Required by opencsv for column-to-bean mapping. | `opencsv` |
| `commons-beanutils2-2.0.0-M2.jar` | 2.0.0-M2 | Modernised beanutils (milestone release). | JasperReports core uses this newer version. | `jasperreports:7.0.6` |
| `commons-compress-1.28.0.jar` | 1.28.0 | Archive format support — zip, tar, 7z, gzip, bzip2. | Direct dep. Apache POI also needs it for OOXML (zipped XML). | Direct |
| `commons-math3-3.6.1.jar` | 3.6.1 | Numerical computing library — statistics, linear algebra, optimization. | Required by Apache POI for Excel formula evaluation. | `poi:5.5.1` |
| `commons-logging-1.3.6.jar` | (see §2 Logging) | | | |

---

## 8. JasperReports core + extensions

The base `jasperreports.jar` is the engine; the rest are feature-specific addons.

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `jasperreports-7.0.6.jar` | 7.0.6 | Core JasperReports engine — fills `.jasper` templates with data, exports to PDF/HTML/Excel/etc. | Direct dep. Commander4j renders all its labels, pallet sheets, despatch reports, etc., through this. | Direct |
| `jasperreports-fonts-7.0.6.jar` | 7.0.6 | Default Jasper font extension (DejaVu Sans, etc.). | Required so reports using default fonts have them available at runtime. | Direct |
| `jasperreports-metadata-7.0.6.jar` | 7.0.6 | Schema/metadata extensions used by JR. | Direct dep — needed for some report features. | Direct |
| `jasperreports-functions-7.0.6.jar` | 7.0.6 | Built-in expression functions library (date, string, math helpers callable from `.jrxml`). | Direct dep — reports may use `DATEFORMAT(...)`, `MSG(...)` etc. | Direct |
| `jasperreports-data-adapters-7.0.6.jar` | 7.0.6 | Data adapter framework — connects reports to data sources (JDBC, JSON, XML, CSV...). | Direct dep — without it Commander4j can't feed report parameters from databases. | Direct |
| `jasperreports-data-adapters-http-7.0.6.jar` | 7.0.6 | HTTP data adapter — fetch JSON/XML/CSV from a URL as report data. | Direct dep — pulls in HttpClient 4. | Direct |
| `jasperreports-pdf-7.0.6.jar` | 7.0.6 | PDF export module — wraps OpenPDF. | Direct dep — your label printing pipeline outputs PDF. | Direct |
| `jasperreports-charts-7.0.6.jar` | 7.0.6 | Chart support inside reports — wraps JFreeChart. | Direct dep. | Direct |
| `jasperreports-chart-themes-7.0.6.jar` | 7.0.6 | Pre-built visual themes for JR charts. | Direct dep — only needed if you use themed charts. | Direct |
| `jasperreports-chart-customizers-7.0.6.jar` | 7.0.6 | Chart customizer extensions (programmatic chart tweaks). | Direct dep — only needed if you customize charts. | Direct |
| `jasperreports-excel-poi-7.0.6.jar` | 7.0.6 | Excel export module — wraps Apache POI for XLS/XLSX. | Direct dep — for Excel report exports. | Direct |
| `jasperreports-javascript-7.0.6.jar` | 7.0.6 | JavaScript expression language support — wraps Mozilla Rhino. | Direct dep — only needed if any `.jrxml` uses `language="javascript"`. | Direct |
| `jasperreports-groovy-7.0.6.jar` | 7.0.6 | Groovy expression language support — wraps Apache Groovy. | Direct dep — required because most of your reports use `language="groovy"`. | Direct |
| `jasperreports-jdt-7.0.6.jar` | 7.0.6 | Java compiler integration — uses Eclipse JDT to compile Java expressions in reports at fill time (or via `compilereports.sh`). | Direct dep — required for `.jrxml` → `.jasper` compilation. | Direct |
| `jasperreports-ant-7.0.6.jar` | 7.0.6 | Ant task `<jrc>` for batch report compilation. | Used by `compile_reports.xml`. | Direct |
| `jasperreports-annotation-processors-7.0.6.jar` | 7.0.6 | Compile-time annotation processors for JR (build-time, not runtime). | Required by JR build pipeline. | Direct |
| `jasperreports-json-7.0.6.jar` | 7.0.6 | JSON data source — read JSON as JR data. | Direct dep — pulls in antlr (legacy 2.7.x). | Direct |
| `jasperreports-jaxen-7.0.6.jar` | 7.0.6 | Jaxen XPath integration for XML data sources. | Direct dep. | Direct |
| `jasperreports-hibernate-7.0.6.jar` | 7.0.6 | Hibernate ORM data source — query Hibernate entities as a JR data source. | Direct dep — pulls in the entire Hibernate stack. | Direct |
| `jasperreports-spring-7.0.6.jar` | 7.0.6 | Spring framework integration — Spring beans as JR data sources, Spring-managed report runners. | Direct dep — pulls in Spring core + beans. | Direct |
| `jasperreports-barbecue-7.0.6.jar` | 7.0.6 | Barbecue barcode integration. | Direct dep — barcode rendering inside reports. | Direct |
| `jasperreports-barcode4j-7.0.6.jar` | 7.0.6 | Barcode4j barcode integration — wider barcode-symbology support than barbecue. | Direct dep. | Direct |
| `jasperreports-servlets-7.0.6.jar` | 7.0.6 | Servlet helpers for JR — only relevant if Commander4j has a servlet/web component. | Direct dep — kept for completeness. | Direct |

---

## 9. JasperReports rendering helpers

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `openpdf-1.3.43.jar` | 1.3.43 | OpenPDF — fork of iText 4 (LGPL/MPL). PDF generation library that JR-PDF uses. | PDF export. | `jasperreports-pdf` |
| `xmpcore-6.1.11.jar` | 6.1.11 | Adobe XMP core library — embeds XMP metadata in PDFs. | PDF metadata embedding by openpdf/JR-PDF. | `jasperreports-pdf` |
| `rhino-1.8.1.jar` | 1.8.1 | Mozilla Rhino — JavaScript engine for the JVM. | Required if any report uses JavaScript expressions. | `jasperreports-javascript` |
| `rhino-tools-1.8.1.jar` | 1.8.1 | Companion tools for Rhino. | Required by Rhino. | `jasperreports-javascript` |
| `ecj-3.21.0.jar` | 3.21.0 | Eclipse compiler for Java — embeddable Java compiler used by JR-JDT to compile Java expressions in `.jrxml` files. | Required for compiling reports that use `language="java"`. | `jasperreports-jdt` |
| `antlr-2.7.7.jar` | 2.7.7 | Very old (2003!) ANTLR runtime — required by JR's JSON path parser. | Legacy dep — keep, can't easily upgrade. | `jasperreports-json` |
| `antlr4-runtime-4.10.1.jar` | 4.10.1 | Modern ANTLR 4 runtime — required by Hibernate's HQL parser. | Hibernate query parsing. | `hibernate-core` |
| `core-3.4.0.jar` | 3.4.0 | Google ZXing barcode core — barcode generation/decoding. | Required by `jasperreports-barcode4j` for some symbologies. | `jasperreports-barcode4j` |

---

## 10. Charts / SVG / graphics

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `jfreechart-1.5.6.jar` | 1.5.6 | JFreeChart — chart library (line/bar/pie/etc.) producing AWT-rendered output. | Direct dep — Commander4j charting. | Direct |
| `barbecue-1.5-beta1.jar` | 1.5-beta1 | Older barcode generator — narrower symbology set than barcode4j. | Direct dep — used directly + via `jasperreports-barbecue`. | Direct |
| `barcode4j-2.4.0.jar` | 2.4.0 | Newer/wider-coverage barcode generator (the singingbush fork — original library is unmaintained). | Direct dep — barcode generation. | Direct |
| `xmlgraphics-commons-2.11.jar` | 2.11 | Apache XML Graphics commons — shared utilities used by Batik. | Used by Batik. | `batik-awt-util` |
| `batik-anim-1.19.jar` … `batik-xml-1.19.jar` (15 jars) | 1.19 | Apache **Batik** — full SVG toolkit. Each module covers a slice: `anim` (animation), `awt-util` (AWT-to-SVG bridges), `bridge` (DOM↔graphics), `css`, `dom`, `gvt` (graphics vector tree), `i18n`, `parser`, `script` (ECMAScript), `svg-dom`, `svggen` (Java-to-SVG), `util`, `xml`, `constants`, `ext`, `shared-resources`. | Direct deps — SVG support for charts/labels with vector graphics, plus required by JR's HTML/PDF rendering pipeline for SVG content. | Direct (each module declared) |

---

## 11. Excel / Office / database file formats

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `poi-5.5.1.jar` | 5.5.1 | Apache POI — read/write Microsoft Office binary formats (XLS .doc .ppt). | Direct dep — Excel I/O. | Direct |
| `poi-ooxml-5.5.1.jar` | 5.5.1 | Apache POI OOXML — modern Office formats (.xlsx .docx .pptx). | Direct dep. | Direct |
| `poi-ooxml-lite-5.5.1.jar` | 5.5.1 | Pre-stripped subset of OOXML schemas (smaller, faster startup). | Required by poi-ooxml. | `poi-ooxml` |
| `xmlbeans-5.3.0.jar` | 5.3.0 | Apache XMLBeans — XML schema → Java binding. POI uses it for OOXML schemas. | Required by poi-ooxml. | `poi-ooxml` |
| `SparseBitSet-1.3.jar` | 1.3 | High-performance sparse bit set. | Required by POI for cell-tracking optimisations. | `poi:5.5.1` |
| `curvesapi-1.08.jar` | 1.08 | Implementation of advanced spline/curve math. | Required by POI for Excel chart shape rendering. | `poi-ooxml` |
| `jackcess-4.0.10.jar` | 4.0.10 | Pure-Java reader/writer for **Microsoft Access** `.mdb` / `.accdb` files. | Direct dep — Commander4j may import legacy Access data. | Direct |

---

## 12. Hibernate ORM stack

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `hibernate-core-6.3.1.Final.jar` | 6.3.1.Final | Hibernate ORM 6 — JPA-compliant Object/Relational mapper. Big runtime: query parser, session/transaction mgmt, dialect, dirty tracking. | Required by `jasperreports-hibernate` data adapter (and possibly direct DB use in Commander4j). | `jasperreports-hibernate` |
| `hibernate-commons-annotations-6.0.6.Final.jar` | 6.0.6.Final | Annotation utilities shared across Hibernate projects. | Required by Hibernate core. | `hibernate-core` (runtime) |
| `jakarta.persistence-api-3.1.0.jar` | 3.1.0 | Jakarta Persistence (JPA) API — `@Entity`, `EntityManager`, etc. | Required as the API surface Hibernate implements. | `jasperreports-hibernate` |
| `jakarta.transaction-api-2.0.1.jar` | 2.0.1 | Jakarta Transaction API — `UserTransaction`, etc. | Required by Hibernate for transaction abstraction. | `hibernate-core` |
| `jakarta.inject-api-2.0.1.jar` | 2.0.1 | Jakarta Inject API (`@Inject`, `Provider<T>`). | Required by Hibernate for some DI hooks. | `hibernate-core` (runtime) |
| `jandex-3.1.2.jar` | 3.1.2 | Annotation indexer — fast offline scan of class annotations. Speeds up Hibernate startup. | Required by Hibernate. | `hibernate-core` (runtime) |
| `classmate-1.5.1.jar` | 1.5.1 | Generic-type introspection helper from FasterXML (used to resolve type parameters at runtime). | Required by Hibernate for type metadata. | `hibernate-core` (runtime) |

---

## 13. Spring framework (small slice)

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `spring-core-6.2.11.jar` | 6.2.11 | Spring core utilities — DI/IoC primitives, type conversion, expression language. | Required by `jasperreports-spring`. | `jasperreports-spring` |
| `spring-beans-6.2.11.jar` | 6.2.11 | Spring beans — bean factory, configuration metadata. | Required by `jasperreports-spring`. | `jasperreports-spring` |

> Only two Spring jars are present — Commander4j is **not** a full Spring app; these come along for JR's optional Spring integration.

---

## 14. Email

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `angus-mail-2.0.5.jar` | 2.0.5 | Eclipse Angus — Jakarta Mail implementation (the SMTP/IMAP/POP runtime). Successor to `com.sun.mail`. | Direct dep — Commander4j sends notification emails. | Direct |
| `jakarta.mail-api-2.1.5.jar` | 2.1.5 | Jakarta Mail API surface (`Session`, `MimeMessage`, etc.). | Required by Angus Mail. | `angus-mail` |

---

## 15. CSV

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `opencsv-5.12.0.jar` | 5.12.0 | OpenCSV — full-featured CSV reader/writer with bean mapping, header binding, etc. | Direct dep — Commander4j reads/writes CSV (data exchange, exports). | Direct |

---

## 16. Hardware / serial / native

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `jSerialComm-2.11.4.jar` | 2.11.4 | Cross-platform serial port library (RS-232 over USB-serial / native ports). Bundles native libs for Windows/macOS/Linux. | Direct dep — typical for industrial systems talking to scales, label printers, scanners over serial. | Direct |
| `jna-5.18.1.jar` | 5.18.1 | Java Native Access — call native libraries without writing JNI. | Direct dep — used for native OS integration. | Direct |
| `jna-platform-5.18.1.jar` | 5.18.1 | JNA platform-specific bindings (Win32 API, Linux syscalls, macOS Cocoa). | Direct dep. | Direct |

---

## 17. Build tooling (runtime-resident)

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `ant-1.10.15.jar` | 1.10.15 | Apache Ant — build/automation engine. **Has runtime APIs**, not just CLI: tasks can be executed programmatically. | Direct dep — `compilereports.sh` uses `JRAntCompileTask`. May also be invoked from Commander4j at runtime. | Direct |
| `ant-launcher-1.10.15.jar` | 1.10.15 | Ant bootstrap launcher. | Required for embedded Ant. | Direct |

---

## 18. Bytecode / proxy

| Jar | Version | Description | Why needed | Pulled by |
|---|---|---|---|---|
| `byte-buddy-1.18.7.jar` | 1.18.7 | Byte Buddy — runtime bytecode generation library. Used to create dynamic proxies, mocks, and class redefinitions. | Direct dep — could be used by Hibernate (lazy-loading proxies) or directly by Commander4j. | Direct |

---

## 19. The c4j_cups4j fork (out-of-band)

| Jar | Version | Description | Why needed | Source |
|---|---|---|---|---|
| `c4j_cups4j-0.8.1-jakarta.jar` | 0.8.1-jakarta | Local fork of cups4j 0.8.1 — Java client for the CUPS / IPP printing protocol. Migrated from `javax.xml.bind` to `jakarta.xml.bind`. | Direct printer communication via IPP — submit print jobs, query queues, cancel jobs. | **Out of band** — see `JAR_CLEANUP.md`. Built from `Commander4j/Source/b8/Scratchpad/c4j_cups4j/`. Not in any Maven repo. |

---

## Quick category counts

| Category | Jar count |
|---|---|
| JasperReports core + extensions | 22 |
| Batik (SVG) | 15 |
| Apache Commons family | 11 |
| Hibernate stack | 7 |
| jakarta JAXB stack | 7 |
| Office/POI stack | 7 |
| HTTP clients (4 + 5) | 5 |
| Logging | 5 |
| Jackson family | 4 |
| JR rendering helpers (PDF/JS/Java/etc.) | 8 |
| Charts/graphics (non-Batik) | 3 |
| XML parsing extras | 5 |
| JNA / serial | 3 |
| Spring | 2 |
| Mail | 2 |
| Build (Ant runtime) | 2 |
| Other singletons (groovy, byte-buddy, opencsv, jackcess, c4j_cups4j) | 6 |
| **Total** | **115** |

---

## Maintenance heuristics

- **Adding a new feature?** Check this table first — chances are the lib is already present (e.g. PDF, Excel, charts, Hibernate).
- **About to remove a jar?** Search `c4j_commander4j/src` for the package name first. Then check `mvn dependency:tree -Dverbose` to see whether it's a transitive of something you still need.
- **Version bump on a direct dep?** Re-run `getdependencies.sh`, diff `target/dependency` against `lib/`, and reconcile. Don't update jars in `lib/` by hand without re-running Maven — drift gets impossible to debug.
- **Suspect a classpath conflict?** Look for two jars covering the same package space (the historical `javax.xml.bind` vs `jakarta.xml.bind` situation is the classic example). The current state is single-stack on the JAXB side; keep it that way.
- **The only legitimate lib-vs-target diff is `c4j_cups4j-0.8.1-jakarta.jar`.** Everything else is a real change to investigate.
