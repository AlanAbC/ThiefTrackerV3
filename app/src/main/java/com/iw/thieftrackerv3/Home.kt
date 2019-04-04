package com.iw.thieftrackerv3

import android.Manifest
import android.animation.Animator
import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.NavigationView
import android.support.transition.Slide
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.iw.thieftrackerv3.Adapters.AdapterFilters
import com.iw.thieftrackerv3.Config.Session
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import java.util.ArrayList
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap


class Home : AppCompatActivity(), AdapterFilters.InterfaceFilters {

    override fun Filter(filter: String) {
        txtFilter!!.text = filter
        var  param: RelativeLayout.LayoutParams = filterselectedContainer!!.layoutParams as RelativeLayout.LayoutParams
        val r = resources
        val dip = 40f
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r.displayMetrics
        )
        param.height = px.toInt()
        filterselectedContainer!!.layoutParams = param
        arrow!!.animate().rotation(360F).start()

        (filterselectedContainer as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        list_filter!!.visibility = View.GONE
    }

    private var mapView: MapView? = null
    internal lateinit var locationManager: LocationManager
    private var bsb_near_points: LinearLayout? = null
    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var user_marker: MarkerOptions
    private var btnMenu: RelativeLayout? = null
    private var btnUserLocation: RelativeLayout? = null
    private var filterselectedContainer: RelativeLayout? = null
    private var btnCloseFilter: RelativeLayout? = null
    private var arrow: ImageView? = null
    private var list_filter: RecyclerView? = null
    private var txtFilter: TextView? = null
    private var logout: TextView? = null

    private var btnSearch: RelativeLayout? = null
    private var nav: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var close_bsb:ImageView? = null
    private var inputsearch:EditText? = null
    private lateinit var mapboxMap: MapboxMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mapbox Access token
        Mapbox.getInstance(getApplicationContext(),getString(R.string.mapbox_access_token))
        setContentView(R.layout.activity_home)
        supportActionBar!!.hide()

        mapView = findViewById(R.id.mapView)
        bsb_near_points = findViewById(R.id.bsb_near_points)
        btnMenu = findViewById(R.id.btnMenu)
        nav = findViewById(R.id.navigation)
        drawerLayout = findViewById(R.id.dLayout)
        close_bsb = findViewById(R.id.close_bsb)
        btnUserLocation = findViewById(R.id.btnUserLocation)
        btnSearch = findViewById(R.id.btnSearch)
        inputsearch = findViewById(R.id.inputsearch)
        btnCloseFilter = findViewById(R.id.btnCloseFilter)
        filterselectedContainer = findViewById(R.id.filterselectedContainer)
        arrow = findViewById(R.id.arrow)
        list_filter = findViewById(R.id.list_filter)
        txtFilter = findViewById(R.id.txtFilter)
        logout = findViewById(R.id.logout)

        val linearLayoutManager = LinearLayoutManager(this)
        list_filter!!.layoutManager = linearLayoutManager
        list_filter!!.adapter = AdapterFilters(this, this)

