package com.iexamcenter.calendarweather.utility;

public class GeolocationDecimalToDegreesConverter {
    public static String convert(Double latitude, Double longitude){
        return convertLatitud(latitude) + ' ' + convertLongitude(longitude);
    }

    public static String convertLatitud(Double latitude){
        String result = "";
        if(latitude != null){
            String direction = "N";
            if(latitude < 0){
                direction = "S";
            }
            result = convert(latitude) + direction;
        }
        return result;
    }

    public static String convertLongitude(Double longitude){
        String result = "";
        if(longitude != null){
            String direction = "E";
            if(longitude < 0){
                direction = "W";
            }
            result = convert(longitude) + direction;
        }
        return result;
    }

    private static String convert(Double d){
        d = Math.abs(d);

        //degrees
        Integer i = d.intValue();
        String s = String.valueOf(i) + '°';

        //minutes
        d = d - i;
        d = d * 60;
        i = d.intValue();
        s = s + i + '\'';

        //seconds
        d = d - i;
        d = d * 60;
        i = (int)Math.round(d);
        s = s + i + '"';

        return s;
    }
}
