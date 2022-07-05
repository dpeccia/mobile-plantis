package ar.utn.frba.mobile.plantis.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.utn.frba.mobile.plantis.*
import ar.utn.frba.mobile.plantis.databinding.FragmentMyPlantisBinding
import com.bumptech.glide.Glide

class MyPlantisFragment : Fragment() {
    lateinit var binding: FragmentMyPlantisBinding
    lateinit var _context: Context
    lateinit var recyclerView: RecyclerView
    lateinit var plantDetails: PlantDetail

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMyPlantisBinding.inflate(inflater, container, false)
        _context = container!!.context
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wantsToAddPlant = arguments?.getBoolean("wantsToAddPlant")!!
        val isMyPlant = arguments?.getBoolean("isMyPlant")!!
        plantDetails = arguments?.getSerializable("details") as PlantDetail

        reloadReminders(plantDetails.reminders, plantDetails.name!!)

        binding.myPlantName.text = plantDetails.name
        binding.plantDescription.text = plantDetails.description
        binding.scientificName.text = plantDetails.scientificName

        val singleToneClass: Global = Global.instance
        singleToneClass.data = plantDetails.wikiUrl

        Glide.with(_context).load(plantDetails.imageUrl).into(binding.plantImage)

        setUpPlantInfo(plantDetails)

        if (!isMyPlant) {
            binding.remindersLayout.visibility = View.GONE
            binding.addReminderButton.visibility = View.GONE
        }

        if (wantsToAddPlant)
            binding.addButton.visibility = View.VISIBLE

        val viewManager = LinearLayoutManager(this.context)
        val viewAdapter = RemindersAdapter(view, plantDetails.reminders)

        recyclerView = binding.plantisReminders.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        binding.addButton.setOnClickListener { openAddPlantDialog(view, plantDetails) }
        binding.addReminderButton.setOnClickListener{ toNewReminder(view) }
    }

    override fun onPause() {
        super.onPause()
        if (plantDetails.reminders.isNotEmpty())
            persistReminders(plantDetails.reminders, plantDetails.name)
    }

    private fun persistReminders(reminders: MutableList<Reminder>, plantName: String?) {
        PlantisStorage.updateReminders(requireActivity(), reminders, plantName)
    }

    private fun reloadReminders(reminders: MutableList<Reminder>, plantName: String) {
        reminders.clear()
        reminders.addAll(PlantisStorage.getReminders(requireActivity(), plantName))
    }

    private fun setUpPlantInfo(plantDetails: PlantDetail) {
        binding.myPlantName.text = plantDetails.name
        binding.plantDescription.text = plantDetails.description
        binding.scientificName.text = plantDetails.scientificName
        Glide.with(_context).load(plantDetails.imageUrl).into(binding.plantImage)
    }

    private fun openAddPlantDialog(view: View, plantDetails: PlantDetail) {
        val builder = activity.let { AlertDialog.Builder(it) }
        val alert = builder.apply {
            setTitle("Add Plant")
            setMessage("Do you want to add this plant to your Garden?")
            setPositiveButton("OK") { dialog, _ ->
                PlantisStorage.addPlant(requireActivity(), plantDetails)
                toMyGarden(dialog, view)
            }
            setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss() }
            create()
        }
        alert.show()
    }

    private fun toMyGarden(dialog: DialogInterface, view: View) {
        dialog.dismiss()
        val action = R.id.action_myPlantisFragment_to_myGardenFragment
        val bundle = bundleOf()
        Navigation.findNavController(view).navigate(action, bundle)
    }

    private fun toNewReminder(view: View) {
        val plantName = plantDetails.name
        val action = R.id.action_myPlantisFragment_to_newReminderFragment
        val bundle = bundleOf("plantName" to plantName)
        Navigation.findNavController(view).navigate(action, bundle)
    }
}