        sheetBehavior = BottomSheetBehavior.from<LinearLayout>(bsb_near_points)
        sheetBehavior.state =BottomSheetBehavior.STATE_HIDDEN
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { mapboxMap ->

            mapboxMap.setStyle(Style.MAPBOX_STREETS) {

                this.mapboxMap = mapboxMap
                mapboxMap.setOnMarkerClickListener { marker ->

                    // Show a toast with the title of the selected marker
                    if(marker.title.equals("User")){
                        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }

                    true
                }

                mapboxMap.uiSettings.isCompassEnabled = false
                getUserLocation()
                setOnClicks()

            }

        }




    }
    fun setOnClicks(){
        logout!!.setOnClickListener{
            val session: Session = Session(applicationContext)
            session.closeSession()
            val intent = Intent(this, Splash::class.java)
            startActivity(intent)
            finish()
        }
        nav!!.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filtrar -> {
                   btnSearch!!.visibility = View.GONE
                    btnCloseFilter!!.visibility = View.VISIBLE
                    filterselectedContainer!!.visibility = View.VISIBLE
                    (btnSearch as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                    (btnCloseFilter as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                }
            }

            drawerLayout!!.closeDrawer(GravityCompat.START)
             true
        })
        filterselectedContainer!!.setOnClickListener{
            if(list_filter!!.visibility == View.VISIBLE){
                var  param: RelativeLayout.LayoutParams = filterselectedContainer!!.layoutParams as RelativeLayout.LayoutParams
                val r = resources
                val dip = 40f
                val px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    r.displayMetrics
                )
                param.height = px.toInt()
                filterselectedContainer!!.layoutParams = param
                arrow!!.animate().rotation(360F).start()

                (filterselectedContainer as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                list_filter!!.visibility = View.GONE
            }else{
                var  param: RelativeLayout.LayoutParams = filterselectedContainer!!.layoutParams as RelativeLayout.LayoutParams
                val r = resources
                val dip = 300f
                val px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    r.displayMetrics
                )
                param.height = px.toInt()
                filterselectedContainer!!.layoutParams = param
                arrow!!.animate().rotation(180F).start()

                (filterselectedContainer as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                list_filter!!.visibility = View.VISIBLE
            }

        }
        btnCloseFilter!!.setOnClickListener{
            if(list_filter!!.visibility == View.VISIBLE){
                arrow!!.animate().rotation(360F).start()
                var  param: RelativeLayout.LayoutParams = filterselectedContainer!!.layoutParams as RelativeLayout.LayoutParams
                val r = resources
                val dip = 40f
                val px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    r.displayMetrics
                )
                param.height = px.toInt()
                filterselectedContainer!!.layoutParams = param
                (btnSearch as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                (btnCloseFilter as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

                val handler = Handler()
                handler.postDelayed(Runnable {
                    btnSearch!!.visibility = View.VISIBLE
                    btnCloseFilter!!.visibility = View.GONE
                    filterselectedContainer!!.visibility = View.GONE
                }, 600)
            }else{
                btnSearch!!.visibility = View.VISIBLE
                btnCloseFilter!!.visibility = View.GONE
                filterselectedContainer!!.visibility = View.GONE
            }

        }
        btnMenu!!.setOnClickListener{
            drawerLayout!!.openDrawer(GravityCompat.START)
        }
        btnUserLocation!!.setOnClickListener{
            getUserLocation()
        }
        close_bsb!!.setOnClickListener{
           sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        btnSearch!!.setOnClickListener{
            val r = resources
            val dip = 40f
            val px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.displayMetrics
            )
            val dipmargintop = 10f
            val pxmargintop = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dipmargintop,
                r.displayMetrics
            )
            val dipmarginend = 15f
            val pxmarginend = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dipmarginend,
                r.displayMetrics
            )
            val dipmarginstart = 20f
            val pxmarginstart = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dipmarginstart,
                r.displayMetrics
            )
            if(inputsearch!!.visibility == View.VISIBLE){
                if(inputsearch!!.text.toString().equals("")){
                    inputsearch!!.visibility = View.GONE
                    inputsearch!!.setText("")
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(inputsearch!!.getWindowToken(), 0)

                    var  param: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(px.toInt(), px.toInt())
                    param.topMargin = pxmargintop.toInt()
                    param.marginEnd = pxmarginend.toInt()
                    param.addRule(RelativeLayout.ALIGN_PARENT_END)
                    btnSearch!!.layoutParams = param

                    (btnSearch as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                }else{
                    val coder = Geocoder(applicationContext)
                    val address = coder.getFromLocationName(inputsearch!!.text.toString(), 1)
                    val location = address[0]
                    if (location != null) {
                        val lat = location.latitude
                        val lon = location.longitude
                        val coordenadas = LatLng(lat, lon)

                        val position = CameraPosition.Builder()
                            .target(coordenadas)
                            .zoom(14.0)
                            .tilt(30.0)
                            .build()

                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000)
                        inputsearch!!.visibility = View.GONE
                        inputsearch!!.setText("")
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(inputsearch!!.getWindowToken(), 0)

                        var  param: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(px.toInt(), px.toInt())
                        param.topMargin = pxmargintop.toInt()
                        param.marginEnd = pxmarginend.toInt()
                        param.addRule(RelativeLayout.ALIGN_PARENT_END)
                        btnSearch!!.layoutParams = param

                        (btnSearch as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                    } else {
                        Toast.makeText(applicationContext, "No se encontró la dirección", Toast.LENGTH_LONG).show()
                    }
                }




            }else{
                inputsearch!!.visibility = View.VISIBLE
                var  param: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, px.toInt())
                param.marginStart = pxmarginstart.toInt()
                param.topMargin = pxmargintop.toInt()
                param.marginEnd = pxmarginend.toInt()
                param.addRule(RelativeLayout.END_OF, R.id.btnMenu)
                btnSearch!!.layoutParams = param

                (btnSearch as ViewGroup).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            }

        }
    }
    //Funcion que se encarg de obtneer la ubicacion actual del usuario a traves de gps
    @SuppressLint("MissingPermission")
    fun getUserLocation() {
                //userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //List<String> providers = locationManager.getProviders(true);
        var providers = arrayListOf<String>()
                providers.add("network")
                providers.add("passive")
                providers.add("gps")
                var bestLocation: Location? = null

                for(value in providers){
                    val location: Location = locationManager.getLastKnownLocation(value)
                    if(bestLocation == null || location.accuracy < bestLocation.accuracy){
                        bestLocation = location
                    }
                }

        if (bestLocation != null) {
            setUserPoint(bestLocation)
        }

    }

    fun setUserPoint(location: Location){
        val position = CameraPosition.Builder()
            .target(LatLng(location.latitude, location.longitude))
            .zoom(14.0)
            .tilt(30.0)
            .build()

        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 5000)

        //val icon = IconFactory.getInstance(this)
        //icon.fromResource(R.drawable. R.drawable.blue_marker)

        // Add the marker to the map
        var icono = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.user_marker)
        var icon = icono.copy(Bitmap.Config.ARGB_8888, true)
        user_marker = MarkerOptions().title("User")
            .position(LatLng(location.latitude, location.longitude)).icon(IconFactory.getInstance(this).fromBitmap(Bitmap.createScaledBitmap(icon, 60, 60, false)))
        mapboxMap?.addMarker(user_marker)

    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}
