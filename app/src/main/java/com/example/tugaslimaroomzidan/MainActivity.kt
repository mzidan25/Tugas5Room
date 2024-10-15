package com.example.tugaslimaroomzidan

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugaslimaroomzidan.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.buttonAdd.setOnClickListener {
            val itemName = binding.editTextItem.text.toString()
            if (itemName.isNotEmpty()) {
                val item = Item(name = itemName)
                CoroutineScope(Dispatchers.IO).launch {
                    database.itemDao().insertItem(item)
                    Log.d("RoomDatabase", "Inserted item: $itemName")
                    loadItems()
                }
            }
        }

        loadItems()
    }

    private fun loadItems() {
        CoroutineScope(Dispatchers.IO).launch {
            val items = database.itemDao().getAllItems()
            Log.d("RoomDatabase", "Retrieved items: $items")
            runOnUiThread {
                adapter = ItemAdapter(items)
                binding.recyclerView.adapter = adapter
            }
        }
    }
}
