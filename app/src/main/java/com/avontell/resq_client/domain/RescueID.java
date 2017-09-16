package com.avontell.resq_client.domain;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A representation of a user, as their Rescue ID
 * @author Aaron Vontell
 */
public class RescueID {

    private JSONArray properties = new JSONArray();
    private Context context;

    private final static String PREFS = "RESQSHAREDPREFSKEY_BOIIIII";
    private final static String ID_KEY = "HERE_COME_DAT_RESQ_ID";

    /**
     * Creates a rescue id. If one alreadt exists on this device, then it is loaded. Otherwise,
     * the id is created from scratch, with no information
     * @param context the context of the calling activity, for loading purposes.
     */
    public RescueID(Context context) {

        this.context = context;
        SharedPreferences sharedPref = context.getSharedPreferences(
                PREFS, Context.MODE_PRIVATE);
        String loaded = sharedPref.getString(ID_KEY, "bad boi");

        if (loaded.equals("bad boi")) {
            reset();
        } else {
            try {
                this.properties = new JSONArray(loaded);
            } catch (JSONException e) {
                reset();
            }
        }

    }

    /**
     * Resets the properties of the RescueID to be default
     */
    public void reset() {

        this.properties = new JSONArray();
        this
                .addProperty("Full Name", "Your full name.", "string", "Aaron Vontell")
                .addProperty("Phone Number", "Your phone number, with extension.", "string", "(860) 805-0050")
                .addProperty("Age", "Your age, in years.", "int", 20)
                .addProperty("Conditions", "A list of conditions that you may have.", "string", "Obesity III, Diabetes")
                .addProperty("Medications", "A list of conditions that you may have.", "string", "Laxatives, Penicillin")
                .addProperty("Number of Children", "The number of children you have in your home.", "int", 0)
                .addProperty("Number of Animals", "The number of animals you have in your home.", "int", 2)
                .addProperty("Weight", "Your weight, in pounds.", "int", 260)
                .addProperty("Height", "Your height, in inches.", "int", 71)
                .addProperty("Allergies", "A list of your allergies.", "string", "Peanut Butter, Cats")
                .addProperty("Note", "An extra note for first responders.", "string", "Front stairs are broken.")
                .addProperty("Spouse", "Name of your spouse, if applicable", "string", "Mary Vontell");

        save();

    }

    /**
     * Saves the properties of the user to the device
     */
    public void save() {

        SharedPreferences sharedPref = context.getSharedPreferences(
                PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ID_KEY, this.toString());
        editor.apply();

    }

    /**
     * Adds a field to the Rescue ID
     * @param title the title of the id property
     * @param description a description of this property
     * @param type the type of the property
     * @param value the value of this property
     * @return this Rescue ID, for chaining purposes
     */
    public RescueID addProperty(String title, String description, String type, Object value) {
        JSONObject prop = new JSONObject();
        try {
            prop.put("title", title);
            prop.put("description", description);
            prop.put("type", type);
            prop.put("value", value);
            properties.put(prop);
            return this;
        } catch (JSONException e) {
            e.printStackTrace();
            return this;
        }
    }

    /**
     * Returns the count of the number of properties in this medical ID
     * @return the count of the number of properties in this medical ID
     */
    public int getCount() {
        return properties.length();
    }

    /**
     * Returns the title of the given property
     * @param i the index of the property that you wish to check
     * @return the title of this property
     */
    public String getTitle(int i) {
        try {
            return properties.getJSONObject(i).getString("title");
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Returns the description of the given property
     * @param i the index of the property that you wish to check
     * @return the description of this property
     */
    public String getDescription(int i) {
        try {
            return properties.getJSONObject(i).getString("desc");
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Returns the type of the given property
     * @param i the index of the property that you wish to check
     * @return the type of this property
     */
    public String getType(int i) {
        try {
            return properties.getJSONObject(i).getString("type");
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Returns the value of the given property
     * @param i the index of the property that you wish to check
     * @return the value of this property. Use type to cast.
     */
    public Object getValue(int i) {
        try {
            String type = getType(i);
            switch(type) {
                case "int":
                    return properties.getJSONObject(i).getInt("value");
                case "string":
                    return properties.getJSONObject(i).getString("value");
                case "boolean":
                    return properties.getJSONObject(i).getBoolean("value");
                default:
                    return null;
            }
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return properties.toString();
    }

}
