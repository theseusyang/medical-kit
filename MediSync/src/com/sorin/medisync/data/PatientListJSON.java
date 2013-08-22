package com.sorin.medisync.data;
import org.json.JSONException;
import org.json.JSONObject;

public class PatientListJSON {
	public void writeJSON() {
		JSONObject object = new JSONObject();
		try {
			object.put("name", "Jack Hack");
			object.put("score", new Integer(200));
			object.put("current", new Double(152.32));
			object.put("nickname", "Hacker");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(object);
	}
}
