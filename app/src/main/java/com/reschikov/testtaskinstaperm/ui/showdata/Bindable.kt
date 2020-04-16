package com.reschikov.testtaskinstaperm.ui.showdata

import com.reschikov.testtaskinstaperm.model.Signal

interface Bindable {
    fun bind(signal: Signal)
}