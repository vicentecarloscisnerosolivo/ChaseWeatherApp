package com.vcco.weather.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.vcco.weather.data.utils.PreferencesConstants
import com.vcco.weather.network.responses.OpenWeatherServiceResponses
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class PreferenceTesting {
    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var sp: Preference

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        Mockito
            .`when`(mockContext.getSharedPreferences(anyString(), anyInt()))
            .thenReturn(mockSharedPreferences)
        Mockito.`when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor)

        sp = Preference(mockContext)
    }

    @Test
    fun saveNewUnitCorrect() {
        val newUnit = "Metric"

        sp.saveUnitValue(newUnit)
        verify(mockEditor).putString(PreferencesConstants.USER_UNIT_PREFERENCE, newUnit)
        verify(mockEditor).apply()
    }

    @Test
    fun getUnitDefaultCorrectValue() {
        Mockito
            .`when`(
                mockSharedPreferences.getString(
                    PreferencesConstants.USER_UNIT_PREFERENCE,
                    PreferencesConstants.DEFAULT_UNIT,
                ),
            ).thenReturn(PreferencesConstants.DEFAULT_UNIT)

        val defaultUnit = sp.unitValue
        assertEquals(defaultUnit, PreferencesConstants.DEFAULT_UNIT)
    }

    @Test
    fun getUpdatedUnitCorrectValue() {
        val savedUnit = "Metric"
        Mockito
            .`when`(
                mockSharedPreferences.getString(
                    PreferencesConstants.USER_UNIT_PREFERENCE,
                    PreferencesConstants.DEFAULT_UNIT,
                ),
            ).thenReturn(savedUnit)

        val unit = sp.unitValue
        assertEquals(unit, savedUnit)
    }

    @Test
    fun saveNewSearchCorrectValue() {
        val newSearch =
            Gson().toJson(OpenWeatherServiceResponses.getCurrentWeatherResponseCorrect())

        sp.saveSearch(newSearch)
        verify(mockEditor).putString(PreferencesConstants.LAST_WEATHER_SEARCH, newSearch)
        verify(mockEditor).apply()
    }

    @Test
    fun getNewSearchEmptyCorrectValue() {
        Mockito
            .`when`(
                mockSharedPreferences.getString(
                    PreferencesConstants.LAST_WEATHER_SEARCH,
                    PreferencesConstants.EMPTY_STRING,
                ),
            ).thenReturn(PreferencesConstants.EMPTY_STRING)

        val defaultSearchValue = sp.lastSearch
        assertEquals(defaultSearchValue, PreferencesConstants.EMPTY_STRING)
    }

    @Test
    fun getUpdatedLastSearchCorrectValue() {
        val savedSearch =
            Gson().toJson(OpenWeatherServiceResponses.getCurrentWeatherResponseCorrect())
        Mockito
            .`when`(
                mockSharedPreferences.getString(
                    PreferencesConstants.LAST_WEATHER_SEARCH,
                    PreferencesConstants.EMPTY_STRING,
                ),
            ).thenReturn(savedSearch)

        val unit = sp.lastSearch
        assertEquals(unit, savedSearch)
    }
}
