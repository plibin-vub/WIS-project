package com.github.drinking_buddies.scripts;

import static com.github.drinking_buddies.jooq.Tables.ADDRESS;
import static com.github.drinking_buddies.jooq.Tables.BAR;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;

import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Address;
import com.github.drinking_buddies.geolocation.GeoLocation;
import com.github.drinking_buddies.jooq.tables.records.AddressRecord;
import com.github.drinking_buddies.webservices.google.Geocoding;
import com.github.drinking_buddies.webservices.google.Geocoding.GoogleGeoCodeResponse.location;
import com.github.drinking_buddies.webservices.rest.exceptions.RestException;

public class ImportBars {
    public static void main(String[] args) throws SQLException, IOException {
        FileInputStream fstream = new FileInputStream("/Users/plibin0/Dropbox/vub/vub2013-2014/semester1/web information systems/project/data/bars-oudemarkt.csv");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        while ((strLine = br.readLine()) != null)   {
            String[] spl = strLine.split(";");
            String nr = spl[0];
            String name = spl[1];
            saveBar("Oude markt", nr.trim(), "3000", "Leuven", "Belgium", name, null);
        }
        in.close();
    }
    
    private static String saveBar(String street, String number, String zipcode,
            String city, String country, String name, String website) {
        Connection conn = null;
        location location;
        try {
            location = Geocoding.addressToLocation(new Address(0, street,
                    number, zipcode, city, country));
        } catch (RestException e1) {
            e1.printStackTrace();
            return null;
        }
        if(location==null){
            return null;
        }
        GeoLocation geoLocation = GeoLocation.fromDegrees(
                Double.parseDouble(location.lat),
                Double.parseDouble(location.lng));
        try {
            conn = DBUtils.getConnection();
            DSLContext dsl = DBUtils.createDSLContext(conn);
            String url = name.toLowerCase().replace(" ", "_");
            Result<Record1<Integer>> r = dsl.select(BAR.ID).from(BAR)
                    .where(BAR.URL.eq(url)).fetch();
            if (r.size() != 0) {
                url = url + (r.size() + 1);
            }
            AddressRecord rr = dsl
                    .insertInto(ADDRESS, ADDRESS.STREET, ADDRESS.NUMBER,
                            ADDRESS.ZIPCODE, ADDRESS.CITY, ADDRESS.COUNTRY)
                    .values(street, number, zipcode, city, country).returning()
                    .fetchOne();

            dsl.insertInto(BAR, BAR.ADDRESS_ID, BAR.NAME, BAR.WEBSITE, BAR.URL,
                    BAR.LOCATION_X, BAR.LOCATION_Y)
                    .values(rr.getId(), name, website, url,
                            new Float(geoLocation.getLatitudeInRadians()),
                            new Float(geoLocation.getLongitudeInRadians()))
                    .execute();
            conn.commit();
            return "/bars/"+url;
        } catch (Exception e) {
            DBUtils.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }
}
