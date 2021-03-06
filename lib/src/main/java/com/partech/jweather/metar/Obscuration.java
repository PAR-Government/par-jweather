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

class Obscuration {

    private int height = 0;

    // intensity flags
    private boolean isSlight = false;
    private boolean isModerate = false;
    private boolean isHeavy = false;
    private boolean isSevere = false;

    // obscuration type flags
    private boolean isMist = false;
    private boolean isFog = false;
    private boolean isSmoke = false;
    private boolean isVolcanicAsh = false;
    private boolean isDust = false;
    private boolean isSand = false;
    private boolean isHaze = false;
    private boolean isSpray = false;

    Obscuration() {
    }

    /**
     *
     * @param phenomena the part of a METAR obscuration token which represents
     * a specific type of phenomena (e.g. 'FG', 'HZ')
     */
    void setPhenomena(String phenomena) {
        switch (phenomena) {
            case MetarConstants.METAR_MIST:
                isMist = true;
                break;
            case MetarConstants.METAR_FOG:
                isFog = true;
                break;
            case MetarConstants.METAR_SMOKE:
                isSmoke = true;
                break;
            case MetarConstants.METAR_VOLCANIC_ASH:
                isVolcanicAsh = true;
                break;
            case MetarConstants.METAR_WIDESPREAD_DUST:
                isDust = true;
                break;
            case MetarConstants.METAR_SAND:
                isSand = true;
                break;
            case MetarConstants.METAR_HAZE:
                isHaze = true;
                break;
            case MetarConstants.METAR_SPRAY:
                isSpray = true;
                break;
        }
    }

    /**
     *
     * @param contraction the part of a METAR obscuration token which represents
     * a contraction for the phenomena (e.g. 'FEW', 'SCT')
     */
    void setContraction(String contraction) {
        switch (contraction) {
            case MetarConstants.METAR_FEW:
                isSlight = true;
                break;
            case MetarConstants.METAR_SCATTERED:
                isModerate = true;
                break;
            case MetarConstants.METAR_BROKEN:
                isHeavy = true;
                break;
            case MetarConstants.METAR_OVERCAST:
                isSevere = true;
                break;
        }
    }

    public boolean isSlight() {
        return isSlight;
    }

    public boolean isModerate() {
        return isModerate;
    }

    public boolean isHeavy() {
        return isHeavy;
    }

    public boolean isSevere() {
        return isSevere;
    }

    public boolean isMist() {
        return isMist;
    }

    public boolean isFog() {
        return isFog;
    }

    public boolean isSmoke() {
        return isSmoke;
    }

    public boolean isVolcanicAsh() {
        return isVolcanicAsh;
    }

    public boolean isDust() {
        return isDust;
    }

    public boolean isSand() {
        return isSand;
    }

    public boolean isHaze() {
        return isHaze;
    }

    public boolean isSpray() {
        return isSpray;
    }

    /**
     *
     * @param height the part of a METAR obscuration token which represents
     * the height of the phenomena (in hundreds of feet)
     */
    void setHeight(int height) {
        this.height = height * 100; // for hundreds of feet
    }

    /**
     *
     * @return a string that represents the obscuration in natural language
     */
    public String getNaturalLanguageString() {
        String temp = "";

        if (isSlight) {
            temp += "Slight";
        } else if (isModerate) {
            temp += "Moderate";
        } else if (isHeavy) {
            temp += "Heavy";
        } else if (isSevere) {
            temp += "Severe";
        }

        if (isMist) {
            temp += " Mist";
        } else if (isFog) {
            temp += " Fog";
        } else if (isSmoke) {
            temp += " Smoke";
        } else if (isVolcanicAsh) {
            temp += " Volcanic Ash";
        } else if (isDust) {
            temp += " Dust";
        } else if (isSand) {
            temp += " Sand";
        } else if (isHaze) {
            temp += " Haze";
        } else if (isSpray) {
            temp += " Spray";
        }

        if (height != 0) {
            temp += " at " + height + " feet";
        } else {
            temp += " at ground level";
        }

        return temp;
    }
}
