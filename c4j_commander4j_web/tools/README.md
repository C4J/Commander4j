# tools/shoot.js - headless screenshot + smoke harness for the pages

Serves `src/main/webapp` statically, mocks every `/api/**` call with canned JSON (no Tomcat,
no DB), shoots 13 pages at 4 viewport sizes, then loads ALL pages at 400x700 and reports page
errors / footer layout, and exercises one menu tap. Written 2026-09-11 for the step 6 restyle;
reuse it when the MC9400's real viewport is known (add the size to `sizes`).

Setup (once, anywhere outside the project, e.g. a scratch folder):

    npm init -y && npm i playwright && npx playwright install chromium

Run:

    node /path/to/c4j_commander4j_web/tools/shoot.js /path/to/output-folder

then look at the PNGs. ROOT inside the script is the absolute path of src/main/webapp.
