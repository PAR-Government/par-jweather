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

import org.apache.oro.text.perl.MalformedPerl5PatternException;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/*
 * examples:
 *  KCNO 060653Z 32004KT 10SM BKN043 13/11 A2993 RMK AO2 SLP133 T01280106
 *  KCNO 070353Z AUTO 29009KT 10SM CLR 13/11 A2991 RMK AO2 SLP127 T01280106
 *  KCNO 071853Z 24010KT 10SM BKN038 OVC048 17/09 A2998 RMK AO2 SLP147 T01670089
 *  KCNO 231653Z VRB04KT 1 3/4SM HZ BKN010 18/15 A2997 RMK AO2 SLP145 HZ FEW000 T01780150
 *  KCNO 291753Z 26006KT 4SM HZ CLR A2991 RMK AO2 SLPNO 57007
 *
 * Body of report:
 *  (1)  Type of report - METAR/SPECI
 *  (2)  Station Identifier - CCCC
 *  (3)  Date and Time of Report (UTC) - YYGGggZ
 *  (4)  Report Modifier - AUTO/COR
 *  (5)  Wind - ddff(f)Gf f (f )KT_d d d Vd d d
 *                       m m  m     n n n  x x x
 *  (6)  Visibility - VVVVVSM
 *  (7)  Runaway Visual Range - RD D /V V V V FT  or  RD D /V V V V VV V V V FT
 *                                r r  r r r r          r r  n n n n  x x x x
 *  (8)  Present Weather - w'w'
 *  (9)  Sky Condition - N N N h h h  or  VVh h h  or  SKC/CLR
 *                        s s s s s s        s s s
 *  (10) Temperature and Dew Point - T'T'/T' T'
 *                                          d  d
 *  (11) Altimeter - AP P P P
 *                     h h h h
 * Remarks section of report:
 *  (1)  Automated, Manual, Plain Language
 *  (2)  Additive and Maintenance Data
 *
 * *note: '_' denotes a required space
 *
 *
 * Table 12-2 Present Weather
 *
 * _________________________________________________________________________________
 * | Intensity  |   Descriptor  |   Precipitation  |   Obscuration |   Other       |
 * +------------+---------------+------------------+---------------+---------------+
 * | - Light    | MI Shallow    | DZ Drizzle       | BR Mist       | PO Well-      |
 * |   Moderate | PR Partial    | RA Rain          | FG Fog        |    Developed  |
 * | + Heavy    | BC Patches    | SN Snow          | FU Smoke      |    Dust/Sand  |
 * |            | DR Low        | SG Snow Grains   | VA Volcanic   |    Whirls     |
 * |            |    Drifting   | IC Ice Crystals  |    Ash        | SQ Squalls    |
 * |            | BL Blowing    | PL Ice Pellets   | DU Widespread | FC Funnel     |
 * |            | SH Shower(s)  | GR Hail          |    Dust       |    Cloud,     |
 * |            | TS Thunder-   | GS Small Hail    | SA Sand       |    Tornado,   |
 * |            |    storm      |    and/or        | HZ Haze       |    Waterspout |
 * |            | FZ Freezing   |    Snow Pellets  | PY Spray      | SS Sandstorm  |
 * |            |               | UP Unknown       |               | DS Duststorm  |
 * |            |               |    Precipitation |               |               |
 * +------------+---------------+------------------+---------------+---------------+
 *
 * up to 3 weather groups can be reported
 */

public class MetarParser {

    private static final Perl5Util utility = new Perl5Util();
    private static final Perl5Matcher matcher = new Perl5Matcher();

    private final ArrayList<String> tokens = new ArrayList<>();

    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm", Locale.US);
    private static final TimeZone gmtZone = TimeZone.getTimeZone("GMT");

    private WeatherCondition weatherCondition = null;
    private SkyCondition skyCondition = null;
    private RunwayVisualRange runwayVisualRange = null;
    private Obscuration obscuration = null;

    private int index = 0;
    private int numTokens = 0;
    private String temp = null;

    static {
        sdf.setTimeZone(gmtZone);
    }

