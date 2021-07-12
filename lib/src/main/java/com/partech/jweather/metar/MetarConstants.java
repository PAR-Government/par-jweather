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

******** NOTICE OF MODIFICATIONS ********
This work has been modified by PAR Government (https://pargovernment.net/) as of July 2021.
*/

package com.partech.jweather.metar;

/**
 * Simple container for METAR weather token constants.
 *
 * @author dennis@bullamanka.com
 */
interface MetarConstants {
    String METAR_IN_THE_VICINITY = "VC";
    /** WeatherMetar string value for fully automated report ('AUTO') */
    String METAR_AUTOMATED = "AUTO";
    /** WeatherMetar string value for corrected report ('COR') */
    String METAR_CORRECTED = "COR";
    /** WeatherMetar string value for Vertical Visibility ('VV') */
    String METAR_VERTICAL_VISIBILITY = "VV";
    /** WeatherMetar string value for Sky Clear ('SKC') */
    String METAR_SKY_CLEAR = "SKC";
    /** WeatherMetar string value for Clear ('CLR') */
    String METAR_CLEAR = "CLR";
    /** WeatherMetar string value for Few ('FEW') */
    String METAR_FEW = "FEW";
    /** WeatherMetar string value for Scattered ('SCT') */
    String METAR_SCATTERED = "SCT";
    /** WeatherMetar string value for Broken ('BKN') */
    String METAR_BROKEN = "BKN";
    /** WeatherMetar string value for Overcast ('OVC') */
    String METAR_OVERCAST = "OVC";
    /** WeatherMetar string value for Cumulonimbus ('CB') */
    String METAR_CUMULONIMBUS = "CB";
    /** WeatherMetar string value for Towering Cumulus ('TCU') */
    String METAR_TOWERING_CUMULUS = "TCU";
    /** WeatherMetar string value for Heavy ('+') */
    String METAR_HEAVY = "+";
    /** WeatherMetar string value for Light ('-') */
    String METAR_LIGHT = "-";
    /** WeatherMetar string value for Shallow ('MI') */
    String METAR_SHALLOW = "MI";
    /** WeatherMetar string value for Partial ('PR') */
    String METAR_PARTIAL = "PR";
    /** WeatherMetar string value for Patches ('BC') */
    String METAR_PATCHES = "BC";
    /** WeatherMetar string value for LowDrifting ('DR') */
    String METAR_LOW_DRIFTING = "DR";
    /** WeatherMetar string value for Blowing ('BL') */
    String METAR_BLOWING = "BL";
    /** WeatherMetar string value for Showers ('SH') */
    String METAR_SHOWERS = "SH";
    /** WeatherMetar string value for Thunderstorms ('TS') */
    String METAR_THUNDERSTORMS = "TS";
    /** WeatherMetar string value for Freezing ('FZ') */
    String METAR_FREEZING = "FZ";
    /** WeatherMetar string value for Drizzle ('DZ') */
    String METAR_DRIZZLE = "DZ";
    /** WeatherMetar string value for Rain ('RA') */
    String METAR_RAIN = "RA";
    /** WeatherMetar string value for Snow ('SN') */
    String METAR_SNOW = "SN";
    /** WeatherMetar string value for Snow Grains ('SG') */
    String METAR_SNOW_GRAINS = "SG";
    /** WeatherMetar string value for Ice Crystals ('IC') */
    String METAR_ICE_CRYSTALS = "IC";
    /** WeatherMetar string value for Ice Pellets ('PL') */
    String METAR_ICE_PELLETS = "PL";
    /** WeatherMetar string value for Hail ('GR') */
    String METAR_HAIL = "GR";
    /** WeatherMetar string value for Small Hail ('GS') */
    String METAR_SMALL_HAIL = "GS";
    /** WeatherMetar string value for Unknown Precip ('UP') */
    String METAR_UNKNOWN_PRECIPITATION = "UP";
    /** WeatherMetar string value for Mist ('BR') */
    String METAR_MIST = "BR";
    /** WeatherMetar string value for Fog ('FG') */
    String METAR_FOG = "FG";
    /** WeatherMetar string value for Smoke ('FU') */
    String METAR_SMOKE = "FU";
    /** WeatherMetar string value for Volcanic Ash ('VA') */
    String METAR_VOLCANIC_ASH = "VA";
    /** WeatherMetar string value for Widespread Dust ('DU') */
    String METAR_WIDESPREAD_DUST = "DU";
    /** WeatherMetar string value for Sand ('SA') */
    String METAR_SAND = "SA";
    /** WeatherMetar string value for Haze ('HZ') */
    String METAR_HAZE = "HZ";
    /** WeatherMetar string value for Spray ('PY') */
    String METAR_SPRAY = "PY";
    /** WeatherMetar string value for Dust Sand Whirls ('PO') */
    String METAR_DUST_SAND_WHIRLS = "PO";
    /** WeatherMetar string value for Squalls ('SQ') */
    String METAR_SQUALLS = "SQ";
    /** WeatherMetar string value for Funnel Cloud ('FC') */
    String METAR_FUNNEL_CLOUD = "FC";
    /** WeatherMetar string value for Sand Storm ('SS') */
    String METAR_SAND_STORM = "SS";
    /** WeatherMetar string value for Dust Storm ('DS') */
    String METAR_DUST_STORM = "DS";
    /** WeatherMetar string value for Remarks ('RMK') */
    String METAR_REMARKS = "RMK";

    /** WeatherMetar string value for Clouds and Visibility Okay ('CAVOK') */
    String METAR_CAVOK = "CAVOK";
    /** WeatherMetar string value for No Significant Change ('NOSIG') */
    String METAR_NO_SIGNIFICANT_CHANGE = "NOSIG";
    /** WeatherMetar string value for No Significant Clouds ('NSC') */
    String METAR_NO_SIGNIFICANT_CLOUDS = "NSC";

    /** WeatherMetar decoded string value for Vertical Visibility ('VV') */
    String METAR_DECODED_VERTICAL_VISIBILITY = "Vertical Visibility";
    /** WeatherMetar decoded string value for Sky Clear ('SKC') */
    String METAR_DECODED_SKY_CLEAR = "Sky Clear";
    /** WeatherMetar decoded string value for Clear ('CLR') */
    String METAR_DECODED_CLEAR = "Clear";
    /** WeatherMetar decoded string value for Few ('FEW') */
    String METAR_DECODED_FEW = "Few";
    /** WeatherMetar decoded string value for Scattered ('SCT') */
    String METAR_DECODED_SCATTERED = "Scattered";
    /** WeatherMetar decoded string value for Broken ('BKN') */
    String METAR_DECODED_BROKEN = "Broken";
    /** WeatherMetar decoded string value for Overcase ('OVC') */
    String METAR_DECODED_OVERCAST = "Overcast";
    /** WeatherMetar decoded string value for Cumulonimbus ('CB') */
    String METAR_DECODED_CUMULONIMBUS = "Cumulonimbus";
    /** WeatherMetar decoded string value for Towering Cumulonimbus ('TCU') */
    String METAR_DECODED_TOWERING_CUMULONIMBUS = "Tower Cumulonimbus";
    /** WeatherMetar decoded string value for Severe */
    String METAR_DECODED_SEVERE = "Severe";
    /** WeatherMetar decoded string value for Heavy ('+') */
    String METAR_DECODED_HEAVY = "Heavy";
    /** WeatherMetar decoded string value for Light ('-') */
    String METAR_DECODED_LIGHT = "Light";
    /** WeatherMetar decoded string value for Slight */
    String METAR_DECODED_SLIGHT = "Slight";
    /** WeatherMetar decoded string value for Light ('-') */
    String METAR_DECODED_MODERATE = "Moderate";
    /** WeatherMetar decoded string value for Shallow ('MI') */
    String METAR_DECODED_SHALLOW = "Shallow";
    /** WeatherMetar decoded string value for Partial ('PR') */
    String METAR_DECODED_PARTIAL = "Partial";
    /** WeatherMetar decoded string value for Patches ('BC') */
    String METAR_DECODED_PATCHES = "Patches";
    /** WeatherMetar decoded string value for LowDrifting ('DR') */
    String METAR_DECODED_LOW_DRIFTING = "Low Drifting";
    /** WeatherMetar decoded string value for Blowing ('BL') */
    String METAR_DECODED_BLOWING = "Blowing";
    /** WeatherMetar decoded string value for Showers ('SH') */
    String METAR_DECODED_SHOWERS = "Showers";
    /** WeatherMetar decoded string value for Thunderstorms ('TS') */
    String METAR_DECODED_THUNDERSTORMS = "Thunderstorms";
    /** WeatherMetar decoded string value for Freezing ('FZ') */
    String METAR_DECODED_FREEZING = "Freezing";
    /** WeatherMetar decoded string value for Drizzle ('DZ') */
    String METAR_DECODED_DRIZZLE = "Drizzle";
    /** WeatherMetar decoded string value for Rain ('RA') */
    String METAR_DECODED_RAIN = "Rain";
    /** WeatherMetar decoded string value for Snow ('SN') */
    String METAR_DECODED_SNOW = "Snow";
    /** WeatherMetar decoded string value for Snow Grains ('SG') */
    String METAR_DECODED_SNOW_GRAINS = "Snow Grains";
    /** WeatherMetar decoded string value for Ice Crystals ('IC') */
    String METAR_DECODED_ICE_CRYSTALS = "Ice Crystals";
    /** WeatherMetar decoded string value for Ice Pellets ('PL') */
    String METAR_DECODED_ICE_PELLETS = "Ice Pellets";
    /** WeatherMetar decoded string value for Hail ('GR') */
    String METAR_DECODED_HAIL = "Hail";
    /** WeatherMetar decoded string value for Small Hail ('GS') */
    String METAR_DECODED_SMALL_HAIL = "Small Hail";
    /** WeatherMetar decoded string value for Unknown Precip ('UP') */
    String METAR_DECODED_UNKNOWN_PRECIP = "Unknown Precip";
    /** WeatherMetar decoded string value for Mist ('BR') */
    String METAR_DECODED_MIST = "Mist";
    /** WeatherMetar decoded string value for Fog ('FG') */
    String METAR_DECODED_FOG = "Fog";
    /** WeatherMetar decoded string value for Smoke ('FU') */
    String METAR_DECODED_SMOKE = "Smoke";
    /** WeatherMetar decoded string value for Volcanic Ash ('VA') */
    String METAR_DECODED_VOLCANIC_ASH = "Volcanic Ash";
    /** WeatherMetar decoded string value for Widespread Dust ('DU') */
    String METAR_DECODED_WIDESPREAD_DUST = "Widespread Dust";
    /** WeatherMetar decoded string value for Sand ('SA') */
    String METAR_DECODED_SAND = "Sand";
    /** WeatherMetar decoded string value for Haze ('HZ') */
    String METAR_DECODED_HAZE = "Haze";
    /** WeatherMetar decoded string value for Spray ('PY') */
    String METAR_DECODED_SPRAY = "Spray";
    /** WeatherMetar decoded string value for Dust Sand Whirls ('PO') */
    String METAR_DECODED_DUST_SAND_WHIRLS = "Dust Sand Whirls";
    /** WeatherMetar decoded string value for Squalls ('SQ') */
    String METAR_DECODED_SQUALLS = "Squalls";
    /** WeatherMetar decoded string value for Funnel Cloud ('FC') */
    String METAR_DECODED_FUNNEL_CLOUD = "Funnel Cloud";
    /** WeatherMetar decoded string value for Sand Storm ('SS') */
    String METAR_DECODED_SAND_STORM = "Sand Storm";
    /** WeatherMetar decoded string value for Dust Storm ('DS') */
    String METAR_DECODED_DUST_STORM = "Dust Storm";

    /** WeatherMetar decoded string value for Clouds and Visibility Okay ('CAVOK') */
    String METAR_DECODED_CAVOK = "Clouds and Visibility Okay";
    /** WeatherMetar decoded string value for No Significant Change ('NOSIG') */
    String METAR_DECODED_NO_SIGNIFICANT_CHANGE = "No significant change";
    /** WeatherMetar decoded string value for No Significant Clouds ('NSC') */
    String METAR_DECODED_NO_SIGNIFICANT_CLOUDS = "No significant clouds";
}
