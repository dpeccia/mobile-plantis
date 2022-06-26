package ar.utn.frba.mobile.plantis.fragments


import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ar.utn.frba.mobile.plantis.PlantDetail
import ar.utn.frba.mobile.plantis.PlantisStorage
import ar.utn.frba.mobile.plantis.R
import ar.utn.frba.mobile.plantis.Reminder
import ar.utn.frba.mobile.plantis.databinding.FragmentNewReminderBinding
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*


class NewReminderFragment : Fragment() {
    lateinit var binding: FragmentNewReminderBinding
    lateinit var plantName: String
    lateinit var selectedTime: LocalTime
    var hour = 0
    var minute = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        plantName = arguments?.getSerializable("plantName") as String
        super.onViewCreated(view, savedInstanceState)
        binding.newReminderTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
        binding.newReminderTimeButton.setOnClickListener{goToTimePicker(view)}
        binding.okNewReminderButton.setOnClickListener{ addReminder(view)}
        binding.cancelNewReminderButton.setOnClickListener{ toMyGarden(view)}
    }

    private fun toMyGarden(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addReminder(view: View) {
        val newReminderName = binding.newReminderName.text.toString()
        val days = mapOf<DayOfWeek, ToggleButton>(
            DayOfWeek.MONDAY to binding.daypickerMonday,
            DayOfWeek.TUESDAY to binding.daypickerTuesday,
            DayOfWeek.WEDNESDAY to binding.daypickerWednesday,
            DayOfWeek.THURSDAY to binding.daypickerThursday,
            DayOfWeek.FRIDAY to binding.daypickerFriday,
            DayOfWeek.SATURDAY to binding.daypickerSaturdat,
            DayOfWeek.SUNDAY to binding.daypickerSunday,
        )
        val newReminderDays = days.filter{ it.value.isChecked}.keys.toList()
        val newRemindarTime = selectedTime
        PlantisStorage.addReminder(requireActivity(), Reminder(newReminderName, newRemindarTime, newReminderDays), plantName)
        Navigation.findNavController(view).popBackStack()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun goToTimePicker(view: View?) {
        val onTimeSetListener =
            OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                hour = selectedHour
                minute = selectedMinute
                selectedTime = LocalTime.of(hour,minute)
                binding.newReminderTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
            }

        // int style = AlertDialog.THEME_HOLO_DARK;
        val timePickerDialog =
            TimePickerDialog(this.context,  /*style,*/onTimeSetListener, hour, minute, true)
        timePickerDialog.setTitle("Select Time")
        timePickerDialog.show()
    }
}


