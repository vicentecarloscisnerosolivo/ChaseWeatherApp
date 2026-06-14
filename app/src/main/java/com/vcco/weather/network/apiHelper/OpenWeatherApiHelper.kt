package com.vcco.weather.network.apiHelper

import com.vcco.weather.model.geoconfig.GeocodeResponse
import com.vcco.weather.model.weather.CurrentWeatherResponse
import com.vcco.weather.model.zip.ZipResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response

/**
 * Helper used to call OpenWeatherService
 */
/*
* Continue with the bridge, here will create the interface to call OpenWeatherService using
* OpenWeatherApiHelper as Proxy as to control access
*/
interface OpenWeatherApiHelper {
    /**
     * Request Current Weather Information from Requested Location
     * @param query: String
     * @param units: String
     *
     * @return Weather response
     */
    fun getWeatherCurrentLocation(
        query: String,
        units: String
    ): Observable<Response<CurrentWeatherResponse>>

    /**
     * Get the location info from the requested Name
     * @param query: String
     *
     * @return Observable<Response<List<GeocodeResponse>>>
     */
    fun getLocationInfoFromName(query: String): Observable<Response<List<GeocodeResponse>>>

    /**
     * Get Location Coordinates with Zip Code and country Code
     * @param zip: String -> Requested Zip with country code
     *
     * @return  Observable<Response<ZipResponse>>
     */
    fun getInfoFromZipCode(zip: String): Observable<Response<ZipResponse>>

    /**
     * Get Info from Requested Coordinates
     * @param latitude: Float
     * @param longitude: Float
     *
     * @return Observable<Response<List<GeocodeResponse>>>
     */
    fun getReverseLocation(
        latitude: Float,
        longitude: Float
    ): Observable<Response<List<GeocodeResponse>>>
}