package com.truecaller.assignment.ui

import android.annotation.SuppressLint
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.truecaller.assignment.R
import com.truecaller.assignment.adapters.WordCountRecyclerAdapter
import com.truecaller.assignment.broadcaster.NetworkChangeReceiver
import com.truecaller.assignment.databinding.ActivityMainBinding
import com.truecaller.assignment.utilities.Utility
import com.truecaller.assignment.utilities.setSafeOnClickListener
import com.truecaller.assignment.utilities.viewGone
import com.truecaller.assignment.utilities.viewVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    enum class Sport { HIKE, RUN, TOURING_BICYCLE, E_TOURING_BICYCLE }

    data class Summary(val sport: Sport, val distance: Int)


    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: WordCountRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel


        val sportStats = listOf(
            Summary(Sport.HIKE, 444),
            Summary(Sport.RUN, 4444),
            Summary(Sport.TOURING_BICYCLE, 4444),
            Summary(Sport.E_TOURING_BICYCLE, 656)
        )

        val sport = sportStats.filter { it.sport != Sport.E_TOURING_BICYCLE }.maxByOrNull { it.distance }
        println(sport?.sport)

        //I used the filter method of kotlin rather than implement the for loop and filter it myself
        // Max by returns the null or a top sport if it finds the top result
        // With kotlin it is concise

//        // Register Network Receiver
//        registerReceiver(internetConnectionReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
//
//        initListeners()
//        setUpAdapter()
//        initObservers()
    }

    private fun initListeners() {
        binding.btnLoadURL.setSafeOnClickListener {
            if (Utility.getConnectivityStatus(this@MainActivity)) {
                binding.llTasks.viewVisible()
                binding.btnLoadURL.viewGone()
                viewModel.readURL()
            } else
                Toast.makeText(
                    this@MainActivity,
                    resources.getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun setUpAdapter() {
        adapter = WordCountRecyclerAdapter(viewModel.words, viewModel.wordsCount)
        binding.wordRecyclerView.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObservers() {
        viewModel.loadingStatus.observe(this) {
            if (it)
                binding.progressBar.viewVisible()
            else
                binding.progressBar.viewGone()
        }

        viewModel.dataError.observe(this) {
            if (it.isNotEmpty()) {
                binding.tvError.viewVisible()
                binding.tvError.text = it
            } else {
                binding.tvError.viewGone()
            }
        }

        viewModel.dataRead.observe(this) {
            if (it) {
                viewModel.getRequiredData()
            }
        }

        viewModel.countCompleted.observe(this) {
            if (it) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internetConnectionReceiver)
    }

    private var internetConnectionReceiver =
        NetworkChangeReceiver(object : NetworkChangeReceiver.ConnectionStatusChangeCallBack {
            override fun connectionLost() {
                binding.tvNoInternetConnection.viewVisible()
                binding.btnLoadURL.isEnabled = false
            }

            override fun connectionFound() {
                binding.tvNoInternetConnection.viewGone()
                binding.btnLoadURL.isEnabled = true
            }
        })

}