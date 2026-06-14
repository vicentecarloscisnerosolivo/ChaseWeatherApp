package com.vcco.weather.network.service

import com.vcco.weather.network.responses.OpenWeatherServiceResponses
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyFloat
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response


class OpenWeatherServiceTest {
    @Mock
    lateinit var service: OpenWeatherService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getCurrentWeatherServicesSuccess() {
        val mockResponse = OpenWeatherServiceResponses.getCurrentWeatherResponseCorrect()
        Mockito.`when`(service.getCurrentWeather(anyString(), anyString(), anyString()))
            .thenReturn(Observable.just(Response.success(mockResponse)))

        val response = service.getCurrentWeather("HomeTown", "1234", "metric")
        val testObserver = response.test()
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue { it.body()?.temperature == mockResponse.temperature }

    }

    @Test
    fun getCurrentWeatherServicesError() {
        val mockErrorResponse = OpenWeatherServiceResponses.getResponseErrorBody()
        Mockito.`when`(service.getCurrentWeather(anyString(), anyString(), anyString()))
            .thenReturn(
                Observable.just(Response.error(404, mockErrorResponse))
            )

        val response = service.getCurrentWeather("HomeTown", "1234", "metric")

        val testObserver = response.test()
        testObserver.assertComplete()
        testObserver.assertValue { it.code() == 404 }
        testObserver.assertValue { it.errorBody() == mockErrorResponse }
        testObserver.assertNoErrors()

    }


    @Test
    fun getInfoFromLocationSuccess() {
        val mockResult = OpenWeatherServiceResponses.getCurrentLocationSuccessResponses()
        Mockito.`when`(service.getReverseLocation(anyFloat(), anyFloat(), anyString()))
            .thenReturn(Observable.just(Response.success(mockResult)))

        val response = service.getReverseLocation(32.814f, -96.9489f, "1234")
        val testObserver = response.test()
        testObserver.assertComplete()
        testObserver.assertValue { it.body()?.size == mockResult.size }
        testObserver.assertNoErrors()
    }

    @Test
    fun getInfoFromLocationError() {
        Mockito.`when`(service.getReverseLocation(anyFloat(), anyFloat(), anyString()))
            .thenReturn(Observable.just(Response.success(emptyList())))

        val response = service.getReverseLocation(32.814f, -96.9489f, "1234")

        val testObserver = response.test()
        testObserver.assertComplete()
        testObserver.assertValue { it.body()?.isEmpty() == true }
        testObserver.assertNoErrors()
    }

    @Test
    fun getZipCodeResponse() {
        val mockResponse = OpenWeatherServiceResponses.getZipResponse()
        Mockito.`when`(service.getInfoFromZipCode(anyString(), anyString()))
            .thenReturn(Observable.just(Response.success(mockResponse)))

        val response = service.getInfoFromZipCode("75039", "1234")
        val testObserver = response.test()
        testObserver.assertComplete()
        testObserver.assertValue { it.body()?.latitude == mockResponse.latitude }
        testObserver.assertValue { it.body()?.longitude == mockResponse.longitude }
        testObserver.assertNoErrors()
    }

    @Test
    fun getZipCodeResponseError() {
        val mockErrorResponse = OpenWeatherServiceResponses.getResponseErrorBody()
        Mockito.`when`(service.getInfoFromZipCode(anyString(), anyString()))
            .thenReturn(Observable.just(Response.error(404, mockErrorResponse)))

        val response = service.getInfoFromZipCode("75039", "1234")
        val testObserver = response.test()
        testObserver.assertComplete()
        testObserver.assertValue { it.code() == 404 }
        testObserver.assertValue { it.errorBody() == mockErrorResponse }
        testObserver.assertNoErrors()
    }

    @Test
    fun getCityNamesResponse() {
        val mockResponse = OpenWeatherServiceResponses.getCurrentLocationSuccessResponses()
        Mockito.`when`(service.getInfoWithLocationName(anyString(), anyString()))
            .thenReturn(Observable.just(Response.success(mockResponse)))

        val response = service.getInfoWithLocationName("Irving", "1234")
        val testObserver = response.test()
        testObserver.assertComplete()
        testObserver.assertValue { it.body()?.size == mockResponse.size }
        testObserver.assertNoErrors()
    }

    @Test
    fun getCityNamesResponseError() {
        Mockito.`when`(service.getInfoWithLocationName(anyString(), anyString()))
            .thenReturn(Observable.just(Response.success(emptyList())))

        val response = service.getInfoWithLocationName("Irving", "1234")
        val testObserver = response.test()
        testObserver.assertComplete()
        testObserver.assertValue { it?.body()?.isEmpty() == true }
        testObserver.assertNoErrors()
    }

}