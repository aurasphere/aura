package co.aurasphere.aura.nebula.utils;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;

import co.aurasphere.aura.nebula.AuraNebula;

public class PropertiesReader {

    private static final String AURA_BASE_PROPERTIES_FILE = "aura.properties";

    private static final String TAG = "PropertiesReader";

    private Properties properties;

    /**
     * Constructor which loads the default properties file.
     */
    @Inject
    public PropertiesReader() {
        properties = new Properties();
        try {
            AssetManager assetManager = AuraNebula.getContext().getAssets();
            InputStream inputStream = assetManager.open(AURA_BASE_PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            Log.e(TAG, "Error while loading properties from file " + AURA_BASE_PROPERTIES_FILE + ":", e);
        }
    }

    /**
     * Method which gets a property from the main file.
     *
     * @param key the property key.
     * @return the corresponding property.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Method which loads a property file and adds the properties to the main property file.
     *
     * @param file the file to load
     */
    public void loadPropertyFile(String file) {
        Properties tempProperties = new Properties();
        try {
            AssetManager assetManager = AuraNebula.getContext().getAssets();
            InputStream inputStream = assetManager.open(file);
            tempProperties.load(inputStream);
            // Puts all properties from the temp file into the main property file.
            properties.putAll(tempProperties);
        } catch (Exception e) {
            Log.e(TAG, "Error while loading properties from file " + file + ":", e);
        }
    }
}
