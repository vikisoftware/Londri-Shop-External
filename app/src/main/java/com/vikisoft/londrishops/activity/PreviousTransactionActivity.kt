package com.vikisoft.londrishops.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vikisoft.londrishops.R
import com.vikisoft.londrishops.adapter.TransacationListAdapter
import kotlinx.android.synthetic.main.activity_previous_transaction.*
import java.security.AccessController.getContext
import java.util.ArrayList

class PreviousTransactionActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_transaction)

        setTransactionDummyAdapter()

        back_iv.setOnClickListener {
            finish()
        }
    }

    private fun init() {

    }

    private fun setTransactionDummyAdapter() {
        val transactionList = listOf("", "", "", "", "", "", "", "", "", "")


        val adapter = TransacationListAdapter(transactionList, this)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setAdapter(adapter)

    }
}