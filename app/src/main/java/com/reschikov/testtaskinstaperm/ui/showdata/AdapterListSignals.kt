package com.reschikov.testtaskinstaperm.ui.showdata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reschikov.testtaskinstaperm.R
import com.reschikov.testtaskinstaperm.model.Signal
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_signal.view.*
import java.text.DateFormat
import java.util.*

class AdapterListSignals : RecyclerView.Adapter<AdapterListSignals.ViewHolder>() {

    var list: List<Signal> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_signal, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as Bindable).bind(list[position])
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer,
        Bindable{

        override fun bind(signal: Signal) {
            with(containerView){
                signal.also {
                    tv_pair.text = it.pair
                    tv_time.text = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(Date(it.actualTime))
                    tv_price.text = it.price.toString()
                    tv_sl.text = it.sl.toString()
                    tv_tp.text = it.tp.toString()
                }
            }
        }
    }
}