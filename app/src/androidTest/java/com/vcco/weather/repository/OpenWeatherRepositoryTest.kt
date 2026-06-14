package com.vcco.weather.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vcco.weather.model.geoconfig.GeocodeResponse
import com.vcco.weather.model.weather.CurrentWeatherResponse
import com.vcco.weather.model.zip.ZipResponse
import com.vcco.weather.network.apiHelper.OpenWeatherApiHelper
import com.vcco.weather.network.apiHelper.OpenWeatherApiHelperImp
import com.vcco.weather.network.repository.OpenWeatherRepository
import com.vcco.weather.network.service.OpenWeatherService
import com.vcco.weather.repository.responses.OpenWeatherServiceResponses
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        hiltRule.inject()
        apiHelper = OpenWeatherApiHelperImp(service)
        repository = OpenWeatherRepository(apiHelper)
    }

    @Test
    fun useGeCurrentWeatherRepositorySuccess() {
        val currentResponse = OpenWeatherServiceResponses.getCurrentWeatherResponseCorrect()
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
        val currentLocationResponse =
            OpenWeatherServiceResponses.getCurrentLocationSuccessResponses()
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
        val zipResponse = OpenWeatherServiceResponses.getZipResponse()
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
        val currentLocationResponse =
            OpenWeatherServiceResponses.getCurrentLocationSuccessResponses()
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
        val responseErrorBody = OpenWeatherServiceResponses.getResponseErrorBody()
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
        val responseErrorBody = OpenWeatherServiceResponses.getResponseErrorBody()
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
