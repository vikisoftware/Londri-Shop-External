package com.vikisoft.externallondrishops.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vikisoft.externallondrishops.R
import com.vikisoft.externallondrishops.adapter.TransacationListAdapter
import kotlinx.android.synthetic.main.activity_previous_transaction.*

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