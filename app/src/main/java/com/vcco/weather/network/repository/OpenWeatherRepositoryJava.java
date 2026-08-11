package com.vcco.weather.network.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vcco.weather.R;
import com.vcco.weather.db.dao.WeatherDao;
import com.vcco.weather.db.model.Clouds;
import com.vcco.weather.db.model.Conditions;
import com.vcco.weather.db.model.Coordinates;
import com.vcco.weather.db.model.CurrentWeather;
import com.vcco.weather.db.model.Rain;
import com.vcco.weather.db.model.Snow;
import com.vcco.weather.db.model.SunTime;
import com.vcco.weather.db.model.Temperature;
import com.vcco.weather.db.model.Wind;
import com.vcco.weather.model.errors.WeatherErrorResponse;
import com.vcco.weather.model.geoconfig.GeocodeResponse;
import com.vcco.weather.model.weather.CurrentWeatherResponse;
import com.vcco.weather.model.zip.ZipResponse;
import com.vcco.weather.network.apiHelper.OpenWeatherApiHelper;
import com.vcco.weather.network.utils.NetworkConstants;

import java.util.ArrayList;
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
    private final WeatherDao dao;

    private static final String TAG = "OpenWeatherRepositoryJava";

    @Inject
    public OpenWeatherRepositoryJava(OpenWeatherApiHelper helper, WeatherDao dao) {
        this.helper = helper;
        this.dao = dao;
    }

    /**
     * getCurrentWeather from the requested location, could be GPS location or searched location
     * by user
     *
     * @param location String -> Could be following conditions: City or City and Country or City,
     *                 State and Country
     * @param units    String -> Could be Metric or Imperil by user selection
     */

    @Deprecated
    public CurrentWeatherResponse getCurrentWeatherFromLocation(
            @NonNull String location,
            @NonNull String units,
            @NonNull Boolean isFromZipCode,
            @NonNull Context context) throws WeatherErrorResponse {

            throw new WeatherErrorResponse(context.getString(R.string.error_server_error));

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
    @Deprecated
    public CurrentWeatherResponse getInfoFromZipCode(
            @NonNull String zipCode,
            @NonNull String unit,
            @NonNull Context context) throws WeatherErrorResponse {
        try {
            Response<ZipResponse> zipResponse = helper.getInfoFromZipCode(zipCode).blockingFirst();
            if (zipResponse.isSuccessful()) {
                ZipResponse response = zipResponse.body();
                String query = context.getString(
                        R.string.format_string_for_city_query,
                        response.getName(),
                        "",
                        response.getCountry()
                );

                return getCurrentWeatherFromLocation(
                        query,
                        unit,
                        true,
                        context
                );
            } else {
                throw new WeatherErrorResponse(
                        context.getString(R.string.error_not_found_location)
                );
            }
        } catch (Exception e) {
            Log.getStackTraceString(e);
            throw new WeatherErrorResponse(e.getMessage() != null ?
                    e.getMessage() : context.getString(R.string.error_server_error));
        }
    }

    /**
     * get location Info from the requested Coordinates
     *
     * @param latitude  Float -> latitude from the location
     * @param longitude Float -> longitude from the location
     *                  <p>
     *                  Warning only can be called from not async threat
     */

    @Deprecated
    public CurrentWeatherResponse getLocationInfoFromCoordinates(
            Float latitude,
            Float longitude,
            String unit,
            Context context) throws WeatherErrorResponse {
        Response<List<GeocodeResponse>> locationResponse = helper.getReverseLocation(latitude, longitude).blockingFirst();
        try {
            if (locationResponse.isSuccessful()) {
                if (locationResponse.body() != null) {
                    GeocodeResponse response = locationResponse.body().get(0);
                    String location =
                            context.getString(R.string.format_string_for_city_query,
                                    response.getName(),
                                    response.getState(),
                                    response.getCountry()
                            );
                    return getCurrentWeatherFromLocation(
                            location,
                            unit,
                            true,
                            context
                    );
                } else {
                    throw new WeatherErrorResponse(
                            context.getString(R.string.error_not_found_location)
                    );
                }
            } else {
                throw new WeatherErrorResponse(
                        context.getString(R.string.error_server_error)
                );
            }
        } catch (Exception e) {
            throw new WeatherErrorResponse(e.getMessage() != null ?
                    e.getMessage() : context.getString(R.string.error_server_error)
            );
        }
    }

    private CurrentWeather convertCurrentWeatherToDBEntity(CurrentWeatherResponse currentWeather) {
        return new CurrentWeather(
                0,
                currentWeather.getVisibility(),
                currentWeather.getBase(),
                currentWeather.getDataCalculation(),
                currentWeather.getTimeZone(),
                currentWeather.getId(),
                currentWeather.getName(),
                true
        );
    }

    private Clouds convertCloudToDBEntity(@NonNull com.vcco.weather.model.weather.Clouds clouds) {
        return new Clouds(0, 0, clouds.getCoverage());
    }

    private List<Conditions> convertConditionsToDBEntity(@NonNull List<com.vcco.weather.model.weather.Conditions> conditions) {
        List<Conditions> result = new ArrayList<>();
        for (com.vcco.weather.model.weather.Conditions condition : conditions) {
            Conditions newCondition = new Conditions(
                    0,
                    0,
                    condition.getId(),
                    condition.getCondition(),
                    condition.getDescription(),
                    condition.getIcon());
            result.add(newCondition);
        }
        return result;
    }

    private Coordinates convertCoordinatesToDBEntity(@NonNull com.vcco.weather.model.weather.Coordinates coordinates) {
        return new Coordinates(
                0,
                0,
                coordinates.getLongitude(),
                coordinates.getLatitude()
        );
    }

    private Rain convertRainToDBEntity(@Nullable com.vcco.weather.model.weather.Rain rain) {
        if (rain != null) {
            return new Rain(
                    0,
                    0,
                    rain.getAmount()
            );
        } else {
            return null;
        }
    }

    private Snow convertSnowToDBEntity(@Nullable com.vcco.weather.model.weather.Snow snow) {
        if (snow != null) {
            return new Snow(
                    0,
                    0,
                    snow.getAmount()
            );
        } else {
            return null;
        }
    }

    private SunTime convertSunTimeToDBEntity(@NonNull com.vcco.weather.model.weather.SunTime sunTime) {
        return new SunTime(
                0,
                0,
                sunTime.getCountry(),
                sunTime.getSunRiseTimestamp(),
                sunTime.getSunSetTimestamp()
        );
    }

    private Temperature convertTemperatureToDBEntity(com.vcco.weather.model.weather.Temperature temperature) {
        return new Temperature(
                0,
                0,
                temperature.getTemperature(),
                temperature.getFeelsLike(),
                temperature.getMinTemperature(),
                temperature.getMaxTemperature(),
                temperature.getHumidity(),
                temperature.getPressure(),
                temperature.getSeaLevel(),
                temperature.getGroundLevel()
        );
    }

    private Wind convertWindToDBEntity(com.vcco.weather.model.weather.Wind wind) {
        return new Wind(
                0,
                0,
                wind.getSpeed(),
                wind.getDirection(),
                wind.getGust()
        );
    }
}

