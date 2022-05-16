package ar.utn.frba.mobile.plantis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class GardenAdapter(val view: View, val plantList: List<PlantDetail>) : RecyclerView.Adapter<GardenAdapter.GardenViewHolder>() {

    class GardenViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val plantItem: LinearLayout= view.findViewById(R.id.plant_item)
        var plantTextView: TextView = view.findViewById(R.id.plant_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GardenViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.view_listitem_plant, parent, false)
        return GardenViewHolder(view)
    }

    override fun onBindViewHolder(holder: GardenViewHolder, position: Int) {
        val plant = plantList[position]
        holder.plantTextView.text = plant.name
        holder.plantItem.setOnClickListener {
            val action = R.id.action_myGardenFragment_to_myPlantisFragment
            val bundle = bundleOf("details" to plant)
            findNavController(view).navigate(action, bundle)
        }
    }

    override fun getItemCount() = plantList.size
}
