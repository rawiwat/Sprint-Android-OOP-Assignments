package com.lambdaschool.abstractionintpoly

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.lambdaschool.abstractionintpoly.model.Person
import com.lambdaschool.abstractionintpoly.model.Starship
import com.lambdaschool.abstractionintpoly.model.SwApiObject
import com.lambdaschool.abstractionintpoly.retrofit.StarWarsAPI
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(),ItemDetailFragment.DetailResponse {

    //override fun provideInfoForObject(info:String){
   //     Toast.makeText(this,"we got this info from the detail:\n$info", Toast.LENGTH_SHORT).show()
    //}
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    var swApiObjects = mutableListOf<SwApiObject>()
    private var viewAdapter: SimpleItemRecyclerViewAdapter? = null

    lateinit var starWarsAPI: StarWarsAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        swApiObjects = mutableListOf()

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        starWarsAPI = StarWarsAPI.Factory.create()

        setupRecyclerView(item_list as RecyclerView)
    }

    // TODO 6: S05M02-6 pull out fields from recyclerview construction and call our method
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        viewAdapter = SimpleItemRecyclerViewAdapter(this, swApiObjects, twoPane)
        recyclerView.adapter = viewAdapter

        if (isNetworkConnected()) {
            getData()
        } else {
            Toast.makeText(this@ItemListActivity, getString(R.string.no_network), Toast.LENGTH_LONG)
                .show()
        }
    }

    // TODO 7: S05M02-7 write a method to retrieve all the data
    private fun getData() {

        // Add people
        val personIds = mutableListOf(1,2,3,4,5,6)
        personIds.shuffle()
        personIds.forEach{
            getPerson(it)
        }
        // Add starships
        val starshipIds = mutableListOf(15,25,53,45,55,10)
        starshipIds.shuffle()
        starshipIds.forEach{
            getStarship(it)
        }
    }

    fun getPerson(id:Int){
        starWarsAPI.getPerson(id).enqueue(object :Callback<Person>{
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                progressBar.visibility = GONE
                if (response.isSuccessful){
                    val person = response.body()
                    person?.let {
                        it.id = id
                        it.category = DrawableResolver.CHARACTER
                        swApiObjects.add(person)
                        viewAdapter?.notifyItemInserted(swApiObjects.size - 1)
                    }
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                progressBar.visibility = GONE
                Toast.makeText(this@ItemListActivity,"Failure",Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getStarship(id: Int){
        starWarsAPI.getStarShip(id).enqueue(object :Callback<Starship>{
            override fun onResponse(call: Call<Starship>, response: Response<Starship>) {
                progressBar.visibility = GONE
                if (response.isSuccessful){
                    val starship = response.body()
                    starship?.let {
                        it.id = id
                        it.category = DrawableResolver.CHARACTER
                        swApiObjects.add(starship)
                        viewAdapter?.notifyItemChanged(swApiObjects.size - 1)
                    }
                }
            }
            override fun onFailure(call: Call<Starship>, t: Throwable) {
                progressBar.visibility = GONE
                Toast.makeText(this@ItemListActivity,"Failure",Toast.LENGTH_SHORT).show()
            }
        })
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private val values: List<SwApiObject>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private var lastPosition = -1

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as SwApiObject
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putSerializable(ItemDetailFragment.ARG_ITEM_ID, item)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item)
                    }

                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        v.context as Activity,
                        v.rootView.image_view,
                        ViewCompat.getTransitionName(v.rootView.image_view)
                    ).toBundle()

                    v.context.startActivity(intent, options)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val swApiObject = values[position]

            // TODO 8: S05M02-8 convert id to string to display
            holder.idView.text = "${swApiObject.id}"
            holder.nameView.text = swApiObject.name?:""
            // TODO 9: S05M02-9 bind data to new views
            holder.categoryView.text = swApiObject.category
            holder.imageView.setImageDrawable(holder.imageView.context.getDrawable(DrawableResolver.getDrawableId(swApiObject.category,swApiObject.id)))

            with(holder.itemView) {
                tag = swApiObject
                setOnClickListener(onClickListener)

                setEnterAnimation(holder.parentView, position)
            }
        }

        override fun getItemCount() = values.size

        private fun setEnterAnimation(viewToAnimate: View, position: Int) {
            if (position > lastPosition) {
                val animation = AnimationUtils.loadAnimation(
                    viewToAnimate.context,
                    android.R.anim.slide_in_left
                )
                viewToAnimate.startAnimation(animation)
                lastPosition = position
            }
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val nameView: TextView = view.name
            val categoryView: TextView = view.category
            val imageView: ImageView = view.image_view
            val parentView: View = view.parent_view
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    override fun providedInfo(info: String) {
        Toast.makeText(this,"got info from the detail:$info",Toast.LENGTH_SHORT).show()
    }

    // TODO 14: Implement the Fragment interface
}
