const { chromium } = require('playwright');
const http = require('http'), fs = require('fs'), path = require('path');
const ROOT = '/Users/dave/Commander4j/Source/b8/Scratchpad/c4j_commander4j_web/src/main/webapp';
const OUT = process.argv[2];
const MIME = { '.html': 'text/html', '.css': 'text/css', '.js': 'application/javascript', '.gif': 'image/gif', '.png': 'image/png' };
const server = http.createServer((req, res) => {
  const p = path.join(ROOT, decodeURIComponent(req.url.split('?')[0]));
  if (fs.existsSync(p) && fs.statSync(p).isFile()) { res.writeHead(200, { 'Content-Type': MIME[path.extname(p)] || 'application/octet-stream' }); fs.createReadStream(p).pipe(res); }
  else { res.writeHead(404); res.end(); }
});
const ok = (data, message) => ({ ok: true, message: message || '', next: '', data: data || {} });
const menu = [
  ['FRM_PAL_PROD_CONFIRM','Production Confirm','pallet_confirm.gif'],['FRM_PAL_PROD_CONFIRM+','Production Confirm +',''],
  ['FRM_ADMIN_DESPATCH','Despatch','despatch.gif'],['FRM_BARCODE_VALIDATE','Validate Barcode',''],['FRM_PAL_DELETE','Pallet Delete','split.gif'],
  ['FRM_PAL_ISSUE','Pallet Issue','issue.png'],['FRM_PAL_RETURN','Pallet Return','return.png'],['FRM_ADMIN_PALLET_HISTORY','Pallet History','pallet_history.gif'],
  ['FRM_PAL_INFO','Pallet Information',''],['FRM_CM_PRINTERS','Printers','print.gif'],['FRM_USER_PASS_CHANGE','Change Password','user.gif'],['SYS_INFO','System Information','']
].map((m, i) => ({ moduleID: m[0], description: m[1], icon: m[2], moduleType: 'FORM', selected: i === 2 }));
const mocks = {
  'api/menu': ok({ options: menu, selected: 'FRM_ADMIN_DESPATCH', menu: 'root', title: '', isRoot: true }),
  'api/lang': ok({ language: 'EN', text: { mod_root: 'Commander4j', web_Exit: 'Exit', web_Logout: 'Logout', web_Yes: 'Yes', web_No: 'No' } }),
  'api/pallets/state': ok({ confirmCount: 3, sscc: '', processOrder: '000012345', material: 'MAT001' }),
  'api/hosts': ok({ hosts: ['mySQL DEV (Local)','mySQL TST (Local)','SQL Server WIS (Docker)','Oracle IJM (Docker)','mySQL AIN (Local)','mySQL ALT (Local)','SQL Server LIV (Docker)','mySQL VAL (Local)','mySQL POL (Local)','Oracle NOR (Docker)','mySQL FRE (Local)','mySQL BIR (Local)'].map((d, i) => ({ siteNumber: String(i + 1), description: d, selected: i === 0 })) }),
  'api/session': ok({ username: 'DAVE', siteDescription: 'mySQL DEV (Local)' }),
  'api/despatch/header': ok({ locations: ['', 'BULK', 'DESPATCH', 'FG', 'QA', 'RETURNS', 'WIP', 'YARD'], despatchNo: 'WK73206', despatchFromLocation: 'FG', despatchToLocation: 'DESPATCH', despatchTrailer: 'TR123', despatchHaulier: 'ACME', despatchLoadNo: 'L1', despatchJourneyRef: 'J9', despatchPalletCount: '4' }),
  'api/despatch/list': ok({ page: 1, maxPages: 3, despatches: Array.from({ length: 8 }, (_, i) => ({ despatchNo: 'WK7320' + i, trailer: 'TR' + i, selected: i === 0 })) }),
  'api/printers': ok({ printers: ['Zebra_ZT411_Line1', 'Zebra_ZT411_Line2', 'Office_Laser'], selected: 'Zebra_ZT411_Line1' }),
  'api/issue/orders': ok({ username: 'DAVE', resources: [{ resource: 'LINE1', description: 'Line 1' }, { resource: 'LINE2', description: 'Line 2' }], selectedResource: 'LINE1', selectedProcessOrder: '000012346',
    orders: [['000012345', 'MAT001', 'Widget 12 pack'], ['000012346', 'MAT002', 'Widget 24 pack, promotional'], ['000012347', 'MAT003', 'Gadget']].map((o, i) => ({ processOrder: o[0], material: o[1], description: o[2], selected: i === 1 })) }),
  'api/issue/bom': ok({ username: 'DAVE', processOrder: '000012346', bomId: 'BOM-24', bomVersion: '3', stages: ['MIX', 'PACK'], selectedStage: 'MIX', materials: [{ material: 'RAW001', description: 'Sugar 25kg', location: 'LANEA' }, { material: 'RAW002', description: 'Flour 25kg', location: 'LANEB' }] }),
  'api/issue/pallet': ok({ username: 'DAVE', processOrder: '000012346', bomId: 'BOM-24', bomVersion: '3', stage: 'MIX', sscc: '350103471130720670', palletProcessOrder: '000011111', material: 'RAW001', description: 'Sugar 25kg', palletStatus: 'Unrestricted', batchNumber: 'B2026091', batchStatus: 'Unrestricted', quantity: '100', uom: 'KG', lotNumber: 'OLD00147113072', location: 'WH-PACKDIS', issueQuantity: '100' }),
  'api/return/orders': ok({ username: 'DAVE', sscc: '350103471130720670', orders: [{ processOrder: '000012346', location: 'LANEA', quantity: '30', uom: 'KG', selected: true }, { processOrder: '000012345', location: 'LANEB', quantity: '12.5', uom: 'KG', selected: false }] }, '2 order(s) available for return.'),
  'api/return/pallet': ok({ username: 'DAVE', processOrder: '000012346', returnLocation: 'LANEA', returnQuantity: '30', sscc: '350103471130720670', palletProcessOrder: '000011111', material: 'RAW001', description: 'Sugar 25kg', palletStatus: 'Unrestricted', batchNumber: 'B2026091', batchStatus: 'Unrestricted', quantity: '70', uom: 'KG', bomId: 'BOM-24', bomVersion: '3', lotNumber: 'OLD00147113072', location: 'WH-PACKDIS' }),
  'api/history': ok({ username: 'DAVE', sscc: '350103471130720670', history: [['2026-09-10 14:02:11', 'ISSUE', 'FROM', '000011111', 'WH-PACKDIS', '30', 'KG'], ['2026-09-10 14:02:11', 'ISSUE', 'TO', '000012346', 'LANEA', '30', 'KG']].map(h => ({ date: h[0], type: h[1], subtype: h[2], processOrder: h[3], location: h[4], quantity: h[5], uom: h[6] })) }, '2 records displayed'),
  'api/qm/score/panelists': ok({ panelists: [{ userID: 'DAVE', name: 'DAVE - Dave Garratt', selected: true }, { userID: 'GUEST01', name: 'GUEST01 - Guest One', selected: false }], selectedPanelist: 'DAVE' }),
  'api/qm/score/panels': ok({ panelist: 'DAVE - Dave Garratt', panels: [{ panelID: '15', plant: 'Pouch', description: 'Daily Panel', selected: true }, { panelID: '14', plant: 'Pouch 4.1', description: 'Daily Panel', selected: false }], selectedPanel: '15' }),
  'api/qm/score/trays': ok({ panelist: 'DAVE - Dave Garratt', panelID: '15', plant: 'Pouch', trays: [{ trayID: '101', traySequence: '1', description: 'Tray 1', selected: true }, { trayID: '102', traySequence: '2', description: 'Tray 2', selected: false }], selectedTray: '101' }),
  'api/qm/score/samples': ok({ panelist: 'DAVE - Dave Garratt', panelID: '15', plant: 'Pouch', traySequence: '1', samples: [{ sampleID: '5001', sequenceID: '1', sequenceLetter: 'A', value: 'OK' }, { sampleID: '5002', sequenceID: '2', sequenceLetter: 'B', value: '' }, { sampleID: '5003', sequenceID: '3', sequenceLetter: 'C', value: '' }], decisions: [{ value: 'OK', description: 'Acceptable' }, { value: 'NOK', description: 'Not acceptable' }], selectedResult: 'OK' }),
  'api/qm/setup/panels': ok({ panels: [{ panelID: '15', plant: 'Pouch', description: 'Daily Panel', status: 'Ready', selected: true }, { panelID: '14', plant: 'Pouch 4.1', description: 'Daily Panel', status: 'Prepare', selected: false }, { panelID: '13', plant: 'Pouch 4', description: 'Daily Panel', status: 'Complete', selected: false }], selectedPanel: '15' }),
  'api/qm/setup/panel': ok({ panelID: '15', plant: 'Pouch', description: 'Daily Panel', status: 'Ready', created: '2026-09-12 10:00', updated: '2026-09-12 11:30' }),
  'api/qm/setup/trays': ok({ panelID: '15', plant: 'Pouch', trays: [{ trayID: '101', traySequence: '1', description: 'Tray 1', selected: true }, { trayID: '102', traySequence: '2', description: 'Tray 2', selected: false }], selectedTray: '101' }),
  'api/qm/setup/tray': ok({ panelID: '15', plant: 'Pouch', trayID: '101', traySequence: '1', description: 'Tray 1', created: '2026-09-12 10:00', updated: '' }),
  'api/qm/setup/samples': ok({ panelID: '15', plant: 'Pouch', traySequence: '1', samples: [{ sampleID: '5001', sequenceID: '1', sequenceLetter: 'A', selected: true }, { sampleID: '5002', sequenceID: '2', sequenceLetter: 'B', selected: false }], selectedSample: '5001' }),
  'api/qm/admin/users': ok({ filter: 'Y', users: [{ userID: 'DAVE', firstname: 'Dave', surname: 'Garratt', enabled: 'Y', selected: true }, { userID: 'GUEST01', firstname: 'Guest', surname: 'One', enabled: 'Y', selected: false }], selectedUser: 'DAVE' }),
  'api/qm/admin/user': ok({ isNew: false, userID: 'DAVE', firstname: 'Dave', surname: 'Garratt', enabled: 'Y' }),
  'api/waste': ok({ transactions: ['WASTE', 'RETURN'], locations: ['', 'LINE1', 'LINE2', 'LINE3'], containers: ['', 'BIN', 'SKIP'], materials: ['', 'MAT001', 'MAT002'], reasons: ['', 'DAMAGED', 'EXPIRED'], wasteTransactionID: 'WASTE', wasteLocationID: 'LINE1', wasteContainerID: 'BIN', wasteMaterialID: 'MAT001', wasteReasonID: '', wasteProcessOrder: '000012345', wasteQuantity: '.000', wasteMaterialUOM: 'KG' }, 'Log 1234 created.')
};
(async () => {
  await new Promise(r => server.listen(18099, r));
  const browser = await chromium.launch();
  const pages = ['menu', 'logoutConfirm', 'productionConfirm', 'wasteLog', 'despatchHeader', 'despatchSelect', 'hosts', 'sysInfo', 'processOrderIssueSelect', 'palletIssueSelect', 'palletIssueConfirm', 'processOrderReturnSelect', 'palletReturnConfirm', 'palletHistory', 'qmScoreUser', 'qmScoreSample', 'qmPanels', 'qmPanelEdit', 'qmSamples', 'qmUsers', 'qmUserEdit'];
  const sizes = [[320, 405], [360, 560], [360, 640], [400, 700], [480, 800], [1024, 768]];   // 320x405 = MC9400 Chrome viewport as measured by sysInfo 2026-09-13 (DPR 1.5, screen 320x534, URL bar + Android nav bar shown); 360x560 was the old guess
  for (const [w, h] of sizes) {
    const ctx = await browser.newContext({ viewport: { width: w, height: h }, deviceScaleFactor: 2, hasTouch: w < 600, isMobile: w < 600 });
    await ctx.route('**/api/**', route => {
      const u = route.request().url().replace(/^.*?\/api\//, 'api/').split('?')[0];
      if (u === 'api/lang') {
        const q = new URL(route.request().url()).searchParams.get('keys') || '';
        const text = { mod_root: 'Commander4j' };
        q.split(',').filter(Boolean).forEach(k => { if (!text[k]) { text[k] = k.replace(/^(web|lbl|mod|dlg|btn)_/, '').replace(/^FRM_ADMIN_|^FRM_/, '').replace(/_/g, ' '); } });
        return route.fulfill({ contentType: 'application/json', body: JSON.stringify(ok({ language: 'EN', text })) });
      }
      const key = Object.keys(mocks).find(k => u === k);
      route.fulfill({ contentType: 'application/json', body: JSON.stringify(key ? mocks[key] : ok({})) });
    });
    for (const pg of pages) {
      const page = await ctx.newPage();
      const errs = [];
      page.on('pageerror', e => errs.push(e.message));
      await page.goto('http://localhost:18099/' + pg + '.html');
      await page.waitForTimeout(400);
      if (pg === 'palletHistory') { await page.fill('#sscc', '350103471130720670'); await page.keyboard.press('Enter'); await page.waitForTimeout(300); }
      await page.screenshot({ path: `${OUT}/${pg}-${w}x${h}.png` });
      const info = await page.evaluate(() => { const b = document.querySelector('.c4j-body'), f = document.querySelector('.c4j-footer'); return { body: b ? [b.clientHeight, b.scrollHeight] : null, footer: !!f, buttons: document.querySelectorAll('.menu-btn').length }; });
      console.log(pg, w + 'x' + h, JSON.stringify(info), errs.length ? 'ERRORS: ' + errs.join(' | ') : '');
      await page.close();
    }
    await ctx.close();
  }
  // ---- all 24 pages at one size: page errors, footer present, button rows in the footer ----
  const ctx = await browser.newContext({ viewport: { width: 400, height: 700 }, deviceScaleFactor: 2, hasTouch: true, isMobile: true });
  const puts = [];
  await ctx.route('**/api/**', route => {
    const req = route.request();
    if (req.method() !== 'GET') { puts.push(req.method() + ' ' + req.url().replace(/^.*?\/api\//, 'api/') + ' ' + (req.postData() || '')); }
    const u = req.url().replace(/^.*?\/api\//, 'api/').split('?')[0];
    if (u === 'api/lang') {
      const q = new URL(req.url()).searchParams.get('keys') || '';
      const text = { mod_root: 'Commander4j' };
      q.split(',').filter(Boolean).forEach(k => { if (!text[k]) { text[k] = k.replace(/^(web|lbl|mod|dlg|btn)_/, '').replace(/^FRM_ADMIN_|^FRM_/, '').replace(/_/g, ' '); } });
      return route.fulfill({ contentType: 'application/json', body: JSON.stringify(ok({ language: 'EN', text })) });
    }
    const key = Object.keys(mocks).find(k => u === k);
    route.fulfill({ contentType: 'application/json', body: JSON.stringify(key ? mocks[key] : ok({})) });
  });
  const all = fs.readdirSync(ROOT).filter(f => f.endsWith('.html')).map(f => f.replace('.html', '')).sort();
  for (const pg of all) {
    const page = await ctx.newPage();
    const errs = [];
    page.on('pageerror', e => errs.push(e.message));
    await page.goto('http://localhost:18099/' + pg + '.html');
    await page.waitForTimeout(300);
    await page.screenshot({ path: `${OUT}/all-${pg}.png` });
    const info = await page.evaluate(() => { const f = document.querySelector('.c4j-footer'); return { footer: !!f, groupsInFooter: f ? f.querySelectorAll('.button-group').length : 0, msgInFooter: !!(f && f.querySelector('#message')) }; });
    console.log('ALL', pg.padEnd(28), JSON.stringify(info), errs.length ? 'ERRORS: ' + errs.join(' | ') : '');
    await page.close();
  }
  // ---- menu tap: one PUT per tap, second immediate tap dropped by the write guard ----
  const page = await ctx.newPage();
  await page.goto('http://localhost:18099/menu.html');
  await page.waitForTimeout(300);
  puts.length = 0;
  await page.evaluate(() => { const b = document.querySelectorAll('.menu-btn'); b[5].click(); b[6].click(); });
  await page.waitForTimeout(300);
  console.log('TAP requests:', JSON.stringify(puts));
  await page.close(); await ctx.close();
  await browser.close(); server.close();
})();
