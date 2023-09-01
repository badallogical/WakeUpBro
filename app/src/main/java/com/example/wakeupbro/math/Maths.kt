package com.example.wakeupbro.ui.math

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.wakeupbro.databinding.FragmentMathsBinding
import com.example.wakeupbro.math.MathApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlin.properties.Delegates

class Maths : Fragment() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var total_question by Delegates.notNull<Int>()
    private lateinit var problem : TextView
    private var questionCount = 1

    private lateinit var binding : FragmentMathsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val mathAPI = MathApi(0)
        total_question = mathAPI.getQuestions()

        // get toolbar
        val toolbar = binding.toolbar
        toolbar.title = "$questionCount/$total_question"

        problem = binding.tvProblem

        var question = mathAPI.next()
        problem.text = question.split(":")[0]

        val editText = binding.etResult
        // check the enter result if correct add the next question by calling updateUIWithText()
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that the text is about to change.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that the text has changed.
                val text = s.toString()

                // This method is called when the text is being changed.
                if (text == question.split(":")[1]) {
                    editText.setText("")
                    try {
                        question = mathAPI.next()
                        updateUIWithText(question)
                        questionCount++
                        toolbar.title = "$questionCount/$total_question"
                    }
                    catch( exception : NoSuchElementException ){
                        updateUIWithText("null")
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called to notify you that the text has been changed.
                // You can access the updated text using 's.toString()'.

            }
        })

        return binding.root
    }

    private fun updateUIWithText(text: String) {
        if(text == "null"){
            val alertDialog = AlertDialog.Builder(requireContext())
                .setTitle("Task Completed")
                .setMessage("You have completed the task.")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .create()

            alertDialog.show()
        }else{
            problem.text = text.split(":")[0]
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel() // Cancel the coroutine scope when the activity is destroyed
    }
}