# c4j_commander4j_web — design notes

Started 2026-09-11. Proof of concept. Supersedes the "merge into web_react" plan in
`Scratchpad/WebArchitectureReview.md` §9.7 (that section is kept for history).


> **2026-09-17 ~13:15 - NESTED RF MENU (schema 217): PHASE 2 (desktop RF Menu editor + core write path) ALSO BUILT.**
> Core: `JInternalFrameRFMenu` rewritten on the `JInternalFrameMenuStructure` pattern (Menu | Assigned | Unassigned,
> 818x543, root pre-selected, Save rewrites the selected menu only); `JDBRFMenu` gained create(menu,module,seq) /
> rewriteRFMenu(menu,list) / renameMenuTo / deleteForMenuId + setMenuId, and LOST the whole-table rewriteRFMenu(list)
> (sql key `JDBRFMenu.rewriteRFMenu` removed from the 3 drivers - the editor was its only caller); `JDBModule` gained
> getRFMenuIds (root + RF-active MENU modules) / getModulesAssignedtoRFMenuForMenu / getModulesUnAssignedtoRFMenuForMenu
> (RF-active FORM+MENU not under the menu, minus the menu itself) with sql keys on 3 drivers, and delete() now clears
> SYS_RF_MENU as module AND as parent (pre-existing orphan gap closed), renameTo() re-points child rows. `ant clean build`
> clean, jar repackaged (bin + release MANIFEST.MF), web lib jar + xml copies refreshed, war rebuilt. Every new MySQL key
> exercised with literals on a scratch 217 copy (menu ids = root + MENU_QM; unassigned root = 5, MENU_QM = 15; rewrite of
> MENU_QM left root's 14 rows; rename/delete cascade; two-column create lands under root). Swing itself NOT driven here -
> Dave's hand-off recipe below. The schema gate (`JDBSchema`) only warns when the DB is OLDER than the jar (Yes/No, can
> continue), so an installed 12.74 desktop connects to a 217 DB silently: **the "don't Save the RF Menu editor" warning
> stands until Dave runs THIS build of the desktop**, and the 217 jar against DEV at 216 asks rather than refuses.
> RF Active can be ticked on a MENU module in Module Properties (not gated by type), so further sub-menus need no SQL.
> **Hand-off for Dave:** apply 217 in DEV via Setup; run this desktop build; RF Menu Maintenance: pick root (Quality
> Management listed), pick MENU_QM (three children), move one, Save, confirm SYS_RF_MENU's other menu untouched; then the
> MC9400 on the new war (`target/`, not media/servlet); start Docker (`open -a Docker`) so I can run the SQL Server +
> Oracle 217 dry-runs + duplicate check - DONE 2026-09-17 ~14:00, both CLEAN (see the Nested menu section); hiding an empty sub-menu on the scanner: CONFIRMED by Dave 2026-09-17 (option 1, no permission changes; 7 DEV groups hold MENU_QM only for the desktop pull-down).
>
> **2026-09-17 - NESTED RF MENU (schema 217) phase 1. Plan: `~/.claude/plans/i-want-to-change-modular-pie.md`.**
> Dave's decisions 2026-09-17: replicate SYS_MENUS exactly (PK (MODULE_ID, MENU_ID), multi-parent allowed); retire
> FRM_QM_PANEL fully in 217; sub-menu heading = mod_MENU_QM "Quality Management"; web first, desktop editor second.
> DONE today: schema 000217 x 3 drivers (DTD-valid; MySQL run twice on a scratch copy of the SYS_ tables - the ALTERs
> auto-commit so the rolled-back-transaction method does not apply; SQL Server + Oracle NOT dry-run yet, Docker was
> down); core `JMenuRFMenu.getMenuOptions(menuID)` + sql key `JMenuRFMenu.getMenuOptions` (3 drivers) + JVersion
> schema 217, jar rebuilt from bin/ with the release MANIFEST.MF and copied into WEB-INF/lib; MenuServlet menu path
> (session `menuPath`), `PUT /api/menu/back`, empty sub-menus hidden + refused; QMMenuServlet + qmMenu.html deleted;
> war built. Smoke-tested end to end on a throwaway Tomcat 18080 against `c4j_smoke_217` (scratch MySQL DB = DEV's 23
> SYS_ tables + 217 applied; DEV itself untouched, still 216): curl walk + headless Chromium 320x405 walk both clean
> (see "Nested menu" section). **NEXT for Dave:** apply 217 in DEV via desktop Setup (the 217 jar's gate refuses a
> 216 DB), then test on the MC9400; **do NOT press Save in the desktop RF Menu editor** until phase 2 (per-menu
> editor + core write path, plan section D) is in - the old whole-table rewrite would flatten the tree. Then SQL
> Server + Oracle dry-runs of 217 once Docker is up (`open -a Docker`): the only untested SQL in 217 is the SQL Server
> DECLARE/EXEC batch and the Oracle anonymous block, and the duplicate-MODULE_ID check (a duplicate would leave SQL
> Server's table with NO primary key, the loader carries on) was run on the 11 MySQL copies only - repeat it on the
> SQL Server + Oracle site copies. Flag for Dave: 7 DEV groups (01_FILLING, 02_PACKING, ...) hold
> MENU_QM without the three children - the web hides the empty sub-menu from them, the desktop shows the pull-down.
>
> **CUT-OVER DECISION (a) 2026-09-16, Dave: ALL FOUR wars keep shipping for the foreseeable future** - the migration needs
> his own testing plus customer acceptance, so the release script keeps building web_react / web_WS / web_Issue alongside
> c4j_commander4j_web. Do not propose dropping the old build blocks until Dave says customers have migrated.
> **CUT-OVER DECISION (b) 2026-09-16, Dave: customer Tomcat delivery is MANUAL** - Dave installs wars and schema updates
> himself over VPN as part of each customer's support package, working with the customer. No installer entry, no delivery
> note, no self-service mechanism needed - non-item.
> **CUT-OVER DECISION (c) 2026-09-16, Dave: schemas 215/216 stay DEV-only for now** - Dave still has further items to add to
> the list before anything rolls out; applying the updates to the 11 customer DB copies is deliberately left as the FINAL
> pre-site-update test. Do not offer to apply them site by site until Dave says the list is complete.
>
> **PAUSED 2026-09-16 (evening) - RESUME WITH the cut-over questions:** (a) when web_react / web_WS / web_Issue stop
> shipping (release script still copies all 4 wars to media/servlet), (b) how customer Tomcats get the new war,
> (c) rolling schemas 215+216 to the other 11 sites (DEV only so far; SQL Server + Oracle dry-run clean), (d) Development
> copy of this project is the 14 Sep build - sync at publish time. Done today: licence sweep (one drift: util_modbusBridge
> modbusClient.jar ships 1.21 vs XML 1.23 - awaiting Dave: copy 16 Sep jar in, or edit XML), `#!/bin/bash` on all 41
> Scripts/*.sh. Still optional: exclude build/+target/ from source zips; delete-or-keep `c4j_commander4j copy.sh` +
> c4j_logopakemulator4j.sh; parked core items (item 7). "web_WS doc pages" question CLOSED 2026-09-16: the 8 API/*.html
> REST doc pages were on the step 8 drop list Dave agreed on 2026-09-12 (no public REST API in the new app), leftover only.**
>
> **2026-09-16: Dave's MC9400 scanner pass = OK (TO DO 1 closed). Non-EN texts for schemas 215/216 accepted as
> written, no native review (TO DO 5 closed). Release script was rerun 2026-09-14 08:09 (media/servlet war = target war);
> Tomcat webapps copy is the 13 Sep 16:42 build. Remaining: cut-over from web_react + 215/216 to the other sites,
> optional script tidies (6), parked core items (7).**
>
> **SAVED 2026-09-13 end of day. Dave: "good progress". Next: Dave works through the scanner test list
> (despatchHeader, the two confirm pages, wasteLog - now on his menu -, palletInfoDisplay, despatchPallet first).**
> **Earlier banner (~22:00): Dave happy with the whole day's UI pass; he is testing on the MC9400 (viewport
> 320x405, see below). All three web apps ported; release script proven; content-hash assets. Current build =
> `target/c4j_commander4j_web.war` (media/servlet + Tomcat webapps copies are still the 10:37 build - rerun
> `Scripts/c4j_commander4j.sh` or copy the war). Open: rest of Dave's scanner pass, translation review, cut-over. Font sizes settled
> 2026-09-13 late: lists + message on the body size, h1/h2 kept. lbl_Issue_Location as the return-from label: Dave OK'd 2026-09-13.**
>
> **Evening additions (after the 17:30 save):** MC9400 viewport measured 320x405 and put first in the harness; `data-footer`
> hook in c4j.layout() pins extra button rows into the fixed footer - now on despatchHeader (+ Assigned count moved into the
> h1), despatchSelect, qmPanels, qmPanelEdit, qmSamples, qmScoreSample, qmTrays; header free-text lines are kv rows on the
> qm pages too (qmScorePanel, qmScoreTray, qmScoreSample, qmSamples, qmTrays, `hdr_*` label ids); `.kv`/`.fields` label
> column shrinks to content (width:1%). Dave confirmed the despatch pages on the scanner.
>
> **UI pass 2026-09-13 (all in `src/main/webapp`, war in target/ rebuilt after each step, harness 43/43 each time; the
> media/servlet war and Tomcat's webapps copy are still the 10:37 build until the release script is rerun or Dave copies
> target/c4j_commander4j_web.war):**
> 1. Footer buttons FLIPPED: Back first/left, action last/right on 34 pages; 3-button rows swap only the ends.
> 2. Label/field alignment: every label+input row is `.divTable` markup; `.fields` (inputs) and `.kv` (read-only) share one
>    rule - right-aligned nowrap label column, 0.6rem gap, aliceblue `--c4j-row-bg` #f0f8ff cells, 1px grid (web_WS look).
>    Confirm/edit pages fold inputs into their kv table (`.divTableRow.gap`). palletInfoDisplay wrapped too.
> 3. BUG FIXED palletInfoDisplay: lang request missing a comma -> `btn_Backweb_SSCC` -> blank SSCC label.
> 4. despatchPallet Add/Remove radios -> `.form-group.radio-row` (centred pairs, 2rem apart).
> 5. User ID / Order / Recipe / Stage / SSCC / Issue Location header lines moved INTO the tables as kv rows on all 7
>    issue/return/history pages (USER/ORDER/RECIPE/STAGE/t_loc script vars gone; palletReturnConfirm's return-from row is
>    labelled lbl_Issue_Location - Dave to confirm the key).
> 6. Fonts: new `--c4j-text-size` drives labels, inputs, selects, buttons and `.kv .value` (values were 16px vs 19.2px on
>    desktop). NOT unified yet, Dave has the list: h1/h2, data-table rows, #message. `--c4j-text` is the COLOUR var.
> Details for each item are in the dated paragraphs further down (search "2026-09-13").
>
> **MC9400 VIEWPORT MEASURED 2026-09-13 evening (sysInfo photo): 320x405 CSS px, DPR 1.5, screen 320x534, Chrome 152,
> Android 10, URL bar + Android 3-button nav bar both shown.** The 360x560 harness size was a guess and far too generous;
> 320x405 is now first in `tools/shoot.js` sizes. Under 400px wide the CSS also drops html to 90% (1rem = 14.4px). At that
> size the harness reports body-visible vs content: despatchHeader overflows 24px (the Add SSCC / Print STN row is what
> Dave saw clipped), wasteLog 71px, palletIssueConfirm 141px, palletReturnConfirm 95px (menu + sysInfo scroll by design;
> every other measured page fits). Prototype (in-browser DOM mock, sources untouched, script proto.js in the session
> scratchpad): option 1 = Add SSCC/Print STN row moved into the fixed footer -> footer 84px, body 285 vs content 305 (only
> the Assigned row scrolls); option 1+2 = also drop the Assigned table row into the h1 -> body 285 = content 285, fits with
> 0px spare. Dave: "do both" -> APPLIED 2026-09-13 evening: c4j.layout() now also pins any element carrying
> `data-footer` into the fixed footer (between the message line and the last button row); despatchHeader's Add SSCC / Print
> STN row carries it, the Assigned table row is gone and the count sits in the h1 ("Despatch No WK73205 · Assigned 0",
> same lbl_Assigned / despatchPalletCount ids so the script is untouched). Harness at 320x405: body 285 = content 285,
> footer holds 2 groups, all sizes clean. War rebuilt. Dave to confirm on the scanner. Dave: "worked very well" on the scanner.
> despatchSelect's Previous/Next Page row got `data-footer` too (2026-09-13): footer = Prev | Next over Back | Amend |
> Create; the 8-row radio list scrolls in the body (285px at 320x405, list fits with the harness's 8 mock rows).
> qmScorePanel 2026-09-13: "Panelist [id - name]" line -> kv row Panelist | id - name (PANELIST var gone). The other qm
> pages still carry free-text lines: qmScoreTray (panelist + plant/panel), qmScoreSample (panelist + tray), qmSamples
> (panel + tray), qmTrays (plant/panel) - DONE 2026-09-13 on Dave's yes: kv tables with `hdr_*` label ids (the
> lbl_* ids are already taken by the list headers) - qmScoreTray Panelist/Panel/Plant, qmScoreSample Panelist/Tray/Plant,
> qmSamples Panel/Tray, qmTrays Panel/Plant; lbl_Plant added to three lang lists; PANEL/TRAY/PANELIST vars gone.
> qmPanels 2026-09-13: New/Edit/Delete + Scan Samples rows pinned with `data-footer` (footer = 3 rows, body 245px at
> 320x405, the panel list scrolls). Other pages with an unpinned extra button row: qmPanelEdit (Load Samples), qmSamples
> (Delete), qmScoreSample (Select All/Deselect All), qmTrays (New/Edit/Delete) - PINNED too on Dave's yes 2026-09-13
> (every page with two button rows now has the first one `data-footer`; qmScoreSample overflows 8px at 320x405 with the
> 3-row mock = the Decision row scrolls, buttons all visible).
> CSS 2026-09-13 late: `.kv`/`.fields` first cell now `width: 1%` + nowrap so the label column shrinks to the widest label on
> tables whose values are short too (qmSamples, palletInfoDisplay had a half-width label column). Input tables unchanged.
>
> **Done 2026-09-12:** step 8 panel grading (web_WS) built + API-tested; schemas 215 + 216 APPLIED in DEV by Dave (SCHEMA
> VERSION 216); JVersion 216 + jars patched; every Cancel/Exit is now Back; footer order FLIPPED 2026-09-13 = Back first (left), action last (right) on all
> 43 pages; missing-key -> English fallback; module descriptions RESOURCE_KEY -> HINT -> id; density pass, harness 360x560;
> MC9400 reachable once host + scanner share a Wi-Fi band. **Release script `Scripts/c4j_commander4j.sh` reworked** (per-web-
> project root/war variables, install4j bin + hosts demo/dev variables, doubled install4j target path fixed, `ant war`
> return-code checks on the 3 older web apps, guarded zip + guarded war copy for this project, recursive DS_Store exclusion,
> hosts restore now includes web_react). **Clean full build 22:23-22:29:** 4 wars in media/servlet incl.
> `c4j_commander4j_web.war`, 5 zips, hosts back to dev, installers 12.73. Lesson: never edit the script mid-run
> (bash reads by offset; the 22:16 edit broke the 22:13 run).
>
> **TO DO, in order:**
> 1. **DONE 2026-09-16 - Dave: scanner pass OK.** (was: device-test verdict on the MC9400 (deploy `c4j_commander4j_web/target/c4j_commander4j_web.war`, built
>    2026-09-13 with hashed asset names; the media/servlet copy is the 2026-09-12 build until the release script is
>    rerun) - density/footer
>    look, the 12 qm pages (checklist in "Step 8"), labels from the DB. Read the sysInfo blue viewport line; if its
>    height < 560, add that size to `tools/shoot.js`.
> 2. **Cache-buster: DONE 2026-09-13 as content-hash asset names**, Dave confirmed "works ok" 2026-09-13 (keep it). The `war`
>    target now packs from a staging copy `build/webapp` where a `fingerprint` macro renames js/c4j.js, js/qm.js and
>    style/commander.css to `name.<md5 first 8>.ext` and rewrites the 43+8+43 tags; sources, Eclipse and GitHub keep
>    the plain names. NoCacheFilter sends `public, max-age=31536000, immutable` for a path matching
>    `.<8 hex>.(js|css)` that exists in the war, everything else (html, api, 404s) keeps no-cache; c4j.js fetch now
>    passes `cache: 'no-store'`. Verified: `ant clean war` twice = identical names (c4j.dab77771.js); throwaway Tomcat
>    18080: menu.html no-cache + ETag + 304 on If-None-Match, hashed js/css immutable, plain js/c4j.js and a bogus hash
>    both 404 with no-cache, api/ping no-store; the war reached DEV (session start, host 4 select, login correctly
>    rejected a placeholder password - real login not exercised, login servlet untouched). Per-session token idea
>    DROPPED (nothing left for it to do). **REVERT = 3 files:** build.xml (war target back to `fileset dir="src/main/
>    webapp"`, drop stage+macro), NoCacheFilter (drop the if-branch), c4j.js (drop `cache: 'no-store'`). Release script
>    NOT rerun yet - it calls the same `ant war`, so the next release build ships hashed names automatically.
> 3. **Release wiring, last step:** CHECKED 2026-09-13: `c4j_commander4j.install4j` has NO war entries at all - the three
>    existing wars are distributed from media/servlet as loose files, not via the installer, so the new war is already
>    shipped the same way (script side DONE). Nothing to add unless Dave says the wars go somewhere else. web_react stays
>    live until the device verdict.
> 4. SQL Server + Oracle dry-run of schemas 215 + 216: DONE 2026-09-13, both engines, both files, 0 errors, inside rolled-
>    back transactions on the two DEV copies (both at 214). Row deltas exactly as designed (215: +72 lang, +3 RF rows;
>    216: +256 lang, +4 modules, +1 RF row, +4 perms). Oracle stores the UTF-8 accents intact (AL32UTF8, TEXT is
>    VARCHAR2(250 BYTE), longest new text 87 bytes). SQL Server's SQL_Latin1_General_CP1 varchar folds the 13 Polish/
>    Hungarian letters outside CP1252 (e->e, n->n, s->s) exactly as the 867 existing PL/HU rows already are - pre-
>    existing schema behaviour, not a 215/216 defect. Method: extract <statement>s, BEGIN TRAN ... ROLLBACK via
>    sqlcmd -f 65001 / sqlplus as sysdba with CURRENT_SCHEMA + NLS_LANG=AL32UTF8 (scripts were in the session scratchpad).
> 5. DONE 2026-09-16 - Dave accepted the Claude-written non-EN texts (215: 9 keys, 216: 32 keys) without native review.
> 6. Script tidies: 2026-09-16 the 11 projects that never import com.install4j had the i4jruntime variables/exports/rsync
>    removed from their Scripts/*.sh (see memory project_install4j_runtime_jar_audit); `--delete` on the xml rsyncs REJECTED
>    (would delete each web app's own xml/log/log4j.xml). Still optional: exclude build/ + target/ from the source zips (Dave 2026-09-13: the zips
>    are not what goes to GitHub, so the hashed staging copy inside them is harmless - demoted back to optional);
>    `#!/bin/bash` shebang DONE 2026-09-16 (all 41 Scripts/*.sh, in place, modes kept; claude_command_line.sh had a space-indented shebang the kernel ignored, fixed); the doubled `${c4j_src}/${c4j_prj}` went with the install4j variable strip - only c4j_autolab4j.sh (live rsync, correct) and the two stale scripts (`c4j_commander4j copy.sh`, c4j_logopakemulator4j.sh - Dave to delete or keep) still have it.
> 7. Parked: core transactional issue/return (D1 lock + D2 atomicity); `JDBUser.changePassword()` never checks the
>    current password (core vs web_react fix); 32px menu icons if soft on the device.
>
## Dave's direction (2026-09-11, his words paraphrased)

- New blank Eclipse Dynamic Web Project `c4j_commander4j_web` (Tomcat 11, Java 25, Servlet 6.1,
  `src/main/java` + `src/main/webapp`).
- Avoid changing existing `commander4j.jar` logic. Additive core changes are acceptable but
  must be discussed first — backward compatibility is the constraint.
- `c4j_web_react` is the live customer code. Its **functions and visible layout must be kept**.
- Instead of a plain servlet, the new project exposes **REST APIs called by JavaScript on the
  device**, and those APIs call the existing business logic in `commander4j.jar`.
- Order: (1) port `web_react` functionality, (2) merge `c4j_web_Issue`, (3) merge `c4j_web_WS`.
- The UI needs a thorough review later (HTML/CSS/JS is not Dave's strength). Target device is
  the **Zebra MC9400**, but the client must be responsive to other screen sizes.

## Design principles (proposed)

1. **Server is the source of truth.** All per-session state stays in the `HttpSession` /
   `Common.sd` exactly as today. The JS holds nothing that matters. A reload of any page asks
   the server what to show. This keeps the properties that make POST→redirect good on a
   handheld (survives reload, tolerates flaky wifi, no client state to lose).
2. **One screen = one page = one endpoint.** Each existing `Process` method becomes one
   endpoint. The JSON reply is `{ok, message, next, data}` where `next` is the page to go to,
   so the JS is the same thin dispatcher on every screen. Business decisions stay in Java.
3. **Same DOM, same `commander.css` in the first cut.** Static HTML with the JSP expressions
   replaced by ids that the JS fills. The old app is then a side-by-side regression test.
   The responsive / MC9400 pass is a separate, later step. Do not redesign while porting.
4. **Session-cookie auth, permission filter from day one.** Login reuses the JSP flow
   (`JDBUser.login`). A servlet filter maps URL prefix → module id and checks
   `JDBUser.isModuleAllowed()` on the server. A small GET returns the session's allowed
   modules so the JS can build the menu / disable buttons. Hiding buttons is not security.
5. **Skeleton from `c4j_web_Issue`**: `web.xml`, Ant `build.xml` wired into
   `Scripts/*.sh`, the 17 library jars, `AppServletContextListener` (log4j + SQL XML paths +
   `hosts.xml` seeding to `$CATALINA_HOME/c4j_config/<context>/`), `AppServletSessionListener`
   (release DB connections), `CanonicalUrlFilter`, Gson.

## Inventory — web_react screens → Process method → core calls → proposed endpoint

`Process.java` is 2525 lines, 24 JSPs, all dispatched by hidden `formName` + `button` fields.
Core classes imported: `JEANBarcode`, `JDBControl`, `JDBDespatch`, `JDBLanguage`, `JDBLocation`,
`JDBPallet`, `JDBReportRequest`, `JDBUser`, `JDBViewBarcodeValidate`, `JDBWaste{Container,
Location,Log,Material,Reasons,TransactionType}`, `JMenuRF{DespatchList,Menu,PrinterList}`,
`Common`, `JHost`, `JPlaySound`, `JPrint`, `JUtility`.

| # | JSP | Process method(s) | Core calls | Endpoint (proposed) |
|---|---|---|---|---|
| 1 | index.jsp | doPost Start/Quit → displayHostSelect | `hostList.getHTMLmenu` | GET /api/hosts |
| 2 | hosts.jsp | displayHosts | `hostList.getHost`, connect | PUT /api/session/host |
| 3 | login.jsp | logon, logonValidate | `JDBUser.login`, `JDBControl`, `userList.addUser`, `hostList.disconnectSessionAllHosts` | PUT /api/session/login, DELETE /api/session |
| 4 | changePassword.jsp | changeUserPassword | `JDBUser` | PUT /api/session/password |
| 5 | sessionTimeout.jsp | Restart → displayHostSelect | — | client only (→ #1) |
| 6 | menu.jsp | displayMenu, menu | `JMenuRFMenu.buildMenu` (HTML) | GET /api/menu, PUT /api/menu/select |
| 7 | productionConfirm.jsp | palletConfirm | `JEANBarcode`, `JDBPallet.getPalletProperties/confirm` | PUT /api/pallets/confirm |
| 8 | productionConfirmPlusSSCC.jsp | palletConfirmPlusSSCC | `JDBPallet`, `JDBViewBarcodeValidate.getProperties` | PUT /api/pallets/confirmPlus/sscc |
| 9 | productionConfirmPlusDU.jsp | palletConfirmPlusDU | `JEANBarcode`, `JDBPallet.confirm` | PUT /api/pallets/confirmPlus/du |
| 10 | productionConfirmPlusError.jsp | palletConfirmPlusError | session only | client only |
| 11 | palletDelete.jsp | palletDelete | `JDBPallet.delete` | PUT /api/pallets/delete |
| 12 | palletInfo.jsp | palletInformation | `JDBPallet.getPalletProperties` | GET /api/pallets/{sscc} |
| 13 | palletInfoDisplay.jsp | palletInformationDisplay | session only | client only |
| 14 | validateDUSelect.jsp | validateDUSelect | `JEANBarcode`, `JDBPallet.getProcessOrder`, `JDBViewBarcodeValidate` | PUT /api/validate/select |
| 15 | validateDUPallet.jsp | validateDUPallet | `JEANBarcode` | PUT /api/validate/pallet |
| 16 | validateDUTray.jsp | validateDUTray | `JEANBarcode` compare | PUT /api/validate/tray |
| 17 | validateDUResult.jsp | validateDUResult | session only | client only |
| 18 | despatchSelect.jsp | despatchSelect, despatchMenuDisplay | `JMenuRFDespatchList.buildDespatchList` (HTML), `JDBDespatch` | GET /api/despatch?page=n |
| 19 | despatchHeader.jsp | despatchHeader, despatchData{CreateNew,RetrieveFromDB,SavetoDB} | `JDBDespatch.create/update/getDespatchProperties/generateNewDespatchNo`, `JDBLocation.getHTMLPullDownCombo` | GET/PUT /api/despatch/{no}, GET /api/locations |
| 20 | despatchPallet.jsp | despatchPallet | `JDBDespatch.assignSSCC/unassignSSCC`, `JEANBarcode` | PUT /api/despatch/{no}/pallets |
| 21 | despatchConfirm.jsp | despatchConfirm, despatchDataConfirm, despatchPrint | `JDBDespatch.confirm`, `JDBReportRequest` | PUT /api/despatch/{no}/confirm |
| 22 | wasteLog.jsp | wasteLog, wasteComboRefesh, wasteLogSave/ClearLastUsed | `JDBWasteLog`, `JDBWasteMaterial`, 5 × `getHTMLPullDownCombo` | GET /api/waste/lists, PUT /api/waste |
| 23 | printerSelect.jsp | printerSelect (+ menu FRM_CM_PRINTERS) | `JMenuRFPrinterList.buildPrinterList_Plain` (HTML) | GET /api/printers, PUT /api/session/printer |
| 24 | sysInfo.jsp | sysInfo | session only | GET /api/sysinfo |
| all | `<jsp:useBean JLanguage>` per page | — | `JDBLanguage` | GET /api/lang?keys=a,b,c (or per page) |

Menu module ids used by `menu()`: `FRM_PAL_PROD_CONFIRM`, `FRM_PAL_PROD_CONFIRM+`,
`FRM_ADMIN_WASTE_LOG`, `FRM_BARCODE_VALIDATE`, `FRM_PAL_DELETE`, `FRM_ADMIN_DESPATCH`,
`FRM_PAL_INFO`, `SYS_INFO`, `FRM_CM_PRINTERS`, `FRM_USER_PASS_CHANGE`. These are the natural
keys for the permission filter (URL prefix → module id).

## Core changes needed — small, additive only

The port calls exactly what `Process.java` already calls, so almost nothing changes in core.
The only gap is helpers that return **HTML strings** instead of data. Checked 2026-09-11:

| Helper (HTML) | Data sibling already in core? | Action |
|---|---|---|
| `JHostList.getHTMLmenu` | **yes** `getHosts()` → `LinkedList<JHost>` | none |
| `JDBLocation.getHTMLPullDownCombo` | **yes** `getLocationList()` | none |
| `JDBWasteLocation/Container/Reasons/Material/TransactionType.getHTMLPullDownCombo*` | **yes** `getWaste…(enabled, mode)` → `LinkedList<JDBListData>` | none |
| `JMenuRFMenu.buildMenu` | **no** | add `getMenuOptions()` → `List<JMenuOption>`; `buildMenu` unchanged (or delegates) |
| `JMenuRFPrinterList.buildPrinterList*` | **yes** — it only wraps `JPrint.getPrinterNames()` | none (confirmed 2026-09-11) |
| `JMenuRFDespatchList.buildDespatchList` | **no** | `getDespatches(status, defaultItem, page, max)` ADDED 2026-09-11 (buildDespatchList untouched) |

So three additive methods in `com.commander4j.html`, nothing in `db`, no signature changes.
Fallback with zero core change: the web project runs the same SQL ids
(`JMenuRFMenu.buildMenu` etc.) itself, as `web_Issue` does with its own `db` classes.
Recommended: the three core methods — one source of truth, and the desktop is unaffected.

The deferred **step 1** work (transactional issue/return, D1 lost update + D2 atomicity)
is still core work and still needed before `web_Issue` is merged in — it is the one
non-trivial core change on the horizon.

## Decisions needed from Dave

1. **Locking model.** Every `Process` method is `synchronized` on the single servlet instance
   — one app-wide lock, so today the scanner app never runs two requests concurrently. A REST
   port with one servlet per screen drops that lock and exposes races it was masking
   (including the pallet D1 lost update). Options: (a) carry a single coarse lock into the
   first cut (behaviour-identical, safe, cheap), then relax per endpoint later; (b) go
   unlocked now and do the transactional core method first.
   **DECIDED 2026-09-11: per-session lock** (`WebSession.lock(sessionID)`, a ReentrantLock
   per HttpSession id, taken in `JsonServlet.service`). One device cannot double-submit
   (DataWedge can fire scan+CR while a request is in flight); different devices run in
   parallel, so a slow despatch confirm no longer stalls every other scanner. What the old
   app-wide lock was really protecting, and how each is handled now:
   - `Common.sd` (two unsynchronised parallel LinkedLists shared JVM-wide): the web app keeps
     ALL its state in HttpSession attributes via `WebSession`; only `silentExceptions` goes
     into `Common.sd`, and that write plus every `JHost.connect()` (its only core reader) run
     under `WebSession.SD_LOCK`.
   - Process instance fields shared by all sessions (`bcode`, `despatchListSize`): now
     per-session attributes / per-request objects.
   - `Common.user_password_expiry_days` / `user_max_password_attempts` statics: still set at
     logon exactly as Process does (core reads them); last-writer-wins across hosts, same as
     today.
   - Read-then-write DB races (pallet confirm, D1): unchanged from today; covered by the
     step 1 transactional core work before web_Issue merges.
2. **Scanner input on the MC9400.** ANSWERED 2026-09-11: DataWedge keystroke output with a CR
   after the data, so "Enter submits" is the mechanism and the first cut needs no scan-event
   hook. SSCC scans include the `00` application identifier; the core barcode classes parse
   it, so the JS passes the raw scan through unchanged.
3. **Per-terminal accounts + permission gate** as in review §9.7 — still applies unchanged.
4. **Which browser on the MC9400** — ANSWERED 2026-09-11: **Chrome**. Modern fetch / flex /
   grid are safe; no kiosk-mode assumptions.

## Steps

| step | work | status |
|---|---|---|
| 0 | Skeleton from web_Issue (web.xml, build.xml + Scripts wiring, libs, listeners, filter, hosts.xml seeding) | **DONE 2026-09-11**, smoke-tested |
| 1 | Session endpoints: hosts, login, logout, password, lang; menu; module permission check | **DONE 2026-09-11**, curl-tested against mySQL DEV (menu list, unported-screen message, refused module, exit) |
| 2 | Pallet screens #7–#13 (same DOM + commander.css) | **DONE 2026-09-11** — `PalletServlet` + 7 pages; non-writing paths curl-tested (parse errors, not found, already confirmed, DU mismatch → error page colours, info display); real confirm/delete untested |
| 3 | Validate DU #14–#17, printers #23, sysinfo #24 | **DONE 2026-09-11** — `ValidateServlet`, `PrinterServlet`, 6 pages; curl-tested (order by SSCC, pallet/tray match + mismatch, printer select). No core change: `JPrint.getPrinterNames()` already exists |
| 4 | Despatch #18–#21 (core `getDespatches` added), waste log #22 | **DONE 2026-09-11** — `DespatchServlet`, `WasteServlet`, 5 pages; curl-tested: list/paging, amend on another user's despatch ("Despatch is assigned to …"), header fields + 7 locations, waste combos (101 locations, 31 reasons), location chosen via AI-91 barcode narrows materials, bad quantity refused. NOT tested: create, header save, assign/unassign, confirm, print STN, waste log write |
| 4a | Desk review of the untested write paths (despatch create/save/assign/confirm/print, waste log write) vs Process.java | **DONE 2026-09-11** — see section below; 2 fixes (wasteLog.html transaction select; c4j.js carries the message across a page change) |
| 5 | Side-by-side regression against web_react on IJM/DEV | not started — checklist in the step 4a section |
| 6 | UI review + responsive/MC9400 pass | **BUILT 2026-09-11** — commander.css rewritten (same classes), c4j.layout() fixed header/scroll body/fixed footer on every page, one-tap menu with icons, sysInfo viewport line; screenshot-verified in headless Chromium; device pass pending |
| 7 | Merge c4j_web_Issue — with its EXISTING logic (Dave 2026-09-11: core transactional issue/return deferred to the back of the list) | **BUILT 2026-09-11** — IssueServlet / ReturnServlet / HistoryServlet, 7 pages, JQMBridgeClient; non-writing paths curl-tested on DEV; writes need a site with BOM + issue data |
| 8 | Merge c4j_web_WS (delete its fork first, review §9.7) | not started |

## Wishlist plan — web_Issue / web_WS permissions + look-and-feel (discussed 2026-09-11)

**Finding:** the port already has the permission model web_Issue lacks. The menu is
`getMenuOptions()` = SYS_RF_MENU rows filtered by `SYS_MODULES.rf_active` + group permissions,
and every servlet is refused unless the user holds its `moduleId()`. web_Issue's options become
more rows in the same scrollable RF menu; no new menu design.

**Module mapping (decided 2026-09-11):**

| web_Issue option | Module | State today |
|---|---|---|
| SSCC Issue | FRM_PAL_ISSUE | exists (schema 213), rf_active = N |
| SSCC Return | FRM_PAL_RETURN | exists (schema 213), rf_active = N |
| SSCC History | FRM_ADMIN_PALLET_HISTORY | exists since schema 1 (desktop Pallet History), rf_active = N |
| SSCC Info | FRM_PAL_INFO | exists, already on the RF menu as Pallet Info -> ONE screen, the ported palletInfo |

No module needs creating. (Dave first named FRM_ADMIN_PALLET_ISSUE; corrected to _HISTORY.)

**Proposed order:** step 5 regression -> schema 215 (config only, 3 drivers: rf_active=Y on the
three modules, SYS_RF_MENU rows, permissions; desktop already has the RF Active checkbox +
RF Menu editor for a DEV trial first; dry run in a rolled-back transaction as for 213) ->
step 6 restyle BEFORE porting web_Issue (one DOM vocabulary + one stylesheet, so web_Issue's
look is a CSS-only change; keep rem units + viewport meta; read the MC9400's real CSS viewport
from Chrome on the device first) -> core transactional issue/return (own discussion) ->
step 7 port web_Issue screens against core classes (modbusBridge pulses must carry over) ->
step 8 web_WS: login via the session endpoints + new module ids (FRM_ADMIN_QM_* exist for
the desktop; panel screens need their own ids).

**RF menu sequence (Dave's DEV SYS_RF_MENU, 2026-09-11):** PROD_CONFIRM 0, PROD_CONFIRM+ 1,
ADMIN_DESPATCH 2, BARCODE_VALIDATE 3, PAL_DELETE 4, PAL_ISSUE 5, PAL_RETURN 6, PAL_INFO 7,
CM_PRINTERS 8, USER_PASS_CHANGE 9, SYS_INFO 10. FRM_ADMIN_PALLET_HISTORY not placed yet.
Schema 215 must only INSERT the new rows; existing rows' sequence_id is customer-editable
config (RF Menu editor) and must not be overwritten.

**Decided 2026-09-11:** (4) web_WS users WILL have Commander4j accounts -> web_WS screens use the
same hosts/login/menu flow and module gate as everything else; panel modules need new RF-active
module ids + language keys in schema 215. FRM_ADMIN_PALLET_HISTORY is on Dave's DEV RF menu
(sequence number not yet told to me).

**Decided 2026-09-11 (5):** menu becomes one-tap tall buttons in a scrolling block between a fixed
header and a fixed footer holding Exit (flex column, 100dvh, list flex:1 overflow-y:auto). Tap =
the existing PUT /api/menu/select, so no server change; Select button dropped; buttons stay
focusable so arrow keys + Enter work on the MC9400 keypad. Device checks in step 6: usable
button count before scrolling, and scroll release must not fire a tap. Sub-screens keep their
Cancel/Exit as now. Menu buttons can carry the module's icon: SYS_MODULES.icon_filename is already
on JMenuOption; desktop images/{16x16,24x24,32x32}/*.gif (32 px max, bitmaps, soft on the MC9400's
high-density screen; SVG per icon if that shows). Button = flex row of <img> + <span>.

**Decided 2026-09-11 (final):** (3b) sequence is read at menu load and customers resequence in
the RF Menu editor, so schema 215 just appends the three new rows after the highest default
(SYS_INFO 9): FRM_PAL_ISSUE 10, FRM_PAL_RETURN 11, FRM_ADMIN_PALLET_HISTORY 12. (4b) web_WS is
NOT migrated for now - web_Issue first; web_WS doc pages question parked with it.

**Schema 215 WRITTEN 2026-09-11** (3 drivers, DTD valid, JVersion 215 + jar patched): 3 × RF_ACTIVE=Y,
3 guarded SYS_RF_MENU inserts at 100/101/102 (PK is MODULE_ID+SEQUENCE_ID, so unguarded inserts
would duplicate rows on sites that added them by hand, e.g. DEV 5/6/7). DEV dry-run in a rolled-back
transaction: run 1 = 3+3 rows, run 2 = 0, rollback clean. Not applied anywhere. Decided: no conflict,
the release ships the new web app and the schema together, so web_react never sees the new rows.

**Wishlist discussion CLOSED 2026-09-11.** Next build items in order: schema 215 (written) -> step 6 restyle
(one-tap menu, icons, web_Issue look) -> core transactional issue/return discussion -> step 7 web_Issue port. - do panellists
have accounts, per-terminal accounts?, and whether the 8 API doc pages + dead samples_old come
across; (5) restyle option: keep radio list + Select, or web_Issue's one-tap tall buttons
(one tap = select + submit in one request, function preserved).

## Step 7 — c4j_web_Issue port (2026-09-11)

**What moved, and how:** web_Issue's `JQMPalletDB` (issueToOrder_rest / returnFromOrder_rest with decisions
A/B/C), `JQMPalletHistoryDB`, `JQMReturnableDB`, `JQMProcessOrderDB`, `JQMViewBomDB`, `JQMResourcesDB`, the six
entities and `JQMBridgeClient` are copied AS-IS into `com.commander4j.web.{db,entity,util}` (provenance header on
each; only package names changed, plus: entities lost their decorative `@Entity`/`@Jsonb*` annotations because
those jars are not in this war and serialisation is Gson anyway; `JQMResourcesDB` lost an unused JQMUserEntity
field). They run core SQL ids and core `JDBPallet`/`JDBLocation`, so nothing touched core. `JQMProcessOrderDB.getProperties`
(SQL id `JDBQMProcessOrder.getProperties` exists nowhere) is not called.

**Servlets (one per module, gated by `moduleId()`):**
```
api/IssueServlet    FRM_PAL_ISSUE            GET  /api/issue/orders            resources + Ready orders (by selected resource) + selected
                                             PUT  /api/issue/resource {resource} | /order {processOrder} -> palletIssueSelect
                                             GET  /api/issue/bom               stages (first selected if none) + materials for the stage
                                             PUT  /api/issue/stage {stage}
                                             PUT  /api/issue/sscc {sscc}       validateMaterial (+ zero-qty check) -> palletIssueConfirm
                                             GET  /api/issue/pallet            pallet fields + issueQuantity default (= pallet qty)
                                             PUT  /api/issue/confirm {location, quantity}   validateLocation THEN issue, one request
                                             PUT  /api/issue/exit              clear flow state -> menu
api/ReturnServlet   FRM_PAL_RETURN           PUT  /api/return/sscc {sscc} -> processOrderReturnSelect;  GET /api/return/orders (returnable rows)
                                             PUT  /api/return/order {processOrder, location, quantity} -> palletReturnConfirm (qty re-read server-side)
                                             GET  /api/return/pallet;  PUT /api/return/confirm {quantity};  PUT /api/return/exit
api/HistoryServlet  FRM_ADMIN_PALLET_HISTORY GET  /api/history (username);  PUT /api/history {sscc} -> rows
api/PalletServlet   FRM_PAL_INFO             info/state now also return description + lotNumber (old material + SSCC digits 8-18)
pages: processOrderIssueSelect, palletIssueSelect, palletIssueConfirm, palletReturnSelect, processOrderReturnSelect, palletReturnConfirm, palletHistory
```
State that web_Issue kept in browser sessionStorage (resource, order, BOM id/version, stage, SSCC, return
location/quantity) is HttpSession (`IssueServlet.STATE_KEYS`), cleared on menu select / exit like web_Issue's
`resetValues()`. `JsonServlet.normaliseSSCC` accepts an 18-digit SSCC, a 20-char "00"+SSCC scan (web_Issue's
formatSSCC) or a full GS1 scan (web_react's parser). `JsonServlet.userId(ws)` = the logged-on user; web_Issue
wrote the browser-supplied username to the pallet/history rows - same value for an honest client, but no longer
client-controlled.

**Bridge:** `xml/bridge/bridge.xml` seed ships `enabled="false"` (web_Issue's seed said true + localhost); copied to
`$CATALINA_HOME/c4j_config/<ctx>/bridge.xml` on first start (edit that copy to enable). The release script's
xml rsync has no --delete, so `xml/bridge/` survives releases. All four pulse sites carry over: scanned location
on wrong-lane, INVALID_MATERIAL, INVALID_QUANTITY (both zero-qty checks + JQMPalletDB's qty guard), INVALID_STATUS
via `getBridgePulseId()`.

**Carried over unchanged (known web_Issue behaviour):** an unknown lane barcode falls back to the pallet's own
location and issues there (`JQMPalletDB.issueToOrder_rest`); core's `JDBPalletHistory.getReturnableBySSCC` SQL
has no `{schema}` prefix (review D6); D1/D2 (lock/atomicity) deferred.

**Dropped on purpose:** Refresh buttons (pages reload their state on load) and Logout (menu Exit is logout);
web_Issue's lime-on-black success colouring (port uses the one yellow message line). Footer = Back + action,
plus Menu on the two confirm pages (three deep).

**Small deliberate differences (not bugs):** the return order list pre-selects the first row when nothing is
selected (web_Issue left it unselected); when a confirm page cannot load its pallet it goes back to the SSCC
page with "Invalid SSCC []" rather than the validation message that caused it; the return list and history
table headings reuse `lbl_Issue_Order` ("Issue Order") for want of a plain "Order" key.

**Language keys used (existing):** mod_FRM_PAL_ISSUE/RETURN, mod_FRM_ADMIN_PALLET_HISTORY, lbl_Issue_Resource/Order/
Location/Quantity, lbl_Return_Quantity, btn_Issue, btn_Return, web_SSCC, lbl_Process_Order, lbl_Material, lbl_Description,
lbl_Pallet_Status, lbl_Batch, lbl_Batch_Status, lbl_Pallet_Quantity, lbl_Pallet_UOM, lbl_Location_ID, lbl_Transaction_*,
web_Exit. The "User [x]" line uses `lbl_User_ID` + " [" (Dave, 2026-09-11) in its own element under the sub-title
(first cut overwrote the h2 with it, so "Select Process Order" never showed - fixed).
**Language keys ADDED to schema 215 (2026-09-11, Dave's conventions: btn_ + label text for buttons,
`lbl_Material_Batch` for "Lot Number", `lbl_User_ID` + " [" for the user line; `btn_Menu` already existed):
`btn_Back`, `lbl_Stage`, `lbl_Recipe`, `lbl_Order` (plain "Order" heading on the return list + history),
`dlg_Select_Process_Order`, `dlg_Issue_SSCC`, `dlg_Issue_To_Order`, `dlg_Return_SSCC`, `dlg_Return_From_Order`
- 9 keys x 8 languages = 72 guarded inserts (NOT EXISTS on key+language, so a site's own text wins). Non-EN
text is Claude-written, not native-reviewed ([[feedback_translation_review_deferred]]). Dry-run on DEV in a
rolled-back transaction: 0 -> 72 -> 72 (second run inserts nothing), UTF-8 intact (Zurück). All pages keep
English as the fallback when a key is absent.

**Verified 2026-09-11:** harness (tools/shoot.js, mocks added) - 7 new pages shot at 4 sizes, all 31 pages load with
the fixed footer and no page errors, menu tap sends one PUT. curl against DEV (site 4, user ADM2GARRATT2, throwaway
Tomcat on 18080): menu shows the 3 new modules; orders 146 -> 7 filtered by POSL1; bad/blank/unknown order refused;
BOM stages empty (DEV has NO view_bom input rows); sscc: blank, bad, zero-quantity pallet, material-not-valid (18- and
20-char forms) all refused with web_Issue's messages; confirm with no SSCC refused; return: bad SSCC refused, returnable
list empty ("No orders with returnable quantity", DEV has NO issue history), qty 0 / abc / 5 refused with the right
messages; history 2 rows; pallet info shows description + lot. APP_PALLET / history counts unchanged after the run,
no ERROR in catalina.out. **Issue and return WRITES are untested** (Docker was down, so IJM was not available tonight).
Never executed: a successful issueToOrder_rest, a successful returnFromOrder_rest, the wrong-lane pulse with the
bridge enabled, and the "material valid but location invalid" branch of confirm.

**IJM checklist for Dave (run the same steps on web_Issue alongside):**
1. Pallet Issue: pick a Ready order, a stage, scan a pallet of a BOM input material -> confirm page.
2. Partial issue to a lane (e.g. LANEA, quantity < pallet): "n KG from <sscc> issued.", pallet quantity down, location unchanged.
3. Full issue (quantity = pallet): pallet quantity 0, location unchanged (decision A).
4. Wrong lane with c4j_config/c4j_commander4j_web/bridge.xml enabled + the bridge running: refused with
   "Material [..] is not valid for Location [..]" and the lane relay pulses.
5. Pallet Return: scan the pallet, pick the (order, lane) row, return part of it: "n KG returned to <sscc>",
   quantity back up; a return above the row's balance is refused ("exceeds quantity available to return").
6. History: ISSUE FROM/TO and RETURN FROM/TO pairs with the lane as RETURN/FROM.

## Step 6 restyle (2026-09-11) — what changed and what is still assumed

**Changed (no sub-page DOM touched, no core change):**
- `style/commander.css` rewritten under the same name and the same class vocabulary (container,
  form-group, divTable*, button-group, submit-btn/cancel-btn/regular-btn). web_Issue's look
  (sans-serif, flat buttons with drop shadow, light-blue focus) but rem + clamp() sizes, not px.
  Colour roles are `:root` custom properties (`--c4j-*`), one line each to flip. Message stays
  yellow (22 pages), focus light blue, Submit/Cancel keep green/red, menu buttons web_Issue yellow.
  The old MC92N0 (500px, 85%, green tint) / MC9300 (400px, 60%, blue tint) media queries are GONE;
  one 90% step at <=400px remains. The pre-restyle file was byte-identical to web_react `WebContent/style/commander.css`, so that is the backup.
- `js/c4j.js` `layout()` runs in `c4j.page()` before init: the page's form is split into
  `.c4j-body` (scrolls) and `.c4j-footer` (message line + the LAST `.button-group`), so Submit /
  Cancel / Exit and the message never scroll away. Handles both nestings (`.container > form` and
  `form > .container` on wasteLog/despatchHeader). Pure DOM move: ids, listeners and Enter-submit
  survive. Opt out with `data-nolayout` on the form.
- `menu.html` is the one-tap list: `.menu-btn` per RF option (icon + text), tap = the existing
  `PUT /api/menu/select`, Select button gone, Exit in the footer; ArrowUp/ArrowDown move focus
  (native buttons only move on Tab), Enter fires; the previously selected option gets focus on
  return. `MenuServlet` now returns `icon` (= SYS_MODULES.icon_filename); icons copied to
  `images/menu/` from the desktop 32x32 set (user.gif only exists at 16x16). Blank icon_filename
  falls back to the module TYPE's default exactly as desktop `JDBModule.getModuleIcon`
  (MENU menu.gif, FORM form.gif, SCANNER scanner.gif, REPORT report.gif, FUNCTION function.gif,
  EXEC execute.gif, USER userreports.gif; `moduleType` now in the /api/menu reply); a named file
  that is missing shows error.gif, as the desktop does. form/scanner/function/execute/error only
  exist at 16x16 in the desktop set, so they are upscaled on the page. Release script does NOT sync icons; they are checked in.
- `sysInfo.html` prints the line to read off the device:
  `Viewport WxH css px | DPR n | screen WxH | visual WxH`.
- hosts.html keeps its radio list + Select (Dave's decision covered the menu only). Dave's first
  look (2026-09-11): list window too short + rows too loose/font too small -> removed the JSP's
  inline `height:200px` scroll box (the list now fills the scrolling body), radio-list labels back
  to the common label size with tight line-height (also tightens the despatch list).

**Verified:** headless Chromium (Playwright, harness checked in as `tools/shoot.js` + README, API mocked,
static pages served from src/main/webapp) at 360x640, 400x700, 480x800, 1024x768: menu shows 11
of 12 buttons at 400x700 before scrolling, footer fixed on all 7 shot pages (menu, productionConfirm,
wasteLog, despatchHeader, despatchSelect, hosts, sysInfo), no page errors.

**Device checklist for Dave (MC9400, Chrome):**
1. Menu > System Information: read the blue Viewport line and tell me the numbers.
2. Menu: how many buttons show before scrolling; scroll by dragging and check the release does
   NOT fire a tap; keypad arrows move the highlight, Enter opens.
3. Production Confirm: tap the SSCC field - does the soft keyboard push the footer up (100dvh)
   or cover it (vh fallback)? Scan with DataWedge - Enter still submits.
4. Waste Log / Despatch Header (the two long pages): all fields reachable by scrolling, Submit /
   Exit always visible, message line readable.
5. Old-device note: if any MC92N0 / MC9300 is still in service, its tuned breakpoints are gone.

## Step 4a desk review — write paths vs Process.java (2026-09-11)

Compared `DespatchServlet` and `WasteServlet` against `Process.despatch*` / `Process.wasteLog*`
and the core helpers the port bypasses (`JDBLocation.getHTMLPullDownCombo`, the five
`JDBWaste*.getHTMLPullDownCombo*`, `JMenuRFDespatchList.buildDespatchList`). Also confirmed:
`JsonServlet.service` really does refuse on `moduleId()` (so the single-module servlets are
gated, not just declared); `defaultPrinter` is set at logon from `JPrint.getPreferredPrinterQueueName()`
in both apps and per-session in the port (no shared static, so two devices confirming at once
print to their own queue); `Common.userList.addUser` happens at login (JDBWasteLog.write reads
the user id from it); despatch list rows render "despNo - trailer" and the Amend radio value is
the bare despatch number in both.

**Mismatch fixed:** `wasteLog.html` added a blank first `<option>` to all five selects and
pre-selected the saved `wasteTransactionID`. Core's `JDBWasteTransactionType.getHTMLPullDownCombo`
has its blank option commented out and Process builds it with default `""`, so in web_react the
first transaction type is always selected and a fresh session can never submit a blank type. The
port would have sent `""` on the first save of a session and `JDBWasteLog.isValidData` refuses it.
Now identical to web_react (no blank, first type selected). The other four selects DO have a blank
option in core, unchanged.

**Second fix (same review): messages lost across a page change.** Six replies carry both a
message and `next` (despatch confirm refused -> header, "Password expired." -> changePassword,
"Not authorised" -> menu, "Unable to connect to database" -> hosts, "SSCC not found." ->
confirmPlus SSCC page, "Barcodes inconsistent" -> error page). `c4j.js` showed the message on the
old page and navigated at once, so the user never saw it; the JSPs showed `_ErrorMessage` after the
redirect. `c4j.goTo` now stashes the message in sessionStorage (guarded) and `c4j.page` shows it on
the next page before `init()` runs. GETs on load only overwrite `#message` when they carry text, so
it survives the page's lang/state calls. Not yet seen in a browser.

**Deliberate differences (port is safer; none change a successful outcome):**
- Waste quantity blank -> port passes 0 and core refuses before the insert with "Weight KG cannot be zero"; non-numeric ->
  "Invalid quantity [x]". web_react throws NumberFormatException (HTTP 500 page).
- Despatch header save: absent form fields are treated as blank; web_react NPEs on a missing parameter.
- Create: if the retrieve after a successful create fails, the port returns the error; web_react
  sends no response at all.
- despatchSelect Exit: port clears `despatchNo`; web_react clears only the HttpSession copy, so its
  Common.sd copy survives and pre-selects the last despatch next time the module is opened.
- Amend with nothing selected: port returns ok with no page change; web_react re-lists.

**web_react quirk carried over on purpose:** `wasteLogSaveLastUsed` saves `wasteTransactionID`
but `wasteComboRefesh` never applies it, so the transaction select snaps back to the first type
after every submit. One-line change in `wasteLog.html` (`fillSelect(..., d.wasteTransactionID, true)`)
if Dave wants it remembered.

**Step 5 checklist for the write paths (expected outcome identical on both apps unless noted):**
1. Despatch > Create: new number appears in the header, FROM/TO blank, pallet count 0; the list
   pre-selects it after Exit.
2. Header > Add Pallets with FROM blank -> "Select FROM Location"; with TO blank -> "Select TO
   Location"; both set -> despatchPallet page, and the header fields are saved even on refusal.
3. Pallets: scan SSCC in Add mode -> count +1, message blank; same SSCC again -> core's error
   (already assigned); Remove mode -> count -1; a non-SSCC scan -> "Invalid barcode." / "Invalid
   SSCC format."; Cancel -> header with the count refreshed.
4. Header > Confirm > No -> header. Yes with no pallets -> core's confirm error, back on header.
   Yes with pallets, Print STN unticked -> despatch list, number cleared. Yes + Print STN ticked
   -> RPT_DESPATCH_SERVICE report request row for the session's default printer.
5. Header > Print STN -> report request row, stays on header.
6. Amend on another user's despatch -> "Despatch is assigned to X" (already curl-tested).
7. Waste: fresh session, pick location/container/material/reason, quantity 1.5, Submit -> "Log N
   created.", quantity resets to .000, process order kept, transaction select back to the first type.
   Quantity 0 -> "Weight KG cannot be zero". Quantity "abc" -> port "Invalid quantity" (web_react 500).
8. Waste: scan an SSCC -> process order filled from the pallet; scan a 91-location barcode ->
   material list narrows to that location; Cancel -> menu and all last-used values cleared.

## What exists (2026-09-11)

```
c4j_commander4j_web/
  build.xml                         copy of web_Issue's, renamed; `ant clean build war` -> target/c4j_commander4j_web.war
  src/main/webapp/WEB-INF/web.xml   Servlet 6.0 descriptor (6.1 breaks Eclipse WTP "Loading descriptor"; the wizard file was 2.4 = annotations ignored); listeners, CanonicalUrlFilter, 60 min timeout
  src/main/webapp/WEB-INF/lib/      web_react's 24 jars (JPrint needs cups4j, reports need mail/jaxb) + gson-2.11.0; commander4j.jar from c4j_commander4j root
  src/main/webapp/xml/              rsync of c4j_commander4j/xml minus interface/samples; hosts.xml = ~/Commander4j/hosts/hosts_dev.xml
  src/main/webapp/{index,hosts,login,changePassword,sessionTimeout,menu}.html   same DOM + commander.css as the JSPs; menu is a placeholder
  src/main/webapp/js/c4j.js         the thin dispatcher: c4j.get/put/del -> {ok,message,next,data}; shows message in #message, follows next
  src/main/java/com/commander4j/web/
    listener/AppServletContextListener   = Process.init/destroy (paths, log4j, hosts seeding to $CATALINA_HOME/c4j_config/<ctx>/, JPrint.init)
    listener/AppServletSessionListener   = Process.sessionCreated/Destroyed (NO auto-connect; the user picks a host)
    filter/CanonicalUrlFilter            unchanged from web_Issue; canon-paths.properties lists the static pages
    filter/NoCacheFilter                 Cache-Control: no-cache on everything - Chrome otherwise reuses stale js/html (bit us 2026-09-11)
    session/WebSession                   the only place session state is touched; per-session lock; SD_LOCK bridge to Common.sd
    api/JsonServlet                      base: JSON body, per-session lock, session guards (requiresHost/requiresLogin), no-store headers
    api/ApiResponse                      {ok,message,next,data}
    api/PingServlet        GET /api/ping                       version/schema/server/host count  (no host, no login)
    api/HostsServlet       GET /api/hosts                      enabled hosts + selected           (no host, no login)
    api/SessionServlet     GET /api/session                    state for page load
                           PUT /api/session/start              index Start / timeout Restart  -> hosts
                           PUT /api/session/host  {selectedHost}                              -> login (or data.redirect for http sites)
                           PUT /api/session/login {username,password}                         -> menu | changePassword
                           PUT /api/session/password {password,newPassword1,newPassword2} | {cancel:true}
                           DELETE /api/session                 login Cancel / index Quit      -> hosts
    api/LangServlet        GET /api/lang?keys=a,b,c            {language, text:{key:text}}     (login required, see note)
    api/MenuServlet        GET /api/menu                       rows directly under the session's current menu (schema 217 tree) + selected, menu, title, isRoot
                           PUT /api/menu/select {selectedMenuOption}   MENU row: descend -> menu; otherwise Process.menu() preparation -> next page, or "not ported yet"
                           PUT /api/menu/back                  inside a sub-menu: up one level (menu left re-selected) -> menu; at root: same as /exit
                           PUT /api/menu/exit                  drop user -> login
                           (module permission: JDBUser.isModuleAllowed, also available to any endpoint via JsonServlet.moduleId() / refuseUnlessAllowed())
    api/PalletServlet      GET /api/pallets/state              all pallet-page session values (counts, order/material, GTIN compare, info fields)
                           PUT /api/pallets/confirm {sscc}                 palletConfirm      (FRM_PAL_PROD_CONFIRM)
                           PUT /api/pallets/confirmPlus/sscc {sscc}        -> productionConfirmPlusDU (FRM_PAL_PROD_CONFIRM+)
                           PUT /api/pallets/confirmPlus/du {trayDU}        -> productionConfirmPlusSSCC | productionConfirmPlusError
                           PUT /api/pallets/confirmPlus/reset | /cancel    error Exit / DU Cancel -> SSCC page; SSCC Cancel -> menu
                           PUT /api/pallets/delete {sscc}                  palletDelete       (FRM_PAL_DELETE)
                           PUT /api/pallets/info {sscc}                    -> palletInfoDisplay (FRM_PAL_INFO)
    pages: productionConfirm, productionConfirmPlusSSCC, productionConfirmPlusDU, productionConfirmPlusError, palletDelete, palletInfo, palletInfoDisplay
    api/ValidateServlet    PUT /api/validate/select {validateOrder,validateSSCC} -> validateDUPallet   (FRM_BARCODE_VALIDATE)
                           PUT /api/validate/pallet {palletDU} -> validateDUTray;  PUT /api/validate/tray {trayDU} -> validateDUResult
                           PUT /api/validate/cancel {to:menu|select}
    api/PrinterServlet     GET /api/printers (names + selected);  PUT /api/printers {selectedPrintQueue} -> menu   (FRM_CM_PRINTERS)
    pages: validateDUSelect, validateDUPallet, validateDUTray, validateDUResult, printerSelect, sysInfo (client-only)
    api/DespatchServlet    GET /api/despatch/list; PUT /api/despatch/page {dir}; PUT create; PUT amend {despatchNo}; PUT exit   (FRM_ADMIN_DESPATCH)
                           GET /api/despatch/header (fields + locations); PUT header {action: addPallets|confirm|print|exit, fields...}
                           PUT /api/despatch/pallets {sscc, addRemoveMode}; PUT pallets/cancel; PUT confirm {yes, printSTNonConfirm}
    api/WasteServlet       GET /api/waste (5 lists + values); PUT /api/waste {combos..., wasteProcessOrder, wasteQuantity, wasteBarcode}; PUT waste/cancel   (FRM_ADMIN_WASTE_LOG)
                           combo change is sent as "<AI><value>" through the barcode path, exactly as the JSP onchange did
    pages: despatchSelect, despatchHeader, despatchPallet, despatchConfirm, wasteLog
    session/WebSession.barcode()         one JEANBarcode per session (Process had ONE per app, built for the first host only); dropped on host change
```

Notes learned while building:
- `JUserList.getUser()` never returns null (blank JDBUser + ERROR log), so "logged on" is a
  session attribute set by the login endpoint, not a userList lookup.
- `JDBLanguage.get(key, lang)` only answers from the cache `preLoad("%")` fills at logon; an
  uncached lookup queries with `hostID + " " + key` and misses. So /api/lang needs a login,
  which matches web_react (no JSP before menu.jsp calls `Lang.getText`).
- The password is no longer stored in the session (Process kept it in Common.sd; nothing read it back).
- **Core finding:** `JDBUser.changePassword()` validates only the new password and never
  compares the current one, so web_react's changePassword "Current" field is decorative -
  any value changes the password. Found when a curl test with a wrong current password
  changed ADM2GARRATT2 in Commander4j_DEV (row restored from the WIS copy: password,
  encrypted flag, version 1, last_password_change 2025-02-12 19:04:55; the history v2 row
  deleted). The new endpoint loads the row (`isValidUserId` + `getUserProperties`, which
  clears any passwords already set, so load FIRST), then `setLoginPassword` +
  `isValidPassword()`, then the new passwords + `changePassword()`. Both paths curl-tested
  (wrong current -> "Invalid username or password", row untouched; correct current -> changed,
  then restored again). Core is untouched; Dave to decide whether core or web_react gets fixed.
- Per-session lock lifetime: `WebSession.releaseLock()` runs only from sessionDestroyed. A
  request handler must never remove the lock it is holding (a concurrent request on the same
  session would mint a fresh one and run in parallel).
- First Chrome run 2026-09-11 (Dave, user DAVE): index → hosts → login → menu worked, but the
  menu list was empty. Cause: c4j.js dropped any request while another was in flight (meant as
  the double-submit guard) and menu.html fires lang + menu GETs back to back. Fix: the guard
  now applies to writes only. Second cause: Chrome kept the old c4j.js (no Cache-Control from
  Tomcat's DefaultServlet) - NoCacheFilter added. Lesson: curl tests are sequential and never
  see request overlap; check the access log for the expected API calls after a page load.
- Core `JEANBarcode.parseBarcodeData` returns false with an EMPTY error message for input shorter
  than 5 characters (the AI accumulator never decides) - web_react shows a blank message too.
- Release script `Scripts/c4j_commander4j.sh` now rsyncs xml + hosts + commander4j.jar into the
  project and runs `ant clean build war` **non-fatally** (a PoC failure must not block a
  release). The war is NOT copied to the media folder or zipped yet.
- Smoke test method: throwaway CATALINA_BASE in the session scratchpad on port 18080, war
  copied to its webapps/, curl with a cookie jar. hosts.xml seeds into the tools Tomcat's
  `c4j_config/c4j_commander4j_web/` (same place Eclipse WTP will use).

## Nested menu - schema 217 (2026-09-17)

SYS_RF_MENU is now a tree like SYS_MENUS: new `MENU_ID` column (parent, default `root`), PK (MODULE_ID, MENU_ID),
so a module may sit under more than one menu. One level is shown at a time (MC9400 320x405). Core
`JMenuRFMenu.getMenuOptions(menuID)` (sql key `JMenuRFMenu.getMenuOptions`: `buildMenu` + `MENU_ID = ?`, bind menu
then user; failure logged at WARN, not debug) - the no-arg method delegates with `JMenuRFMenu.ROOT_MENU_ID`.
`buildMenu` (web_react) is untouched and still flat: web_react users now see MENU_QM + the three children listed and
cannot run them (accepted, same as the 215/216 rows).

- Session: `MenuServlet.K_MENU_PATH` = "menuPath", menu ids from the top "/"-joined, blank at root.
  `MenuServlet.resetMenu(ws)` clears path + selectedMenuOption: login, password change, exit.
- `list()`: `menu`, `title` (moduleDescription of the sub-menu; blank at root -> the page keeps mod_root), `isRoot`.
  MENU rows whose child list is empty for this user are left out; `select()` refuses them too ("Not authorised").
  The desktop shows a permitted-but-empty pull-down; the web hides it (Dave to confirm - 7 DEV groups hold MENU_QM
  without the three children).
- `select()`: MENU-type row (JDBModule.getType) -> push, selectedMenuOption blank (first child focused), -> menu.
  FRM_QM_PANEL_SCORES/SETUP/ADMIN are now in PAGE_FOR_MODULE (qmScoreUser / qmPanels / qmUsers) with the QMState
  clear + K_OPTION set that QMMenuServlet used to do.
- `back()`: pop, selectedMenuOption = the menu just left (re-focused, as the desktop tree), -> menu; at root = exit.
  menu.html's footer Back always calls `api/menu/back` (one handler, the server decides).
- qmPanels / qmScoreUser / qmUsers Back -> `c4j.goTo('menu')` (the session path still holds MENU_QM).
- Removed: QMMenuServlet.java, qmMenu.html, canon-paths /qmmenu.html, QMState.MODULE_PARENT / PAGE_MENU, harness stub.

Schema 217 (`c4j_commander4j/xml/schema/<driver>/000217.xml`, 15/16/16 statements): ALTER ADD MENU_ID (MySQL AFTER
MODULE_ID; SQL Server named default DF_SYS_RF_MENU_MENU_ID; Oracle `ADD (... DEFAULT 'root' NOT NULL)`), re-key
(MySQL DROP/ADD PRIMARY KEY; SQL Server drops the PK by whatever name `sys.key_constraints` reports then ADD
CONSTRAINT RF_MENU_PK; Oracle PL/SQL block drops any P/U constraint or index on the table then CREATE UNIQUE INDEX
RF_MENU_PK - 000000 keyed it with a unique index, a site may carry either), MENU_QM rf_active Y + guarded root row in
FRM_QM_PANEL's slot (COALESCE(MAX(seq of FRM_QM_PANEL), 103)), three guarded children seq 0/1/2, guarded ADMIN
permission, then FRM_QM_PANEL deleted from SYS_RF_MENU / SYS_GROUP_PERMISSIONS / SYS_MENUS / SYS_TOOLBAR /
SYS_LANGUAGE (mod_FRM_QM_PANEL) / SYS_MODULES. The loader (`JDBSchema.executeDDL`) carries on past a failed statement
and only records it. No duplicate MODULE_ID exists in any of the 11 MySQL site copies (checked 2026-09-17), so the
re-key cannot fail on data. JVersion.getSchemaVersion() = 217.

**Smoke test 2026-09-17** (throwaway Tomcat 18080 in the session scratchpad, CATALINA_HOME = the copy so its own
`c4j_config/c4j_commander4j_web/hosts.xml` has site 4 repointed at `c4j_smoke_217`, a scratch MySQL DB holding DEV's 23
SYS_ tables with 217 applied and SCHEMA VERSION 217; DEV untouched): ping schema 217; ADM2GARRATT2 (scratch password)
root = 14 rows incl. MENU_QM type MENU icon blank (page falls back to menu.gif); select MENU_QM -> 3 children, title
"Quality Management", isRoot false; select FRM_QM_PANEL_SETUP -> qmPanels; GET menu again still at MENU_QM level;
back -> root with MENU_QM selected; select FRM_QM_PANEL -> Not authorised; re-login resets the path; back at root ->
login. Scratch user SMOKEQM (01_FILLING only: holds MENU_QM, no children): root has 4 rows, no MENU_QM; select
MENU_QM -> Not authorised. Headless Chromium 320x405: Main Menu 14 buttons -> tap Quality Management -> title +
3 buttons, first focused -> Back -> Main Menu, Quality Management focused and scrolled into view -> Back -> login;
no page errors, no 4xx. Harness `tools/shoot.js`: 162 shots, no page errors. SQL Server / Oracle 217 dry-runs and the
MC9400 still to do.

**SQL Server + Oracle dry-runs 2026-09-17 ~14:00 (Docker up):** both DEV copies were at 214, so 215+216+217 ran in sequence
(359 statements) - SQL Server DEV inside one transaction with GO per statement, then ROLLBACK (2 columns / 9 rows restored);
Oracle on a scratch common user C##C4JDRY217 holding CTAS copies of the six tables plus the 000000-style unique index, dropped
afterwards. ZERO errors on either. Results identical to MySQL: key = (MODULE_ID, MENU_ID) (SQL Server constraint RF_MENU_PK
+ named default DF_SYS_RF_MENU_MENU_ID; Oracle unique index RF_MENU_PK, MENU_ID NOT NULL, two-column insert lands under root),
MENU_QM rf_active Y at slot 103 with the three children, FRM_QM_PANEL gone from all six tables, ADMIN perm present. The
DECLARE/EXEC batch and the PL/SQL block both work through their engines' plain statement path. Duplicate-MODULE_ID check:
0 on all 10 SQL Server and 5 Oracle site copies (plus the 11 MySQL ones). 217 can go wherever 215/216 go.

## Core method for the menu (DONE 2026-09-11)

`JMenuRFMenu.buildMenu(defaultItem)` runs SQL id `JMenuRFMenu.buildMenu` for the user and
emits radio-button HTML. Proposed additive sibling in `com.commander4j.html.JMenuRFMenu`:

```java
public LinkedList<JMenuOption> getMenuOptions()   // same SQL, same order, no HTML
```

`buildMenu` stays byte-for-byte (verified: diff is the 49 added lines only). Same shape later for
`JMenuRFPrinterList.getPrinters()` and `JMenuRFDespatchList.getDespatches(status, page, max)`.
Zero-core-change fallback: the web project runs SQL id `JMenuRFMenu.buildMenu` itself and
loads `JMenuOption` rows, exactly as `buildMenu` does.


## Step 8 — c4j_web_WS (panel grading) port (2026-09-12)

**Dave's brief (2026-09-12):** migrate the remaining web module; it now uses the standard login; its old menu.html
(Panel Scores / Setup Panel / Admin) becomes a sub-menu reached from the main RF menu, showing only the options the user
holds, with a Back button at the bottom in the house style; new module ids FRM_QM_PANEL_SCORES / FRM_QM_PANEL_SETUP /
FRM_QM_PANEL_ADMIN (Dave created these three in DEV himself during the session, hint "3 mod_..." and blank icons - the
blank icons were set from the schema values, the hints left alone).

**Module shape:** the sub-menu itself needs a fourth id, `FRM_QM_PANEL` (`QMState.MODULE_PARENT`, rf_active Y, on
SYS_RF_MENU - DEV seq 12, schema default 103). The three children are SYS_MODULES + group permission only and must NOT
go on SYS_RF_MENU (they would appear on the main menu as well). ADMIN (Claude's guarded insert) and PANEL (Dave's decision 2026-09-12, the existing group that already holds the desktop QM screens) hold all four in DEV; a scoring-only group for per-terminal accounts was offered and declined; schema 216 grants ADMIN only. Gotcha: a group needs the parent FRM_QM_PANEL as well as a child or the sub-menu never appears. web_WS's one-button
admin.html is dropped: FRM_QM_PANEL_ADMIN opens the panellist list directly.

**What moved, and how:** web_WS `JQMPanelDB`, `JQMTrayDB`, `JQMTraySampleDB`, `JQMTrayResultDB`, `JQMUserDB` + 5 entities
copied AS-IS into `com.commander4j.web.{db,entity}` (package change, `@Entity` dropped, `JDBControl`/`JDBQMSample` now the
core classes - same signatures; the QM SQL ids were already in the core sql xml). Decision list `ZWSIPANE` read through
core `JDBQMSelectList.getSelectList`. NOT carried: the fork's 15 util classes, `/Controls` + `/SelectLists` endpoints, the
8 `API/*.html` doc pages, `samples_old.html`, Refresh buttons, the users.html `prompt()` for a new id (the id is typed on
the edit page instead). Panel create still defaults plant "Pouch" / description "Daily Panel" inside the copied DAO.

**Servlets (gated by `moduleId()`), pages, state:**
```
(QMMenuServlet + FRM_QM_PANEL + qmMenu.html REMOVED 2026-09-17: the three modules are children of MENU_QM on the nested RF menu, MenuServlet opens their first page)
api/QMScoreServlet  FRM_QM_PANEL_SCORES  GET|PUT /api/qm/score/panelists {userID}  (enabled APP_QM_USERS; typed/scanned id or combo) -> qmScorePanel
                                         GET|PUT /panels {panelID} (status Ready) -> qmScoreTray;  GET|PUT /trays {trayID} -> qmScoreSample
                                         GET /samples (results for tray+panellist, decisions);  PUT /samples {result, sampleIDs[]} creates() = create-else-update
api/QMSetupServlet  FRM_QM_PANEL_SETUP   GET /api/qm/setup/panels (newest 24); PUT /panels/new|edit|trays|samples|delete {panelID}
                                         GET|PUT /panel {plant,description,status,action:save|trays}
                                         GET /trays; PUT /trays/new|edit|samples|delete {trayID};  GET|PUT /tray {description, action:save|samples}
                                         GET /samples (by panel + tray SEQUENCE); PUT /samples/scan {data} ("TRAYn" selects, number adds; tray created on first sample); PUT /samples/delete {sampleID}
api/QMAdminServlet  FRM_QM_PANEL_ADMIN   GET /api/qm/admin/users (enabled filter); PUT /users/filter|new|edit; GET|PUT /user {userID,firstname,surname,enabled}
pages: qmScoreUser, qmScorePanel, qmScoreTray, qmScoreSample, qmPanels, qmPanelEdit, qmTrays, qmTrayEdit, qmSamples, qmUsers, qmUserEdit
js/qm.js: radio/checkbox table builder, chosen()/checked()/setAll(), status colours (web_WS getStatusColour)
```
State (`QMState.K_*`: option, panel + plant, tray id + sequence, sample, panellist + name, result, user filter, user, new
flag) is HttpSession, cleared on every menu select as web_WS's menu.html cleared sessionStorage. The panellist is
deliberately NOT the logged-on account (scores keyed on APP_QM_USERS.USER_ID; terminal shared by the panel); it is
validated server-side and never taken from the write request. Selection is sent with each action and pre-selected on
reload (web_WS pre-selected the first row when nothing was stored - kept).

**Deliberate fixes vs web_WS:** sample delete resolves the tray from panel + tray sequence (web_WS deleted against a stale
selectedTray after a TRAY scan); a sample scan with no tray chosen is refused (web_WS would have created tray sequence 0);
tray delete sets the entity's queryType (the DAO builds its SQL id from it - found by the smoke test).

**Language keys:** reused existing `lbl_Panel_ID, lbl_Plant, lbl_Description, lbl_Panel_Status ("Panel Status", because
lbl_Status is "MHN Status"), lbl_Tray_ID, lbl_Sample_ID, lbl_Sequence_ID, lbl_Result, lbl_Decision, lbl_Created,
lbl_Enabled, lbl_Disabled, lbl_Filter_By, lbl_User_ID, lbl_surname (lower-case key), btn_New/Edit/Delete/Save/Add,
dlg_Sample_Delete`, plus schema 215's `btn_Back`. **Schema 216** (3 drivers, identical, DTD valid, 265 statements):
4 guarded SYS_MODULES rows, 4 ADMIN permissions, SYS_RF_MENU FRM_QM_PANEL 103, and 32 keys x 8 languages (4 mod_ keys +
28 UI keys: btn_Apply/Next/Select_All/Deselect_All/Load_Samples/Scan_Samples/Setup_Trays/Setup_Samples/Trays/Samples,
lbl_Panel/Panels/Tray/Trays/Samples/Panelist/Panelists/First_Name/Updated/Scan_Tray_Sample, dlg_Select_Panelist/Panel/Tray,
dlg_Panel_Create/Delete/Delete_Warning, dlg_Tray_Create/Delete). Generator: session scratch `gen216.py` (not kept).
DEV dry-run in a rolled-back transaction: lang 4 -> 256 -> 256, rollback 4; UTF-8 intact. JVersion bumped to 216 on 2026-09-12 (Dave agreed); core jar, bin and the web lib copy patched, war rebuilt.

**Verified 2026-09-12:** `ant clean build war` clean; harness: 12 new pages, no page errors, fixed footer on all, sub-menu
shows 3 buttons (127 shots). curl against DEV (throwaway Tomcat 18080, temporary ADMIN user QMWEBTEST created and deleted
afterwards): login -> main menu shows Panel Grading -> sub-menu 3 options; setup: new panel 16, save, bad status refused,
save+trays, new tray, save+samples, scans (blank / junk / unknown sample / 2 valid / duplicate refused / TRAY2 creates
tray 2 on first sample / sample already on tray 1 refused / TRAY1 back), sample delete + repeat refused; scores: unknown
and disabled panellist refused, Ready filter (panel 16 refused while Prepare), tray list, decisions (58 ZWSIPANE rows),
apply with no decision / bad decision / no samples refused, apply wrote 1 result, re-apply updated it; admin: filter
Y=101/N=5, new user blank id refused, create CLAUDETEST, duplicate refused, edit + save disabled; cleanup through the
app: tray delete (after the queryType fix), panel delete cascades. Row counts before = after
(panels 13, trays 59, samples 450, results 243, users 106). **Not run:** Chrome/device, the confirm() dialogs, arrow-key
focus on the sub-menu.

**Checklist for Dave:** main menu -> Panel Grading -> the three options (permission-filtered) -> Back; Panel Setup: New
(confirm dialog) -> edit -> Trays -> New -> Samples -> scan TRAY1 + sample ids -> Delete; Panel Scores: pick a panellist
(combo or scan the id), Ready panel, tray, tick samples, decision, Apply; Panel Admin: filter, Add (id typed on the edit
page), Edit. Then apply 215 + 216 in DEV (JVersion 216 needed for the desktop to run them).

## Button label consistency (2026-09-12, Dave's request)

Every Cancel / Exit button across the 43 pages now reads **Back** (existing ids and click handlers untouched), sourced from
the `btn_Back` language key (schema 215) with "Back" as the English fallback. 21 pages changed: hosts + login are pre-login
so static text only (LangServlet needs a logged-on user); sysInfo gained a one-key lang call; the 13 pages that fetched no
key for the button now fetch `btn_Back` and set it after their existing labels; changePassword / wasteLog / despatchHeader
/ despatchSelect / menu swapped `web_Cancel` / `web_Exit` for `btn_Back`. Yes / No on despatchConfirm and Restart on
sessionTimeout were left alone. Safety check: unlike web_react, where the JSP `button` value drove `Process.java`, no page
here sends a button's text or value - every click calls a fixed endpoint with a fixed JSON body - so the relabel is
cosmetic. Semantics unchanged: menu Back still logs the user off to the login page, login Back drops the host, hosts Back
returns to index. `web_Cancel` / `web_Exit` are no longer referenced by the web app. Harness re-run: no page errors.

## Module descriptions and missing language keys (2026-09-12, Dave's clarification)

**How core derives a module's description:** `JMenuOption.load` / `JDBModule.setResourceKey` take SYS_MODULES.RESOURCE_KEY
(a legacy misnomer: it holds the SYS_LANGUAGE key, e.g. `mod_FRM_QM_PANEL`) and look it up in SYS_LANGUAGE for the
user's LANGUAGE_ID. When no row exists `JDBLanguage.get` answers with `<hostID> " " <key>` (that is where the desktop's
"3 mod_FRM_QM_PANEL_SCORES" came from - host 3 in Dave's hosts.xml). There is NO hint fallback in core, and
`JDBModule.getHint()` returns the description, not the HINT column; nothing in core exposes that column.

**What the web app now does (all three fixed 2026-09-12, live-tested on DEV):**
- `JsonServlet.langText`: the `<host> <key>` miss pattern is mapped to blank, so `/api/lang` returns "" for a missing key
  and the page keeps its English text (before this fix a missing key showed as "4 btn_Back" on the button - confirmed
  live with `zzz_nokey` -> "4 zzz_nokey").
- `JsonServlet.moduleDescription`: RESOURCE_KEY -> user's language -> SYS_MODULES.HINT (read by `moduleHint` with core's
  own `JDBModule.getModuleProperties` statement) -> module id. Used by `QMMenuServlet` (was a hard-coded "mod_" + id)
  and by `MenuServlet` only when core's option text is the miss pattern. Verified: parent and child pointed at a
  non-existent key show their HINT; blank HINT shows the id.

**DEV state 2026-09-12 (checked live):** SCHEMA VERSION = 216, schema 215's and 216's keys all present (8 languages each),
so Dave applied both through the desktop after the earlier "not applied" check. Module hints set to Panel Scores /
Panel Setup / Panel Admin.

**Footer order (2026-09-12, then FLIPPED 2026-09-13 at Dave's request):** every page now ends with Back FIRST (left) and the action button LAST (right); on the three-button rows only the two ends swapped, the middle button (Menu / Trays / Samples / Amend / Edit) stays put. Mechanical edit: swap the first and last <button> lines of the last .button-group in 34 pages (single-button rows and despatchConfirm's Yes | No untouched - it has no Back). Handlers/ids unchanged; Enter still hits the submit button (Back is type=button). Harness: 43/43 pages clean, war rebuilt. Item closed.

**Label/field alignment (2026-09-13, Dave: "no column alignment" on qmUserEdit vs the old web_WS panelEdit table):** every
label + input/select row now sits in the app's existing `.divTable` markup (label cell | field cell), and the label column of
those tables is right-aligned, sized to the widest label, with a 0.6rem gap before the box - the `.kv` rule in commander.css
extended to a second class `.fields`. Flex `.form-group` label rows converted on 10 pages (qmUserEdit, qmPanelEdit,
palletIssueSelect, qmScoreUser, palletHistory, palletReturnSelect, processOrderIssueSelect, qmSamples, qmScoreSample,
validateDUTray); on palletIssueConfirm / palletReturnConfirm / qmTrayEdit / qmPanelEdit the input rows share the page's kv
table so both halves use one label column (`.divTableRow.gap` = 0.6rem top padding on the first input row after the read-only
rows); `fields` added to the 12 web_react input tables (login/changePassword labels were still left-aligned). ids, for=,
handlers untouched; qmUsers' Filter By radio row and despatchPallet's Add/Remove radio table left alone. Harness 43/43 clean,
qmUserEdit/qmPanelEdit/palletIssueConfirm/login eyeballed at 360x560, war rebuilt 16:30. Gotcha: label cells are nowrap, so a
long DB label (DE/PL) narrows the input - check on the device. **Colours added same day (Dave, from the web_WS panelEdit
screenshot):** `.kv` and `.fields` tables are border-collapse with every cell on `--c4j-row-bg` #f0f8ff (web_WS's aliceblue
td colour) and a 1px `--c4j-border` grid, cell padding 0.25rem 0.5rem; inputs keep their white box. Note the focus tint
`--c4j-focus` #e6f2ff is close to the row colour - the focused field still shows by its blue ring. Harness 43/43, war
rebuilt. palletInfoDisplay 2026-09-13: bare divTableBody wrapped in `.divTable kv` (same look); BUG FIXED - its lang
request was `mod_FRM_PAL_INFO,,btn_Back' + keys.join(',')` (missing comma), so the server was asked for `btn_Backweb_SSCC`
and the SSCC label came back blank (Back only survived on its English fallback); label now also carries the SSCC fallback. despatchPallet 2026-09-13: the Add/Remove radio pair was a 5-cell 100%-wide
divTable (labels and radios spread across the row) - now one `.form-group.radio-row` of two `<label><input radio> text</label>`
pairs (qmUsers Filter By pattern), centred, 2rem apart; ids/accesskeys/name kept, script untouched. Harness 43/43 footers OK. processOrderIssueSelect 2026-09-13 (Dave): the free-text "User ID [DAVE]" line is now the
first row of the Resource table (`.fields.kv`, label lbl_User_ID | value = username); the USER variable and the "[...]" wrapper
are gone. The six sibling pages (palletIssueSelect, palletReturnSelect, processOrderReturnSelect, palletHistory,
palletIssueConfirm, palletReturnConfirm) DONE the same way 2026-09-13 on Dave's yes: palletIssueSelect = User ID / Order /
Recipe rows above Stage; palletReturnSelect + palletHistory = User ID row above SSCC; processOrderReturnSelect = new kv table
User ID / SSCC (web_SSCC key added to its lang list, replaces the hard-coded 'SSCC ['); palletIssueConfirm = User ID / Order /
Recipe / Stage rows on top of its kv table (SSCC row gets `gap`); palletReturnConfirm = User ID / Order / Issue Location rows
(the return-from location was labelled lbl_Location_ID like the pallet's own location row - now lbl_Issue_Location, key added
to its lang list). All the USER/ORDER/RECIPE/STAGE/t_loc script variables and '[...]' wrappers are gone. Harness 43/43.

**Font sizes (2026-09-13, Dave):** page audit of palletIssueSelect found 6 size rules; the blue `.kv .value` spans had none and
inherited body 1rem (16px on a desktop vs 19.2px labels; identical on the MC9400 only because every clamp bottoms out at
14.4px there). New `--c4j-text-size: clamp(1rem, 4vw, 1.2rem)` now drives label, input/select, button AND `.kv .value`
(NB `--c4j-text` is the text COLOUR - a first attempt reused that name and turned labels black; the measurement script
caught it). Still separate: h1, h2, data-table rows clamp(0.9rem, 3.4vw, 1.15rem), #message clamp(0.95rem, 3.6vw, 1.25rem)
- Dave chose my recommendation 2026-09-13 late: data-table rows and #message now use `--c4j-text-size` too (19.2px
desktop / 14.4px scanner, same as labels); h1/h2 keep their own larger clamps. Overflow list at 320x405 unchanged apart
from +1px on the 3-sample qmScoreSample mock. Measuring tool: a 20-line Playwright script reading getComputedStyle at 650 and 360
px (session scratchpad, easy to recreate).

**Footer spacing (2026-09-12, Dave):** the gap above the footer buttons was ~3.5rem (footer padding + a reserved message line + two margins). Now: footer padding 0.25rem, no form-group margin, button row margin 0.25rem, and the yellow message line collapses while blank (it was reserved so the footer would not jump; the footer grows upward when a message appears, so the buttons never move anyway). CSS only, `commander.css` bottom; harness re-shot clean, message still renders above the buttons.

**Density pass (2026-09-12, MC9400 photos of despatchHeader needing a scroll):** tokens only in `commander.css` - touch height 3 -> 2.5rem, field padding 0.4 -> 0.2rem, row gap 0.4 -> 0.25rem, label/field/button font cap 1.4 -> 1.2rem, h1/h2 margins trimmed; tables and the 3.25rem menu buttons untouched. Harness gained a 360x560 size (estimated MC9400 portrait Chrome viewport: 640 minus URL bar and nav bar). At that size every page fits without scrolling except menu (12 options, meant to scroll) and sysInfo (by ~30px). despatchHeader: 478/478. Real viewport still to be read off the sysInfo blue line.
