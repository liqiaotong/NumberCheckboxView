package com.liqiaotong.numbercheckbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.liqiaotong.lib.view.NumberSwitchView

class MainActivity : AppCompatActivity() {

    private var nsvs: MutableList<NumberSwitchView?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<NumberSwitchView>(R.id.nsv)?.apply {
            setOnClickListener {
                isSelected = !isSelected
            }
        }

        nsvs.add(findViewById<NumberSwitchView>(R.id.nsv1)?.apply {
            setOnClickListener { select(0) }
        })
        nsvs.add(findViewById<NumberSwitchView>(R.id.nsv2)?.apply {
            setOnClickListener { select(1) }
        })
        nsvs.add(findViewById<NumberSwitchView>(R.id.nsv3)?.apply {
            setOnClickListener { select(2) }
        })

    }

    private fun select(position: Int) {
        nsvs?.forEachIndexed { index, numberSwitchView ->
            numberSwitchView?.isSelected = position == index
        }
    }
}