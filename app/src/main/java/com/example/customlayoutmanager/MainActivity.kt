package com.example.customlayoutmanager

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.customlayoutmanager.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Daten nach Kategorien sortiert
        val data = items.sortedBy { it.category }
        binding.itemsVP.adapter = ItemAdapter(data)

        //Kategorien gehardcoded, kann zu Problemen führen wenn:
        //1. kein Item in einer Kategorie vorhanden ist
        //2. Ein Item eine Kategorie hat die nicht in der Liste ist
        val categories = listOf("A", "B", "C", "D", "E", "F")

        //Kategorien dynamisch aus den Daten auslesen, kann zu Problemen führen wenn:
        //Zu viele verschiedene Kategorien existieren
//        val categories = items.map { it.category }.toSet().toList().sorted()


        //Tabs erzeugen
        for (category in categories) {
            binding.tabsTL.addTab(binding.tabsTL.newTab().setText(category))
        }

        binding.tabsTL.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val clickedCategory = tab!!.text.toString()
                val firstItemOfCategory = data.first { it.category == clickedCategory }
                val indexOfFirstItemOfCategory = data.indexOf(firstItemOfCategory)

                //Das if ist die Extra Logik um zu verhindern dass gescrollt wird wenn der Tab durch den PageListener selected wird
                val currentItem = data[binding.itemsVP.currentItem]
                if (currentItem.category != clickedCategory) {
                    binding.itemsVP.setCurrentItem(indexOfFirstItemOfCategory, true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.itemsVP.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                Log.d("registerOnPageChangeCallback", position.toString())

                val category = data[position].category
                val indexOfCategory = categories.indexOf(category)
                val tabOfCategory = binding.tabsTL.getTabAt(indexOfCategory)
                binding.tabsTL.selectTab(tabOfCategory)
            }
        })
    }

}