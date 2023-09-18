package com.example.wakeupbro.home

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.Ringtone
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wakeupbro.R
import com.example.wakeupbro.databinding.FragmentDefaultRingtoneBinding
import com.example.wakeupbro.databinding.FragmentLocalRingtonesBinding
import com.example.wakeupbro.databinding.FragmentRingtonesBinding
import com.example.wakeupbro.databinding.RingtoneItemBinding
import java.io.IOException

/**
 */
class LocalRingtonesFragment : Fragment() {

    private var _binding : FragmentDefaultRingtoneBinding? = null;
    private val binding get() = _binding!!

    private var mediaPlayer : MediaPlayer? = null
    private var selectedItem: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDefaultRingtoneBinding.inflate(inflater)
        val view = binding.root

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(READ_EXTERNAL_STORAGE),
                123
            )
        } else {
            loadMusic()
        }
    }

    private fun loadMusic() {
        val musicList = mutableListOf<MusicItem>()
        val cursor = requireContext().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            val titleColumn = it.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val pathColumn = it.getColumnIndex(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val title = it.getString(titleColumn)
                val path = it.getString(pathColumn)

                musicList.add(MusicItem(title, path))
            }
        }

        binding.rvDefaultRingtones.adapter = MusicAdapter(requireContext(), musicList){ item ->
            selectedItem = item
            binding.rvDefaultRingtones.adapter?.notifyDataSetChanged()
        }
    }

    fun playAudio( path : String ) {

        // Release any existing MediaPlayer
        releaseMediaPlayer()

        mediaPlayer = MediaPlayer()

        try {
            mediaPlayer?.setDataSource(path)

            // Set audio attributes (optional)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            mediaPlayer?.setAudioAttributes(audioAttributes)

            // Prepare and start playback
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            reset()
            release()
        }
        mediaPlayer = null
    }

    override fun onPause() {
        super.onPause()
        releaseMediaPlayer()
    }

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer()
    }

    data class MusicItem(val title: String, val path: String)

    private inner class MusicAdapter(private val context: Context, private val musicList: List<MusicItem>, private val onItemSelectionChanged: (String) -> Unit) :
        RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
            val binding = RingtoneItemBinding.inflate(layoutInflater)
            return MusicViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
            val musicItem = musicList[position]
            holder.bind(musicItem)

        }

        override fun getItemCount(): Int {
            return musicList.size
        }

        private inner class MusicViewHolder( val binding: RingtoneItemBinding ) : RecyclerView.ViewHolder(binding.root){
            fun bind(item : MusicItem ){
                binding.tvRingtoneName.text = item.title
                binding.rbSlRingtone.isChecked = item.title == selectedItem

                binding.rbSlRingtone.setOnClickListener{
                        onItemSelectionChanged(item.title)
                        playAudio(item.path)
                }
            }
        }
    }

}