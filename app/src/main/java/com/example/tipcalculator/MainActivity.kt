package com.example.tipcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// 1.Split the bill by N people

private const val TAG = "MainActivity"
private const val  INITIAL_TIP_PERCENT = 15
private const val INITIAL_SPLIT_PERCENT = 15

class MainActivity : AppCompatActivity() {

    private lateinit var eTBill: EditText
    private lateinit var tipseekBar: SeekBar
    private lateinit var tipAmount: TextView
    private lateinit var totalAmount: TextView
    private lateinit var tVParcentage: TextView

    // split variables
    private lateinit var splitPercent: TextView
    private lateinit var etGroupOf: EditText
    private lateinit var etSplitBill: EditText
    private lateinit var seekSplitBar: SeekBar
    private lateinit var etSplitTip: TextView
    private lateinit var etSplitTotal: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // single person variables
        eTBill = findViewById(R.id.eTBill)
        tipseekBar = findViewById(R.id.tipseekBar)
        tipAmount = findViewById(R.id.tipAmount)
        totalAmount = findViewById(R.id.totalAmount)
        tVParcentage = findViewById(R.id.tVParcentage)

        // split bill variables
        splitPercent = findViewById(R.id.splitPercent)
        etGroupOf = findViewById(R.id.etGroupOf)
        etSplitBill = findViewById(R.id.etSplitBill)
        seekSplitBar = findViewById(R.id.seekSplitBar)
        etSplitTip = findViewById(R.id.etSplitTip)
        etSplitTotal = findViewById(R.id.etSplitTotal)


        tipseekBar.progress = INITIAL_TIP_PERCENT
        seekSplitBar.progress = INITIAL_SPLIT_PERCENT
        tVParcentage.text = "$INITIAL_TIP_PERCENT%"
        splitPercent.text = "$INITIAL_SPLIT_PERCENT%"

        seekSplitBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                splitPercent.text = "$progress%"
                computeSplit()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        etGroupOf.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeSplit()
            }
        })

        etSplitBill.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeSplit()
            }

        })

        tipseekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tVParcentage.text = "$progress%"
                computeTipandTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        eTBill.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipandTotal()
            }

        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun computeTipandTotal() {
        if(eTBill.text.isEmpty()){
            tipAmount.text = ""
            totalAmount.text = ""
            return
        }
        //1.get the value of the base and tip percent
        val billAmount = eTBill.text.toString().toDouble()
        val tipPercent = tipseekBar.progress

        //2.compute the tip and total
        val totalTipAmount = billAmount * tipPercent / 100
        val totalBill = billAmount + totalTipAmount


        //3.update the UI
        tipAmount.text = "%.2f".format(totalTipAmount)
        totalAmount.text = "%.2f".format(totalBill)



    }
    private fun computeSplit(){
        // for the split bill
        if(etSplitBill.text.isEmpty() and etGroupOf.text.isEmpty()){
            etSplitTip.text = ""
            etSplitTotal.text = ""
            return
        }
        // get the value of the bill, tip percent and number of people
        val numberofPeople = etGroupOf.text.toString().toDouble()
        val billtoBeSplit = etSplitBill.text.toString().toDouble()
        val splitTipPercentage = seekSplitBar.progress

        // compute total amount, your tip
        val personalBill = billtoBeSplit / numberofPeople
        val personalTip = personalBill * splitTipPercentage / 100
        val personalTotal = personalBill + personalTip

        // update split UI
        etSplitTip.text = "%.2f".format(personalTip)
        etSplitTotal.text = "%.2f".format(personalTotal)
    }
}