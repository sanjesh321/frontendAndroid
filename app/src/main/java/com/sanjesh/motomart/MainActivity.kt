package com.sanjesh.motomart

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sanjesh.motomart.Fragment.CategoryFragment
import com.sanjesh.motomart.Fragment.HomeFragment
import com.sanjesh.motomart.Fragment.SettingsFragment
import com.sanjesh.motomart.Notification.NotificationSender

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private var sensor2: Sensor? = null
    private var sensor3: Sensor? = null
    private lateinit var navigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if (!checkSensor()) {
            return
        } else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensor3 = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, sensor2, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, sensor3, SensorManager.SENSOR_DELAY_NORMAL)
        }
        navigation = findViewById(R.id.dropdown)
        val homeFragments = HomeFragment()
        val categoryFragment = CategoryFragment()
        val settings = SettingsFragment()


        getFragment(homeFragments)
        navigation.setOnNavigationItemReselectedListener() {
            when (it.itemId) {
                R.id.home -> getFragment(homeFragments)
                R.id.partPickerFragment -> getFragment(categoryFragment)
                R.id.SettingFragment -> getFragment(settings)
            }
        }
    }

    private fun checkSensor():Boolean
    {
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null)
        {
            flag = false
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null)
        {
            flag = false
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null)
        {
            flag = false
        }
        return flag
    }


    fun getFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_PROXIMITY)
        {
            val values = event!!.values[0]
            if(values <= 4)
            {
                val logOut = Logout(this,this)
                logOut.logout()
            }
        }
        if(event!!.sensor.type == Sensor.TYPE_LIGHT)
        {
            val values = event!!.values[0]
            if(values > 20000)
            {
                NotificationSender(this,"WARNING: High Light","").createHighPriority()
            }
        }
        if(event!!.sensor.type == Sensor.TYPE_ACCELEROMETER)
        {
            val values = event!!.values[0]
            if(values > 7)
            {
                val intent = Intent(this@MainActivity,LogInAct::class.java)

                startActivity(intent)
            }
            else if(values > 7 && values < 10)
            {
                val intent = Intent(this@MainActivity,SignUp::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}