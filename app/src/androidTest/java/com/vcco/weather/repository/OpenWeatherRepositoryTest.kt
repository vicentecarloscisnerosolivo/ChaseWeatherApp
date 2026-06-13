package com.vcco.weather.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vcco.weather.model.geoconfig.GeocodeResponse
import com.vcco.weather.model.weather.Clouds
import com.vcco.weather.model.weather.Conditions
import com.vcco.weather.model.weather.Coordinates
import com.vcco.weather.model.weather.CurrentWeatherResponse
import com.vcco.weather.model.weather.Rain
import com.vcco.weather.model.weather.Snow
import com.vcco.weather.model.weather.SunTime
import com.vcco.weather.model.weather.Temperature
import com.vcco.weather.model.weather.Wind
import com.vcco.weather.model.zip.ZipResponse
import com.vcco.weather.network.apiHelper.OpenWeatherApiHelper
import com.vcco.weather.network.apiHelper.OpenWeatherApiHelperImp
import com.vcco.weather.network.repository.OpenWeatherRepository
import com.vcco.weather.network.service.OpenWeatherService
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyFloat
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

/**
 * Instrumented test for OpenWeatherRepository
 */
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class OpenWeatherRepositoryTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Mock
    lateinit var service: OpenWeatherService

    private lateinit var apiHelper: OpenWeatherApiHelper
    private lateinit var repository: OpenWeatherRepository

    // models response
    private lateinit var currentResponse: CurrentWeatherResponse
    private lateinit var currentLocationResponse: List<GeocodeResponse>
    private lateinit var zipResponse: ZipResponse

    //error response
    private lateinit var errorJson: String
    private lateinit var responseErrorBody: ResponseBody

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        hiltRule.inject()
        apiHelper = OpenWeatherApiHelperImp(service)
        repository = OpenWeatherRepository(apiHelper)

        currentResponse = CurrentWeatherResponse(
            coordinates = Coordinates(longitude = -96.9489f, latitude = 32.814f),
            conditions = listOf(Conditions(1, "clear", "clear", "01d")),
            temperature = Temperature(30.0f, 31.1f, 20.0f, 32.0f, 60),
            visibility = 10000,
            wind = Wind(12.4f, 2),
            clouds = Clouds(10),
            rain = Rain(2.5f),
            snow = Snow(2.5f),
            dataCalculation = 1727377390,
            sunTime = SunTime("US", 1727353131, 1727396337),
            timeZone = -18000,
            id = 4700168,
            name = "Irving"
        )
        currentLocationResponse = listOf(
            GeocodeResponse(
                longitude = -96.9489f, latitude = 32.814f, name = "Irving",
                localNames = mapOf("Guadalajara" to "en"), country = "US", state = "Texas"
            )
        )
        zipResponse = ZipResponse(
            zipCode = "75039",
            name = "Irving",
            longitude = -96.9489f,
            latitude = 32.814f,
            country = "US"
        )
        errorJson = """{"code": "404", "message": "not found"}"""
        responseErrorBody = errorJson.toResponseBody("application/json".toMediaTypeOrNull())
    }

    @Test
    fun useGeCurrentWeatherRepositorySuccess() {
        runBlocking {
            Mockito.`when`(service.getCurrentWeather(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(Response.success(currentResponse)))

            val response = repository.getCurrentWeatherFromLocation("HomeTown", "metric")
            val testObserver = TestObserver<Response<CurrentWeatherResponse>>()
            response.subscribe(testObserver)
            testObserver.assertComplete()
            testObserver.assertValue { it.body()?.temperature == currentResponse.temperature }
            testObserver.assertNoErrors()
        }
    }

    @Test
    fun userGetInfoFromLocation() {
        Mockito.`when`(service.getReverseLocation(anyFloat(), anyFloat(), anyString()))
            .thenReturn(Observable.just(Response.success(currentLocationResponse)))

        val response = repository.getLocationInfoFromCoordinates(32.814f, -96.9489f)
        val testObserver = TestObserver<Response<List<GeocodeResponse>>>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertValue { it.body()?.size == currentLocationResponse.size }
        testObserver.assertNoErrors()
    }

    @Test
    fun userGetZipCodeResponse() {
        Mockito.`when`(service.getInfoFromZipCode(anyString(), anyString()))
            .thenReturn(Observable.just(Response.success(zipResponse)))

        val response = repository.getInfoFromZipCode("75039")
        val testObserver = TestObserver<Response<ZipResponse>>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertValue { it.body()?.latitude == zipResponse.latitude }
        testObserver.assertValue { it.body()?.longitude == zipResponse.longitude }
        testObserver.assertNoErrors()
    }

    @Test
    fun userGetCityNamesResponse() {
        Mockito.`when`(service.getInfoWithLocationName(anyString(), anyString()))
            .thenReturn(Observable.just(Response.success(currentLocationResponse)))

        val response = repository.getLocationInfoFromName("Irving")
        val testObserver = TestObserver<Response<List<GeocodeResponse>>>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertValue { it.body()?.size == currentLocationResponse.size }
        testObserver.assertNoErrors()
    }

    @Test
    fun useGeCurrentWeatherRepositoryError() {
        runBlocking {
            Mockito.`when`(service.getCurrentWeather(anyString(), anyString(), anyString()))
                .thenReturn(
                    Observable.just(Response.error(404, responseErrorBody))
                )

            val response = repository.getCurrentWeatherFromLocation("HomeTown", "metric")
            val testObserver = TestObserver<Response<CurrentWeatherResponse>>()
            response.subscribe(testObserver)
            testObserver.assertComplete()
            testObserver.assertValue { it.code() == 404 }
            testObserver.assertValue { it.errorBody() == responseErrorBody }
            testObserver.assertNoErrors()
        }
    }

    @Test
    fun userGetInfoFromLocationError() {
        Mockito.`when`(service.getReverseLocation(anyFloat(), anyFloat(), anyString()))
            .thenReturn(Observable.just(Response.success(emptyList())))

        val response = repository.getLocationInfoFromCoordinates(32.814f, -96.9489f)
        val testObserver = TestObserver<Response<List<GeocodeResponse>>>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertValue { it.body()?.isEmpty() == true }
        testObserver.assertNoErrors()
    }

    @Test
    fun userGetZipCodeResponseError() {
        Mockito.`when`(service.getInfoFromZipCode(anyString(), anyString()))
            .thenReturn(Observable.just(Response.error(404, responseErrorBody)))

        val response = repository.getInfoFromZipCode("75039")
        val testObserver = TestObserver<Response<ZipResponse>>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertValue { it.code() == 404 }
        testObserver.assertValue { it.errorBody() == responseErrorBody }
        testObserver.assertNoErrors()
    }

    @Test
    fun userGetCityNamesResponseError() {
        Mockito.`when`(service.getInfoWithLocationName(anyString(), anyString()))
            .thenReturn(Observable.just(Response.success(emptyList())))

        val response = repository.getLocationInfoFromName("Irving")
        val testObserver = TestObserver<Response<List<GeocodeResponse>>>()
        response.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertValue { it?.body()?.isEmpty() == true }
        testObserver.assertNoErrors()
    }
}
