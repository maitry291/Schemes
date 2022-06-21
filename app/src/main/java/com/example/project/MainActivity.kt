package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.adapters.Adapter
import com.example.project.adapters.SchemesAdapter
import com.example.project.models.RegistrationDetails
import com.example.project.models.SchemesTable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var recycler:RecyclerView
    lateinit var sa:SchemesAdapter
    val db=Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val category=findViewById<Spinner>(R.id.spinner_category)

        val list1 = arrayListOf<String>(
            "Department",
            "Department of Education",
            "Department of Health",
            "Department of Empowering Women",
            "Department of Agriculture",
            "Department of Electricity",
            "Department of Housing",
            "Department of Senior Citizen",
            "Department of Employment"
        )
        val adapter1 = Adapter(list1, this)
        category.adapter = adapter1

        val list2 = arrayListOf<String>("Gender","M", "F", "OTHER")
        val adapter2 = Adapter(list2, this)
        findViewById<Spinner>(R.id.spinner_gender).adapter = adapter2

        val list3 = arrayListOf<String>("Income","< 50,000", "50,000 - 1,00,000", "1,00,000 - 5,00,000", "5,00,000 - 10,00,000","> 10,00,000","Rather not say")
        val adapter3 = Adapter(list3, this)
        findViewById<Spinner>(R.id.spinner_salary).adapter = adapter3

        val list4 = arrayListOf<String>("Age","00-18","18-30","30-45","45-60","60-99")
        val adapter4 = Adapter(list4, this)
        findViewById<Spinner>(R.id.spinner_age).adapter = adapter4

        val list5 = arrayListOf<String>("Disability","yes","no")
        val adapter5 = Adapter(list5, this)
        findViewById<Spinner>(R.id.spinner_disability).adapter = adapter5

        val list6 = arrayListOf<String>("Caste","all","OBC","SC","ST")
        val adapter6 = Adapter(list6, this)
        findViewById<Spinner>(R.id.spinner_caste).adapter = adapter6


        //toolbar.title="Hello,"+FirebaseAuth.getInstance().currentUser?.uid+"👋"
        setSupportActionBar(toolbar as Toolbar)

        //some default schemes
        val default=ArrayList<SchemesTable>()
        default.add(SchemesTable("234","Department of Education",339,"Girls hostel scheme","central","\t\tThis is a new Centrally " +
                "Sponsored Scheme launched in 2008-09 and is being implemented from 2009-10 to set up a 100-bedded girls Hostel " +
                "in each of 3479 Educationally Backward Blocks (EBBs) in the country. The Scheme has replaced the earlier NGO driven" +
                " Scheme for construction and running of Girls Hostels for Students of Secondary and Higher Secondary Schools, " +
                "under which assistance was provided to voluntary organisations for running Girls Hostels.","Female",40000,"no","all",
        9,25))

        default.add(SchemesTable("2","Department of Housing",120,"Pradhan Mantri Gramin Awas Yojana","central","The main aim of the PMAY-G scheme is to provide pucca " +
                "house with some of the basic amenities. This scheme is meant for people who do not own a house and " +
                "people who live in kutcha houses or houses which are severely damaged. At present, the minimum size of the houses " +
                "to be built under the PMAY-G scheme has been increased to 25 sq. mt. from 20 sq. mt.","Female,Male,Other",20000,"no","all",30,55))

        default.add(SchemesTable("4","Department of Agriculture",238,"Gramin Bhandaran Yojna","state","To create scientific storage " +
                "capacity with allied facilities in rural areas.","Female,Male",10000,"no","SC,OBC,ST",20,70))

        default.add(SchemesTable("5","Department of empowering women",457,"Working Women Hostel","central","Through this women empowerment scheme, the Government provides" +
                " grant-in-aid for construction and new hostel buildings and extension of an existing building in rented premises."
        ,"Female",30000,"no","all",13,50))

        default.add(SchemesTable("6","Department of electricity",564,"Karnataka Surya Raitha Scheme","state","To provide quality power on demand to all consumers, Govt. of Karnataka would undertake." +
                " Unbinding of the transmission and distribution functions. Formation of distribution companies." +
                " Privatisation of distribution of electricity in a time-bound manner. Karnataka would work to achieve 100% electrification of" +
                " villages and hamlets and extensive coverage of rural households. Karnataka will undetake energy audit at all levels as per the programme" +
                " to be mutually agreed upon in order to reduce system losses. Having set up the SERC, the State Govt. would provide full support to " +
                "the Commission to enable it to discharge its statutory responsibilities.","Female,Male,Other",50000,"yes","all",24,90))


        //recycler initialize
        recycler=findViewById(R.id.recyclerView)
        recycler.layoutManager=LinearLayoutManager(this)
        recycler.adapter=SchemesAdapter(default,this)


        records.setOnClickListener {
            pb.isVisible=true

            val spindept =category.selectedItemPosition
            val spinGender=spinner_gender.selectedItemPosition
            val spinIncome=spinner_salary.selectedItemPosition
            val spinlage=spinner_age.selectedItemPosition
            val spinuage=spinner_age.selectedItemPosition
            val spinDis=spinner_disability.selectedItemPosition
            val spinCaste=spinner_caste.selectedItemPosition

            val a=list1[spindept]
            val b=list2[spinGender]
            var c=0  //higher limit
            //var h=0  //lower limit
            val d=list4[spinlage]
            val e=list4[spinuage]
            val f=list5[spinDis]
            val g=list6[spinCaste]
            //Log.d("dept",dept.toString())

            if(spinIncome==1){
                c=50000
            }
            if(spinIncome==2){
                c=100000
            }
            if(spinIncome==3){
                c=500000
            }
            if(spinIncome==4){
                c=1000000
            }
            if(spinIncome==5){
                c=Integer.MAX_VALUE
            }

            val apiSet=ApiController.getClient().create(apiSet::class.java)
            val call : Call<List<SchemesTable>> = apiSet.getData()

            call.enqueue(object : Callback<List<SchemesTable>>{
                override fun onResponse(
                    call: Call<List<SchemesTable>>,
                    response: Response<List<SchemesTable>>
                ) {
                    //this list has the all data from table acc to query written in php file
                    val data: List<SchemesTable>? = response.body()

                    //to filer schemes from list we will apply loop here and store the filtered query in list
                    //tht list will link to recycler and showed to user
                    val ans=ArrayList<SchemesTable>()

                    for(i in data!!){
                        //from database
                        val dept=i.Department
                        val gender=i.gender
                        val income=i.income
                        val lage=i.lower_age
                        val uage=i.upper_age
                        val dis=i.disabilities
                        val caste=i.caste
                        //if user has not selected any filter then show all for that filter

                        val userlage=Integer.parseInt(d.subSequence(0,2) as String)
                        val useruage=Integer.parseInt(d.subSequence(3,5) as String)
                        //if ith schemes department matches with the a which is selected by user

                        if(a==dept && gender.contains(b) &&(income<=c||income==0) && ((lage>=userlage
                            && uage<=useruage)||(lage<=userlage&&uage>=useruage))
                            && dis==f && caste.contains(g)){
                            ans.add(i)
                            Log.d("gender", (Integer.parseInt(d.subSequence(0,2) as String)>=lage).toString())
                        }
                    }
                    if(ans.size==0){
                        pb.isVisible=false
                        head.text="--No schemes matched your data"
                        Toast.makeText(this@MainActivity, "No schemes found as per your data", Toast.LENGTH_SHORT).show()
                        sa = SchemesAdapter(ans, this@MainActivity)
                        recycler.layoutManager = LinearLayoutManager(this@MainActivity)
                        recycler.adapter = sa
                    }
                    else {
                        pb.isVisible=false
                        //edit the textview
                        head.text="--Applicable schemes"
                        for (i in ans) {
                            sa = SchemesAdapter(ans, this@MainActivity)
                            recycler.layoutManager = LinearLayoutManager(this@MainActivity)
                            recycler.adapter = sa
                        }
                    }
                }

                override fun onFailure(call: Call<List<SchemesTable>>, t: Throwable) {
                    Log.d("error",t.toString())
                    Toast.makeText(this@MainActivity, "Failed $t", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    // Inflating menu to Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        // get name from firebase
        db.getReference("Users").child(FirebaseAuth.getInstance().
        uid.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val model=snapshot.getValue(RegistrationDetails::class.java)
                if(model!!.email=="maitrymakwana196@gmail.com"){
                    menu!!.findItem(R.id.i3).isVisible=true
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Code to performed when menuItem clicked
            R.id.i1 ->{
                startActivity(Intent(this,UserDetails::class.java))
            }
            R.id.i2 ->{
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, Login::class.java))
                overridePendingTransition(
                    androidx.appcompat.R.anim.abc_fade_in,
                    androidx.appcompat.R.anim.abc_fade_out
                )
            }
            R.id.i3->{
                startActivity(Intent(this,Accessdatabase::class.java))
            }

        }
        return true
    }
}