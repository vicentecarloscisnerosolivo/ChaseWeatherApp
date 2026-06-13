package com.vcco.weather.network.repository;

import androidx.annotation.NonNull;

import com.vcco.weather.model.geoconfig.GeocodeResponse;
import com.vcco.weather.model.weather.CurrentWeatherResponse;
import com.vcco.weather.model.zip.ZipResponse;
import com.vcco.weather.network.apiHelper.OpenWeatherApiHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

/**
 * Repository for OpenWeatherService, do network calls on Java
 *
 * @Inject: OpenWeatherApiHelper
 */
public class OpenWeatherRepositoryJava {
    private final OpenWeatherApiHelper helper;

    @Inject
    public OpenWeatherRepositoryJava(OpenWeatherApiHelper helper) {
        this.helper = helper;
    }

    /**
     * getCurrentWeather from the requested location, could be GPS location or searched location
     * by user
     *
     * @param location String -> Could be following conditions: City or City and Country or City,
     *                 State and Country
     * @param units    String -> Could be Metric or Imperil by user selection
     */

    @NonNull
    public Observable<Response<CurrentWeatherResponse>> getCurrentWeatherFromLocation(@NonNull String location, @NonNull String units) {
        return helper.getWeatherCurrentLocation(location, units);
    }

    /**
     * get the location information like coordinates local names from the name of the City
     *
     * @param location String -> Could be following conditions: City or City and Country or City,
     *                 State and Country
     */
    @NonNull
    public Observable<Response<List<GeocodeResponse>>> getLocationInfoFromName(@NonNull String location) {
        return helper.getLocationInfoFromName(location);
    }


    /**
     * get the location info for the giving ZipCode
     *
     * @param zipCode String -> , must use ZipCode,Country Code ie. 90210, US
     */
    @NonNull
    public Observable<Response<ZipResponse>> getInfoFromZipCode(@NonNull String zipCode) {
        return helper.getInfoFromZipCode(zipCode);
    }

    /**
     * get location Info from the requested Coordinates
     *
     * @param latitude  Float -> latitude from the location
     * @param longitude Float -> longitude from the location
     *                  <p>
     *                  Warning only can be called from not async threat
     */
    @NonNull
    public Observable<Response<List<GeocodeResponse>>> getLocationInfoFromCoordinates(Float latitude, Float longitude) {
        return helper.getReverseLocation(latitude, longitude);
    }
}
