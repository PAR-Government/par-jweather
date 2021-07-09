/*
jWeather(TM) is a Java library for parsing raw weather data
Copyright (C) 2004 David Castro

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

For more information, please email arimus@users.sourceforge.net
*/

package com.partech.jweather.metar;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Metar {

    private static final String TAG = "WeatherMetar";

    private Date date = null;
    private String reportModifier = "";
    private String stationID = "";
    private Integer windDirection = null;
    private Integer windDirectionMin = null;
    private Integer windDirectionMax = null;
    private boolean windDirectionIsVariable = false;
    private Float windSpeed = null; // (in knots x 1.1508 = MPH)
    private Float windGusts = null; // (in knots x 1.1508 = MPH)
    private boolean isCavok = false;
    private Float visibilityMiles = null; // in miles
    private Float visibilityKilometers = null; // in kilometers
    private Float visibilityMeters = null; // in meters
    private boolean visibilityLessThan = false;
    private Float pressure = null;
    private Float temperature = null;
    private Float temperaturePrecise = null;
    private Float dewPoint;
    private Float dewPointPrecise = null;
    private final List<WeatherCondition> weatherConditions = new ArrayList<>();
    private final List<SkyCondition> skyConditions = new ArrayList<>();
    private final List<RunwayVisualRange> runwayVisualRanges = new ArrayList<>();
    private final List<Obscuration> obscurations = new ArrayList<>();
    private boolean isNoSignificantChange = false;
    private String rawText = "";
    private String rawDate = "";
    private String flightCategory = "";
    private float latitude;
    private float longitude;
    private URI uri;


    public void setRawDate(String rawDate) {
        this.rawDate = rawDate;
    }

    public String getRawDate() {
        return rawDate;
    }

    public void setFlightCategory(String flightCategory) {
        this.flightCategory = flightCategory;
    }

    public String getFlightCategory() {
        return flightCategory;
    }

    /**
     * @param value the date this METAR report was generated
     */
    protected void setDate(Date value) {
        this.date = value;
    }

    /**
     * @return the date this METAR report was generated
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param value the modifier of the report, which specifies whether this
     *              report was an automated report or was a corrected report
     */
    void setReportModifier(String value) {
        this.reportModifier = value;
    }

    /**
     * @return the modifier of the report, which specifies whether this report
     * was an automated report or was a corrected report
     */
    public String getReportModifier() {
        return reportModifier;
    }

    /**
     * @param rawText the data string that represents this WeatherMetar
     */
    void setRawText(String rawText) {
        this.rawText = rawText;
    }

    /**
     * @return the data string that represents this MEtar
     */
    public String getRawText() {
        return rawText;
    }

    /**
     * @param value the station id of the station that generated this METAR
     *              report
     */
    void setStationID(String value) {
        this.stationID = value;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    /**
     * @return the station id of the station that generated this METAR report
     */
    public String getStationID() {
        return stationID;
    }

    /**
     * @param value the direction the wind is blowing in (in degrees)
     */
    void setWindDirection(Integer value) {
        this.windDirection = value;
    }

    /**
     * @return the direction the wind is blowing in (in degrees)
     */
    public Integer getWindDirection() {
        return windDirection;
    }

    /**
     * @param value the minimum wind direction (in degrees) for variable wind
     *              directions
     */
    void setWindDirectionMin(Integer value) {
        this.windDirectionMin = value;
    }

    /**
     * @return the minimum wind direction (in degrees) for variable wind
     * directions
     */
    public Integer getWindDirectionMin() {
        return windDirectionMin;
    }

    /**
     * @param value the maximum wind direction (in degrees) for variable wind
     *              directions
     */
    void setWindDirectionMax(Integer value) {
        this.windDirectionMax = value;
    }

    /**
     * @return the maximum wind direction (in degrees) for variable wind
     * directions
     */
    public Integer getWindDirectionMax() {
        return windDirectionMax;
    }

    /**
     * @param value whether or not the wind direction is variable
     */
    protected void setWindDirectionIsVariable(boolean value) {
        this.windDirectionIsVariable = value;
    }

    /**
     * @return whether or not the wind direction is variable
     */
    public boolean getWindDirectionIsVariable() {
        return windDirectionIsVariable;
    }

    /**
     * @param value wind speed in knots
     */
    protected void setWindSpeed(Float value) {
        this.windSpeed = value;
    }

    /**
     * @param value wind speed in meters per second
     */
    protected void setWindSpeedInMPS(Float value) {
        this.windSpeed = value / 0.5148f;
    }

    /**
     * @return wind speed in meters per second
     */
    public Float getWindSpeedInMPS() {
        return this.windSpeed * 0.5148f;
    }

    /**
     * @return wind speed in knots
     */
    public Float getWindSpeedInKnots() {
        return this.windSpeed;
    }

    /**
     * @return wind speed in MPH
     */
    public Float getWindSpeedInMPH() {
        if (this.windSpeed == null) {
            return null;
        }

        float f = this.windSpeed * 1.1508f;

        // round to the nearest MPH
        f = Math.round(f);

        return f;
    }

    /**
     * @param value wind gust speed in knots
     */
    protected void setWindGusts(Float value) {
        this.windGusts = value;
    }

    /**
     * @param value wind gust speed in meters per second
     */
    protected void setWindGustsInMPS(Float value) {
        this.windGusts = value / 0.5148f;
    }

    /**
     * @return wind gust speed in meters per second
     */
    public Float getWindGustsInMPS() {
        return this.windGusts * 0.5148f;
    }

    /**
     * @return wind gust speed in knots
     */
    public Float getWindGustsInKnots() {
        return this.windGusts;
    }

    /**
     * @return wind gust speed in MPH
     */
    public Float getWindGustsInMPH() {
        if (this.windGusts == null) {
            return null;
        }

        float f = this.windGusts * 1.1508f;

        // round to the nearest MPH
        f = Math.round(f);

        return f;
    }

    /**
     * this function will also set visibility to 10KM, since
     * CAVOK means visibility is greater than 10KM
     *
     * @param value boolean whether or not CAVOK was given
     */
    protected void setIsCavok(boolean value) {
        this.isCavok = value;
        // set visibility to 10
        setVisibilityInKilometers(10f);
    }

    /**
     * @return value boolean whether or not CAVOK is true
     */
    public boolean getIsCavok() {
        return this.isCavok;
    }

    /**
     * @param value visibility in miles
     */
    protected void setVisibility(Float value) {
        this.visibilityMiles = value;
        this.visibilityKilometers = null;
        this.visibilityMeters = null;
    }

    /**
     * @param value visibility in kilometers
     */
    protected void setVisibilityInKilometers(Float value) {
        this.visibilityKilometers = value;
        this.visibilityMiles = null;
        this.visibilityMeters = null;
    }

    /**
     * @param value visibility in meters
     */
    protected void setVisibilityInMeters(Float value) {
        this.visibilityMeters = value;
        this.visibilityMiles = null;
        this.visibilityKilometers = null;
    }

    /**
     * @return visibility in miles
     */
    public Float getVisibility() {
        if (visibilityMiles != null) {
            return visibilityMiles;
        } else if (visibilityKilometers != null) {
            return visibilityKilometers / 1.609344f;
        } else if (visibilityMeters != null) {
            return visibilityMeters / 1609.344f;
        }
        return null;
    }

    /**
     * @return visibility in kilometers
     */
    public Float getVisibilityInKilometers() {
        if (visibilityKilometers != null) {
            return visibilityKilometers;
        } else if (visibilityMeters != null) {
            return visibilityMeters / 1000;
        } else if (visibilityMiles != null) {
            return visibilityMiles * 1.609344f;
        }
        return null;
    }

    /**
     * @return visibility in meters
     */
    public Float getVisibilityInMeters() {
        if (visibilityMeters != null) {
            return visibilityMeters;
        } else if (visibilityKilometers != null) {
            return visibilityKilometers * 1000;
        }
        return null;
    }

    /**
     * @param value visibility less than
     */
    protected void setVisibilityLessThan(boolean value) {
        this.visibilityLessThan = value;
    }

    /**
     * @return visibility less than
     */
    public boolean getVisibilityLessThan() {
        return visibilityLessThan;
    }

    /**
     * @param value pressure in inches Hg
     */
    protected void setPressure(Float value) {
        this.pressure = value;
    }

    /**
     * @return pressure in inches Hg
     */
    public Float getPressure() {
        return pressure;
    }

    /**
     * @param value temperature in celsius
     */
    protected void setTemperature(Float value) {
        this.temperature = value;
    }

    /**
     * @return temperature in celsius
     */
    public Float getTemperatureInCelsius() {
        return this.temperature;
    }

    /**
     * @return temperature in fahrenheit
     */
    public Float getTemperatureInFahrenheit() {
        if (this.temperature == null) {
            return null;
        }

        // round to the nearest 1/10th

        return (float) Math
                .round((this.temperature * 9 / 5 + 32) * 10) / 10;
    }

    /**
     * @param value precise temperature in celsius
     */
    protected void setTemperaturePrecise(Float value) {
        this.temperaturePrecise = value;
    }

    /**
     * @return precise temperature in celsius (nearest 1/10th degree)
     */
    public Float getTemperaturePreciseInCelsius() {
        return this.temperaturePrecise;
    }

    /**
     * @return precise temperature in fahrenheit (nearest 1/10th degree)
     */
    public Float getTemperaturePreciseInFahrenheit() {
        if (this.temperaturePrecise == null) {
            return null;
        }

        // round to the nearest 1/10th
        return (float) Math
                .round((this.temperaturePrecise * 9 / 5 + 32) * 10)
                / 10;
    }

    /**
     * @return most precise temperature in celsius (nearest 1/10th degree)
     */
    public Float getTemperatureMostPreciseInCelsius() {
        if (this.temperaturePrecise != null) {
            return this.temperaturePrecise;
        } else {
            return this.temperature;
        }
    }

    /**
     * @return most precise temperature in fahrenheit (nearest 1/10th degree)
     */
    public Float getTemperatureMostPreciseInFahrenheit() {
        if (this.temperaturePrecise != null) {
            // round to the nearest 1/10th

            return (float) Math.round(
                    (this.temperaturePrecise * 9 / 5 + 32) * 10)
                    / 10;
        } else if (this.temperature != null) {
            // round to the nearest 1/10th

            return (float) Math
                    .round((this.temperature * 9 / 5 + 32) * 10)
                    / 10;
        } else {
            return null;
        }
    }

    /**
     * @param value dew point in celsius
     */
    protected void setDewPoint(Float value) {
        this.dewPoint = value;
    }

    /**
     * @return dew point in celsius
     */
    public Float getDewPointInCelsius() {
        return this.dewPoint;
    }

    /**
     * @return dew point in fahrenheit
     */
    public Float getDewPointInFahrenheit() {
        if (this.dewPoint == null) {
            return null;
        }

        // round to the nearest 1/10th

        return (float) Math
                .round((this.dewPoint * 9 / 5 + 32) * 10) / 10;
    }

    /**
     * @param value precise dew point in celsius
     */
    protected void setDewPointPrecise(Float value) {
        this.dewPointPrecise = value;
    }

    /**
     * @return dew point in celsius (nearest 1/10th degree)
     */
    public Float getDewPointPreciseInCelsius() {
        return this.dewPointPrecise;
    }

    /**
     * @return dew point in fahrenheit (nearest 1/10th degree)
     */
    public Float getDewPointPreciseInFahrenheit() {
        if (this.dewPointPrecise == null) {
            return null;
        }

        // round to the nearest 1/10th

        return (float) Math
                .round((this.dewPointPrecise * 9 / 5 + 32) * 10)
                / 10;
    }

    /**
     * @return most precise dew point in celsius (nearest 1/10th degree)
     */
    public Float getDewPointMostPreciseInCelsius() {
        if (this.dewPointPrecise != null) {
            return this.dewPointPrecise;
        } else {
            return this.dewPoint;
        }
    }

    /**
     * @return most precise dew point in fahrenheit (nearest 1/10th degree)
     */
    public Float getDewPointMostPreciseInFahrenheit() {
        if (this.dewPointPrecise != null) {
            // round to the nearest 1/10th

            return (float) Math.round(
                    (this.dewPointPrecise * 9 / 5 + 32) * 10) / 10;
        } else if (this.dewPoint != null) {
            // round to the nearest 1/10th

            return (float) Math
                    .round((this.dewPoint * 9 / 5 + 32) * 10) / 10;
        } else {
            return null;
        }
    }

    /**
     * @param value whether or not the weather has changed significantly
     */
    protected void setIsNoSignificantChange(boolean value) {
        this.isNoSignificantChange = value;
    }

    /**
     * @return whether or not there has been a significant change in
     * weather
     */
    public boolean getIsNoSignificantChange() {
        return isNoSignificantChange;
    }

    /**
     * @param wc a WeatherCondition object
     * @see WeatherCondition
     */
    public void addWeatherCondition(WeatherCondition wc) {
        weatherConditions.add(wc);
    }

    /**
     * @return a WeatherCondition object
     * @see WeatherCondition
     */
    public WeatherCondition getWeatherCondition(int i) {
        if (weatherConditions.size() >= i) {
            return weatherConditions.get(i);
        } else {
            return null;
        }
    }

    /**
     * @return an ArrayList of WeatherCondition objects
     * @see WeatherCondition
     */
    public List<WeatherCondition> getWeatherConditions() {
        return weatherConditions;
    }

    /**
     * @param sc a SkyCondition object
     * @see SkyCondition
     */
    public void addSkyCondition(SkyCondition sc) {
        skyConditions.add(sc);
    }

    /**
     * @return a SkyCondition object
     * @see SkyCondition
     */
    public SkyCondition getSkyCondition(int i) {
        if (skyConditions.size() >= i) {
            return skyConditions.get(i);
        } else {
            return null;
        }
    }

    /**
     * @return an ArrayList of SkyCondition objects
     * @see SkyCondition
     */
    public List<SkyCondition> getSkyConditions() {
        return skyConditions;
    }

    /**
     * @param rvr a RunwayVisualRange object
     * @see RunwayVisualRange
     */
    public void addRunwayVisualRange(RunwayVisualRange rvr) {
        runwayVisualRanges.add(rvr);
    }

    /**
     * @return a RunwayVisualRange object
     * @see RunwayVisualRange
     */
    public RunwayVisualRange getRunwayVisualRange(int i) {
        if (runwayVisualRanges.size() >= i) {
            return runwayVisualRanges.get(i);
        } else {
            return null;
        }
    }

    /**
     * @return an ArrayList of RunwayVisualRange objects
     * @see RunwayVisualRange
     */
    public List<RunwayVisualRange> getRunwayVisualRanges() {
        return runwayVisualRanges;
    }

    /**
     * @param o an Obscuration object
     * @see Obscuration
     */
    public void addObscuration(Obscuration o) {
        obscurations.add(o);
    }

    /**
     * @return a Obscuration object
     * @see Obscuration
     */
    public Obscuration getObscuration(int i) {
        if (obscurations.size() >= i) {
            return obscurations.get(i);
        } else {
            return null;
        }
    }

    /**
     * @return an ArrayList of Obscuration objects
     * @see Obscuration
     */
    public List<Obscuration> getObscurations() {
        return obscurations;
    }
}
