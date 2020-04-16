package com.reschikov.testtaskinstaperm.data.repository

import com.reschikov.testtaskinstaperm.data.network.model.ServerResponse
import com.reschikov.testtaskinstaperm.model.Signal

class Mapping : Transformed {

    override fun transform(responses: List<ServerResponse>): List<Signal> {
        return responses.map {response -> createSignal(response)  }
    }

    private fun createSignal(response: ServerResponse) : Signal {
        return response.run {
            Signal(id, actualTime, pair, price, sl, tp)
        }
    }
}