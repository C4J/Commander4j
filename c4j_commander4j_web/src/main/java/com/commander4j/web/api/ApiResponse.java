package com.commander4j.web.api;

/**
 * The one reply shape every endpoint returns:
 *   ok      - the request did what it was asked
 *   message - text for the yellow status label (blank when nothing to say)
 *   next    - page the client should show next ("" = stay where you are)
 *   data    - endpoint-specific payload (may be null)
 */

import java.util.LinkedHashMap;
import java.util.Map;

public class ApiResponse
{
	public boolean ok = true;
	public String message = "";
	public String next = "";
	public Object data = null;

	public static ApiResponse ok()
	{
		return new ApiResponse();
	}

	public static ApiResponse ok(Object data)
	{
		ApiResponse r = new ApiResponse();
		r.data = data;
		return r;
	}

	public static ApiResponse fail(String message)
	{
		ApiResponse r = new ApiResponse();
		r.ok = false;
		r.message = message;
		return r;
	}

	public static ApiResponse goTo(String next)
	{
		ApiResponse r = new ApiResponse();
		r.next = next;
		return r;
	}

	public ApiResponse withMessage(String message)
	{
		this.message = message == null ? "" : message;
		return this;
	}

	public ApiResponse withNext(String next)
	{
		this.next = next == null ? "" : next;
		return this;
	}

	public ApiResponse withData(Object data)
	{
		this.data = data;
		return this;
	}

	/** Convenience for small payloads: ApiResponse.ok().put("a", 1).put("b", "x") */
	@SuppressWarnings("unchecked")
	public ApiResponse put(String key, Object value)
	{
		if (data == null || (data instanceof Map) == false)
		{
			data = new LinkedHashMap<String, Object>();
		}
		((Map<String, Object>) data).put(key, value);
		return this;
	}
}
