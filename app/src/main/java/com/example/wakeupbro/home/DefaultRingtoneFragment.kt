package com.example.wakeupbro.home

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wakeupbro.databinding.FragmentDefaultRingtoneBinding
import com.example.wakeupbro.databinding.RingtoneItemBinding
import com.google.android.material.snackbar.Snackbar

/**
 */
class DefaultRingtoneFragment : Fragment() {

    private var _binding : FragmentDefaultRingtoneBinding? = null
    private val binding get() = _binding!!

    private val requestCode = 123

    private var ringtone : Ringtone? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflate layout
        _binding = FragmentDefaultRingtoneBinding.inflate(inflater)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request Storage Permission.
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(READ_EXTERNAL_STORAGE),
                requestCode
            )
        } else {
            loadRingtones()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == this.requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadRingtones()
            } else {
                // Handle permission denied
                Toast.makeText(context, "Permission Denied",Toast.LENGTH_LONG).show();
            }
        }
    }

    private fun loadRingtones() {
        val ringtoneList = mutableListOf<String>()

        // Query and add system ringtones to the list
        val ringtoneCursor: Cursor? = RingtoneManager.getActualDefaultRingtoneUri(
            requireContext(),
            RingtoneManager.TYPE_RINGTONE
        )?.let { ringtoneUri ->
            val projection = arrayOf(MediaStore.Audio.Media.TITLE)
            val selection = MediaStore.Audio.Media.IS_MUSIC + "=1"
            val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

            val contentResolver: ContentResolver = requireContext().contentResolver
            contentResolver.query(
                ringtoneUri,
                projection,
                selection,
                null,
                sortOrder
            )
        }

        ringtoneCursor?.use {
            val titleColumn = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
            while (it.moveToNext()) {
                val title = it.getString(titleColumn)
                ringtoneList.add(title)
            }
        }

        Toast.makeText(context, "Called ${ringtoneList.size} ",Toast.LENGTH_LONG).show();
        // Populate the list view or adapter with the ringtoneList
        val adapter = DefaultRingtoneAdapter(ringtoneList)
        binding.rvDefaultRingtones.adapter = adapter
    }

   // Pause Interaction , paritially visible but on foreground.
    override fun onPause() {
        super.onPause()
        Toast.makeText(context, " F visible", Toast.LENGTH_LONG).show()
        if (ringtone?.isPlaying == true) {

            ringtone?.stop()
            ringtone = null
        }
    }

    private inner class DefaultRingtoneAdapter( val items : List<String>) :
        RecyclerView.Adapter<DefaultRingtoneAdapter.RingtoneViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingtoneViewHolder {
            val binding = RingtoneItemBinding.inflate(layoutInflater)
            binding.rbSlRingtone.setOnClickListener{
                ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getActualDefaultRingtoneUri(context,RingtoneManager.TYPE_RINGTONE));
                ringtone?.play()
            }
            return RingtoneViewHolder(binding)
        }

        override fun onBindViewHolder(holder: RingtoneViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int {
            return items.size;
        }

        private inner class RingtoneViewHolder( val binding: RingtoneItemBinding ) : RecyclerView.ViewHolder(binding.root){
            fun bind(item : String){
                binding.tvRingtoneName.text = item;

            }
        }

    }


}