    public static Metar parse(String metarData) throws MetarParseException {
        MetarParser mp = new MetarParser();
        return mp.parseData(metarData);
    }

    private Metar parseData(String metarData) throws MetarParseException {

        if (metarData == null) {
            throw new MetarParseException("empty metar data");
        }

        Metar metar = new Metar();
        metar.setRawText(metarData);

        // test data
        //metarData += "KCNO 070353Z AUTO 29009KT 1 1/2SM R01L/0800V1600FT CLR 13/11 A2991 RMK AO2 SLP127 T01280106\n";

        // split the two lines of raw weatherMetar data apart

        // split the second line, the METAR data, on whitespace into tokens for
        // processing
        try {
            utility.split(tokens, metarData);
        } catch (MalformedPerl5PatternException e) {

            throw new MetarParseException(
                    "error spliting weatherMetar data on whitespace: " + e);
        }

        // the number of tokens we have
        numTokens = tokens.size();

        // type of report should be present (METAR/SPECI)???

        // station id will always be present in
        // format: CCCC
        //     CCCC - alphabetic characters only [a-zA-Z]
        metar.setStationID(tokens.get(index++));

        // date and time of the report
        // format: YYGGggZ
        //     YY - date
        //     GG - hours
        //     gg - minutes
        //     Z  - Zulu (UTC)
        if (tokens.get(index).endsWith("Z")) {
            // steal year and month from date string
            Calendar calendar = Calendar.getInstance(gmtZone);

            int dayInt, hourInt, minuteInt;
            try {
                String day = tokens.get(index).substring(0, 2);

                String hour = tokens.get(index).substring(2, 4);

                String minute = tokens.get(index).substring(4, 6);

                dayInt = Integer.parseInt(day);
                hourInt = Integer.parseInt(hour);
                minuteInt = Integer.parseInt(minute);

                // case where the month may have rolled. In this case, the
                // calendar should be rolled back one day
                if (dayInt > calendar.get(Calendar.DAY_OF_MONTH)) {
                    calendar.roll(Calendar.DAY_OF_MONTH, false);
                }

                calendar.set(Calendar.DAY_OF_MONTH, dayInt);
                calendar.set(Calendar.HOUR_OF_DAY, hourInt);
                calendar.set(Calendar.MINUTE, minuteInt);
            } catch (NumberFormatException nfe) {

                throw new MetarParseException(
                        "unable to parse WeatherMetar date value: " + nfe);
            }

            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            metar.setDate(calendar.getTime());

            // on to the next token
            if (index < numTokens - 1) {
                index++;
            }

        } else {

            // unexpected token...should have been data in Zulu (UTC)
        }

        // report modifier
        // format: (AUTO or COR)
        //     AUTO - fully automated with no human intervention or oversight
        //     COR  - corrected report
        if (tokens.get(index).equals(MetarConstants.METAR_AUTOMATED)
                ||
                tokens.get(index)
                        .equals(MetarConstants.METAR_CORRECTED)) {
            metar.setReportModifier(tokens.get(index));
            // on to the next token
            if (index < numTokens - 1) {
                index++;
            }

        } else {

        }

        // wind group (speed and direction)
        // format: dddff(f)Gf f (f )KT_d d d Vd d d
        //                   m m  m     n n n  x x x
        //     ddd           - wind direction (may be VRB (variable))
        //     ff(f)         - wind speed
        //     Gf f (f )     - wind gust speed
        //       m m  m
        //     KT (or) MPS   - knots (or) meters per second
        //     d d d Vd d d  - variable wind direction > 6 knots, degree=>degree
        //      n n n  x x x   e.g. 180V210 => variable from 180deg to 210deg

        temp = tokens.get(index);
        if (temp.endsWith("KT") || temp.endsWith("MPS")) {
            int pos = 0;
            boolean windInKnots = false;

            if (temp.endsWith("KT")) {

                windInKnots = true;
            } else {

            }

            if (!tokens.get(index).startsWith("VRB")) {
                // we have gusts
                Integer windDirection = Integer.valueOf(
                        tokens.get(index).substring(0, 3));
                metar.setWindDirection(windDirection);
            } else {
                metar.setWindDirectionIsVariable(true);
            }

            temp = tokens.get(index).substring(5, 5);
            try {
                if (matcher.matches(temp, new Perl5Compiler().compile("\\d"))) {
                    // have three-digit wind speed

                    if (windInKnots) {
                        metar.setWindSpeed(Float.valueOf(
                                tokens.get(index).substring(3, 6)));
                    } else {
                        metar.setWindSpeedInMPS(Float.valueOf(
                                tokens.get(index).substring(3, 6)));
                    }
                    pos = 6;
                } else {
                    // have two-digit wind speed

                    if (windInKnots) {
                        metar.setWindSpeed(Float.valueOf(
                                tokens.get(index).substring(3, 5)));
                    } else {
                        metar.setWindSpeedInMPS(Float.valueOf(
                                tokens.get(index).substring(3, 5)));
                    }
                    pos = 5;
                }
            } catch (MalformedPatternException ignored) {

            }

            if (tokens.get(index).charAt(pos) == 'G') {
                // we have wind gusts

                pos++;

                temp = tokens.get(index).substring(pos + 2, pos + 2);
                //if (((String)tokens.get(index)).substring(pos+2,pos+2).matches("\\d")) {
                try {
                    if (matcher.matches(temp,
                            new Perl5Compiler().compile("\\d"))) {
                        // have three-digit wind speed

                        if (windInKnots) {
                            metar.setWindGusts(
                                    Float.valueOf(tokens.get(index)
                                            .substring(pos, pos + 3)));
                        } else {
                            metar.setWindGustsInMPS(
                                    Float.valueOf(tokens.get(index)
                                            .substring(pos, pos + 3)));
                        }
                    } else {
                        // have two-digit wind speed

                        if (windInKnots) {
                            metar.setWindGusts(
                                    Float.valueOf(tokens.get(index)
                                            .substring(pos, pos + 2)));
                        } else {
                            metar.setWindGustsInMPS(
                                    Float.valueOf(tokens.get(index)
                                            .substring(pos, pos + 2)));
                        }
                    }
                } catch (MalformedPatternException ignored) {

                }
            }

            if (windInKnots) {

            } else {

            }

            // on to the next token
            if (index < numTokens - 1) {
                index++;
            }

            // if we have variable wind direction
            temp = tokens.get(index);
            try {
                if (matcher.matches(temp,
                        new Perl5Compiler().compile(".*\\d\\d\\dV\\d\\d\\d"))) {
                    metar.setWindDirectionIsVariable(true);

                    metar.setWindDirectionMin(Integer.valueOf(
                            tokens.get(index).substring(0, 3)));
                    metar.setWindDirectionMax(Integer.valueOf(
                            tokens.get(index).substring(4, 7)));

                    // on to the next token
                    if (index < numTokens - 1) {
                        index++;
                    }
                }
            } catch (MalformedPatternException ignored) {

            }
        } else {
            // unexpected token...should have been wind speed

        }

        try {
            // CAVOK
            //
            // Visibility greater than 10Km, no cloud below 5000 ft or minimum
            // sector altitude, whichever is the lowest and no CB (Cumulonimbus) or
            // over development and no significant weather.
            if (tokens.get(index)
                    .equals(MetarConstants.METAR_CAVOK)) {
                metar.setIsCavok(true);

                // on to the next token
                if (index < numTokens - 1) {
                    index++;
                }
                // Horizontal visibility in meters
            } else if (matcher.matches(tokens.get(index),
                    new Perl5Compiler().compile("/^(\\d+)$/"))) {
                int tmp = Integer.parseInt(matcher.getMatch().toString());
                metar.setVisibilityInMeters((float) tmp);

                // on to the next token
                if (index < numTokens - 1) {
                    index++;
                }
                // Horizontal visibility of 10Km and above
            } else if (tokens.get(index).equals("9999")) {
                metar.setVisibilityInKilometers(10f);

                // on to the next token
                if (index < numTokens - 1) {
                    index++;
                }

                // get visibility
                // format: (M)VVVVVSM
                //     (M)   - used to indicate less than
                //     VVVVV - miles (00001SM)
                //     SM    - statute miles
            } else if (tokens.get(index).endsWith("SM") ||
                    ((index + 1 < numTokens)
                            && tokens.get(index + 1).endsWith("SM"))
                    ||
                    tokens.get(index).endsWith("KM") ||
                    ((index + 1 < numTokens) && tokens.get(index + 1)
                            .endsWith("KM"))) {

                String whole, fraction = "";
                float visibility;
                boolean isLessThan = false;
                String token = tokens.get(index);
                boolean visibilityInStatuteMiles = false;

                if (tokens.get(index).endsWith("SM") ||
                        ((index + 1 < numTokens)
                                && tokens.get(index + 1)
                                .endsWith("SM"))) {
                    visibilityInStatuteMiles = true;
                }

                if (token.startsWith("M")) {

                    isLessThan = true;
                    token = token.substring(1);
                }

                if (token.endsWith("SM") || token.endsWith("KM")) {
                    if (token.indexOf('/') == -1) {
                        // no fractions to deal with
                        whole = token.substring(0, token.length() - 2);
                    } else {
                        whole = "0";
                        fraction = token.substring(0, token.length() - 2);
                    }
                } else {
                    whole = token;
                    // next token is the fraction part
                    index++;
                    fraction = tokens.get(index).substring(0,
                            tokens.get(index).length() - 2);
                }

                visibility = Float.parseFloat(whole);

                if (!fraction.equals("")) {
                    // we have a fraction to convert
                    ArrayList<String> frac = new ArrayList<>();
                    try {
                        utility.split(frac, "/\\//", fraction);
                    } catch (MalformedPerl5PatternException e) {

                        throw new MetarParseException(
                                "error spliting fraction on /: " + e);
                    }

                    visibility = visibility
                            + Float.parseFloat(frac.get(0))
                            / Float.parseFloat(frac.get(1));
                }

                if (visibilityInStatuteMiles) {
                    metar.setVisibility(visibility);
                } else {
                    metar.setVisibilityInKilometers(visibility);
                }
                metar.setVisibilityLessThan(isLessThan);

                // on to the next token
                if (index < numTokens - 1) {
                    index++;
                }

            } else {
                String token = tokens.get(index);
                boolean isLessThan = false;

                if (utility.match("/M?\\d+/", token)) {

                    if (token.startsWith("M")) {

                        isLessThan = true;
                        token = token.substring(1);
                    }

                    try {
                        metar.setVisibilityInMeters(Float.valueOf(token));
                    } catch (Exception ignored) {

                    }
                    metar.setVisibilityLessThan(isLessThan);

                    // on to the next token
                    if (index < numTokens - 1) {
                        index++;
                    }
                } else {
                    // unexpected token...should have been visibility

                }
            }
        } catch (MalformedPatternException ignored) {

        }

        // see if we have a Runaway Visual Range Group token
        // format: RD D /V V V V FT  or  RD D /V V V V VV V V V FT
        //           r r  r r r r          r r  n n n n  x x x x
        //    R        - runway number follows
        //    D D      - runway number
        //     r r
        //    (D )     - runway approach directions
        //      r        L (left), R (right), C (center)
        //    (M/P)    - M (less than 0600FT), P (greater than 6000FT)
        //    V V V V  - (lowest) visual range, constant reportable value
        //     r r r r
        //    V        - separates lowest/highest visual range
        //    V V V V  - (highest) visual range, constant reportable value
        //     x x x x
        //    FT       - feet
        //
        while (tokens.get(index).startsWith("R")) {
            // check that first character after the R is a digit. this helps
            // qualify this as a real RVR. Otherwise we could be grabbing the
            // wx descriptor 'RA'
            if (!Character.isDigit(tokens.get(index).charAt(1))) {
                break;
            }

            // we have a runway visual range
            runwayVisualRange = new RunwayVisualRange();

            // get our runway number
            runwayVisualRange.setRunwayNumber(Integer
                    .parseInt(tokens.get(index).substring(1, 3)));

            int pos = 3;
            if (tokens.get(index).charAt(pos) != '/') {
                runwayVisualRange.setApproachDirection(
                        tokens.get(index).charAt(pos));

                pos += 2; // increment past the '/'
            } else {
                pos++;
            }

            // determine if we have a modifier for above 6000ft or below 600ft
            switch (tokens.get(index).charAt(pos)) {
                case 'P': // below 600ft
                case 'M': // above 6000ft
                    runwayVisualRange.setReportableModifier(
                            tokens.get(index).charAt(pos));

                    pos++;
            }
            runwayVisualRange.setLowestReportable(Integer.parseInt(
                    tokens.get(index).substring(pos, pos + 4)));

            pos += 4;
            // if we are using the format with highest reportable
            if (tokens.get(index).charAt(pos) == 'V') {
                pos++; // increment past V
                runwayVisualRange.setHighestReportable(Integer.parseInt(
                        tokens.get(index).substring(pos, pos + 4)));

            }

            // on to the next token
            if (index < numTokens - 1) {
                index++;
            }

            metar.addRunwayVisualRange(runwayVisualRange);
        }

        // weather groups
        // format: (+/-)ddpp
        //     (+/-) - intensity, light (-), moderate (default), heavy (+)
        //     dd    - descriptor, qualifier/adjective for phenomena
        //     pp    - phenomena (rain, hail, tornado, etc.)
        // we know we have a weather group if the token starts with one of:
        // _________________________________________________________________________________
        // | Intensity  |   Descriptor  |   Precipitation  |   Obscuration |   Other       |
        // +------------+---------------+------------------+---------------+---------------+
        // | - Light    | MI Shallow    | DZ Drizzle       | BR Mist       | PO Well-      |
        // |   Moderate | PR Partial    | RA Rain          | FG Fog        |    Developed  |
        // | + Heavy    | BC Patches    | SN Snow          | FU Smoke      |    Dust/Sand  |
        // |   3)VC In  | DR Low        | SG Snow Grains   | VA Volcanic   |    Whirls     |
        // | Vicinity   |    Drifting   | IC Ice Crystals  |    Ash        | SQ Squalls    |
        // |            | BL Blowing    | PL Ice Pellets   | DU Widespread | FC Funnel     |
        // |            | SH Shower(s)  | GR Hail          |    Dust       |    Cloud,     |
        // |            | TS Thunder-   | GS Small Hail    | SA Sand       |    Tornado,   |
        // |            |    storm      |    and/or        | HZ Haze       |    Waterspout |
        // |            | FZ Freezing   |    Snow Pellets  | PY Spray      | SS Sandstorm  |
        // |            |               | UP Unknown       |               | DS Duststorm  |
        // |            |               |    Precipitation |               |               |
        // +------------+---------------+------------------+---------------+---------------+
        while (tokens.get(index)
                .startsWith(MetarConstants.METAR_HEAVY) ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_LIGHT)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_IN_THE_VICINITY)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SHALLOW)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_PARTIAL)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_PATCHES)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_LOW_DRIFTING)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_BLOWING)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SHOWERS)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_THUNDERSTORMS)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_FREEZING)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_DRIZZLE)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_RAIN)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SNOW)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SNOW_GRAINS)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_ICE_CRYSTALS)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_ICE_PELLETS)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_HAIL)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SMALL_HAIL)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_UNKNOWN_PRECIPITATION)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_MIST)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_FOG)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SMOKE)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_VOLCANIC_ASH)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_WIDESPREAD_DUST)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SAND)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_HAZE)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SPRAY)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_DUST_SAND_WHIRLS)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SQUALLS)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_FUNNEL_CLOUD)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SAND_STORM)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_DUST_STORM)
                ||
                tokens.get(index).startsWith(
                        MetarConstants.METAR_NO_SIGNIFICANT_CHANGE)) {

            int pos = 0;

            // we have a weather condition
            weatherCondition = new WeatherCondition();

            if (tokens.get(index)
                    .startsWith(MetarConstants.METAR_HEAVY) ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_LIGHT) ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_IN_THE_VICINITY))
            {
                weatherCondition.setIntensity(
                        String.valueOf(tokens.get(index).charAt(0)));

                pos++;
            } else {

            }

            // if we have a descriptor
            if (tokens.get(index).substring(pos, pos + 2)
                    .startsWith(MetarConstants.METAR_SHALLOW) ||
                    tokens.get(index).substring(pos, pos + 2)
                            .startsWith(MetarConstants.METAR_PARTIAL)
                    ||
                    tokens.get(index).substring(pos, pos + 2)
                            .startsWith(MetarConstants.METAR_PATCHES)
                    ||
                    tokens.get(index).substring(pos, pos + 2)
                            .startsWith(MetarConstants.METAR_LOW_DRIFTING)
                    ||
                    tokens.get(index).substring(pos, pos + 2)
                            .startsWith(MetarConstants.METAR_BLOWING)
                    ||
                    tokens.get(index).substring(pos, pos + 2)
                            .startsWith(MetarConstants.METAR_SHOWERS)
                    ||
                    tokens.get(index).substring(pos, pos + 2)
                            .startsWith(MetarConstants.METAR_THUNDERSTORMS)
                    ||
                    tokens.get(index).substring(pos, pos + 2)
                            .startsWith(MetarConstants.METAR_FREEZING)) {
                weatherCondition.setDescriptor(
                        tokens.get(index).substring(pos, pos + 2));

                pos += 2;
            } else {

            }

            // if we have phenomena (we should always!)
            if (tokens.get(index)
                    .startsWith(MetarConstants.METAR_DRIZZLE, pos) ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_RAIN, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_SNOW, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_SNOW_GRAINS, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_ICE_CRYSTALS, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_ICE_PELLETS, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_HAIL, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_SMALL_HAIL, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_UNKNOWN_PRECIPITATION, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_MIST, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_FOG, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_SMOKE, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_VOLCANIC_ASH, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_WIDESPREAD_DUST, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_SAND, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_HAZE, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_SPRAY, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_DUST_SAND_WHIRLS, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_SQUALLS, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_FUNNEL_CLOUD, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_SAND_STORM, pos)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_DUST_STORM, pos)) {
                weatherCondition.setPhenomena(
                        tokens.get(index).substring(pos, pos + 2));

                metar.addWeatherCondition(weatherCondition);

            } else {

            }

            // on to the next token
            if (index < numTokens - 1) {
                index++;
            }
        }

        // sky condition
        // format: NNNhhh or VVhhh or CLR/SKC
        //     NNN - amount of sky cover
        //     hhh - height of layer (in hundreds of feet above the surface)
        //     VV  - vertical visibility, indefinite ceiling
        //     SKC - clear skies (reported by manual station)
        //     CLR - clear skies (reported by automated station)
        while (tokens.get(index)
                .startsWith(MetarConstants.METAR_VERTICAL_VISIBILITY) ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SKY_CLEAR)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_CLEAR)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_FEW)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_SCATTERED)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_BROKEN)
                ||
                tokens.get(index)
                        .startsWith(MetarConstants.METAR_OVERCAST)
                ||
                tokens.get(index).startsWith(
                        MetarConstants.METAR_NO_SIGNIFICANT_CLOUDS)) {

            // we have a sky condition
            skyCondition = new SkyCondition();

            if (tokens.get(index)
                    .startsWith(MetarConstants.METAR_FEW) ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_SCATTERED)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_BROKEN)
                    ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_OVERCAST)) {
                skyCondition.setContraction(
                        tokens.get(index).substring(0, 3));

                skyCondition.setHeight(Integer.parseInt(
                        tokens.get(index).substring(3, 6)));

                if (tokens.get(index).length() > 6) {
                    // we have a modifier
                    skyCondition.setModifier(
                            tokens.get(index).substring(6
                            ));

                }
            } else if (tokens.get(index)
                    .startsWith(MetarConstants.METAR_SKY_CLEAR) ||
                    tokens.get(index)
                            .startsWith(MetarConstants.METAR_CLEAR)) {
                skyCondition.setContraction(
                        tokens.get(index).substring(0, 3));

            } else if (tokens.get(index)
                    .startsWith(MetarConstants.METAR_VERTICAL_VISIBILITY)) {
                skyCondition.setContraction(
                        tokens.get(index).substring(0, 2));

                skyCondition.setHeight(Integer.parseInt(
                        tokens.get(index).substring(2, 5)));

            } else if (tokens.get(index)
                    .startsWith(MetarConstants.METAR_NO_SIGNIFICANT_CLOUDS)) {
                skyCondition.setContraction(
                        tokens.get(index).substring(0, 3));

            } else {

            }

            metar.addSkyCondition(skyCondition);

            // on to the next token
            if (index < numTokens - 1) {
                index++;
            }
        }

        // temperature / dew point
        // format: (M)T'T'/(M)T' T'
        //                d  d
        //     (M)    - sub-zero temperature
        //     T'T'   - temerature (in celsius)
        //     T' T'  - dew point (in celsius)
        //       d  d
        //
        // TF = ( 9 / 5 ) x TC + 32 (conversion from celsius to fahrenheit)
        if (tokens.get(index).contains("/")) {

            ArrayList<String> temps = new ArrayList<>();

            try {
                utility.split(temps, "/\\//", tokens.get(index));
            } catch (MalformedPerl5PatternException e) {

                throw new MetarParseException(
                        "error spliting temperature on /: " + e);
            }

            // we have a sub-zero temperature
            float temperature;
            if (temps.get(0).startsWith("M")) {
                temperature = Float.parseFloat(temps.get(0).substring(1, 3));
                temperature = temperature
                        - temperature * 2; // negate
                metar.setTemperature(temperature);
            } else {
                temperature = Float.parseFloat(temps.get(0));
                metar.setTemperature(temperature);
            }

            // we have a sub-zero temperature
            Float dewPoint;
            if (temps.get(1).startsWith("M")) {
                dewPoint = Float.valueOf(temps.get(1).substring(1, 3));
                dewPoint = dewPoint - dewPoint * 2; // negate
                metar.setDewPoint(dewPoint);
            } else {
                dewPoint = Float.valueOf(temps.get(1));
                metar.setDewPoint(dewPoint);
            }

            //temperature = new Float(((String)tokens.get(index)).substring(1,5)).floatValue();
            //dewPoint = new Float(((String)tokens.get(index)).substring(5,9)).floatValue();

            // on to the next token
            if (index < numTokens - 1) {
                index++;
            }
        } else {

            metar.setTemperature(null);
            metar.setDewPoint(null);
        }

        // altimeter
        // get pressure, which is reported in hundreths
        //
        // format: AP P P P
        //           h h h h
        //     A        - altimeter in inches of mercury
        //     P P P P  - tens, units, tenths and hundreths inches mercury
        //      h h h h   (no decimal point coded)
        if (tokens.get(index).startsWith("A")) {
            float pressure = Float.parseFloat(tokens.get(index).substring(1, 5));
            // correct for no decimal point
            pressure = pressure / 100;
            metar.setPressure(pressure);

            // on to the next token
            if (index < numTokens - 1) {
                index++;
            }
        } else {

        }

        // remarks
        if (!tokens.get(index)
                .equals(MetarConstants.METAR_REMARKS)) {
            // we have no remarks

        } else {

            index++;
        }

        // remarks
        // -------
        // volcanic eruptions
        // funnel cloud
        // type of automated station (A01/A02)
        //     A01 - stations without a precipitation descriminator
        //     A02 - stations with a precipitation descriminator
        // peak wind, PK_WND_dddff(f)/(hh)mm
        // wind shift, WSHFT_(hh)mm (FROPA)
        // tower or surface visibility
        // variable prevailing visbility
        // sector visbility
        // visbility at second location
        // lightning
        // beginning and ending of precipitation
        // beginning and ending of thunderstorms
        // thunderstorm location
        // hailstone size
        // virga
        // variable ceiling height
        // obscurations
        // variable sky condition
        // significant cloud types
        // ceiling height at second location
        // pressure rising or falling rapidly
        // sea-level pressure
        // aircraft mishap
        // no SPECI reports taken
        // snow increasing rapidly
        // other significant information

        // additive data
        // -------------
        // precipitation
        // cloud types
        // duration of sunshine

        // hourly temperature and dewpoint
        // format: Ts T'T'T's T' T' T'
        //           n       n  d  d  d
        //     T         - group indicator
        //     s         - sign of the temperature (1=sub-zero, 0=zero+)
        //      n
        //     T'T'T'    - temperature
        //     T' T' T'  - dew point
        //       d  d  d
        //
        // see if we have hourly temperature
        while (index < numTokens) {

            // if we have temperature
            temp = tokens.get(index);
            //if (((String)tokens.get(index)).matches("T\\d{8}")) {
            try {
                if (matcher.matches(temp,
                        new Perl5Compiler().compile("T\\d{8}"))) {

                    // we have a sub-zero temperature
                    float temperaturePrecise = Float.parseFloat(tokens.get(index).substring(2, 5));
                    if (tokens.get(index).charAt(1) == '1') {
                        temperaturePrecise = temperaturePrecise
                                - temperaturePrecise * 2; // negate
                    }
                    // it is in tenths
                    temperaturePrecise = temperaturePrecise / 10;
                    metar.setTemperaturePrecise(temperaturePrecise);

                    // we have a sub-zero dew point
                    float dewPointPrecise = Float.parseFloat(tokens.get(index).substring(6, 9));
                    if (tokens.get(index).charAt(5) == '1') {
                        dewPointPrecise = dewPointPrecise
                                - dewPointPrecise * 2; // negate
                    }
                    // it is in tenths
                    dewPointPrecise = dewPointPrecise / 10;
                    metar.setDewPointPrecise(dewPointPrecise);

                    // if we have an obscuration
                } else if (tokens.get(index)
                        .equals(MetarConstants.METAR_MIST) ||
                        tokens.get(index)
                                .equals(MetarConstants.METAR_FOG)
                        ||
                        tokens.get(index)
                                .equals(MetarConstants.METAR_SMOKE)
                        ||
                        tokens.get(index)
                                .equals(MetarConstants.METAR_VOLCANIC_ASH)
                        ||
                        tokens.get(index)
                                .equals(MetarConstants.METAR_WIDESPREAD_DUST)
                        ||
                        tokens.get(index)
                                .equals(MetarConstants.METAR_SAND)
                        ||
                        tokens.get(index)
                                .equals(MetarConstants.METAR_HAZE)
                        ||
                        tokens.get(index)
                                .equals(MetarConstants.METAR_SPRAY)) {
                    // we have an obscuration
                    obscuration = new Obscuration();
                    obscuration.setPhenomena(tokens.get(index));

                    // move to quantity and height token
                    index++;

                    // we have a quantity and height too
                    if (tokens.get(index)
                            .startsWith(MetarConstants.METAR_FEW) ||
                            tokens.get(index)
                                    .startsWith(MetarConstants.METAR_SCATTERED)
                            ||
                            tokens.get(index)
                                    .startsWith(MetarConstants.METAR_BROKEN)
                            ||
                            tokens.get(index).startsWith(
                                    MetarConstants.METAR_OVERCAST)) {
                        obscuration.setContraction(
                                tokens.get(index).substring(0, 3));
                        obscuration.setHeight(Integer.parseInt(
                                tokens.get(index).substring(3, 6)));

                        metar.addObscuration(obscuration);

                    }

                    index++;
                    // there has been no significant change in weather
                } else if (tokens.get(index)
                        .equals(MetarConstants.METAR_NO_SIGNIFICANT_CHANGE)) {
                    // have no significant change
                    metar.setIsNoSignificantChange(true);
                }
            } catch (MalformedPatternException ignored) {

            }

            index++;
        }

        // 6-hourly maximum temperature
        // 6-hourly minimum temperature
        // 24-hour maximum and minimum temperature
        // 3-hourly pressure tendency

        return metar;
    }
}

