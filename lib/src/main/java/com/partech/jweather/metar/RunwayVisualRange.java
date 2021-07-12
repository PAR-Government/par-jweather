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

class RunwayVisualRange {
    private int runwayNumber = 0; // runway number
    private char approachDirection = ' '; // L/R/C
    private char reportableModifier = ' '; // P - below, M - above
    private int lowestReportable = 0; // (ft)
    private int highestReportable = 0; // (ft)

    RunwayVisualRange() {
    }

    /**
     *
     * @param runwayNumber the part of a METAR RVR token which represents a
     * runway number
     */
    void setRunwayNumber(int runwayNumber) {
        this.runwayNumber = runwayNumber;
    }

    /**
     *
     * @param direction the part of a METAR RVR token which represents an
     * approach direction (e.g. 'L', 'R')
     */
    void setApproachDirection(char direction) {
        this.approachDirection = direction;
    }

    /**
     *
     * @param modifier the part of a METAR RVR token which represents a
     * modifier used to specify if the visual range is above or below the
     * following value
     */
    void setReportableModifier(char modifier) {
        this.reportableModifier = modifier;
    }

    /**
     *
     * @param lowestReportable the part of a METAR RVR token which represents
     * the lowest reportable value for visual range
     */
    void setLowestReportable(int lowestReportable) {
        this.lowestReportable = lowestReportable;
    }

    /**
     *
     * @param highestReportable the part of a METAR RVR token which represents
     * the highest reportable value for visual range
     */
    void setHighestReportable(int highestReportable) {
        this.highestReportable = highestReportable;
    }

    /**
     *
     * @return a string that represents the runway visual range in natural language
     */
    public String getNaturalLanguageString() {
        String temp = Integer.valueOf(runwayNumber).toString();

        temp += approachDirection;

        if (reportableModifier == 'M') {
            temp += " less than";
        } else if (reportableModifier == 'P') {
            temp += " greater than";
        }

        if (highestReportable > 0) {
            temp += " " + lowestReportable;
            temp += " to " + highestReportable + "feet.";
        } else {
            temp += " " + lowestReportable + "feet.";
        }

        return temp;
    }
}
