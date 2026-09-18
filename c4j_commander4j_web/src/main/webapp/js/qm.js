/*
 * qm.js - helpers shared by the panel-grading pages (c4j_web_WS port, step 8, 2026-09-12).
 * Loaded after c4j.js. Nothing here talks to the server; pages call c4j.get/put themselves.
 *
 *   qm.rows(tbodyId, rows, opts)   fill a .data-table body: one radio (or checkbox) per row + the given cells,
 *                                  the row's tr toggles the input, .selected follows the radio. Returns the tbody.
 *       opts.id        property that becomes the input's value (e.g. 'panelID')
 *       opts.cells     [function(row) -> text | {text, cls}] one per column AFTER the input column
 *       opts.first     text placed next to the input (default row[opts.id])
 *       opts.multi     true = checkboxes (score samples), else radios named 'row'
 *       opts.colour    function(row) -> css colour for the first cell text (panel status)
 *   qm.chosen()                    value of the checked radio, or '' (radios named 'row')
 *   qm.checked()                   values of all checked boxes (name 'rows')
 *   qm.setAll(on)                  check / uncheck every box
 *   qm.confirm(txt)                window.confirm wrapper (one place to swap for a nicer dialog later)
 *   qm.status(s)                   web_WS getStatusColour: Ready green, Prepare blue, Complete tomato
 */
var qm = (function () {
	'use strict';

	function status(s) {
		if (s === 'Ready') { return 'DarkGreen'; }
		if (s === 'Prepare') { return 'Blue'; }
		if (s === 'Complete') { return 'Tomato'; }
		return '';
	}

	function rows(tbodyId, list, opts) {
		var tb = document.getElementById(tbodyId);
		tb.innerHTML = '';
		var focusMe = null;
		(list || []).forEach(function (r, i) {
			var tr = document.createElement('tr');
			var td = document.createElement('td');
			var input = document.createElement('input');
			input.type = opts.multi ? 'checkbox' : 'radio';
			input.name = opts.multi ? 'rows' : 'row';
			input.value = String(r[opts.id]);
			input.checked = !!r.selected;
			td.appendChild(input);
			td.appendChild(document.createTextNode(' ' + (opts.first ? opts.first(r) : String(r[opts.id]))));
			if (opts.colour) { var c = opts.colour(r); if (c) { td.style.color = c; } }
			tr.appendChild(td);
			(opts.cells || []).forEach(function (fn) {
				var v = fn(r);
				var cell = document.createElement('td');
				if (v && typeof v === 'object') { cell.textContent = v.text; if (v.cls) { cell.className = v.cls; } if (v.colour) { cell.style.color = v.colour; } }
				else { cell.textContent = (v === undefined || v === null) ? '' : String(v); }
				tr.appendChild(cell);
			});
			if (input.checked && !opts.multi) { tr.className = 'selected'; focusMe = input; }
			tr.addEventListener('click', function (ev) {
				if (opts.multi) {
					if (ev.target !== input) { input.checked = !input.checked; }
					tr.className = input.checked ? 'selected' : '';
					return;
				}
				input.checked = true;
				Array.prototype.forEach.call(tb.querySelectorAll('tr'), function (x) { x.className = ''; });
				tr.className = 'selected';
			});
			if (opts.multi) {
				input.addEventListener('change', function () { tr.className = input.checked ? 'selected' : ''; });
				if (input.checked) { tr.className = 'selected'; }
			}
			tb.appendChild(tr);
		});
		if (focusMe) { focusMe.focus(); }
		return tb;
	}

	function chosen() {
		var e = document.querySelector('input[name="row"]:checked');
		return e ? e.value : '';
	}

	function checked() {
		return Array.prototype.map.call(document.querySelectorAll('input[name="rows"]:checked'), function (e) { return e.value; });
	}

	function setAll(on) {
		Array.prototype.forEach.call(document.querySelectorAll('input[name="rows"]'), function (e) {
			e.checked = on;
			var tr = e.closest('tr');
			if (tr) { tr.className = on ? 'selected' : ''; }
		});
	}

	function confirm(txt) {
		return window.confirm(txt);
	}

	return { rows: rows, chosen: chosen, checked: checked, setAll: setAll, confirm: confirm, status: status };
}());
