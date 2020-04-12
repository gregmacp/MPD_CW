//<!--Greg MacPherson S1509595-->
package com.example.mpd_cw;

import android.icu.util.LocaleData;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

public class ParseIncident {
    private static final String TAG = "ParseIncidents";
    private ArrayList<TrafficItem> applications;



    public ParseIncident() {
        this.applications = new ArrayList<>();
    }

    private Date[] getDates(String[] parts){
//        Start Date: Thursday, 12 March 2020 - 00:00<br />End Date: Thursday, 30 April 2020 - 00:00<br />Delay Information: No reported delay.
//        Log.d(TAG, "DATES: " + "--");
//        String sDs = parts[0].substring(12).replace(",", "");


        Date newformatStartDate = null;
        Date newformatEndDate = null;

        try{
            //string dates
        String sDate = parts[0].substring(12);
        String eDate = parts[1].substring(10);

        //first format
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMMMM yyyy - HH:mm", Locale.ENGLISH);
        Date sd = sdf.parse(sDate);
        Date ed = sdf.parse(eDate);

        //reformat
            sdf.applyPattern("dd/MM/yy HH:mm");
        String startDate = sdf.format(sd);
        String endDate = sdf.format(ed);
        newformatStartDate = sdf.parse(startDate);
        newformatEndDate = sdf.parse(endDate);

        } catch (ParseException e){
            e.printStackTrace();
        }
//        Log.d(TAG, "DATES: " + startDate);
//        Log.d(TAG, "DATES: " + endDate);

        return new Date[]{newformatStartDate, newformatEndDate};
    };

    private long getDur (Date[] dates){
        Date startDate = dates[0];
        Date endDate = dates[1];

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

//        Log.d(TAG, "DATES, duration: " +diff);

        return diff;
    }

    public ArrayList<TrafficItem> getApplications() {
        return applications;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        TrafficItem currentTrafficItem = null;
        boolean inEntry = false;
        String textValue = "";
        Log.d(TAG, "parse: xmldata: " + xmlData);

        try {
                Log.d(TAG, "parse: Try");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
//            eventType = xpp.next();
//
            Log.d(TAG, "parse: Event type: " + eventType);


            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if("item".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentTrafficItem = new TrafficItem();
                            Log.d(TAG, "parse: NEW ITEM");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:



                        if(inEntry) {
                            if("item".equalsIgnoreCase(tagName)) {
                                applications.add(currentTrafficItem);
                                inEntry = false;
                            } else if("title".equalsIgnoreCase(tagName)) {
                                currentTrafficItem.setTitle(textValue);
                            }else if("description".equalsIgnoreCase(tagName)) {
                                if (textValue.contains("Start Date:")) {
                                    String[] parts = textValue.split("<br />");
                                    Date dates[] = getDates(parts);
                                    long duration = getDur(dates);
                                    Log.d("MyTag", ""+parts[0]);
                                    Log.d("MyTag", ""+parts[1]);
                                    Log.d("MyTag", "DURATION: "+duration);

                                    String period = dates[0].toString().substring(0, 10) + " - " + dates[1].toString().substring(0, 10) ;
                                    String period2 = dates[0].toString().substring(0, 23) + " - " + dates[1].toString().substring(0, 23) ;
//                                    String period = dates[0].toString();

                                    currentTrafficItem.setDesc(parts[2]);
                                    currentTrafficItem.setStartDate(dates[0]);
                                    currentTrafficItem.setEndDate(dates[1]);
                                    currentTrafficItem.setPeriod(period);
                                    currentTrafficItem.setPeriod2(period2);
                                    currentTrafficItem.setDuration(duration);
                                    currentTrafficItem.setInc(false);
//                                    Log.d(TAG, "DATES: " + currentTrafficItem.getStartDate());
//                                    Log.d(TAG, "DATES: " + currentTrafficItem.getEndDate());
//                                    Log.d(TAG, "DATES: " + currentTrafficItem.getDuration());
//                                    Log.d(TAG, "DATES: " + currentTrafficItem.getPeriod());
                                } else {currentTrafficItem.setInc(true); currentTrafficItem.setDesc(textValue);}
                            }else if("link".equalsIgnoreCase(tagName)) {
                                currentTrafficItem.setLink(textValue);
                            }else if("point".equalsIgnoreCase(tagName)) {
                                Log.d("MyTag", "GEO TAG: "+textValue);
                                currentTrafficItem.setGeoRss(textValue);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();

            }
            Log.d("ParseIncidents", "new items: ");
            for (TrafficItem app: applications) {
//                Log.d(TAG, "******************");
//                Log.d(TAG, app.toString());
            }

        } catch(Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }



}