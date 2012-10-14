/**
 * Occurrence.java
 */

/**
 *  Copyright 2010,2012 Roger Lamb 
 * 
 *  This file is part of GPS Notify.
 *
 *  GPS Notify is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  GPS Notify is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with GPS Notify.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.eskeyLamb.apps.gpsNotify;

/**
 * this enum identifies the different occurrences that can occur.
 * 
 * @author rlamb
 *
 * @version 1
 */
public enum Occurrence {
	NONE,
	ONCE,
	MONDAY,
	TUESDAY,
	WEDNESDAY,
	THURSDAY,
	FRIDAY,
	SATURDAY,
	SUNDAY,
	DAILY,
	WEEKEND,
	WEEKLY
}
