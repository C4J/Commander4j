/*
 * c4j.js - the thin dispatcher shared by every page.
 *
 * Every page:  c4j.page(init)        run init once the DOM is ready
 *              c4j.get(url) / c4j.put(url, body) / c4j.post / c4j.del
 *                                    fetch JSON, resolve with {ok,message,next,data};
 *                                    shows message in #message, follows next
 *              c4j.onSubmit(formId, fn)  Enter (DataWedge CR suffix) or button submits
 *              c4j.focus(id)         same job as focusIt() in the JSPs
 *
 * Server holds all state; a page reload just asks the server again.
 */
var c4j = (function () {
	'use strict';

	var inflight = false;

	function el(id) { return document.getElementById(id); }

	function text(id, value) {
		var e = el(id);
		if (e) { e.textContent = (value === undefined || value === null) ? '' : String(value); }
	}

	function value(id) {
		var e = el(id);
		return e ? e.value : '';
	}

	function setValue(id, v) {
		var e = el(id);
		if (e) { e.value = (v === undefined || v === null) ? '' : v; }
	}

	function message(msg) { text('message', msg || ''); }

	function focus(id) {
		var e = el(id);
		if (e) { e.focus(); if (e.select) { e.select(); } }
	}

	// A reply that carries BOTH a message and a next page (confirm refused -> header,
	// "Password expired." -> changePassword, "Not authorised" -> menu ...) must still show
	// the message after the page change, as the JSPs showed _ErrorMessage after a redirect.
	// The message is stashed for the next page; sessionStorage may be unavailable, so guarded.
	var STASH = 'c4j.message';

	function stash(msg) {
		try { if (msg) { window.sessionStorage.setItem(STASH, msg); } } catch (e) { /* ignore */ }
	}

	function unstash() {
		var msg = '';
		try { msg = window.sessionStorage.getItem(STASH) || ''; window.sessionStorage.removeItem(STASH); } catch (e) { /* ignore */ }
		return msg;
	}

	function goTo(next, msg) {
		if (!next) { return; }
		stash(msg);
		var page = next.indexOf('.') >= 0 ? next : next + '.html';
		window.location.assign(page);
	}

	// Double-submit guard: one WRITE (PUT/POST/DELETE) at a time per page, so a
	// second scan+Enter while a request is in flight is ignored. GETs are never
	// blocked - pages fire several on load (lang + menu, session + lang).
	function request(method, url, body) {
		var write = method !== 'GET';
		if (write && inflight) { return Promise.resolve({ ok: false, message: '', next: '', data: null, busy: true }); }
		if (write) { inflight = true; }
		var opts = { method: method, credentials: 'same-origin', cache: 'no-store', headers: { 'Accept': 'application/json' } };
		if (body !== undefined && write) {
			opts.headers['Content-Type'] = 'application/json';
			opts.body = JSON.stringify(body);
		}
		return fetch(url, opts)
			.then(function (res) { return res.json(); })
			.then(function (r) {
				if (write) { inflight = false; }
				if (r.message || write) { message(r.message); }
				if (r.next) { goTo(r.next, r.message); }
				return r;
			})
			.catch(function (err) {
				if (write) { inflight = false; }
				message('Network error: ' + err);
				return { ok: false, message: String(err), next: '', data: null };
			});
	}

	function onSubmit(formId, fn) {
		var f = el(formId);
		if (!f) { return; }
		f.addEventListener('submit', function (ev) {
			ev.preventDefault();
			fn(ev);
			return false;
		});
	}

	// Page skeleton (step 6 restyle): the first form inside .container is split into a
	// scrolling body and a fixed footer holding the message line and the page's LAST
	// button row, so Submit / Cancel / Exit stay visible however long the content is.
	// Any element with data-footer is pinned too (between the message line and that last row).
	// Pure DOM move: the nodes keep their ids and listeners, and the buttons stay inside
	// the form so Enter still submits. Pages can opt out with data-nolayout on the form.
	function layout() {
		var container = document.querySelector('.container');
		if (!container || container.querySelector('.c4j-body')) { return; }
		// two nestings exist: .container > form (most pages) and form > .container (wasteLog, despatchHeader)
		var form = container.querySelector('form') || container.closest('form');
		if (!form || form.hasAttribute('data-nolayout')) { return; }
		var host = container.contains(form) ? form : container;
		var body = document.createElement('div');
		body.className = 'c4j-body';
		var kids = Array.prototype.slice.call(host.childNodes);
		kids.forEach(function (n) {
			if (n.nodeType === 1 && (n.tagName === 'H1' || n.tagName === 'H2') && host === container) { return; }   // the title stays as the fixed header
			body.appendChild(n);
		});
		host.appendChild(body);

		var footer = document.createElement('div');
		footer.className = 'c4j-footer';
		var msg = body.querySelector('#message');
		if (msg) {
			var row = msg.parentNode;
			footer.appendChild(row.classList && row.classList.contains('form-group') && row.children.length === 1 ? row : msg);
		}
		Array.prototype.forEach.call(body.querySelectorAll('[data-footer]'), function (n) { footer.appendChild(n); });   // extra rows a page pins (despatchHeader's Add SSCC / Print STN)
		var groups = body.querySelectorAll('.button-group');
		if (groups.length) { footer.appendChild(groups[groups.length - 1]); }
		if (footer.childNodes.length) { host.appendChild(footer); }
	}

	function page(init) {
		var start = function () {
			layout();
			var carried = unstash();
			if (carried) { message(carried); }   // GETs on load only overwrite #message when they carry one
			init();
		};
		if (document.readyState === 'loading') {
			document.addEventListener('DOMContentLoaded', start);
		} else {
			start();
		}
	}

	// lang(keys) -> Promise of {key: text}; label(id, text) sets the element only when the text is non-empty,
	// so the English hard-coded in the page survives a missing SYS_LANGUAGE key.
	function lang(keys) {
		return request('GET', 'api/lang?keys=' + keys.join(',')).then(function (r) { return (r.ok && r.data && r.data.text) ? r.data.text : {}; });
	}
	function label(id, txt) { if (txt) { text(id, txt); } }

	return {
		page: page,
		lang: lang,
		label: label,
		get: function (url) { return request('GET', url); },
		post: function (url, body) { return request('POST', url, body || {}); },
		put: function (url, body) { return request('PUT', url, body || {}); },
		del: function (url) { return request('DELETE', url); },
		onSubmit: onSubmit,
		text: text,
		value: value,
		setValue: setValue,
		message: message,
		focus: focus,
		goTo: goTo
	};
}());
